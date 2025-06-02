package com.bus_reservation_system.service;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.model.User;
import com.bus_reservation_system.repository.BusRepository;
import com.bus_reservation_system.repository.TripRepository;
import com.bus_reservation_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

class BusServiceTest {

    @Mock
    private BusRepository busRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BusService busService;

    private Bus bus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bus = Bus.builder()
                .id("bus123")
                .busNumber("TN07AA1234")
                .operator("KPN Travels")
                .totalSeats(40)
                .departureTime("10:00 AM")
                .arrivalTime("4:00 PM")
                .tripId("trip456")
                .build();
    }

    @Test
    void testSaveBus() {
        when(busRepository.save(bus)).thenReturn(bus);
        Bus savedBus = busService.saveBus(bus);

        assertThat(savedBus).isNotNull();
        assertThat(savedBus.getBusNumber()).isEqualTo("TN07AA1234");
    }

    @Test
    void testGetBusById() {
        when(busRepository.findById("bus123")).thenReturn(Optional.of(bus));
        Optional<Bus> found = busService.getBusById("bus123");

        assertThat(found).isPresent();
        assertThat(found.get().getOperator()).isEqualTo("KPN Travels");
    }

    @Test
    void testGetAllBuses() {
        when(busRepository.findAll()).thenReturn(Arrays.asList(bus));
        List<Bus> buses = busService.getAllBuses();

        assertThat(buses).hasSize(1);
        assertThat(buses.get(0).getId()).isEqualTo("bus123");
    }

    @Test
    void testUpdateBusById() {
        Bus updated = Bus.builder()
                .busNumber("TN07AA9999")
                .operator("Parveen Travels")
                .totalSeats(45)
                .departureTime("1:00 PM")
                .arrivalTime("7:00 PM")
                .build();

        when(busRepository.findById("bus123")).thenReturn(Optional.of(bus));
        when(busRepository.save(any(Bus.class))).thenReturn(updated);

        Bus result = busService.updateBusbyId("bus123", updated);

        assertThat(result.getBusNumber()).isEqualTo("TN07AA9999");
        assertThat(result.getTripId()).isEqualTo("trip456");
    }

    @Test
    void testDeleteBusById_WhenTripAndUsersExist() {
        Trip trip = new Trip();
        trip.setId("trip456");

        User user = new User();
        user.setId("user1");
        user.setBus(bus);

        when(busRepository.findById("bus123")).thenReturn(Optional.of(bus));
        when(tripRepository.findById("trip456")).thenReturn(Optional.of(trip));
        when(userRepository.findAll()).thenReturn(List.of(user));

        busService.deleteBusById("bus123");

        assertThat(trip.getBus()).isNull();
        assertThat(user.getBus()).isNull();

        verify(tripRepository).save(trip);
        verify(userRepository).save(user);
        verify(busRepository).deleteById("bus123");
    }

    @Test
    void testDeleteBusById_WhenBusNotFound() {
        when(busRepository.findById("bus123")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> busService.deleteBusById("bus123"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("User not found");
    }
}
