package dat.routes;

import dat.controllers.impl.DoctorController;

import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class DoctorRoutes {
    private final DoctorController doctorController = new DoctorController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", doctorController::create, Role.ANYONE);
            get("/", doctorController::readAll, Role.ANYONE);
            get("/{id}", doctorController::readById, Role.ANYONE);
            get("/speciality/{speciality}", doctorController::readBySpeciality, Role.ANYONE);
            get("/birthday/range/{startDate}/{endDate}", doctorController::readyByBirthdayRange, Role.ANYONE);
            put("/{id}", doctorController::update);
        };
    }
}
