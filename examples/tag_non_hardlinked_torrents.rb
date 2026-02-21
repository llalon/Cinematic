#!/usr/bin/env jruby
# This script will tag torrents that do not have any hard links on them.
# This can be useful if sonarr and radarr are configured to use hardlinks.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

TAG_NAME = 'no-hardlinks'

library = Library.new

puts "Scanning torrents for missing hardlinks..."

library.torrents.each do |torrent|
  content_path = torrent.get_content_path
  next if content_path.nil? || content_path.empty?
  next unless File.exist?(content_path)

  non_hardlinked = false

  begin
    if File.directory?(content_path)
      # Walk every file in the content directory tree.
      Dir.glob(File.join(content_path, '**', '*')).each do |entry|
        next if File.directory?(entry)

        if File.stat(entry).nlink <= 1
          non_hardlinked = true
          break
        end
      end
    else
      non_hardlinked = File.stat(content_path).nlink <= 1
    end
  rescue => e
    puts "  Warning: could not stat '#{content_path}': #{e.message}"
    next
  end

  if non_hardlinked
    puts "  [TORRENT] Tagging '#{torrent.get_name}' with '#{TAG_NAME}' (#{torrent.get_hash})"
    torrent.add_tag(TAG_NAME)
  end
end

puts "Done."