package com.bus_reservation_system.controller;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.service.BusService;
import com.bus_reservation_system.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @Autowired
    private BusService busService;

    @GetMapping("/add")
    public String showTripForm(Model model) {
        model.addAttribute("trip", new Trip());
        model.addAttribute("buses", busService.getAllBuses());
        return "trip/trip-form";
    }

    @PostMapping("/add")
    public String createTrip(@ModelAttribute Trip trip) {
        tripService.saveTrip(trip);
        return "redirect:/trips/list";
    }

    @GetMapping("/list")
    public String listTrips(Model model) {
        List<Trip> allTrips = tripService.getAllTrips();
        List<Bus> allBuses = busService.getAllBuses();

        model.addAttribute("trips", allTrips);
        model.addAttribute("bus", allBuses);
        return "trip/trip-list";
    }

    @GetMapping("/update/{id}")
    public String ShowUpdateTripform(@PathVariable String id,Model model){
        Trip trip=tripService.getTripById(id).orElseThrow(
                ()->new ResourceNotFoundException("Trip not found")
        );
        List<Bus> bus=busService.getAllBuses();
        model.addAttribute("trip",trip);
        model.addAttribute("bus",bus);

        return "trip/trip-update";
    }

    @PostMapping("/update/{id}")
    public String updateTripById(@PathVariable String id,@ModelAttribute Trip trip){
        tripService.updateTripById(id, trip);
        return "redirect:/trips/list";
    }

    @GetMapping("/delete/{id}")
    public String deletetrip(@PathVariable String id) {
        tripService.deleteTripById(id);
        return "redirect:/trips/list";
    }
}
