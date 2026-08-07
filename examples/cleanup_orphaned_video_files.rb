#!/usr/bin/env jruby
#
# This script will find video files in the media tv and movie directories which are not tracked by sonarr/radarr and move them to a recycle bin

require 'java'
require 'fileutils'
require 'set'

java_import 'de.llalon.cinematic.domain.Library'

LIBRARY_SHARE = "/mnt/user/media"
TV_PATH       = "#{LIBRARY_SHARE}/tv"
MOVIE_PATH    = "#{LIBRARY_SHARE}/movies"
RECYCLE_PATH  = "#{LIBRARY_SHARE}/.Recycle.Bin"

VIDEO_EXT = %w[.mkv .mp4 .avi .mov .wmv .flv .m4v]

def video_file?(path)
  VIDEO_EXT.include?(File.extname(path).downcase)
end

def move_to_recycle(disk_file)
  relative_path = disk_file.sub(LIBRARY_SHARE + "/", "")
  recycle_target = File.join(RECYCLE_PATH, relative_path)

  # ToDo: Enable for non dry run
  #FileUtils.mkdir_p(File.dirname(recycle_target))
  #FileUtils.mv(disk_file, recycle_target)

  puts " → Moved to: #{recycle_target}"
end

library = Library.new

puts "Finding orphaned video files..."
puts "----------------------------------"

# TV (Sonarr)
tracked_tv   = {}

library.series.each do |series|
  series.episodes.each do |episode|
    next unless episode.get_has_file

    episode.files.each do |file|
      tracked_tv[file.get_path] = true
    end
  end
end

puts "Scanning TV library (#{tracked_tv.size}) for orphaned files..."

Dir.glob("#{TV_PATH}/**/*").each do |disk_file|
  next unless File.file?(disk_file)
  next unless video_file?(disk_file)

  unless tracked_tv[disk_file]
    puts "ORPHAN (TV): #{disk_file}"
    move_to_recycle(disk_file)
  end
end

tracked_tv.clear

# Movies (Radarr)
tracked_movies = {}

library.movies.each do |movie|
  next unless movie.get_has_file

  movie.files.each do |file|
    tracked_movies[file.get_path] = true
  end
end

puts "\nScanning Movie library (#{tracked_movies.size}) for orphaned files..."

Dir.glob("#{MOVIE_PATH}/**/*").each do |disk_file|
  next unless File.file?(disk_file)
  next unless video_file?(disk_file)

  unless tracked_movies[disk_file]
    puts "ORPHAN (Movie): #{disk_file}"
    move_to_recycle(disk_file)
  end
end

tracked_movies.clear

puts "----------------------------------"
puts "Done."
