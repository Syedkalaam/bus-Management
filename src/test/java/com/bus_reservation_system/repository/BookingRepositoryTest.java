package com.bus_reservation_system.repository;

import com.bus_reservation_system.model.Booking;
import com.bus_reservation_system.model.Trip;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    private Trip sampleTrip;

    @BeforeEach
    void setup() {
        bookingRepository.deleteAll();
        tripRepository.deleteAll();

        sampleTrip = Trip.builder()
                .source("Chennai")
                .destination("Coimbatore")
                .travelDate("2025-05-30")
                .fare(550.0)
                .build();

        sampleTrip = tripRepository.save(sampleTrip);
    }

    @Test
    void testSaveBooking() {
        Booking booking = Booking.builder()
                .trip(sampleTrip)
                .userId("user123")
                .seatNumbers(new HashSet<>(Arrays.asList(1, 2)))
                .bookedAt("2025-05-24T10:00:00")
                .status("CONFIRMED")
                .build();

        Booking saved = bookingRepository.save(booking);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserId()).isEqualTo("user123");
        assertThat(saved.getSeatNumbers()).containsExactlyInAnyOrder(1, 2);
        assertThat(saved.getTrip().getId()).isEqualTo(sampleTrip.getId());
    }

    @Test
    void testFindById() {
        Booking booking = Booking.builder()
                .trip(sampleTrip)
                .userId("user456")
                .seatNumbers(Set.of(5))
                .bookedAt("2025-05-24T11:00:00")
                .status("PENDING")
                .build();

        bookingRepository.save(booking);

        Optional<Booking> found = bookingRepository.findById(booking.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo("user456");
    }

    @Test
    void testDeleteById() {
        Booking booking = Booking.builder()
                .trip(sampleTrip)
                .userId("user789")
                .seatNumbers(Set.of(10))
                .bookedAt("2025-05-24T12:00:00")
                .status("CANCELLED")
                .build();

        booking = bookingRepository.save(booking);
        bookingRepository.deleteById(booking.getId());

        Optional<Booking> deleted = bookingRepository.findById(booking.getId());
        assertThat(deleted).isNotPresent();
    }

    @Test
    void testFindAll() {
        Booking booking1 = Booking.builder()
                .trip(sampleTrip)
                .userId("user1")
                .seatNumbers(Set.of(3))
                .bookedAt("2025-05-24T13:00:00")
                .status("CONFIRMED")
                .build();

        Booking booking2 = Booking.builder()
                .trip(sampleTrip)
                .userId("user2")
                .seatNumbers(Set.of(4))
                .bookedAt("2025-05-24T14:00:00")
                .status("CONFIRMED")
                .build();

        bookingRepository.saveAll(List.of(booking1, booking2));

        List<Booking> bookings = bookingRepository.findAll();
        assertThat(bookings).hasSize(2);
    }
}
