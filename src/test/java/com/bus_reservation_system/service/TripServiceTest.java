package com.bus_reservation_system.service;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.*;
import com.bus_reservation_system.repository.BookingRepository;
import com.bus_reservation_system.repository.BusRepository;
import com.bus_reservation_system.repository.TripRepository;
import com.bus_reservation_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @InjectMocks
    private TripService tripService;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private BusRepository busRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    private Trip trip;
    private Bus bus;

    @BeforeEach
    void setUp() {
        bus = Bus.builder()
                .id("bus123")
                .busNumber("TN-01-4567")
                .totalSeats(50)
                .build();

        trip = Trip.builder()
                .id("trip123")
                .source("Chennai")
                .destination("Madurai")
                .travelDate("2025-05-25")
                .fare(500.0)
                .bus(bus)
                .users(new ArrayList<>())
                .bookedSeats(new HashSet<>())
                .build();
    }

    @Test
    void testSaveTrip() {
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);
        when(busRepository.findById(bus.getId())).thenReturn(Optional.of(bus));

        tripService.saveTrip(trip);

        verify(tripRepository, times(1)).save(trip);
        verify(busRepository, times(1)).save(bus);
        assertThat(bus.getTripId()).isEqualTo(trip.getId());
    }

    @Test
    void testGetTripById_Exists() {
        when(tripRepository.findById("trip123")).thenReturn(Optional.of(trip));

        Optional<Trip> result = tripService.getTripById("trip123");

        assertThat(result).isPresent();
        assertThat(result.get().getSource()).isEqualTo("Chennai");
    }

    @Test
    void testGetTripById_NotFound() {
        when(tripRepository.findById("invalid")).thenReturn(Optional.empty());

        Optional<Trip> result = tripService.getTripById("invalid");

        assertThat(result).isEmpty();
    }

    @Test
    void testUpdateTripById() {
        Trip updatedTrip = Trip.builder().source("Coimbatore").build();
        when(tripRepository.findById("trip123")).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(updatedTrip);

        Trip result = tripService.updateTripById("trip123", updatedTrip);

        assertThat(result.getSource()).isEqualTo("Coimbatore");
    }

    @Test
    void testDeleteTripById() {
        trip.setUsers(List.of(User.builder().id("u1").build()));
        Booking booking = Booking.builder().id("b1").trip(trip).build();

        when(tripRepository.findById("trip123")).thenReturn(Optional.of(trip));
        when(busRepository.findById(bus.getId())).thenReturn(Optional.of(bus));
        when(userRepository.findAll()).thenReturn(List.of(User.builder().id("u1").build()));
        when(bookingRepository.findAll()).thenReturn(List.of(booking));

        tripService.deleteTripById("trip123");

        verify(tripRepository, times(1)).deleteById("trip123");
        verify(bookingRepository, times(1)).deleteById("b1");
        verify(busRepository, times(1)).save(any(Bus.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindBySourceAndDestination() {
        when(tripRepository.findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCase("chen", "mad"))
                .thenReturn(List.of(trip));

        List<Trip> result = tripService.findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCase("chen", "mad");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDestination()).isEqualTo("Madurai");
    }
}
