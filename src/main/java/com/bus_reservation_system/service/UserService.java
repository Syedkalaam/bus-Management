package com.bus_reservation_system.service;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Booking;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.model.User;
import com.bus_reservation_system.repository.BookingRepository;
import com.bus_reservation_system.repository.TripRepository;
import com.bus_reservation_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_"+user.getRoles().toUpperCase());
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUserById(String id) {
        User user=userRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("user not found")
        );
        if (user.getBookings()!=null){
            Booking booking=bookingRepository.findById(user.getBookings().getId()).orElseThrow(
                    ()->new ResourceNotFoundException("Booking not found")
            );
            Trip trip=tripRepository.findById(booking.getTrip().getId()).orElseThrow(
                    ()->new ResourceNotFoundException("Trip not found")
            );
            trip.getBookedSeats().removeIf(seat -> booking.getSeatNumbers().contains(seat));
            trip.getUsers().removeIf(user1 -> user1.getId().equals(id));
            tripRepository.save(trip);
            bookingRepository.deleteById(booking.getId());
        }

        userRepository.deleteById(id);
    }

    public User updateUserbyId(String id, User updateduser){

        updateduser.setPassword(passwordEncoder.encode(updateduser.getPassword()));
        updateduser.setRoles("ROLE_"+updateduser.getRoles().toUpperCase());
        updateduser.setEnabled(true);

        User user=getUserById(id).orElseThrow();
        updateduser.setId(user.getId());
        updateduser.setBookings(user.getBookings());
        updateduser.setBus(user.getBus());
        updateduser.setTrips(user.getTrips());
        return userRepository.save(updateduser);
    }
}
