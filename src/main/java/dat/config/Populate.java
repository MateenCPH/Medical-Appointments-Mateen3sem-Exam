package dat.config;

import dat.entities.Plant;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Populate {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Set<Plant> plants = getPlants();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            for (Plant plant : plants) {
                em.persist(plant);  // Persist each plant
            }
            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Plant> getPlants() {
        Plant rose = new Plant(Plant.PlantType.ROSE, "Red Rose", 150, 12.99);
        Plant bush = new Plant(Plant.PlantType.BUSH, "Blueberry Bush", 200, 25.99);
        Plant rhododendron = new Plant(Plant.PlantType.RHODODENDRON, "Purple Rhododendron", 250, 35.99);
        Plant appleTree = new Plant(Plant.PlantType.FRUIT_AND_BERRIES, "Apple Tree", 500, 45.50);

        Plant[] plantArray = {rose, bush, rhododendron, appleTree};
        return Set.of(plantArray);
    }
}
