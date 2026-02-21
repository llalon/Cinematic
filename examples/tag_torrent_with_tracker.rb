#!/usr/bin/env jruby
# This script will tag torrents based on their tracker URL.
# Uses substring matching against a configurable map of tracker -> tag.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

TRACKER_TAG_MAP = {
  'passthepopcorn'   => 'ptp',
  'alpharatio'       => 'ar',
  'torrentleech'     => 'tl',
  'privatehd'        => 'phd',
  'beyond-hd'        => 'bhd',
  'myanonamouse'     => 'mam',
  'redacted.ch'      => 'red',
  'orpheus.network'  => 'ops',
  'morethantv'       => 'mtv',
}

library = Library.new

puts "Tagging torrents by tracker..."

library.torrents.each do |torrent|
  tracker = torrent.get_tracker.to_s.downcase

  if tracker.empty?
    puts "  [TORRENT] No tracker for '#{torrent.get_name}', skipping"
    next
  end

  matched = false
  TRACKER_TAG_MAP.each do |substr, tag|
    next unless tracker.include?(substr)

    puts "  [TORRENT] Tagging '#{torrent.get_name}' with '#{tag}' (matched '#{substr}')"
    torrent.add_tag(tag)
    matched = true
  end

  puts "  [TORRENT] No matching tracker tag for '#{torrent.get_name}' (#{tracker})" unless matched
end

puts "Done."