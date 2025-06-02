package com.bus_reservation_system.service;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.model.User;
import com.bus_reservation_system.repository.BusRepository;
import com.bus_reservation_system.repository.TripRepository;
import com.bus_reservation_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {
    @Autowired
    private BusRepository busRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    public Bus saveBus(Bus bus) {
        return busRepository.save(bus);
    }

    public Optional<Bus> getBusById(String id) {
        return busRepository.findById(id);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Bus updateBusbyId(String id,Bus updatedbus){
        Bus bus=busRepository.findById(id).orElseThrow();
        updatedbus.setId(id);
        updatedbus.setTripId(bus.getTripId());
        return busRepository.save(updatedbus);
    }

    public void deleteBusById(String id) {
        Bus bus=busRepository.findById(id)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("User not found")
                );
        if (bus.getTripId()!=null){
            Trip trip=tripRepository.findById(bus.getTripId())
                    .orElseThrow(()-> new ResourceNotFoundException("Trip not found"));
            trip.setBus(null);
            tripRepository.save(trip);
        }

        List<User> users=userRepository.findAll();
        for (User user:users){
            if (user.getBus().getId().equals(id)){
                user.setBus(null);
                userRepository.save(user);
            }
        }
        busRepository.deleteById(id);
    }
}