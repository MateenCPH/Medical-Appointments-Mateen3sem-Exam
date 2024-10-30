package dat.routes;

import dat.controllers.impl.DoctorController;

import dat.controllers.impl.DoctorMockController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class DoctorRoutes {
    private final DoctorController doctorController = new DoctorController();
    private final DoctorMockController doctorMockController = new DoctorMockController();

    protected EndpointGroup getRoutes() {

        return () -> {
//            //Mock
//            get("/", doctorMockController::readAll, Role.ANYONE);
//            get("/{id}", doctorMockController::readById, Role.ANYONE);
//            get("/speciality/{speciality}", doctorMockController::readBySpeciality, Role.ANYONE);
//            get("/birthdate/range", doctorMockController::readByBirthdayRange, Role.ANYONE);
//            post("/", doctorMockController::create, Role.ANYONE);
//            put("/{id}", doctorMockController::update);


            //Hibernate
            get("/", doctorController::readAll, Role.ANYONE);
            get("/{id}", doctorController::readById, Role.ANYONE);
            get("/speciality/{speciality}", doctorController::readBySpeciality, Role.ANYONE);
            get("/birthdate/range", doctorController::readyByBirthdayRange, Role.ANYONE);
            post("/", doctorController::create, Role.ANYONE);
            put("/{id}", doctorController::update);
            delete("/{id}", doctorController::delete);
        };
    }
}
