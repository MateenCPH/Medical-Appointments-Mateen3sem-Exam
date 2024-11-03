package dat.controllers;

import dat.exceptions.ApiException;
import io.javalin.http.Context;

public interface IController<T, D> {
    void readById(Context ctx);
    void readAll(Context ctx);
    void readBySpeciality(Context ctx);
    void readyByBirthdayRange(Context ctx);
    void create(Context ctx);
    void update(Context ctx);
    void delete(Context ctx);
}