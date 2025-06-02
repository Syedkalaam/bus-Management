package com.bus_reservation_system.repository;

import com.bus_reservation_system.model.Bus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class BusRepositoryTest {

    @Autowired
    private BusRepository busRepository;

    @Test
    void testSaveBus() {
        Bus bus = Bus.builder()
                .busNumber("TN45AB1234")
                .operator("KPN Travels")
                .totalSeats(40)
                .departureTime("10:00 AM")
                .arrivalTime("4:00 PM")
                .tripId(null)
                .build();

        Bus savedBus = busRepository.save(bus);

        assertThat(savedBus).isNotNull();
        assertThat(savedBus.getId()).isNotNull();
        assertThat(savedBus.getBusNumber()).isEqualTo("TN45AB1234");
    }

    @Test
    void testFindById() {
        Bus bus = Bus.builder()
                .busNumber("TN10XY5678")
                .operator("SRM Travels")
                .totalSeats(30)
                .departureTime("5:00 AM")
                .arrivalTime("11:00 AM")
                .tripId(null)
                .build();

        Bus savedBus = busRepository.save(bus);
        Optional<Bus> foundBus = busRepository.findById(savedBus.getId());

        assertThat(foundBus).isPresent();
        assertThat(foundBus.get().getOperator()).isEqualTo("SRM Travels");
    }

    @Test
    void testFindAll() {
        Bus bus1 = Bus.builder()
                .busNumber("TN01ZZ9999")
                .operator("Kallada")
                .totalSeats(45)
                .departureTime("2:00 PM")
                .arrivalTime("8:00 PM")
                .tripId(null)
                .build();

        Bus bus2 = Bus.builder()
                .busNumber("TN02YY8888")
                .operator("Parveen Travels")
                .totalSeats(35)
                .departureTime("6:00 PM")
                .arrivalTime("12:00 AM")
                .tripId(null)
                .build();

        busRepository.save(bus1);
        busRepository.save(bus2);

        List<Bus> allBuses = busRepository.findAll();
        assertThat(allBuses.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testDeleteById() {
        Bus bus = Bus.builder()
                .busNumber("TN03XX7777")
                .operator("ABT Travels")
                .totalSeats(50)
                .departureTime("9:00 AM")
                .arrivalTime("3:00 PM")
                .tripId(null)
                .build();

        Bus savedBus = busRepository.save(bus);
        String id = savedBus.getId();

        busRepository.deleteById(id);

        Optional<Bus> deleted = busRepository.findById(id);
        assertThat(deleted).isNotPresent();
    }
}
