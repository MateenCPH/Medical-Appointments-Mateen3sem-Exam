package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Routes {

    private final DoctorRoutes doctorRoute = new DoctorRoutes();
//    private final PlantRoute plantRoute = new PlantRoute();

    public EndpointGroup getRoutes() {
        return () -> {
//                path("/plants", plantRoute.getRoutes());
                path("/doctors", doctorRoute.getRoutes());
        };
    }
}