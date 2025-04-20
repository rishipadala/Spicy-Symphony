package com.spicysymphony.restaurant_booking.controller;

import com.mongodb.MongoWriteException;
import com.spicysymphony.restaurant_booking.model.ReservationModel;
import com.spicysymphony.restaurant_booking.repository.RestaurantBookingRepo;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "*") // Allow all origins to access the API
@EnableAsync // Enable async support
public class Restaurant_bookingController {
    private static final Logger log = LoggerFactory.getLogger(ReservationModel.class);



    @Autowired
    private RestaurantBookingRepo restaurantBookingRepo;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.sender.email}")
    private String senderEmail;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;


    // Add your code here
    @PostMapping
    public ResponseEntity<?> addReservation(@Valid @RequestBody ReservationModel reservation) {
        try {
            // Check if reservation with same phone or email already exists
            if (restaurantBookingRepo.existsByPhone(reservation.getPhone()) ||
                    restaurantBookingRepo.existsByEmail(reservation.getEmail())) {
                return new ResponseEntity<>("Email or phone number already exists", HttpStatus.BAD_REQUEST);
            }

            restaurantBookingRepo.insert(reservation);
            sendNotificationsAsync(reservation); // Call async method
            return new ResponseEntity<>("Reservation successful", HttpStatus.CREATED);
        } catch (MongoWriteException e) {
            if (e.getError().getCode() == 11000) {
                return new ResponseEntity<>("Email or phone number already exists", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Error in booking. Please try again.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Server error. Please try later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Async // Mark this method as asynchronous
    private void sendNotificationsAsync(ReservationModel reservation) {
        sendEmail(reservation);
        sendSMS(reservation);
    }

    private void sendEmail(@Valid ReservationModel reservation) {
        try {
            String recipientEmail = reservation.getEmail();
            String subject = "Reservation Confirmation from Spicy Symphony";
            String message = constructEmailBody(reservation);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setTo(recipientEmail);
            messageHelper.setSubject(subject);
            messageHelper.setFrom(senderEmail);
            messageHelper.setReplyTo(senderEmail);
            messageHelper.setSentDate(new Date());
            messageHelper.setText(message, true);

            mailSender.send(mimeMessage);
            log.info("Email sent to {}", recipientEmail);
        } catch (MailAuthenticationException e) {
            log.error("Authentication failed for SMTP server. Check credentials: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", reservation.getEmail(), e.getMessage(), e);
        }
    }

    private String constructEmailBody(@Valid ReservationModel reservation) {
        String name = reservation.getName();
        String date = reservation.getDate();
        String time = reservation.getTime(); // Corrected to getTime()
        int persons = reservation.getPersons();
        String message = reservation.getMessage();
        String phone = reservation.getPhone();
        String email = reservation.getEmail();

        String body = "<html>" +
                "<body>" +
                "<h1>Reservation Confirmation</h1>" +
                "<p>Dear " + name + ",</p>" +
                "<p>Thank you for booking a table at Spicy Symphony.</p>" +
                "<p>Your reservation details are as follows:</p>" +
                "<ul>" +
                "<li><strong>Date:</strong> " + date + "</li>" +
                "<li><strong>Time:</strong> " + time + "</li>" +
                "<li><strong>Number of persons:</strong> " + persons + "</li>" +
                "<li><strong>Phone:</strong> " + phone + "</li>" +
                "<li><strong>Email:</strong> " + email + "</li>" +
                "<li><strong>Message:</strong> " + message + "</li>" +
                "</ul>" +
                "Disclaimer: If you do not arrive within 30 minutes of your reserved time, your booking will be canceled, and the table may be given to other waiting customers.\n\n" +
                "<p>Please note that this is a confirmation of your reservation. If you have any questions or need to make changes, please contact us at +91-9175734828 or spicysymphony.bookings@gmail.com.</p>" +
                "<p>Looking forward to serving you.</p>" +
                "<p>Best regards,</p>" +
                "<p> Spicy Symphony </p>" +
                "</body>" +
                "</html>";

        return body;
    }

    private void sendSMS(@Valid ReservationModel reservation) {
        try {
            String recipientPhone = reservation.getPhone();
            // Convert to E.164 format: remove spaces
            String e164Phone = recipientPhone.replaceAll("\\s", "");
            String messageBody = "Hi " + reservation.getName() + ",Thank You for your reservation at Spicy Symphony on " +
                    reservation.getDate() + " at " + reservation.getTime() + " for " +
                    reservation.getPersons() + " is confirmed! Note: Arrive within 30 mins of your reserved time, or your booking may be canceled. " +
                    "Call or Email us at  +91 9175734828 or spicysymphony.bookings@gmail.com for any changes.";

            Message message = Message.creator(
                    new PhoneNumber(e164Phone), // To number
                    new PhoneNumber(twilioPhoneNumber), // From number
                    messageBody // Message body
            ).create();

            log.info("SMS sent successfully to {} with SID: {}", recipientPhone, message.getSid());

        } catch (ApiException e) {
            log.error("Twilio API error: Code {}, Message: {}", e.getCode(), e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error sending SMS to {}: {}", reservation.getPhone(), e.getMessage(), e);
        }

    }







    @GetMapping
    public ResponseEntity<List<ReservationModel>> getAllReservations() {
        List<ReservationModel> reservations = restaurantBookingRepo.findAll();
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("id/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable String id) {
        Optional<ReservationModel> reservation = restaurantBookingRepo.findById(id);
        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservation.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
    }

    @PutMapping("id/{id}")
    public ResponseEntity<?> updateReservation(@PathVariable String id, @Valid @RequestBody ReservationModel reservation) {
        if (!restaurantBookingRepo.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }
        reservation.setId(id);
        ReservationModel updatedReservation = restaurantBookingRepo.save(reservation);
        return ResponseEntity.ok(updatedReservation);
    }


        @DeleteMapping("id/{id}")
        public ResponseEntity<?> deleteReservation(@PathVariable String id) {
            if (!restaurantBookingRepo.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
            }
            restaurantBookingRepo.deleteById(id);
            return ResponseEntity.ok("Reservation deleted successfully");
        }

    }

