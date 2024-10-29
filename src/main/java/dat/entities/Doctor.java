package dat.entities;

import dat.dtos.DoctorDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "doctor")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Doctor {

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

    public Doctor(String name, LocalDate dateOfBirth, int yearOfGraduation, String clinic, DoctorSpeciality speciality) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.yearOfGraduation = yearOfGraduation;
        this.clinic = clinic;
        this.speciality = speciality;
    }

    public Doctor (DoctorDTO doctorDTO) {
        this.id = doctorDTO.getId();
        this.name = doctorDTO.getName();
        this.dateOfBirth = doctorDTO.getDateOfBirth();
        this.yearOfGraduation = doctorDTO.getYearOfGraduation();
        this.clinic = doctorDTO.getClinic();
        this.speciality = doctorDTO.getSpeciality();

    }

    public enum DoctorSpeciality {
        surgery, family, medicin, psychiatry, pediatrics, geriatrics;
    }


}
