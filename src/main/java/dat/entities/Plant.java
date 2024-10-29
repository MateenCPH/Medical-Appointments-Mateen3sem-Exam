package dat.entities;

import dat.dtos.PlantDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "plant")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "plant_type", nullable = false)
    private PlantType plantType;

    @Setter
    @Column(name = "plant_name", nullable = false)
    private String plantName;

    @Setter
    @Column(name = "plant_max_height", nullable = false)
    private int maxHeight;

    @Setter
    @Column(name = "plant_price", nullable = false)
    private double plantPrice;

    public Plant (PlantType plantType, String plantName, int maxHeight, double plantPrice){
        this.plantType = plantType;
        this.plantName = plantName;
        this.maxHeight = maxHeight;
        this.plantPrice = plantPrice;
    }

    public Plant (PlantDTO plantDTO) {
        this.id = plantDTO.getId();
        this.plantType = plantDTO.getPlantType();
        this.plantName = plantDTO.getPlantName();
        this.maxHeight = plantDTO.getMaxHeight();
        this.plantPrice = plantDTO.getPlantPrice();
    }

    public enum PlantType{
        ROSE, BUSH, FRUIT_AND_BERRIES, RHODODENDRON;
    }
}
