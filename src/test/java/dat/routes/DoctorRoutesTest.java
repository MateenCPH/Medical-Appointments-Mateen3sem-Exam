package dat.routes;

import dat.Populator.Populator;
import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.DoctorDAO;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.CoreMatchers.equalTo;

class DoctorRoutesTest {

    private static Javalin app;
    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final DoctorDAO dao = DoctorDAO.getInstance(emf);
    private static final Populator populator = new Populator(emf, dao);
    private static final String BASE_URL = "http://localhost:7007/api/doctors";

    private static DoctorDTO d1DTO, d2DTO;

    @BeforeAll
    static void beforeAll() {
        app = ApplicationConfig.startServer(7007);
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

    @AfterAll
    static void afterAll() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void getAllDoctors() {
        DoctorDTO[] doctorDTOS =
        given()
                .when()
                .get(BASE_URL)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(DoctorDTO[].class);
        assertThat(doctorDTOS, arrayContainingInAnyOrder(d1DTO, d2DTO));
    }

    @Test
    void getDoctorById() {
        DoctorDTO doctorDTO =
                given()
                        .when()
                        .get(BASE_URL + "/" + d1DTO.getId())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(DoctorDTO.class);
        assertThat(doctorDTO, is(equalTo(d1DTO)));
    }

    @Test
    void getDoctorBySpeciality() {
        DoctorDTO[] doctorDTOS =
                given()
                        .when()
                        .get(BASE_URL + "/speciality/" + d1DTO.getSpeciality())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(DoctorDTO[].class);
        assertThat(doctorDTOS, arrayContainingInAnyOrder(d1DTO));
    }

    @Test
    void getDoctorByBirthdateRange() {
        DoctorDTO[] doctorDTOS =
                given()
                        .when()
                        .get(BASE_URL + "/birthdate/range?from=" + d1DTO.getDateOfBirth() + "&to=" + d2DTO.getDateOfBirth().minusDays(1))
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(DoctorDTO[].class);
        assertThat(doctorDTOS, arrayWithSize(1));
        assertThat(doctorDTOS, arrayContainingInAnyOrder(d1DTO));
    }

    @Test
    void createDoctor() {
        DoctorDTO toCreate = new DoctorDTO("Dr. Niels", LocalDate.of(1990, 1, 1),
                2000, "Familielægerne", Doctor.DoctorSpeciality.FAMILY);
        DoctorDTO created =
                given()
                        .contentType("application/json")
                        .body(toCreate)
                        .when()
                        .post(BASE_URL)
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(DoctorDTO.class);
        assertThat(created.getName(), equalTo(toCreate.getName()));
    }

    @Test
    void updateDoctor() {
        assertThat(d1DTO.getName(), equalTo("Dr. John Doe"));
        DoctorDTO toUpdate = new DoctorDTO("Dr. Updated Name", LocalDate.of(1911,1,1),
                2011, "Clinic 1", Doctor.DoctorSpeciality.FAMILY);

        DoctorDTO updated =
                given()
                        .contentType("application/json")
                        .body(toUpdate)
                        .when()
                        .put(BASE_URL + "/" + d1DTO.getId())
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(DoctorDTO.class);
        assertThat(updated.getName(), equalTo(toUpdate.getName()));
    }

    @Test
    void deleteDoctor() {
        assertThat(dao.readAll(), hasSize(2));
        assertThat(dao.readAll(), containsInAnyOrder(d1DTO, d2DTO));

        given()
                .when()
                .delete(BASE_URL + "/" + d2DTO.getId())
                .then()
                .log().all()
                .statusCode(204);
        assertThat(dao.readAll(), hasSize(1));
        assertThat(dao.readAll(), containsInAnyOrder(d1DTO));
    }
}