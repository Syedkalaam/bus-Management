package com.bus_reservation_system.repository;

import com.bus_reservation_system.model.Trip;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends MongoRepository<Trip,String> {
    List<Trip> findBySourceContainingIgnoreCaseAndDestinationContainingIgnoreCase(String source, String destination);
}
