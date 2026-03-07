#!/usr/bin/env jruby
#
# Finds all torrents in category "tv" or "movie" that do NOT have any filesystem
# hardlinks for their content files and adds the "NoHL" tag to them.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'
java_import 'java.nio.file.Paths'
java_import 'java.nio.file.Files'

NOHL_TAG = 'noHL'
CATEGORIES = ['tv', 'movie']

library = Library.new

puts "Searching torrents in categories #{CATEGORIES.join(', ')} without hardlinks..."

library.torrents.each do |torrent|
  begin
    category = torrent.getCategory
    next if category.nil? || category.empty?
    cat = category.to_s.downcase
    next unless CATEGORIES.include?(cat)

    name = torrent.getName
    hash = torrent.getHash
    puts "Checking: #{name} (#{hash})"

    # Use domain getters (no reflection) to obtain content path and completion info
    content_path = torrent.getContentPath

    # Skip torrents that are not completed yet
    unless torrent.isCompleted
      puts "  [SKIP] Torrent not completed: #{name} (state=#{torrent.getState})"
      next
    end

    if content_path.nil? || content_path.to_s.empty?
      puts "  [SKIP] No content path for #{name}"
      next
    end

    begin
      base = Paths.get(content_path.to_s)
    rescue => e
      puts "  [WARN] Invalid content path for #{name}: #{content_path} (#{e})"
      next
    end

    # If the base path doesn't exist on this host, skip it safely
    unless Files.exists(base)
      puts "  [SKIP] Content path does not exist on this host: #{base}"
      next
    end

    no_hardlinks = true

    if Files.isDirectory(base)
      # Walk files under the directory
      walker = Files.walk(base)
      begin
        walker.forEach do |p|
          next if Files.isDirectory(p)
          # If a file disappeared between listing and check, skip torrent conservatively
          unless Files.exists(p)
            puts "  [WARN] File disappeared: #{p}"
            no_hardlinks = false
            break
          end
          begin
            nlink = Files.getAttribute(p, 'unix:nlink')
            nlink_val = nlink.respond_to?(:intValue) ? nlink.intValue : nlink.to_i
            if nlink_val > 1
              puts "  [HARDLINK] #{p} has #{nlink_val} links"
              no_hardlinks = false
              break
            end
          rescue => e
            puts "  [WARN] Could not read link count for #{p}: #{e}"
            no_hardlinks = false
            break
          end
        end
      ensure
        walker.close
      end
    else
      # Single-file torrent
      p = base
      unless Files.exists(p)
        puts "  [SKIP] File not found: #{p}"
        next
      end
      begin
        nlink = Files.getAttribute(p, 'unix:nlink')
        nlink_val = nlink.respond_to?(:intValue) ? nlink.intValue : nlink.to_i
        if nlink_val > 1
          puts "  [HARDLINK] #{p} has #{nlink_val} links"
          no_hardlinks = false
        end
      rescue => e
        puts "  [WARN] Could not read link count for #{p}: #{e}"
        no_hardlinks = false
      end
    end

    if no_hardlinks
      puts "  [TAG] Adding '#{NOHL_TAG}' to #{name}"
      torrent.addTag(NOHL_TAG)
    else
      puts "  [SKIP] Not tagging #{name}"
    end
  rescue => e
    puts "  [ERROR] Processing torrent #{torrent.getName}: #{e}"
  end
end

puts "Done."
