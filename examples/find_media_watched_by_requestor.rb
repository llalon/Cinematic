#!/usr/bin/env jruby
# This script will print out all movies and series which have been watched to completion at least once by the user who requested them.
# This script can be modified to delete them from radarr/sonarr automatically.

require 'java'
require 'set'
java_import 'de.llalon.cinematic.domain.Library'

library = Library.new

puts "Finding media watched by requestor"

library.movies.each do |movie|
  requests = movie.requests.map { |r| r.user.email }.to_set
  if requests.empty?
    puts "Skipping movie with no requests: #{movie.titleSlug}"
    next
  end

  watches = movie.watches.each_with_object({}) do |watch, h|
    email = watch.user.email
    puts "Movie #{movie.titleSlug} watched by #{email} x#{watch.watchedStatus}"

    h[email] = watch.watchedStatus && watch.watchedStatus >= 0.95
  end

  if watches.empty?
    puts "Skipping movie with no watches: #{movie.titleSlug}"
    next
  end

  requests.each do |email|
    puts " !!! Movie #{movie.title} watched by requestor #{email}" if watches.fetch(email, false)
  end
end

puts "Done."