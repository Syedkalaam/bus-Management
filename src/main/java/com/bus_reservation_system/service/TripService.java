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

import java.util.List;
import java.util.Optional;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private  BusRepository busRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;


    public void saveTrip(Trip trip) {




       Trip savedtrip= tripRepository.save(trip);
       Bus updatedBus = busRepository.findById(savedtrip.getBus().getId())
               .orElseThrow(
                       ()->new ResourceNotFoundException("Bus not found")
               );
       updatedBus.setTripId(savedtrip.getId());
       busRepository.save(updatedBus);

    }

    public Optional<Trip> getTripById(String id) {
        return tripRepository.findById(id);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip updateTripById(String id, Trip updatedTrip){
        getTripById(id);
        updatedTrip.setId(id);
        return tripRepository.save(updatedTrip);
    }

    public void deleteTripById(String id) {
        Trip trip = tripRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("Trip not found")
        );
        Bus bus= busRepository.findById(trip.getBus().getId()).orElseThrow(
                ()->new ResourceNotFoundException("Bus not found")
        );
        List<User> alluser=userRepository.findAll();
        List<User> user= trip.getUsers();
        for (User user1:user){
            for (User user2:alluser){
                if (user1.getId().equals(user2.getId())){
                    user2.setTrips(null);
                    userRepository.save(user2);
                }
            }
        }
        bus.setTripId(null);
        busRepository.save(bus);
        List<Booking>bookings=bookingRepository.findAll();
        for (Booking booking:bookings){
            if (booking.getTrip().getId().equals(id)){
                bookingRepository.deleteById(booking.getId());
            }
        }
        tripRepository.deleteById(id);
    }

    public List<Trip> findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCase(String source, String destination){
        return tripRepository.findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCase(source, destination);
    }
}
