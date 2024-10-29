package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

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
            List<DoctorDTO> doctors = query.getResultList();
            if (doctors.isEmpty()) {
                throw new EntityNotFoundException("No doctors found");
            }
            return doctors;
        }
    }

    @Override
    public List<DoctorDTO> readBySpeciality(Doctor.DoctorSpeciality speciality) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.speciality = :speciality", DoctorDTO.class);
            query.setParameter("speciality", speciality);
            List<DoctorDTO> doctors = query.getResultList();
            if (doctors.isEmpty()) {
                throw new EntityNotFoundException("No doctors found with speciality: " + speciality);
            }
            return doctors;
        }
    }

    @Override
    public List<DoctorDTO> readByBirthdayRange(LocalDate start, LocalDate end) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<DoctorDTO> query = em.createQuery("SELECT new dat.dtos.DoctorDTO(d) FROM Doctor d WHERE d.dateOfBirth BETWEEN :startDate AND :endDate ORDER BY d.dateOfBirth DESC", DoctorDTO.class);
            query.setParameter("startDate", start);
            query.setParameter("endDate", end);
            List<DoctorDTO> doctors = query.getResultList();
            if (doctors.isEmpty()) {
                throw new EntityNotFoundException("No doctors found with birthday between: " + start + " and " + end);
            }
            return doctors;
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
            throw new ApiException(400, "Doctor could not be created: " + e.getMessage());
        }
    }

    @Override
    public DoctorDTO update(Integer id, DoctorDTO updatedDoctorDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = em.find(Doctor.class, id);

            if (doctor == null) {
                em.getTransaction().rollback();
                throw new EntityNotFoundException("Doctor with id: " + id + " not found");
            }

            if (updatedDoctorDTO.getName() != null && !updatedDoctorDTO.getName().isEmpty()) {
                doctor.setName(updatedDoctorDTO.getName());
            }
            if (updatedDoctorDTO.getDateOfBirth() != null) {
                doctor.setDateOfBirth(updatedDoctorDTO.getDateOfBirth());
            }
            if (updatedDoctorDTO.getYearOfGraduation() > 0) {
                doctor.setYearOfGraduation(updatedDoctorDTO.getYearOfGraduation());
            }
            if (updatedDoctorDTO.getClinic() != null && !updatedDoctorDTO.getClinic().isEmpty()) {
                doctor.setClinic(updatedDoctorDTO.getClinic());
            }
            if (updatedDoctorDTO.getSpeciality() != null) {
                doctor.setSpeciality(updatedDoctorDTO.getSpeciality());
            }

            Doctor mergedDoctor = em.merge(doctor);
            em.getTransaction().commit();
            return new DoctorDTO(mergedDoctor);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());
        } catch (Exception e) {
            throw new ApiException(400, "Doctor could not be updated: " + e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = em.find(Doctor.class, id);

            if (doctor == null) {
                em.getTransaction().rollback();
                throw new EntityNotFoundException("Doctor with id: " + id + " not found");
            }

            em.remove(doctor);
            em.getTransaction().commit();
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, e.getMessage());
        } catch (Exception e) {
            throw new ApiException(400, "Doctor could not be deleted: " + e.getMessage());
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Doctor doctor = em.find(Doctor.class, integer);
            return doctor != null;
        }
    }
}