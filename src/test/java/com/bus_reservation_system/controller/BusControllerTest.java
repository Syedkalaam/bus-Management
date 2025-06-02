package com.bus_reservation_system.controller;

import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.service.BusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(BusController.class)
class BusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusService busService;

    private Bus sampleBus;

    @BeforeEach
    void setup() {
        sampleBus = Bus.builder()
                .id("bus123")
                .busNumber("TN07AA1111")
                .operator("KPN")
                .totalSeats(40)
                .departureTime("9:00 AM")
                .arrivalTime("3:00 PM")
                .tripId("trip123")
                .build();
    }

    @Test
    void testShowAddForm() throws Exception {
        mockMvc.perform(get("/buses/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bus/bus-form"))
                .andExpect(model().attributeExists("bus"));
    }

    @Test
    void testAddBus() throws Exception {
        mockMvc.perform(post("/buses/add")
                        .param("busNumber", "TN07AA1111")
                        .param("operator", "KPN")
                        .param("totalSeats", "40")
                        .param("departureTime", "9:00 AM")
                        .param("arrivalTime", "3:00 PM"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buses/list"));
    }

    @Test
    void testViewAllBuses() throws Exception {
        when(busService.getAllBuses()).thenReturn(Arrays.asList(sampleBus));

        mockMvc.perform(get("/buses/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bus/bus-list"))
                .andExpect(model().attributeExists("buses"));
    }

    @Test
    void testShowBusDetail() throws Exception {
        when(busService.getBusById("bus123")).thenReturn(Optional.of(sampleBus));

        mockMvc.perform(get("/buses/detail/bus123"))
                .andExpect(status().isOk())
                .andExpect(view().name("bus/bus-detail"))
                .andExpect(model().attributeExists("bus"));
    }

    @Test
    void testShowUpdateForm() throws Exception {
        when(busService.getBusById("bus123")).thenReturn(Optional.of(sampleBus));

        mockMvc.perform(get("/buses/update/bus123"))
                .andExpect(status().isOk())
                .andExpect(view().name("bus/bus-update"))
                .andExpect(model().attributeExists("bus"));
    }

    @Test
    void testUpdateBusById() throws Exception {
        mockMvc.perform(post("/buses/update/bus123")
                        .param("busNumber", "TN07AA9999")
                        .param("operator", "SRS")
                        .param("totalSeats", "50")
                        .param("departureTime", "11:00 AM")
                        .param("arrivalTime", "6:00 PM"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buses/list"));
    }

    @Test
    void testDeleteBusById() throws Exception {
        doNothing().when(busService).deleteBusById("bus123");

        mockMvc.perform(get("/buses/delete/bus123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buses/list"));
    }
}
