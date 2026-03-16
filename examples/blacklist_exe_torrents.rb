#!/usr/bin/env jruby
#
# This script will find all torrents which have a malicious .exe file in them.
# It will BLACKLIST the torrent in sonarr/radarr and DELETE the torrent and files.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

library = Library.new

puts "Scanning torrents for .exe files..."

found = 0

library.torrents.each do |torrent|
  name = torrent.get_name
  hash = torrent.get_hash

  begin
    exe_files = torrent.files.select { |f| f.get_name.to_s.downcase.end_with?('.exe') }
    next if exe_files.empty?

    found += 1
    puts "\n[MALWARE] #{name} (#{hash})"
    exe_files.each { |f| puts "  .exe: #{f.get_name}" }

    puts "  Blacklisting torrent..."
    torrent.blacklist

    puts "  Deleting torrent and files from qBittorrent..."
    torrent.remove(true)

    puts "  Done."
  rescue => e
    puts "  [ERROR] Failed processing torrent #{name} (#{hash}): #{e}"
  end
end

puts "\nScan complete. #{found} torrent(s) with .exe files found."
