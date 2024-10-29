package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.DoctorDTO;
import dat.dtos.PlantDTO;
import dat.entities.Doctor;
import dat.entities.Plant;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;

public class DoctorDAO implements IDAO<DoctorDTO, Integer, LocalDate, LocalDate, Doctor.DoctorSpeciality> {

    private static DoctorDAO instance;
    private static EntityManagerFactory emf;

    public static DoctorDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DoctorDAO();
        }
        return instance;
    }

    @Override
    public DoctorDTO readById(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor == null) {
                throw new EntityNotFoundException("Doctor with id: " + id + " not found");
            }
            return new DoctorDTO(doctor);
        }
    }

    @Override
    public List<DoctorDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d", DoctorDTO.class);
            if (query.getResultList().isEmpty()) {
                throw new EntityNotFoundException("No doctors found");
            }
            return query.getResultList();
        }
    }

    @Override
    public List<DoctorDTO> readBySpeciality(Doctor.DoctorSpeciality speciality) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.speciality = :speciality", DoctorDTO.class);
            query.setParameter("speciality", speciality);
            if (query.getResultList().isEmpty()) {
                throw new EntityNotFoundException("No doctors found with speciality: " + speciality);
            }
            return query.getResultList();
        }
    }

    @Override
    public List<DoctorDTO> readByBirthdayRange(LocalDate start, LocalDate end) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.dateOfBirth BETWEEN :startDate AND :endDate ORDER BY d.dateOfBirth DESC", DoctorDTO.class);
            query.setParameter("startDate", start);
            query.setParameter("endDate", end);
            if (query.getResultList().isEmpty()) {
                throw new EntityNotFoundException("No doctors found with birthday between: " + start + " and " + end);
            }
            return query.getResultList();
        }
    }

    @Override
    public DoctorDTO create(DoctorDTO doctorDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = new Doctor(doctorDTO);
            em.persist(doctor);
            em.getTransaction().commit();
            return new DoctorDTO(doctor);
        } catch (Exception e) {
            throw new ApiException(400, "Doctor could not be created" + e.getMessage());
        }
    }


    @Override
    public DoctorDTO update(Integer integer, DoctorDTO doctorDTO) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Doctor doctor = em.find(Doctor.class, integer);
            return doctor != null;
        } catch (EntityNotFoundException e) {
            e.getMessage();
            e.getCause();
            e.printStackTrace();
            return false;
        }
    }
}
