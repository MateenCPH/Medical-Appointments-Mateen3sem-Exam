package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import jakarta.persistence.EntityManagerFactory;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(7007);
        Populate.main(args);
    }
}