package com.spicysymphony.restaurant_booking.model;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reservations")
@Data
public class ReservationModel {

    @Id
    private String id;

    @NotBlank(message = "Name cannot be empty")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Name should contain only alphabets")
    private String name;

    @Indexed(unique = true)
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+91\\s[0-9]{5}\\s[0-9]{5}$", message = "Phone number must be in the format '+91 XXXXX XXXXX'")
    private String phone;

    @Indexed(unique = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Date is required")
    private String date;

    @NotBlank(message = "Time is required")
    private String time;

    private int persons;

    private String message;



    public ReservationModel(String name, String phone,String email, String date, String time, int persons, String message) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.date = date;
        this.time = time;
        this.persons = persons;
        this.message = message;
    }


}


