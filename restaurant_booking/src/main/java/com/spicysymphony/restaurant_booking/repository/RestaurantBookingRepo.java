package com.spicysymphony.restaurant_booking.repository;

import com.spicysymphony.restaurant_booking.model.ReservationModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantBookingRepo extends MongoRepository<ReservationModel, String> {

    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}

