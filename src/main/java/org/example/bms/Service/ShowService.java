package org.example.bms.Service;


import org.example.bms.dto.ShowDto;
import org.example.bms.model.Movie;
import org.example.bms.model.Screen;
import org.example.bms.model.Show;
import org.example.bms.reposatory.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    }
public ShowDto mapToDto(Show show){

}
}
