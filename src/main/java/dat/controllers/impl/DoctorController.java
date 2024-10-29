package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.DoctorDAO;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class DoctorController implements IController<DoctorDTO, Integer> {
    private final DoctorDAO dao;
    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

    public DoctorController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = DoctorDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
            DoctorDTO doctorDTO = dao.readById(id);
            ctx.res().setStatus(200);
            ctx.json(doctorDTO);
        } catch (EntityNotFoundException e) {
            LOGGER.error("Doctor not found: ", e);
            ctx.status(404).json(new ApiException(404, "Doctor not found"));
        } catch (Exception e) {
            LOGGER.error("An error occurred while reading doctor by id: ", e);
            ctx.status(500).json(new ApiException(500, "Internal server error"));
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<DoctorDTO> doctors = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(doctors);
        } catch (EntityNotFoundException e) {
            LOGGER.error("No doctors found: ", e);
            ctx.status(404).json(new ApiException(404, "No doctors found"));
        } catch (Exception e) {
            LOGGER.error("An error occurred while reading all doctors: ", e);
            ctx.status(500).json(new ApiException(500, "Internal server error"));
        }
    }

    @Override
    public void readBySpeciality(Context ctx) {
        try {
            Doctor.DoctorSpeciality speciality = Doctor.DoctorSpeciality.valueOf(ctx.pathParam("speciality"));
            List<DoctorDTO> doctors = dao.readBySpeciality(speciality);
            ctx.res().setStatus(200);
            ctx.json(doctors);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid speciality: ", e);
            ctx.status(400).json(new ApiException(400, "Invalid speciality"));
        } catch (EntityNotFoundException e) {
            LOGGER.error("No doctors found with the specified speciality: ", e);
            ctx.status(404).json(new ApiException(404, "No doctors found with the specified speciality"));
        } catch (Exception e) {
            LOGGER.error("An error occurred while reading doctors by speciality: ", e);
            ctx.status(500).json(new ApiException(500, "Internal server error"));
        }
    }

    @Override
    public void readyByBirthdayRange(Context ctx) {
        try {
            LocalDate startDate = LocalDate.parse(ctx.pathParam("startDate"));
            LocalDate endDate = LocalDate.parse(ctx.pathParam("endDate"));

            List<DoctorDTO> doctorDTOS = dao.readByBirthdayRange(startDate, endDate);
            ctx.res().setStatus(200);
            ctx.json(doctorDTOS);
        } catch (EntityNotFoundException e) {
            LOGGER.error("No doctors found within the birthday range: ", e);
            ctx.status(404).json(new ApiException(404, "No doctors found within the birthday range"));
        } catch (Exception e) {
            LOGGER.error("An error occurred while reading doctors by birthday range: ", e);
            ctx.status(500).json(new ApiException(500, "Internal server error"));
        }
    }

    @Override
    public void create(Context ctx) {
        try {
            DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
            DoctorDTO createdDoctor = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(createdDoctor);
        } catch (ApiException e) {
            LOGGER.error("Error creating doctor: ", e);
            ctx.status(e.getStatusCode()).json(new ApiException(e.getStatusCode(), e.getMessage()));
        } catch (Exception e) {
            LOGGER.error("An error occurred while creating doctor: ", e);
            ctx.status(500).json(new ApiException(500, "Internal server error"));
        }
    }

    @Override
    public void update(Context ctx) {
        // Implement update logic with proper exception handling
    }

    @Override
    public void delete(Context ctx) {
        // Implement delete logic with proper exception handling
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public DoctorDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(DoctorDTO.class).get();
    }
}