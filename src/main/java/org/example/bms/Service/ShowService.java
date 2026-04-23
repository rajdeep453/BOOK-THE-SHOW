package org.example.bms.Service;


import org.example.bms.dto.*;
import org.example.bms.exception.ResourceNotFoundException;
import org.example.bms.model.Movie;
import org.example.bms.model.Screen;
import org.example.bms.model.Show;
import org.example.bms.model.ShowSeat;
import org.example.bms.reposatory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ShowService {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ShowSeatRepository showSeatRepository;
    public ShowDto createShow(ShowDto showDto){
        Show show=new Show();
        show.setStartTime(showDto.getStartTime());
        show.setEndTime(showDto.getEndTime());
        Movie movie=movieRepository.findById(showDto.getMovie().getId()).orElseThrow(()->new RuntimeException("Movie Not Found"));
        show.setMovie(movie);
        Screen screen=screenRepository.getById(showDto.getScreen().getId());
        show.setScreen(screen);
        Show savedShow=showRepository.save(show);

        List<ShowSeat> availableSeats=
                showSeatRepository.findByShowIdAndStatus(savedShow.getId(),"AVAILABLE");
return mapToDto(savedShow,availableSeats);
    }
    public ShowDto getShowById(Long id){
        Show show=showRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Show not found  with id: "+id));
        List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(id,"AVAILABLE");
        return mapToDto(show,availableSeats);


    }
public List<ShowDto> getAllShows(){
        List<Show> shows=showRepository.findAll();
        return shows.stream().map(
                show -> {
                    List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                }
        ).toList();
}
    public List<ShowDto> getShowsByMovie(Long movieId){
        List<Show> shows=showRepository.findByMovieId(movieId);
        return shows.stream().map(
                show -> {
                    List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                }
        ).toList();
    }
    public List<ShowDto> getShowsByMovieAndCity(Long movieId,String City){
        List<Show> shows=showRepository.findByMovie_IdAndScreen_Theater_City(movieId,City);

        return shows.stream().map(
                show -> {
                    List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                }
        ).toList();
    }
    public List<ShowDto> getShowsByDateRange(LocalDateTime startDate, LocalDateTime endDate){
        List<Show> shows=showRepository.findByStartTimeBetween(startDate,endDate);

        return shows.stream().map(
                show -> {
                    List<ShowSeat> availableSeats=showSeatRepository.findByShowIdAndStatus(show.getId(),"AVAILABLE");
                    return mapToDto(show,availableSeats);
                }
        ).toList();
    }

public ShowDto mapToDto(Show show,List<ShowSeat> availableSeats){
    ShowDto showDto=new ShowDto();
    showDto.setId(show.getId());
    showDto.setStartTime(show.getStartTime());
    showDto.setEndTime(show.getEndTime());
    showDto.setMovie(
            new MovieDto(
                    show.getMovie().getId(),
                    show.getMovie().getTitle(),
                    show.getMovie().getDescription(),
                    show.getMovie().getLanguage(),
                    show.getMovie().getGenre(),
                    show.getMovie().getDurationMins(),
                    show.getMovie().getReleaseDate(),
                    show.getMovie().getPosterUrl()
            )
    );
    //creating theatre dto for  screendto
    TheaterDto  theaterDto=new TheaterDto();
    theaterDto.setId(show.getScreen().getTheater().getId());
    theaterDto.setName(show.getScreen().getTheater().getName());
    theaterDto.setAddress(show.getScreen().getTheater().getAddress());
    theaterDto.setCity(show.getScreen().getTheater().getCity());
    theaterDto.setTotalScreens(show.getScreen().getTheater().getTotalScreens());
    showDto.setScreen(
            new ScreenDto(
                    show.getScreen().getId(),
                    show.getScreen().getName(),
                    show.getScreen().getTotalSeats(),
                    theaterDto
            )
    );
List<ShowSeatDto>seatDtos=availableSeats.stream().map(
        seat->{
            ShowSeatDto showSeatDto=new ShowSeatDto();
            showSeatDto.setId(seat.getId());
            showSeatDto.setStatus(seat.getStatus());
            showSeatDto.setPrice(seat.getPrice());
            SeatDto seatDto=new SeatDto();
            seatDto.setId(seat.getSeat().getId());
            seatDto.setSeatNumber(seat.getSeat().getSeatNumber());
            seatDto.setSeatType(seat.getSeat().getSeatType());
            showSeatDto.setSeat(seatDto);
            return showSeatDto;
        }
).toList();
showDto.setAvailableSeats(seatDtos);


    return showDto;

}

}
