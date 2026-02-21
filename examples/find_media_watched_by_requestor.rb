#!/usr/bin/env jruby
# This script will print out all movies and series which have been watched to completion at least once by the user who requested them.
# This script can be modified to delete them from radarr/sonarr automatically.

require 'java'
java_import 'de.llalon.cinematic.domain.Library'

library = Library.new

# TODO: User domain object does not yet expose a Tautulli user ID or Plex username,
#       so we cannot reliably correlate a requesting user (identified by email in
#       Overseerr) with their Tautulli watch entries. A future User#plexUsername or
#       User#tautulliId accessor would remove the need for username-based matching.
def watched_to_completion_by_username?(watches, plex_username)
  return false if plex_username.nil?

  watches.each do |watch|
    ws = watch.get_watched_status
    next if ws.nil?
    return true if watch.get_user == plex_username && ws.to_f >= 1.0
  end

  false
end

puts "Scanning movies..."
library.movies.each do |movie|
  movie.requests.each do |request|
    user  = request.user
    email = user.get_email

    # TODO: resolve the requesting user's Plex username from the User domain object
    plex_username = email

    next unless watched_to_completion_by_username?(movie.watches, plex_username)

    puts "  [MOVIE] #{email} has fully watched '#{movie.get_title}' (which they requested)"
  end
end

puts "Scanning series..."
library.series.each do |series|
  series.requests.each do |request|
    user  = request.user
    email = user.get_email

    # TODO: same as above – resolve Plex username from User domain object.
    plex_username = email

    next unless watched_to_completion_by_username?(series.watches, plex_username)

    puts "  [SERIES] #{email} has watched at least one episode of '#{series.get_title}' to completion (which they requested)"
  end
end

puts "Done."