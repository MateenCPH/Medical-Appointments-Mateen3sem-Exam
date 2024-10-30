package dat.daos.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.Populator.Populator;
import dat.config.HibernateConfig;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DoctorDAOTest {

    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static DoctorDAO dao = DoctorDAO.getInstance(emf);
    Populator populator = new Populator(emf, dao);
    private static DoctorDTO d1DTO, d2DTO;

    @BeforeAll
    static void beforeAll() {
    }

    @BeforeEach
    void setUp() {
        populator.populateDatabase();

        d1DTO = dao.readAll().get(0);
        d2DTO = dao.readAll().get(1);
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
    @Order(1)
    void readById() {
        DoctorDTO expected;
        expected = d1DTO;
        DoctorDTO actual = dao.readById(expected.getId());

        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    @Order(2)
    void readAll() {
        List<DoctorDTO> doctors = dao.readAll();

        assertThat(doctors, hasSize(2));
        assertThat(doctors, containsInAnyOrder(d1DTO, d2DTO));
    }

    @Test
    void readBySpeciality() {
        List<DoctorDTO> doctors = dao.readBySpeciality(Doctor.DoctorSpeciality.FAMILY);

        assertThat(doctors, hasSize(1));
        assertThat(doctors.get(0), is(equalTo(d1DTO)));
    }

    @Test
    void readByBirthdayRange() {
        List<DoctorDTO> doctors = dao.readByBirthdayRange(LocalDate.of(1910, 1, 1), LocalDate.of(1912, 1, 1));

        assertThat(doctors, hasSize(1));
        assertThat(doctors.get(0), is(equalTo(d1DTO)));
    }

    @Test
    void createDoctor() {
        assertThat(dao.readAll(), hasSize(2));

        try {
            DoctorDTO d3DTOCreated = dao.create(new DoctorDTO("Dr. Mateen", LocalDate.of(2001, 6, 21),
                    2018, "Familielægerne", Doctor.DoctorSpeciality.FAMILY));

            assertThat(dao.readAll(), hasSize(3));
            assertThat(d3DTOCreated, is(equalTo(dao.readById(d3DTOCreated.getId()))));
        } catch (ApiException | InvalidFormatException | JsonParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void updateDoctor() {
        assertThat(dao.readById(d1DTO.getId()).getName(), is(equalTo("Dr. John Doe")));

        DoctorDTO newDoctorDTO = DoctorDTO.builder()
                .name("Dr. Zaki")
                .dateOfBirth(LocalDate.of(1999, 4, 10))
                .yearOfGraduation(2016)
                .clinic("Familielægerne")
                .speciality(Doctor.DoctorSpeciality.FAMILY)
                .build();

        d1DTO.setName(newDoctorDTO.getName());
        dao.update(d1DTO.getId(), d1DTO);

        assertThat(dao.readById(d1DTO.getId()).getName(), is(equalTo(newDoctorDTO.getName())));
    }

    @Test
    void deleteDoctor() {
        assertThat(dao.readAll(), hasSize(2));
        dao.delete(d1DTO.getId());
        assertThat(dao.readAll(), hasSize(1));
    }
}