package dat.controllers.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.DoctorDAO;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DoctorController implements IController<DoctorDTO, Integer> {
    private final DoctorDAO dao;

    public DoctorController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = DoctorDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            DoctorDTO doctorDTO = dao.readById(id);
            ctx.res().setStatus(200);
            ctx.json(doctorDTO);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (NoResultException e) {
            throw new ApiException(404, "Doctor with id: " + ctx.pathParam("id") + " not found");
        }
    }

    @Override
    public void readAll(Context ctx) {
        try {
            List<DoctorDTO> doctors = dao.readAll();
            ctx.res().setStatus(200);
            ctx.json(doctors);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "No doctors found");
        }
    }

    @Override
    public void readBySpeciality(Context ctx) {
        try {
            Doctor.DoctorSpeciality speciality = Doctor.DoctorSpeciality.valueOf(ctx.pathParam("speciality"));
            List<DoctorDTO> doctors = dao.readBySpeciality(speciality);
            ctx.res().setStatus(200);
            ctx.json(doctors);
            if (doctors.isEmpty())
                throw new ApiException(404, "No doctors found with the specified speciality: " + ctx.pathParam("speciality"));
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid speciality");
        }
    }

    @Override
    public void readyByBirthdayRange(Context ctx) { //HER
        try {
            LocalDate from = LocalDate.parse(ctx.queryParam("from"));
            LocalDate to = LocalDate.parse(ctx.queryParam("to"));

            List<DoctorDTO> doctorDTOS = dao.readByBirthdayRange(from, to);
            ctx.res().setStatus(200);
            ctx.json(doctorDTOS);
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "No doctors found with the birthdates: " + ctx.queryParam("from") + " " + ctx.queryParam("to"));
        } catch (DateTimeParseException e) {
            throw new ApiException(400, "Invalid date(s)" + ctx.queryParam("from") + " " + ctx.queryParam("to"));
        }
    }

    //
    @Override
    public void create(Context ctx) {
        try {
            DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
            DoctorDTO createdDoctor = dao.create(jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(createdDoctor);
            //Catch the exception thrown by the DAO and handle it here
        } catch (ApiException e) { //This catch currently catches the error of a unique constraint violation
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (InvalidFormatException | JsonParseException | ValidationException | DateTimeParseException e) {
            throw new ApiException(400, e.getMessage());
        }
    }

    @Override
    public void update(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));

            DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
            DoctorDTO updatedDoctor = dao.update(id, jsonRequest);
            ctx.res().setStatus(201);
            ctx.json(updatedDoctor);
        } catch (ApiException e) {
            throw new ApiException(e.getStatusCode(), e.getMessage());
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Doctor with id: " + ctx.pathParam("id") + " not found");
        }
    }

    //Delete a doctor by id and catch exceptions and handle them
    @Override
    public void delete(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            dao.delete(id);
            ctx.res().setStatus(204);
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid id: " + ctx.pathParam("id"));
        } catch (EntityNotFoundException e) {
            throw new ApiException(404, "Doctor with id: " + ctx.pathParam("id") + " not found");
        }
    }
}