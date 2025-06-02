package com.bus_reservation_system.service;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Booking;
import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.model.User;
import com.bus_reservation_system.repository.BookingRepository;
import com.bus_reservation_system.repository.BusRepository;
import com.bus_reservation_system.repository.TripRepository;
import com.bus_reservation_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusRepository busRepository;


    public Booking createBooking(Booking booking) throws Exception {

        booking.setBookedAt(LocalDateTime.now().toString());
        booking.setStatus("confirmed");
        User user= userRepository.findById(booking.getUserId()).orElseThrow(
                ()->new ResourceNotFoundException("user not found")
        );
        if (user.getBookings()==null){
            bookingRepository.save(booking);
        }else {
            booking.setId(user.getBookings().getId());
            bookingRepository.save(booking);
        }

        Trip trip=tripRepository.findById(booking.getTrip().getId()).orElseThrow(
                ()->new ResourceNotFoundException("Trip not found")
        );

        trip.getBookedSeats().addAll(booking.getSeatNumbers());
        trip.getUsers().add(user);

        Trip savedtrip=tripRepository.save(trip);

        Bus bus= busRepository.findById(savedtrip.getBus().getId()).orElseThrow(
                ()->new ResourceNotFoundException("Bus not found")
        );
        bus.setTripId(savedtrip.getId());
        busRepository.save(bus);
        user.setBookings(booking);
        user.setTrips(savedtrip);
        user.setBus(bus);
        userRepository.save(user);


        return booking;
    }

    public Optional<Booking> getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }


    public void cancelBooking(String bookingId) throws Exception {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            throw new Exception("Booking not found");
        }
        Booking booking = optionalBooking.get();

        if (booking.getStatus().equals("CANCELLED")) {
            throw new Exception("Booking already cancelled");
        }

        Trip trip = booking.getTrip();
        if (trip == null) {
            throw new Exception("Trip info missing");
        }


        trip.getBookedSeats().removeAll(booking.getSeatNumbers());
        tripRepository.save(trip);

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    public void deleteBookingById(String id){
        Booking booking=bookingRepository
                .findById(id).orElseThrow(
                        ()->new ResourceNotFoundException("Booking not found")
                );
        Trip trip=tripRepository.findById(booking.getTrip().getId()).orElseThrow(
                ()->new ResourceNotFoundException("Trip not found")
        );
        trip.getBookedSeats().removeAll(booking.getSeatNumbers());
        trip.getUsers().removeIf(user->user.getId().equals(booking.getUserId()));
        User user=userRepository.findById(booking.getUserId()).orElseThrow(
                ()->new ResourceNotFoundException("User not found")
        );
        user.setBus(null);
        user.setBookings(null);
        user.setTrips(null);
        userRepository.save(user);
        trip.setBus(null);
        tripRepository.save(trip);
        bookingRepository.deleteById(id);
    }
}
