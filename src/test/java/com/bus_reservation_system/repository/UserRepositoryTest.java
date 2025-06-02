package com.bus_reservation_system.repository;

import com.bus_reservation_system.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Save and Find User by Username")
    void testFindByUsername() {
        // Given
        User user = User.builder()
                .username("testuser")
                .password("password")
                .email("testuser@example.com")
                .roles("USER")
                .gender("Male")
                .age(25)
                .address("123 Main Street")
                .enabled(true)
                .build();

        userRepository.save(user);

        // When
        User found = userRepository.findByUsername("testuser");

        // Then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo("testuser@example.com");
    }

    @Test
    @DisplayName("Delete User")
    void testDeleteUser() {
        User user = User.builder()
                .username("deleteuser")
                .password("password")
                .email("delete@example.com")
                .roles("USER")
                .gender("Female")
                .age(30)
                .enabled(true)
                .build();

        userRepository.save(user);
        userRepository.delete(user);

        Optional<User> deleted = userRepository.findById(user.getId());
        assertThat(deleted).isEmpty();
    }
}
