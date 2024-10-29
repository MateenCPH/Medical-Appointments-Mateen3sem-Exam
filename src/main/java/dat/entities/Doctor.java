package dat.entities;

import dat.dtos.DoctorDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Table(name = "doctor")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class Doctor {

    @Column(name = "doctor_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "year_of_graduation")
    private int yearOfGraduation;

    private String clinic;

    @Enumerated(EnumType.STRING)
    @Column(name = "speciality")
    private DoctorSpeciality speciality;

    @Column(name = "created_date_time", nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @ToString.Exclude
    @Column(name = "updated_date_time", nullable = false)
    private LocalDateTime updatedDateTime;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    public Doctor(String name, LocalDate dateOfBirth, int yearOfGraduation, String clinic, DoctorSpeciality speciality) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.yearOfGraduation = yearOfGraduation;
        this.clinic = clinic;
        this.speciality = speciality;
    }

    public Doctor(DoctorDTO doctorDTO) {
        this.id = doctorDTO.getId();
        this.name = doctorDTO.getName();
        this.dateOfBirth = doctorDTO.getDateOfBirth();
        this.yearOfGraduation = doctorDTO.getYearOfGraduation();
        this.clinic = doctorDTO.getClinic();
        this.speciality = doctorDTO.getSpeciality();

    }

    @PrePersist
    public void prePersist() {
        if (createdDateTime == null) {
            createdDateTime = LocalDateTime.now();
        }
        if (updatedDateTime == null) {
            updatedDateTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedDateTime = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return yearOfGraduation == doctor.yearOfGraduation && Objects.equals(id, doctor.id) && Objects.equals(name, doctor.name) && Objects.equals(dateOfBirth, doctor.dateOfBirth) && Objects.equals(clinic, doctor.clinic) && speciality == doctor.speciality && Objects.equals(createdDateTime, doctor.createdDateTime) && Objects.equals(updatedDateTime, doctor.updatedDateTime) && Objects.equals(appointments, doctor.appointments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, yearOfGraduation, clinic, speciality, createdDateTime, updatedDateTime, appointments);
    }

    public enum DoctorSpeciality {
        surgery, family, medicin, psychiatry, pediatrics, geriatrics, empty, mySpeciality;
    }

}
