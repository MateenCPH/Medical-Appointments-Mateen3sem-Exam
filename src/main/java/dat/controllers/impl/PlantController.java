package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.PlantDAO;
import dat.dtos.PlantDTO;
import dat.entities.Plant;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PlantController implements IController<PlantDTO, Integer> {
    private final PlantDAO dao;

    public PlantController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = PlantDAO.getInstance(emf);
    }

    @Override
    public void readById(Context ctx)  {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        PlantDTO plantDTO = dao.readById(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(plantDTO, PlantDTO.class);
    }

    @Override
    public void readAll(Context ctx) {
        // List of DTOS
        List<PlantDTO> plantDTOS = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(plantDTOS, PlantDTO.class);
    }

    @Override
    public void readByType(Context ctx) {
        // request
        Plant.PlantType type = Plant.PlantType.valueOf(ctx.pathParam("type"));
        // List of DTOS
        List<PlantDTO> plantDTOS = dao.readByType(type);
        // response
        ctx.res().setStatus(200);
        ctx.json(plantDTOS, PlantDTO.class);
    }

    @Override
    public void create(Context ctx) {
        // request
        PlantDTO jsonRequest = ctx.bodyAsClass(PlantDTO.class);
        // DTO
        PlantDTO plantDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(plantDTO, PlantDTO.class);
    }

    @Override
    public void update(Context ctx) {

    }

    @Override
    public void delete(Context ctx) {

    }

    public void readByMaxHeight(Context ctx) {
        //request
        int height = ctx.pathParamAsClass("maxHeight", Integer.class).check(h -> h > 0, "Height must be greater than 0").get();
        // List of DTOS
        List<PlantDTO> plantDTOS = dao.readByMaximumHeight(height);
        // response
        ctx.res().setStatus(200);
        ctx.json(plantDTOS, PlantDTO.class);
    }

    public void readAndReturnNames(Context ctx) {
        // List of plant names only
        List<String> plantNames = dao.readPlantNames();
        ctx.status(200);
        ctx.json(plantNames);
    }

    public void readAndReturnNamesSorted(Context ctx) {
        // List of plant names only
        List<PlantDTO> plantDTOS = dao.readPlantsAndSortByName();
        ctx.status(200);
        ctx.json(plantDTOS, PlantDTO.class);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public PlantDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(PlantDTO.class)
                .check( h -> h.getPlantType() != null, "Plant type must be set")
                .check( h -> h.getPlantName() != null && !h.getPlantName().isEmpty(), "Plant name must be set")
                .check(h -> h.getMaxHeight() > 0, "Plant height must be greater than 0")
                .check(h -> h.getPlantPrice() > 0, "Plant price must be greater than 0")
                .get();
    }
}
