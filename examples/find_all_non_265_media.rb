#!/usr/bin/env jruby
#
# This script will search for and print all media item files that aren't x265 format.


require 'java'
java_import 'de.llalon.cinematic.domain.Library'

library = Library.new

def h265?(format)
  codec = format.get_video_codec.to_s.downcase
  codec.include?('265') || codec.include?('hevc') || codec.include?('x265')
end

puts "Movies not in 265/HEVC format:"

total_movies = 0
non_265_movies = 0
movies_without_format_info = 0
total_episodes = 0
non_265_episodes = 0
episodes_without_format_info = 0

library.movies.each do |movie|
  total_movies += 1
  formats = movie.formats.to_a

  if formats.empty? || formats.none? { |format| h265?(format) }
    non_265_movies += 1
    movies_without_format_info += 1 if formats.empty?
    codecs = formats.map { |format| format.get_video_codec || 'unknown' }.uniq.join(', ')
    codecs = 'no format info' if codecs.empty?

    puts "- #{movie.title} (#{movie.year}) [#{codecs}]"
  end
end

puts
puts "Series episodes not in 265/HEVC format:"

library.series.each do |series|
  series.episodes.each do |episode|
    total_episodes += 1
    formats = episode.formats.to_a

    if formats.empty? || formats.none? { |format| h265?(format) }
      non_265_episodes += 1
      episodes_without_format_info += 1 if formats.empty?
      codecs = formats.map { |format| format.get_video_codec || 'unknown' }.uniq.join(', ')
      codecs = 'no format info' if codecs.empty?

      puts format(
        "- %s S%02dE%02d - %s [%s]",
        series.title,
        episode.season_number,
        episode.episode_number,
        episode.title,
        codecs
      )
    end
  end
end

puts
puts "--- Summary ---"
puts "Movies scanned: #{total_movies}"
puts "Movies not in 265/HEVC format: #{non_265_movies}"
puts "Movies with no format info: #{movies_without_format_info}"
puts "Episodes scanned: #{total_episodes}"
puts "Episodes not in 265/HEVC format: #{non_265_episodes}"
puts "Episodes with no format info: #{episodes_without_format_info}"
puts "Total media files not in 265/HEVC format: #{non_265_movies + non_265_episodes}"
