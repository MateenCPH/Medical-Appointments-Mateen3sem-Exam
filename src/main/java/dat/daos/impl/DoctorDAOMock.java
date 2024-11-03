package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAOMock implements IDAO<DoctorDTO, Integer> {

    private static List<DoctorDTO> doctors = new ArrayList<>();
    private static Integer idCounter = 1;

    // create a singleton
    private static DoctorDAOMock instance;

    public static DoctorDAOMock getInstance() {
        if (instance == null) {
            instance = new DoctorDAOMock();
            instance.populate();
        }
        return instance;
    }

    //Create seven doctorDTOs with different names
    private void populate() {
        create(new DoctorDTO(1, "Dr. John Doe", LocalDate.of(1911, 1, 1), 2011, "Clinic 1", Doctor.DoctorSpeciality.FAMILY));
        create(new DoctorDTO(2, "Dr. Jane Jackson", LocalDate.of(1922, 2, 2), 2022, "Clinic 2", Doctor.DoctorSpeciality.SURGERY));
        create(new DoctorDTO(3, "Dr. Jack Hardy", LocalDate.of(1933, 3, 3), 2033, "Clinic 3", Doctor.DoctorSpeciality.PEDIATRICS));
        create(new DoctorDTO(4, "Dr. Jill Hartmann", LocalDate.of(1944, 4, 4), 2044, "Clinic 4", Doctor.DoctorSpeciality.MEDICINE));
        create(new DoctorDTO(5, "Dr. Jim Bertelsen", LocalDate.of(1955, 5, 5), 2055, "Clinic 5", Doctor.DoctorSpeciality.PSYCHIATRY));
        create(new DoctorDTO(6, "Dr. Joan Muyanga", LocalDate.of(1966, 6, 6), 2066, "Clinic 6", Doctor.DoctorSpeciality.FAMILY));
        create(new DoctorDTO(7, "Dr. Joe Rasmussen", LocalDate.of(1977, 7, 7), 2077, "Clinic 7", Doctor.DoctorSpeciality.GERIATRICS));
    }

    @Override
    public List<DoctorDTO> readAll() {
        return new ArrayList<>(doctors);
    }

    //Read doctor by ID using streams
    @Override
    public DoctorDTO readById(Integer integer) {
        return doctors.stream()
                .filter(doctorDTO -> doctorDTO.getId().equals(integer))
                .findFirst()
                .orElse(null);
    }

    //Read doctor by speciality using streams
    public List<DoctorDTO> doctorBySpeciality(Doctor.DoctorSpeciality speciality) {
        return doctors.stream()
                .filter(doctorDTO -> doctorDTO.getSpeciality() == speciality)
                .toList();
    }

    //Read doctor by birthday range using streams
    public List<DoctorDTO> doctorByBirthdayRange(LocalDate from, LocalDate to) {
        return doctors.stream()
                .filter(doctorDTO -> !doctorDTO.getDateOfBirth().isAfter(to) && !doctorDTO.getDateOfBirth().isBefore(from))
                .toList();
    }

    //Create a new doctor
    @Override
    public DoctorDTO create(DoctorDTO doctorDTO) {
        doctorDTO.setId(idCounter++);
        doctors.add(doctorDTO);
        return doctorDTO;
    }

    //Update a doctor by ID using streams
    @Override
    public DoctorDTO update(Integer integer, DoctorDTO doctorDTO) {
        doctors.stream()
                .filter(d -> d.getId().equals(integer))
                .findFirst()
                .ifPresent(d -> {
                    d.setName(doctorDTO.getName());
                    d.setDateOfBirth(doctorDTO.getDateOfBirth());
                    d.setYearOfGraduation(doctorDTO.getYearOfGraduation());
                    d.setClinic(doctorDTO.getClinic());
                    d.setSpeciality(doctorDTO.getSpeciality());
                });
        return doctorDTO;
    }

    @Override
    public void delete(Integer integer) {

    }
}