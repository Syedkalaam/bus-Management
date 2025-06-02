package com.bus_reservation_system.controller;

import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.service.BusService;
import com.bus_reservation_system.service.TripService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TripController.class)
class TripControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TripService tripService;

    @MockBean
    private BusService busService;

    @Test
    void testShowTripForm() throws Exception {
        when(busService.getAllBuses()).thenReturn(Collections.singletonList(new Bus()));

        mockMvc.perform(get("/trips/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trip/trip-form"))
                .andExpect(model().attributeExists("trip"))
                .andExpect(model().attributeExists("buses"));
    }

    @Test
    void testCreateTrip() throws Exception {
        mockMvc.perform(post("/trips/add")
                        .param("source", "Chennai")
                        .param("destination", "Madurai")
                        .param("travelDate", "2025-05-25")
                        .param("fare", "500"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trips/list"));

        verify(tripService, times(1)).saveTrip(any(Trip.class));
    }

    @Test
    void testListTrips() throws Exception {
        when(tripService.getAllTrips()).thenReturn(List.of(new Trip()));
        when(busService.getAllBuses()).thenReturn(List.of(new Bus()));

        mockMvc.perform(get("/trips/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trip/trip-list"))
                .andExpect(model().attributeExists("trips"))
                .andExpect(model().attributeExists("bus"));
    }

    @Test
    void testShowUpdateTripForm() throws Exception {
        Trip trip = Trip.builder().id("trip1").build();
        when(tripService.getTripById("trip1")).thenReturn(Optional.of(trip));
        when(busService.getAllBuses()).thenReturn(List.of(new Bus()));

        mockMvc.perform(get("/trips/update/trip1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trip/trip-update"))
                .andExpect(model().attributeExists("trip"))
                .andExpect(model().attributeExists("bus"));
    }

    @Test
    void testUpdateTripById() throws Exception {
        mockMvc.perform(post("/trips/update/trip1")
                        .param("source", "Chennai")
                        .param("destination", "Coimbatore")
                        .param("travelDate", "2025-06-01")
                        .param("fare", "650"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trips/list"));

        verify(tripService, times(1)).updateTripById(eq("trip1"), any(Trip.class));
    }

    @Test
    void testDeleteTrip() throws Exception {
        mockMvc.perform(get("/trips/delete/trip1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trips/list"));

        verify(tripService, times(1)).deleteTripById("trip1");
    }
}
