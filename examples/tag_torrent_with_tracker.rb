#!/usr/bin/env jruby
#
# This script will tag torrents based on their tracker URL.
# Uses substring matching against a configurable map of tracker -> tag.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

TRACKER_TAG_MAP = {
  'passthepopcorn' => 'ptp',
  'alpharatio' => 'ar',
  'torrentleech' => 'tl',
  'privatehd' => 'phd',
  'beyond-hd' => 'bhd',
  'myanonamouse' => 'mam',
  'redacted.ch' => 'red',
  'orpheus.network' => 'ops',
  'morethantv' => 'mtv',
}

library = Library.new

puts "Tagging torrents by tracker..."

library.torrents.each do |torrent|
  tracker = torrent.get_tracker.to_s.downcase

  next if tracker.empty?

  TRACKER_TAG_MAP.each do |substr, tag|
    next unless tracker.include?(substr.to_s.downcase)

    puts "  [TORRENT] Tagging '#{torrent.get_name}' with '#{tag}')"
    torrent.add_tag(tag)
  end
end

puts "Done."