package dat.dtos;

import dat.entities.Appointment;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class AppointmentDTO {

    private Integer id;
    private String clientName;
    private LocalDate date;
    private String time;
    private String comment;

    public AppointmentDTO (Appointment appointment) {
        this.id = appointment.getId();
        this.clientName = appointment.getClientName();
        this.date = appointment.getDate();
        this.time = appointment.getTime();
        this.comment = appointment.getComment();
    }

    public AppointmentDTO (String clientName, LocalDate date, String time, String comment) {
        this.clientName = clientName;
        this.date = date;
        this.time = time;
        this.comment = comment;
    }
}