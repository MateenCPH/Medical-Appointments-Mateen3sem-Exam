package dat.Populator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.daos.impl.DoctorDAO;
import dat.dtos.DoctorDTO;
import dat.entities.Appointment;
import dat.entities.Doctor;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.List;

public class Populator {
    private static EntityManagerFactory emf;
    private static DoctorDAO dao;

    public Populator(EntityManagerFactory _emf, DoctorDAO dao_) {
        this.emf = _emf;
        this.dao = dao_;
    }

    public static void populateDatabase() {
        Doctor doctorEntity1 = Doctor.builder()
                .name("Dr. John Doe")
                .dateOfBirth(LocalDate.of(1911, 1, 1))
                .yearOfGraduation(2011)
                .clinic("Clinic 1")
                .speciality(Doctor.DoctorSpeciality.FAMILY)
                .build();

        Doctor doctorEntity2 = Doctor.builder()
                .name("Dr. Jane Doe")
                .dateOfBirth(LocalDate.of(1922, 2, 2))
                .yearOfGraduation(2022)
                .clinic("Clinic 2")
                .speciality(Doctor.DoctorSpeciality.SURGERY)
                .build();

        Appointment appointmentEntity1 = Appointment.builder()
                .clientName("Alice")
                .date(LocalDate.of(2021, 1, 1))
                .time("11:00")
                .comment("Checkup")
                .build();

        Appointment appointmentEntity2 = Appointment.builder()
                .clientName("Bob")
                .date(LocalDate.of(2022, 2, 2))
                .time("22:00")
                .comment("Surgery")
                .build();

        doctorEntity1.setAppointments(List.of(appointmentEntity1));
        doctorEntity2.setAppointments(List.of(appointmentEntity2));

        try {
            DoctorDTO d1DTO = dao.create(new DoctorDTO(doctorEntity1));
            DoctorDTO d2DTO = dao.create(new DoctorDTO(doctorEntity2));
        } catch (ApiException | InvalidFormatException | JsonParseException e) {
            e.printStackTrace();
        }
    }
}
