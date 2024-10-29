package dat.dtos;

import dat.entities.Doctor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
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

    public List<DoctorDTO> toDoctorDTOList(List<Doctor> doctors) {
        return doctors.stream().map(DoctorDTO::new).collect(Collectors.toList());
    }
}
