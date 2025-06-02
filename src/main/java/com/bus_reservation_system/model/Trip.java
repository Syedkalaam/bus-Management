package com.bus_reservation_system.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Document(collection = "trips")
public class Trip {
    @Id
    private String id;




    @NotBlank(message = "source should not blank")
    private String source;

    @NotBlank(message = "destination should not blank")
    private String destination;

    @NotBlank(message = "travelDate should not blank")
    private String travelDate;

    @NotBlank(message = "fare should not blank")
    @Min(10)
    private Double fare;

    private Set<Integer> bookedSeats = new HashSet<>();

    @DBRef
    private List<User> users = new ArrayList<>();

    @DBRef
    private Bus bus;

    public Trip() {
    }

    public Trip(String id, String source, String destination, String travelDate, Double fare, Set<Integer> bookedSeats, List<User> users, Bus bus) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.travelDate = travelDate;
        this.fare = fare;
        this.bookedSeats = bookedSeats;
        this.users = users;
        this.bus = bus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Set<Integer> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(Set<Integer> bookedSeats) {
        this.bookedSeats = bookedSeats;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
}
