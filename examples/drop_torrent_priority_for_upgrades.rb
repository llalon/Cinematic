#!/usr/bin/env jruby
#
# This script will drop the priority of torrents for movies and series which are already available and are just being upgraded.
# This helps manage the torrent queue and allows missing media to be prioritized over upgrades.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

library = Library.new

puts "Scanning for upgrade torrents..."

puts " Checking movies..."
library.movies.each do |movie|
  next unless movie.get_has_file

  movie.torrents.each do |torrent|
    puts "  [MOVIE] Dropping priority for upgrade: '#{movie.get_title}' (#{torrent.get_hash})"
    torrent.setBottomPriority()
  end
end

puts "Done."