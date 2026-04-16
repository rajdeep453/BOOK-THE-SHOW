package org.example.bms.Service;

import org.example.bms.dto.MovieDto;
import org.example.bms.exception.ResourceNotFoundException;
import org.example.bms.model.Movie;
import org.example.bms.reposatory.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    public MovieDto createMovie(MovieDto movieDto){
Movie movie=new Movie();
movie=mapToEntity(movieDto);
movie=movieRepository.save(movie);
return mapToDto(movie);
    }
public MovieDto getMovieById(Long id){
        Movie movie=movieRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Movie NOT Found"));
        return mapToDto(movie);
}
    public List<MovieDto> getAllMovies(){
        List<Movie> movies=movieRepository.findAll();
        return movies.stream().map(
                movie -> {
                    return mapToDto(movie);
                }
        ).collect(Collectors.toList());
    }
    public List<MovieDto> getMovieByLanguage(String language){
        List<Movie> movies=movieRepository.findByLanguage(language);
        return movies.stream().map(
                movie -> {
                    return mapToDto(movie);
                }
        ).collect(Collectors.toList());
    }
    public List<MovieDto> getMovieByGenre(String genre){
        List<Movie> movies=movieRepository.findByLanguage(genre);
        return movies.stream().map(
                movie -> {
                    return mapToDto(movie);
                }
        ).collect(Collectors.toList());
    }
    public List<MovieDto> searchMovies(String title ){
        List<Movie> movies=movieRepository.findByLanguage(title);
        return movies.stream().map(
                movie -> {
                    return mapToDto(movie);
                }
        ).collect(Collectors.toList());
    }

    public MovieDto updateMovie(MovieDto movieDto,Long id){
        Movie movie=movieRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Movie NOT Found"));
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        movie=movieRepository.save(movie);
        return mapToDto(movie);

    }
    public  void deleteMovie(Long id){
        Movie movie=movieRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Movie NOT Found"));
        movieRepository.delete(movie);
    }
    public  MovieDto mapToDto(Movie movie){
        MovieDto movieDto=new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setTitle(movie.getTitle());
        movieDto.setDescription(movie.getDescription());
        movieDto.setLanguage(movie.getLanguage());
        movieDto.setGenre(movie.getGenre());
        movieDto.setDurationMins(movie.getDurationMins());
        movieDto.setReleaseDate(movie.getReleaseDate());
        movieDto.setPosterUrl(movie.getPosterUrl());
        return movieDto;
    }
    public Movie mapToEntity(MovieDto movieDto){
        Movie movie=new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setLanguage(movieDto.getLanguage());
        movie.setGenre(movieDto.getGenre());
        movie.setDurationMins(movieDto.getDurationMins());
        movie.setReleaseDate(movieDto.getReleaseDate());
        movie.setPosterUrl(movieDto.getPosterUrl());
        return movie;

    }
}
