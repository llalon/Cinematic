#!/usr/bin/env jruby
#
# This script will tag torrents based on their tracker URL.
# Uses substring matching against a configurable map of tracker -> tag.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

TRACKER_TAG_MAP = {
  'EXAMPLE_passthepopcorn' => 'ptp',
  'EXAMPLE_alpharatio' => 'ar',
  'EXAMPLE_torrentleech' => 'tl',
  'EXAMPLE_privatehd' => 'phd',
  'EXAMPLE_beyond-hd' => 'bhd',
  'EXAMPLE_myanonamouse' => 'mam',
  'EXAMPLE_redacted.ch' => 'red',
  'EXAMPLE_orpheus.network' => 'ops',
  'EXAMPLE_morethantv' => 'mtv',
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