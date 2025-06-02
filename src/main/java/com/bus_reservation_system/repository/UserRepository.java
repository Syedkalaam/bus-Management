package com.bus_reservation_system.repository;

import com.bus_reservation_system.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
   public User findByUsername(String username);
}
