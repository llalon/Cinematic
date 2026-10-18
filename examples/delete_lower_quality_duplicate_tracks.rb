#!/usr/bin/env jruby
#
# Finds duplicate recordings within each Lidarr artist, including across albums.
# Requires LIDARR_URL / LIDARR_API_KEY and Cinematic plus its dependencies on CLASSPATH.
# Run: jruby examples/delete_lower_quality_duplicate_tracks.rb
# Set DRY_RUN to false below to move lower-quality duplicates to the recycle bin.
#
# Uses MusicBrainz recording IDs, falling back to release-track IDs when absent.
# Higher Lidarr qualityWeight wins; ties and unknown quality are retained.
# Recycled files keep their path relative to LIBRARY_SHARE, including music/.
# A shared file is moved only if ALL its tracks have strictly better retained copies.
# Rescan Lidarr afterwards to refresh its file records.

require 'java'
require 'fileutils'
require 'set'

java_import 'de.llalon.cinematic.domain.Library'

LIBRARY_SHARE = "/mnt/user/media"
MUSIC_PATH    = "#{LIBRARY_SHARE}/music"
RECYCLE_PATH  = "#{LIBRARY_SHARE}/.Recycle.Bin"

DRY_RUN = true

def music_file?(path)
  !path.strip.empty? &&
    File.expand_path(path).start_with?(File.expand_path(MUSIC_PATH) + File::SEPARATOR) &&
    File.file?(path)
end

def move_to_recycle(disk_file)
  return false unless music_file?(disk_file)

  relative_path = File.expand_path(disk_file).delete_prefix(File.expand_path(LIBRARY_SHARE) + File::SEPARATOR)
  recycle_target = File.join(RECYCLE_PATH, relative_path)

  if File.exist?(recycle_target) || File.symlink?(recycle_target)
    puts "  [SKIP] Recycle destination already exists: #{recycle_target}"
    return false
  end

  if DRY_RUN
    puts "  Would move to: #{recycle_target}"
    return false
  end

  FileUtils.mkdir_p(File.dirname(recycle_target))
  FileUtils.mv(disk_file, recycle_target)
  puts "  Moved to: #{recycle_target}"
  true
end

def identifier(value)
  text = value.to_s.strip.downcase
  text.empty? ? nil : text
end

def recording_key(artist, track)
  recording = identifier(track.get_foreign_recording_id)
  return [artist.get_id, :recording, recording] if recording

  release_track = identifier(track.get_foreign_track_id)
  [artist.get_id, :release_track, release_track] if release_track
end

def register(files, file)
  id = file.get_id
  raise 'Cannot safely identify a track file without its Lidarr ID' if id.nil?

  files[id] ||= {
    file: file,
    path: file.get_path.to_s.empty? ? '' : File.expand_path(file.get_path.to_s),
    weight: file.get_quality_weight&.to_i,
    recordings: Set.new
  }
end

def scan(library)
  files = {}
  library.artists.each do |artist|
    puts "Scanning #{artist.get_artist_name}..."
    # Include files with no linked track, which must be retained.
    artist.files.each { |file| register(files, file) }
    artist.tracks.each do |track|
      key = recording_key(artist, track)
      track.files.each do |file|
        register(files, file)[:recordings].add(key)
      end
    end
  end
  files.values
end

def deletion_plan(files)
  by_recording = Hash.new { |hash, key| hash[key] = [] }
  by_path = files.group_by { |entry| entry[:path] }
  files.each do |entry|
    entry[:recordings].each { |key| by_recording[key] << entry unless key.nil? }
  end

  known_quality = lambda do |entry|
    !entry[:weight].nil? && entry[:weight] > 0 && music_file?(entry[:path])
  end

  # Protect every highest-quality copy, including ties, for every recording.
  winners = {}
  by_recording.each do |key, entries|
    ranked = entries.select { |entry| known_quality.call(entry) }
    highest = ranked.map { |entry| entry[:weight] }.max
    winners[key] = ranked.select { |entry| entry[:weight] == highest }
  end

  files.filter_map do |entry|
    next unless known_quality.call(entry)
    next if entry[:recordings].empty? || entry[:recordings].include?(nil)
    # Multiple Lidarr IDs pointing to one path are not independent copies.
    next if by_path.fetch(entry[:path]).size > 1

    replacements = entry[:recordings].map do |key|
      winners.fetch(key).find do |winner|
        winner[:weight] > entry[:weight] && winner[:path] != entry[:path]
      end
    end
    next if replacements.any?(&:nil?)

    { remove: entry, keep: replacements.uniq }
  end
end

library = Library.new

# Finish the entire scan before moving anything. Scan failures abort the run.
files = scan(library)
plan = deletion_plan(files)
puts "\n#{files.size} imported file(s); #{plan.size} lower-quality duplicate(s)."
puts(DRY_RUN ? 'Preview only. Set DRY_RUN = false to apply.' : 'Moving duplicates to the recycle bin...')

moved = 0
plan.each do |item|
  entry = item[:remove]
  puts "\n#{DRY_RUN ? 'WOULD RECYCLE' : 'RECYCLE'} [#{entry[:weight]}] #{entry[:path]}"
  item[:keep].each { |keep| puts "  KEEP [#{keep[:weight]}] #{keep[:path]}" }
  unless item[:keep].all? { |keep| music_file?(keep[:path]) }
    puts '  [SKIP] A retained copy is no longer available.'
    next
  end

  moved += 1 if move_to_recycle(entry[:path])
end
puts "\nDone. #{moved} file(s) moved to the recycle bin."
