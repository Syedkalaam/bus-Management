package com.bus_reservation_system.repository;

import com.bus_reservation_system.model.Bus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusRepository extends MongoRepository<Bus,String> {
}
