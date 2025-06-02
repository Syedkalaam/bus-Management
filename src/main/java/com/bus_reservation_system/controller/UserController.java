package com.bus_reservation_system.controller;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Booking;
import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.model.User;
import com.bus_reservation_system.service.BookingService;
import com.bus_reservation_system.service.BusService;
import com.bus_reservation_system.service.TripService;
import com.bus_reservation_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TripService tripService;

    @Autowired
    private BusService busService;



    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/user-register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/details/{id}")
    public String showUserDetailForm(@PathVariable String id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "user/user-details";
    }

    @GetMapping("/list")
    public String viewUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "user/user-list";
    }

    @GetMapping("/delete/{id}")
    public String deleteuser(@PathVariable String id) {
        userService.deleteUserById(id);
        return "redirect:/users/list";
    }

    @GetMapping("/update/{id}")
    public String showupdateform(@PathVariable String id,Model model){
        User user=userService.getUserById(id).orElseThrow(
                ()->new ResourceNotFoundException("user not found")
        );
        model.addAttribute("user",user);
        return "user/user-update";
    }

    @PostMapping("update/{id}")
    public String updateUserById(@PathVariable String id,@ModelAttribute User user){
        userService.updateUserbyId(id,user);
        return "redirect:/users/list";
    }
}
