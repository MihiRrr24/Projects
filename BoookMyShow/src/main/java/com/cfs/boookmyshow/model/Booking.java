package com.cfs.boookmyshow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingNumber;

    @Column(nullable = false)
    private LocalDateTime bookingTime;

    // one user can have many bookings
    // so booking to user mapping -> many to one
    @ManyToOne
    @Column(name = "show_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String status;        // CONF, CAN, PEND

    @Column(nullable = false)
    private Double totalAmount;

    // booking ka kisse relation h -> hum book particular seat kr rhe jo particular show me chalta
    // one booking can have multiple seats
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<ShowSeat> showSeats;

    // booking k end me ky aayegi -> ha payment
    // booking ki ek hi payment hogi whether its multiple booking or only one booking
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
