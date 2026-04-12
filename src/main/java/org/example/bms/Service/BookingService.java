package org.example.bms.Service;

import jakarta.transaction.Transactional;
import org.example.bms.dto.BookingDto;
import org.example.bms.dto.BookingRequestDto;
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
        throw new SeatUnavailableException("Seat "+ seat.getSeat().getSeatNumber() + "Is Not Avalable")
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
selectedSeats.forEach(seat->{
    seat.setStatus("Booked");
    seat.setBooking(booking);
}

);
showSeatRepository.saveAll(selectedSeats);

return mapToBookingDto()
    }
    public BookingDto  mapToBookingDto(Booking booking,List<ShowSeat>seats){
        
    }
}
