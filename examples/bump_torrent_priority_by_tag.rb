#!/usr/bin/env jruby
#
# This script will bump the priority of torrents for movies and series with a given tag.
# This can be used to mark certain media as high priority and to maintain high priority in the torrent queue.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

PRIORITY_TAG = 'hp'

library = Library.new

puts "Scanning for torrents belonging to media tagged '#{PRIORITY_TAG}'..."

puts " Checking movies..."
library.movies.each do |movie|
  next unless movie.hasTag(PRIORITY_TAG)

  movie.torrents.each do |torrent|
    puts "  [MOVIE] Bumping priority for '#{movie.get_title}' (#{torrent.get_hash})"
    torrent.setTopPriority()
  end
end

puts " Checking series..."
library.series.each do |series|
  next unless series.hasTag(PRIORITY_TAG)

  series.torrents.each do |torrent|
    puts "  [SERIES] Bumping priority for '#{series.get_title}' (#{torrent.get_hash})"
    torrent.setTopPriority()
  end
end

puts "Done."