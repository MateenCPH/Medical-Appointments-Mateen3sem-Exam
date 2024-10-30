package dat.dtos;

import dat.entities.Doctor;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class DoctorDTO {

    private Integer id;
    private String name;
    private LocalDate dateOfBirth;
    private int yearOfGraduation;
    private String clinic;
    private Doctor.DoctorSpeciality speciality;

    public DoctorDTO (Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.dateOfBirth = doctor.getDateOfBirth();
        this.yearOfGraduation = doctor.getYearOfGraduation();
        this.clinic = doctor.getClinic();
        this.speciality = doctor.getSpeciality();
    }

    public DoctorDTO (String name, LocalDate dateOfBirth, int yearOfGraduation, String clinic, Doctor.DoctorSpeciality speciality) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.yearOfGraduation = yearOfGraduation;
        this.clinic = clinic;
        this.speciality = speciality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorDTO doctorDTO = (DoctorDTO) o;
        return yearOfGraduation == doctorDTO.yearOfGraduation && Objects.equals(id, doctorDTO.id) && Objects.equals(name, doctorDTO.name) && Objects.equals(dateOfBirth, doctorDTO.dateOfBirth) && Objects.equals(clinic, doctorDTO.clinic) && speciality == doctorDTO.speciality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, yearOfGraduation, clinic, speciality);
    }
}
