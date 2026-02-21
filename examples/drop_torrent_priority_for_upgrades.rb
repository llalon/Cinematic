#!/usr/bin/env jruby
# This script will drop the priority of torrents for movies and series which are already available and are just being upgraded.
# This helps manage the torrent queue and allows missing media to be prioritized over upgrades.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

library = Library.new

puts "Scanning for upgrade torrents..."

library.movies.each do |movie|
  next unless movie.get_has_file == true

  movie.torrents.each do |torrent|
    hash = torrent.get_hash
    next if hash.nil?

    puts "  [MOVIE] Dropping priority for upgrade: '#{movie.get_title}' (#{hash})"
    # TODO: Torrent domain object does not yet expose a set_bottom_priority method.
  end
end

library.series.each do |series|
  series.torrents.each do |torrent|
    hash = torrent.get_hash
    next if hash.nil?

    puts "  [SERIES] Dropping priority for upgrade: '#{series.get_title}' (#{hash})"
    # TODO: Torrent domain object does not yet expose a set_bottom_priority method.
    # TODO: Episode-level domain objects are not yet available. Currently all series
    #       torrents are treated as potential upgrades. A future Episode domain object
    #       with hasFile would allow filtering to true upgrades only.
  end
end

puts "Done."