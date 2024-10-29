package dat.daos.impl;

import dat.config.HibernateConfig;
import dat.dtos.DoctorDTO;
import dat.entities.Appointment;
import dat.entities.Doctor;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

class DoctorDAOTest {

    private static EntityManagerFactory emf;
    private static DoctorDAO dao;
    private static DoctorDTO d1DTO, d2DTO;

    @BeforeAll
    static void beforeAll() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = DoctorDAO.getInstance(emf);
    }

    @BeforeEach
    void setUp() {
        Doctor doctorEntity1 = Doctor.builder()
                .name("Dr. John Doe")
                .dateOfBirth(LocalDate.of(1911, 1, 1))
                .yearOfGraduation(2011)
                .clinic("Clinic 1")
                .speciality(Doctor.DoctorSpeciality.family)
                .build();

        Doctor doctorEntity2 = Doctor.builder()
                .name("Dr. Jane Doe")
                .dateOfBirth(LocalDate.of(1922, 2, 2))
                .yearOfGraduation(2022)
                .clinic("Clinic 2")
                .speciality(Doctor.DoctorSpeciality.surgery)
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
            d1DTO = dao.create(new DoctorDTO(doctorEntity1));
            d2DTO = dao.create(new DoctorDTO(doctorEntity2));
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Appointment").executeUpdate();
            em.createQuery("DELETE FROM Doctor").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE appointments_appointment_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE doctor_doctor_id_seq RESTART WITH 1").executeUpdate();

            em.getTransaction().commit();
        }
    }

    @Test
    void readById() {
        DoctorDTO expected;
            expected = d1DTO;
            DoctorDTO actual = dao.readById(expected.getId());

            assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void readAll() {
        List<DoctorDTO> doctors = dao.readAll();
        System.out.println(doctors);
        System.out.println(d1DTO);
        System.out.println(d2DTO);
        assertThat(doctors, hasSize(2));
        assertThat(doctors, containsInAnyOrder(d1DTO, d2DTO));
    }

    @Test
    void readBySpeciality() {
        List<DoctorDTO> doctors = dao.readBySpeciality(Doctor.DoctorSpeciality.family);
        assertThat(doctors, hasSize(1));
        assertThat(doctors.get(0), is(equalTo(d1DTO)));
    }

    @Test
    void readByBirthdayRange() {
        List<DoctorDTO> doctors = dao.readByBirthdayRange(LocalDate.of(1910, 1, 1), LocalDate.of(1912, 1, 1));
        assertThat(doctors, hasSize(1));
        assertThat(doctors.get(0), is(equalTo(d1DTO)));
    }
}