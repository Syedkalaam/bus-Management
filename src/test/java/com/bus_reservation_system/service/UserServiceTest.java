package com.bus_reservation_system.service;

import com.bus_reservation_system.exception.ResourceNotFoundException;
import com.bus_reservation_system.model.Booking;
import com.bus_reservation_system.model.Trip;
import com.bus_reservation_system.model.User;
import com.bus_reservation_system.repository.BookingRepository;
import com.bus_reservation_system.repository.TripRepository;
import com.bus_reservation_system.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_shouldEncodePasswordAndSave() {
        User user = User.builder()
                .username("arun")
                .password("plain123")
                .roles("user")
                .enabled(false)
                .build();

        when(passwordEncoder.encode("plain123")).thenReturn("encoded123");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User savedUser = userService.saveUser(user);

        assertThat(savedUser.getPassword()).isEqualTo("encoded123");
        assertThat(savedUser.getRoles()).isEqualTo("ROLE_USER");
        assertThat(savedUser.isEnabled()).isTrue();

        verify(userRepository, times(1)).save(savedUser);
    }

    @Test
    void getUserById_shouldReturnUser() {
        User user = new User();
        user.setId("123");
        when(userRepository.findById("123")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById("123");

        assertThat(result).isPresent().contains(user);
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userService.getAllUsers();

        assertThat(users).hasSize(2);
    }

    @Test
    void deleteUserById_shouldDeleteUserAndBooking() {
        User user = new User();
        user.setId("u1");

        Booking booking = new Booking();
        booking.setId("b1");


        Trip trip = new Trip();
        trip.setId("t1");

        trip.setUsers(new ArrayList<>(List.of(user)));

        user.setBookings(booking);
        booking.setTrip(trip);

        when(userRepository.findById("u1")).thenReturn(Optional.of(user));
        when(bookingRepository.findById("b1")).thenReturn(Optional.of(booking));
        when(tripRepository.findById("t1")).thenReturn(Optional.of(trip));

        userService.deleteUserById("u1");


        verify(bookingRepository).deleteById("b1");
        verify(userRepository).deleteById("u1");
    }

    @Test
    void updateUserById_shouldUpdateAndReturnUser() {
        User existingUser = new User();
        existingUser.setId("u1");
        existingUser.setBookings(new Booking());
        existingUser.setBus(null);
        existingUser.setTrips(new Trip());

        User updated = new User();
        updated.setUsername("newName");
        updated.setPassword("newPass");
        updated.setRoles("user");

        when(userRepository.findById("u1")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.updateUserbyId("u1", updated);

        assertThat(result.getUsername()).isEqualTo("newName");
        assertThat(result.getPassword()).isEqualTo("encodedPass");
        assertThat(result.getId()).isEqualTo("u1");
        assertThat(result.getRoles()).isEqualTo("ROLE_USER");

        verify(userRepository).save(result);
    }

    @Test
    void deleteUserById_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteUserById("invalid"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("user not found");
    }
}
