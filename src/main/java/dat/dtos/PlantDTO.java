package dat.dtos;

import dat.entities.Plant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PlantDTO {
    private Integer id;
    private Plant.PlantType plantType;
    private String plantName;
    private int maxHeight;
    private double plantPrice;

    public PlantDTO(Plant plant) {
        this.id = plant.getId();
        this.plantType = plant.getPlantType();
        this.plantName = plant.getPlantName();
        this.maxHeight = plant.getMaxHeight();
        this.plantPrice = plant.getPlantPrice();
    }

    public PlantDTO(Plant.PlantType plantType, String name, int maxHeight, double price) {
        this.plantType = plantType;
        this.plantName = name;
        this.maxHeight = maxHeight;
        this.plantPrice = price;
    }

    public static List<PlantDTO> toPlantDTOList(List<Plant> plants) {
        return plants.stream().map(PlantDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof PlantDTO plantDTO)) return false;

        return getId().equals(plantDTO.getId());
    }

    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }

}
