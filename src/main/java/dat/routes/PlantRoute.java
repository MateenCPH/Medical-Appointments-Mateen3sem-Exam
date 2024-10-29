package dat.routes;

import dat.controllers.impl.PlantController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class PlantRoute {
    private final PlantController plantController = new PlantController();

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/", plantController::create, Role.ANYONE);
            get("/", plantController::readAll, Role.ANYONE);
            get("/plantNames", plantController::readAndReturnNames, Role.ANYONE);
            get("/pantNamesSorted", plantController::readAndReturnNamesSorted, Role.ANYONE);
            get("/{id}", plantController::readById, Role.ANYONE);
            get("/type/{type}", plantController::readByType, Role.ANYONE);
            get("/maxHeight/{maxHeight}", plantController::readByMaxHeight, Role.ANYONE);
        };
    }
}
