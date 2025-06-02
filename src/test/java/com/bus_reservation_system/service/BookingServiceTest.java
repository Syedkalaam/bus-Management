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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BusRepository busRepository;

    private Booking booking;
    private User user;
    private Trip trip;
    private Bus bus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bus = Bus.builder()
                .id("bus1")
                .busNumber("TN01AB1234")
                .operator("Operator A")
                .totalSeats(30)
                .departureTime("10:00")
                .arrivalTime("14:00")
                .build();

        trip = Trip.builder()
                .id("trip1")
                .source("Chennai")
                .destination("Coimbatore")
                .travelDate("2025-05-30")
                .fare(500.0)
                .bus(bus)
                .bookedSeats(new HashSet<>())
                .users(new ArrayList<>())
                .build();

        user = User.builder()
                .id("user1")
                .username("Test User")
                .email("test@example.com")
                .bookings(null)
                .trips(null)
                .bus(null)
                .build();

        booking = Booking.builder()
                .id("booking1")
                .trip(trip)
                .userId(user.getId())
                .seatNumbers(new HashSet<>(Arrays.asList(1, 2)))
                .status("confirmed")
                .bookedAt(LocalDateTime.now().toString())
                .build();
    }

    @Test
    void createBooking_success() throws Exception {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(tripRepository.findById(trip.getId())).thenReturn(Optional.of(trip));
        when(busRepository.findById(bus.getId())).thenReturn(Optional.of(bus));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);
        when(tripRepository.save(any(Trip.class))).thenAnswer(i -> i.getArguments()[0]);
        when(busRepository.save(any(Bus.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        Booking created = bookingService.createBooking(booking);

        assertThat(created.getStatus()).isEqualTo("confirmed");
        verify(bookingRepository).save(any(Booking.class));
        verify(tripRepository).save(any(Trip.class));
        verify(busRepository).save(any(Bus.class));
        verify(userRepository).save(any(User.class));
        assertThat(trip.getBookedSeats()).containsAll(booking.getSeatNumbers());
        assertThat(trip.getUsers()).contains(user);
    }

    @Test
    void createBooking_userNotFound_throwsException() {
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(booking))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("user not found");
    }

    @Test
    void cancelBooking_success() throws Exception {
        booking.setStatus("confirmed");
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(tripRepository.save(any(Trip.class))).thenAnswer(i -> i.getArguments()[0]);
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        booking.getTrip().getBookedSeats().addAll(booking.getSeatNumbers());

        bookingService.cancelBooking(booking.getId());

        assertThat(booking.getStatus()).isEqualTo("CANCELLED");
        assertThat(booking.getTrip().getBookedSeats()).doesNotContainAnyElementsOf(booking.getSeatNumbers());
        verify(bookingRepository).save(booking);
    }

    @Test
    void cancelBooking_alreadyCancelled_throwsException() {
        booking.setStatus("CANCELLED");
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(booking.getId()))
                .isInstanceOf(Exception.class)
                .hasMessage("Booking already cancelled");
    }

    @Test
    void deleteBookingById_success() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(tripRepository.findById(trip.getId())).thenReturn(Optional.of(trip));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(bookingRepository).deleteById(booking.getId());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(tripRepository.save(any(Trip.class))).thenAnswer(i -> i.getArguments()[0]);

        bookingService.deleteBookingById(booking.getId());

        assertThat(trip.getBookedSeats()).doesNotContainAnyElementsOf(booking.getSeatNumbers());
        assertThat(trip.getUsers()).doesNotContain(user);
        assertThat(user.getBus()).isNull();
        assertThat(user.getBookings()).isNull();
        assertThat(user.getTrips()).isNull();

        verify(bookingRepository).deleteById(booking.getId());
    }
}
