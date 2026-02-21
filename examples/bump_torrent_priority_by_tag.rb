#!/usr/bin/env jruby
# This script will bump the priority of torrents for movies and series with a given tag.
# This can be used to mark certain media as high priority and to maintain high priority in the torrent queue.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'
java_import 'de.llalon.cinematic.domain.Tag'

PRIORITY_TAG = 'high-priority'

library = Library.new

puts "Scanning for torrents belonging to media tagged '#{PRIORITY_TAG}'..."

tag = Tag.new(library.get_context, PRIORITY_TAG)

tag.movies.each do |movie|
  movie.torrents.each do |torrent|
    hash = torrent.get_hash
    next if hash.nil?

    puts "  [MOVIE] Bumping priority for '#{movie.get_title}' (#{hash})"
    # TODO: Torrent domain object does not yet expose a set_top_priority / increase_priority method.
  end
end

tag.series.each do |series|
  series.torrents.each do |torrent|
    hash = torrent.get_hash
    next if hash.nil?

    puts "  [SERIES] Bumping priority for '#{series.get_title}' (#{hash})"
    # TODO: Torrent domain object does not yet expose a set_top_priority / increase_priority method.
  end
end

puts "Done."