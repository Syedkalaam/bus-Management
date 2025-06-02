package com.bus_reservation_system.repository;

import com.bus_reservation_system.model.Bus;
import com.bus_reservation_system.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    private Trip sampleTrip;

    @BeforeEach
    void setUp() {
        tripRepository.deleteAll();

        Bus bus = Bus.builder()
                .id("bus1")
                .busNumber("TN-01-1234")
                .totalSeats(40)
                .build();

        sampleTrip = Trip.builder()
                .source("Chennai")
                .destination("Madurai")
                .travelDate("2025-05-25")
                .fare(450.0)
                .bus(bus)
                .build();

        tripRepository.save(sampleTrip);
    }

    @Test
    void testFindAllTrips() {
        List<Trip> trips = tripRepository.findAll();
        assertThat(trips).isNotEmpty();
        assertThat(trips.get(0).getSource()).isEqualTo("Chennai");
    }

    @Test
    void testFindTripById() {
        Optional<Trip> optionalTrip = tripRepository.findById(sampleTrip.getId());
        assertThat(optionalTrip).isPresent();
        assertThat(optionalTrip.get().getDestination()).isEqualTo("Madurai");
    }

    @Test
    void testDeleteTrip() {
        tripRepository.deleteById(sampleTrip.getId());
        Optional<Trip> optionalTrip = tripRepository.findById(sampleTrip.getId());
        assertThat(optionalTrip).isNotPresent();
    }
}
