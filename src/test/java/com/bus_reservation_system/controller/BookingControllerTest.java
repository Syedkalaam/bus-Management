package com.bus_reservation_system.controller;

import com.bus_reservation_system.model.Booking;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.model.User;
import com.bus_reservation_system.service.BookingService;
import com.bus_reservation_system.service.TripService;
import com.bus_reservation_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private UserService userService;

    @MockBean
    private TripService tripService;

    private Booking booking;
    private User user;
    private Trip trip;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id("user1")
                .username("Test User")
                .build();

        trip = Trip.builder()
                .id("trip1")
                .source("CityA")
                .destination("CityB")
                .build();

        booking = Booking.builder()
                .id("booking1")
                .userId(user.getId())
                .trip(trip)
                .build();
    }

    @Test
    void showBookingForm_displaysForm() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));
        when(tripService.getAllTrips()).thenReturn(Collections.singletonList(trip));

        mockMvc.perform(get("/bookings/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/booking-form"))
                .andExpect(model().attributeExists("booking"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("trips"));
    }

    @Test
    void createBooking_success_redirectsToUserDetails() throws Exception {
        doNothing().when(bookingService).createBooking(any(Booking.class));

        mockMvc.perform(post("/bookings/new")
                        .param("userId", user.getId())
                        .param("trip.id", trip.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/details/" + user.getId()));
    }

    @Test
    void createBooking_failure_showsFormWithError() throws Exception {
        doThrow(new Exception("Booking error")).when(bookingService).createBooking(any(Booking.class));
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));
        when(tripService.getAllTrips()).thenReturn(Collections.singletonList(trip));

        mockMvc.perform(post("/bookings/new")
                        .param("userId", user.getId())
                        .param("trip.id", trip.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/booking-form"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeExists("booking"));
    }

    @Test
    void viewBookings_showsList() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(Collections.singletonList(booking));

        mockMvc.perform(get("/bookings/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("booking/booking-list"))
                .andExpect(model().attributeExists("bookings"));
    }

    @Test
    void cancelBooking_redirectsToList() throws Exception {
        doNothing().when(bookingService).cancelBooking("booking1");

        mockMvc.perform(post("/bookings/cancel/booking1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));
    }

    @Test
    void cancelBooking_withError_redirectsToListWithError() throws Exception {
        doThrow(new Exception("Cancel error")).when(bookingService).cancelBooking("booking1");

        mockMvc.perform(post("/bookings/cancel/booking1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));
        // Note: Model attribute error won't survive redirect; to test it, you'd need a flash attribute.
    }

    @Test
    void deleteBooking_redirectsToList() throws Exception {
        doNothing().when(bookingService).deleteBookingById("booking1");

        mockMvc.perform(get("/bookings/delete/booking1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bookings/list"));
    }
}
