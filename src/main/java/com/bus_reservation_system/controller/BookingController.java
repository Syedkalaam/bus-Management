package com.bus_reservation_system.controller;

import com.bus_reservation_system.model.Booking;
import com.bus_reservation_system.service.BookingService;
import com.bus_reservation_system.service.TripService;
import com.bus_reservation_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TripService tripService;

    @Autowired
    private UserService userService;

    @GetMapping("/new")
    public String showBookingForm(Model model) {
        model.addAttribute("booking", new Booking());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("trips", tripService.getAllTrips());
        return "booking/booking-form";
    }


    @PostMapping("/new")
    public String createBooking(@ModelAttribute Booking booking, Model model) {
        try {
            bookingService.createBooking(booking);
            String userId=booking.getUserId();
            return "redirect:/users/details/" +userId;
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("booking", booking);
            return "booking/booking-form";
        }
    }


    @GetMapping("/list")
    public String viewBookings(Model model) {
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "booking/booking-list";
    }

    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable String id, Model model) {
        try {
            bookingService.cancelBooking(id);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/bookings/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBookingById(@PathVariable String id){
        bookingService.deleteBookingById(id);
        return "redirect:/bookings/list";
    }
}
