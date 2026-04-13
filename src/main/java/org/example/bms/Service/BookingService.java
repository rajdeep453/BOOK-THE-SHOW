package org.example.bms.Service;

import jakarta.transaction.Transactional;
import org.example.bms.dto.*;
import org.example.bms.exception.ResourceNotFoundException;
import org.example.bms.exception.SeatUnavailableException;
import org.example.bms.model.*;
import org.example.bms.reposatory.BookingRepository;
import org.example.bms.reposatory.ShowRepository;
import org.example.bms.reposatory.ShowSeatRepository;
import org.example.bms.reposatory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class BookingService {
    @Autowired
  private   UserRepository userRepository;
    @Autowired
  private   ShowRepository showRepository;
    @Autowired
  private   ShowSeatRepository showSeatRepository;
    @Autowired
  private   BookingRepository bookingRepository;
    public BookingDto createBooking(BookingRequestDto bookingRequest) {
  User user=userRepository.findById(bookingRequest.getUserId()).orElseThrow(()->new ResourceNotFoundException("User Not Found"));

  Show show=showRepository.findById(bookingRequest.getShowId()).orElseThrow(()->new ResourceNotFoundException("Show Not Found"));
List<ShowSeat> selectedSeats=showSeatRepository.findAllById(bookingRequest.getSeatIds());
for(ShowSeat seat:selectedSeats){
    if(!"AVAILABLE".equals(seat.getStatus())){
        throw new SeatUnavailableException("Seat "+ seat.getSeat().getSeatNumber() + "Is Not Avalable");
    }
    seat.setStatus("Locked");
}
showSeatRepository.saveAll(selectedSeats);
Double totalAmount=selectedSeats.stream().mapToDouble(seat->seat.getPrice()).sum();
        Payment payment=new Payment();
        payment.setAmount(totalAmount);
        payment.setStatus("SUCCESS");
        payment.setPaymentTime(java.time.LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());
        payment.setPaymentMethod(bookingRequest.getPaymentMethod());
        Booking booking=new Booking();

        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("CONFIRMED");
        booking.setTotalAmount(totalAmount);
        booking.setBookingNumber(UUID.randomUUID().toString());
        booking.setPayment(payment);
        Booking saveBooking=bookingRepository.save(booking);
selectedSeats.forEach(seat->{
    seat.setStatus("Booked");
    seat.setBooking(saveBooking);
}

);
showSeatRepository.saveAll(selectedSeats);

return mapToBookingDto(saveBooking,selectedSeats);
    }
    public BookingDto  mapToBookingDto(Booking booking,List<ShowSeat>seats){
        BookingDto bookingDto=new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setBookingNumber(booking.getBookingNumber());
        bookingDto.setBookingTime(booking.getBookingTime());
        bookingDto.setStatus(bookingDto.getStatus());
        bookingDto.setTotalAmount(booking.getTotalAmount());
        //user

        UserDto userDto=new UserDto();
        userDto.setId(booking.getUser().getId());
        userDto.setName(booking.getUser().getName());
        userDto.setEmail(booking.getUser().getEmail());
        userDto.setPhoneNumber(booking.getUser().getPhoneNumber());
        bookingDto.setUser(userDto);

        // ShowDto
        ShowDto showDto=new ShowDto();
        showDto.setId(booking.getShow().getId());
        showDto.setStartTime(booking.getShow().getStartTime());
        showDto.setEndTime(booking.getShow().getEndTime());
        //Movie Dto
        MovieDto movieDto = new MovieDto();
        movieDto.setId(booking.getShow().getMovie().getId());
        movieDto.setTitle(booking.getShow().getMovie().getTitle());
        movieDto.setDescription(booking.getShow().getMovie().getDescription());
        movieDto.setLanguage(booking.getShow().getMovie().getLanguage());
        movieDto.setGenre(booking.getShow().getMovie().getGenre());
        movieDto.setDurationMins(booking.getShow().getMovie().getDurationMins());
        movieDto.setReleaseDate(booking.getShow().getMovie().getReleaseDate());
        movieDto.setPosterUrl(booking.getShow().getMovie().getPosterUrl());
        showDto.setMovie(movieDto);
        //Screendto
        ScreenDto screenDto=new ScreenDto();
        screenDto.setId(booking.getShow().getScreen().getId());
        screenDto.setName(booking.getShow().getScreen().getName());
        screenDto.setTotalSeats(booking.getShow().getScreen().getTotalSeats());
        //Theatredto
        TheaterDto theaterDto=new TheaterDto();
        theaterDto.setId(bookingDto.getShow().getScreen().getTheater().getId());
        theaterDto.setName(bookingDto.getShow().getScreen().getTheater().getName());
        theaterDto.setAddress(bookingDto.getShow().getScreen().getTheater().getAddress());
        theaterDto.setCity(bookingDto.getShow().getScreen().getTheater().getCity());
        theaterDto.setTotalScreens(bookingDto.getShow().getScreen().getTheater().getTotalScreens());

        screenDto.setTheater(theaterDto);
        showDto.setScreen(screenDto);
        bookingDto.setShow(showDto);
List<ShowSeatDto> seatDtos=seats.stream().map(seat->{
    ShowSeatDto showSeatDtoDto=new ShowSeatDto();
    showSeatDtoDto.setId(seat.getId());
    showSeatDtoDto.setPrice(seat.getPrice());
    showSeatDtoDto.setStatus(seat.getStatus());
    SeatDto baseseat=new SeatDto();
    baseseat.setId(seat.getSeat().getId());
    baseseat.setSeatType(seat.getSeat().getSeatType());

    baseseat.setSeatNumber(seat.getSeat().getSeatNumber());

showSeatDtoDto.setSeat(baseseat);
return showSeatDtoDto;
}).collect(Collectors.toList());
bookingDto.setSeats(seatDtos);

        if(booking.getPayment()!=null)
        {
            PaymentDto paymentDto=new PaymentDto();
            paymentDto.setId(booking.getPayment().getId());
            paymentDto.setAmount(booking.getPayment().getAmount());
            paymentDto.setPaymentMethod(booking.getPayment().getPaymentMethod());
            paymentDto.setPaymentTime(booking.getPayment().getPaymentTime());
            paymentDto.setStatus(booking.getPayment().getStatus());
            paymentDto.setTransactionId(booking.getPayment().getTransactionId());
            bookingDto.setPayment(paymentDto);
        }
        return bookingDto;


    }
}
