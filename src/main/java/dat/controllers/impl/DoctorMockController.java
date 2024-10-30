package dat.controllers.impl;

import dat.daos.impl.DoctorDAOMock;
import dat.dtos.DoctorDTO;
import dat.entities.Doctor;
import dat.exceptions.ApiException;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DoctorMockController {

    private final DoctorDAOMock doctorDAOMock;

    public DoctorMockController() {
        doctorDAOMock = DoctorDAOMock.getInstance();
    }

    public void readAll(Context ctx) throws ApiException {
        //request
        try {
            List<DoctorDTO> doctors = doctorDAOMock.readAll();
            ctx.res().setStatus(200);
            ctx.json(doctors);
        } catch (Exception e) {
            throw new ApiException(404, "No doctors found");
        }
    }

    //GØR SÅDAN HER MED EXCEPTIONS
    public void readById(Context ctx) {
        int id = 0;
        try {
            //request
            id = Integer.parseInt(ctx.pathParam("id"));
            DoctorDTO doctorDTO = doctorDAOMock.readById(id);
            //response
            ctx.res().setStatus(200);
            ctx.json(doctorDTO, DoctorDTO.class);
        } catch (Exception e) {
            ctx.status(404).json(new ApiException(404, "Doctor not found with id " + id));
        }
    }

    public void readBySpeciality(Context ctx) {
        try {
            //request
            Doctor.DoctorSpeciality speciality = Doctor.DoctorSpeciality.valueOf(ctx.pathParam("speciality"));
            List<DoctorDTO> doctors = doctorDAOMock.doctorBySpeciality(speciality);
            //response
            ctx.res().setStatus(200);
            ctx.json(doctors);
        } catch (IllegalArgumentException e) {
            ctx.status(404).json(new ApiException(404, "No doctors found with speciality " + ctx.pathParam("speciality")));
        }
    }

    public void readByBirthdayRange(Context ctx) {
        try {
            //request
            LocalDate start = LocalDate.parse(ctx.queryParam("from"));
            LocalDate end = LocalDate.parse(ctx.queryParam("to"));
            List<DoctorDTO> doctors = doctorDAOMock.doctorByBirthdayRange(start, end);
            //response
            ctx.res().setStatus(200);
            ctx.json(doctors);
        } catch (Exception e) {
            ctx.status(404).json(new ApiException(404, "No doctors found with birthday in range from " + ctx.queryParam("from") + " to " + ctx.queryParam("to")));
        }
    }

    public void create(Context ctx) {
        try {
            //request
            DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
            DoctorDTO createdDoctor = doctorDAOMock.create(jsonRequest);
            //response
            ctx.res().setStatus(201);
            ctx.json(createdDoctor);
        } catch (Exception e) {
            ctx.status(400).json(new ApiException(400, "Doctor could not be created: " + e.getMessage()));
        }
    }

    public void update(Context ctx) {
        try {
            //request
            int id = Integer.parseInt(ctx.pathParam("id"));
            DoctorDTO jsonRequest = ctx.bodyAsClass(DoctorDTO.class);
            DoctorDTO updatedDoctor = doctorDAOMock.update(id, jsonRequest);
            //response
            ctx.res().setStatus(200);
            ctx.json(updatedDoctor);
        } catch (Exception e) {
            ctx.status(400).json(new ApiException(400, "Doctor could not be updated: " + e.getMessage()));
        }
    }
}