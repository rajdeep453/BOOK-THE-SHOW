package org.example.bms.Controller;

import jakarta.validation.Valid;
import org.example.bms.dto.BookingRequestDto;
import org.example.bms.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@GetMapping
public class BookingController {
    @Autowired
    BookingService  bookingService;
    public ResponseEntity<Booking>createBooking(@Valid  @RequestBody BookingRequestDto bookingRequestDto){
return new ResponseEntity<>(bookingService.createBooking(bookingRequestDto), HttpStatus.CREATED);
    }
}
