package dat.config;

import dat.controllers.impl.ExceptionController;
import dat.routes.Routes;
import dat.exceptions.ApiException;

import dat.security.controllers.AccessController;
import dat.security.enums.Role;
import dat.security.routes.SecurityRoutes;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ApplicationConfig {

    private static Routes routes = new Routes();
    private static final ExceptionController exceptionController = new ExceptionController();
    private static AccessController accessController = new AccessController();

    public static void configuration(JavalinConfig config) {
        config.showJavalinBanner = false;
        config.bundledPlugins.enableRouteOverview("/routes", Role.ANYONE);
        config.bundledPlugins.enableDevLogging();
        config.router.contextPath = "/api"; // base path for all endpoints
        config.router.apiBuilder(routes.getRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecuredRoutes());
        config.router.apiBuilder(SecurityRoutes.getSecurityRoutes());
    }

    private static void exceptionContext(Javalin app) {
        app.exception(ApiException.class, exceptionController::apiExceptionHandler);
        app.exception(Exception.class, exceptionController::exceptionHandler);
    }

    public static Javalin startServer(int port) {
        Javalin app = Javalin.create(ApplicationConfig::configuration);
        exceptionContext(app);
        app.beforeMatched(accessController::accessHandler);
        app.beforeMatched(ctx -> accessController.accessHandler(ctx));
        app.start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}