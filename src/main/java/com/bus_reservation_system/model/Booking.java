package com.bus_reservation_system.model;


import ch.qos.logback.core.status.Status;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.HashSet;
import java.util.Set;


@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;

    @DBRef
    private Trip trip;


    private String userId;


    @Min(value = 1)
    @Max(value = 25)
    private Set<Integer> seatNumbers = new HashSet<>();

    private String bookedAt;

    private String status;

    public Booking() {

    }

    public Booking(String id, Trip trip, String userId, Set<Integer> seatNumbers, String bookedAt, String status) {
        this.id = id;
        this.trip = trip;
        this.userId = userId;
        this.seatNumbers = seatNumbers;
        this.bookedAt = bookedAt;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<Integer> getSeatNumbers() {
        return seatNumbers;
    }

    public void setSeatNumbers(Set<Integer> seatNumbers) {
        this.seatNumbers = seatNumbers;
    }

    public String getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(String bookedAt) {
        this.bookedAt = bookedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
