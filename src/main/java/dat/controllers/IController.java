package dat.controllers;

import io.javalin.http.Context;

public interface IController<T, D> {
    void readById(Context ctx);
    void readAll(Context ctx);
    void readByType(Context ctx);
    void create(Context ctx);
    void update(Context ctx);
    void delete(Context ctx);
    boolean validatePrimaryKey(D d);
    T validateEntity(Context ctx);

}
