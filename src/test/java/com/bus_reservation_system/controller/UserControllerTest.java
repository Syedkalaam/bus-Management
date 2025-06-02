package com.bus_reservation_system.controller;

import com.bus_reservation_system.model.User;
import com.bus_reservation_system.service.BookingService;
import com.bus_reservation_system.service.BusService;
import com.bus_reservation_system.service.TripService;
import com.bus_reservation_system.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private TripService tripService;

    @MockBean
    private BusService busService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id("1")
                .username("arun")
                .email("arun@mail.com")
                .roles("USER")
                .build();
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testRegisterUser() throws Exception {
        when(userService.saveUser(any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/users/register")
                        .param("username", "arun")
                        .param("password", "1234")
                        .param("email", "arun@mail.com")
                        .param("roles", "USER")
                        .param("gender", "male")
                        .param("age", "25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testShowUserDetailForm() throws Exception {
        when(userService.getUserById("1")).thenReturn(Optional.of(sampleUser));

        mockMvc.perform(get("/users/details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-details"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testViewUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(sampleUser));

        mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-list"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUserById("1");

        mockMvc.perform(get("/users/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/list"));
    }

    @Test
    void testShowUpdateForm() throws Exception {
        when(userService.getUserById("1")).thenReturn(Optional.of(sampleUser));

        mockMvc.perform(get("/users/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/user-update"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testUpdateUserById() throws Exception {
        when(userService.updateUserbyId(eq("1"), any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(post("/users/update/1")
                        .param("username", "arun")
                        .param("password", "newpass")
                        .param("email", "arun@mail.com")
                        .param("roles", "USER")
                        .param("gender", "male")
                        .param("age", "25"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/list"));
    }
}
