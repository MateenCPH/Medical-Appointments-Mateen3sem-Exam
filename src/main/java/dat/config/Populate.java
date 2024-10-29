package dat.config;

import dat.entities.Doctor;
import dat.entities.Doctor.DoctorSpeciality;
import dat.entities.Appointment;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Set<Doctor> doctors = getDoctors();
        List<Appointment> appointments = getAppointments(doctors);

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            for (Doctor doctor : doctors) {
                em.persist(doctor);  // Persist each doctor
            }
            for (Appointment appointment : appointments) {
                em.persist(appointment);  // Persist each appointment
            }
            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Doctor> getDoctors() {
        Doctor doctor1 = new Doctor("Dr. John Doe", LocalDate.of(1975, 5, 20), 2000, "City Hospital", DoctorSpeciality.surgery);
        Doctor doctor2 = new Doctor("Dr. Jane Smith", LocalDate.of(1980, 8, 15), 2005, "Downtown Clinic", DoctorSpeciality.family);
        Doctor doctor3 = new Doctor("Dr. Emily Johnson", LocalDate.of(1983, 2, 10), 2007, "Central Medical Center", DoctorSpeciality.pediatrics);
        Doctor doctor4 = new Doctor("Dr. William Brown", LocalDate.of(1970, 11, 5), 1995, "Northern Health Center", DoctorSpeciality.geriatrics);
        Doctor doctor5 = new Doctor("Dr. Linda White", LocalDate.of(1985, 7, 25), 2010, "Westside Clinic", DoctorSpeciality.psychiatry);

        Doctor[] doctorArray = {doctor1, doctor2, doctor3, doctor4, doctor5};
        return Set.of(doctorArray);
    }

    @NotNull
    private static List<Appointment> getAppointments(Set<Doctor> doctors) {
        Doctor[] doctorArray = doctors.toArray(new Doctor[0]);
        Appointment appointment1 = new Appointment("Alice Brown", LocalDate.of(2024, 11, 1), "10:00 AM", "Routine check-up", doctorArray[0]);
        Appointment appointment2 = new Appointment("Bob Smith", LocalDate.of(2024, 11, 2), "11:00 AM", "Consultation", doctorArray[1]);
        Appointment appointment3 = new Appointment("Charlie Johnson", LocalDate.of(2024, 11, 3), "02:00 PM", "Vaccination", doctorArray[2]);
        Appointment appointment4 = new Appointment("Diana Prince", LocalDate.of(2024, 11, 4), "09:30 AM", "Geriatric care", doctorArray[3]);
        Appointment appointment5 = new Appointment("Eve Adams", LocalDate.of(2024, 11, 5), "01:00 PM", "Psychiatric consultation", doctorArray[4]);

        return List.of(appointment1, appointment2, appointment3, appointment4, appointment5);
    }
}
