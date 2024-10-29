package dat.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "appointments")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Appointment {

    @Column(name = "appointment_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="client_name")
    private String clientName;

    private LocalDate date;

    private String time;

    private String comment;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    public Appointment(String clientName, LocalDate date, String time, String comment, Doctor doctor) {
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.comment = comment;
        this.doctor = doctor;
    }
}