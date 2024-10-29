package dat;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.PlantDAO;
import jakarta.persistence.EntityManagerFactory;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(7007);
        Populate.main(args);
        //New test
        //new test again
//        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
//        PlantDAO dao = PlantDAO.getInstance(emf);
//        dao.readPlantNames().forEach(System.out::println);
    }
}