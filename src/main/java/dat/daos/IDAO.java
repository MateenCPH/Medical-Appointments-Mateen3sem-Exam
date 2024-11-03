package dat.daos;

import dat.exceptions.ApiException;

import java.util.List;

public interface IDAO<T, I> {

    T readById(I i) throws ApiException;
    List<T> readAll();
    T create(T t) throws Exception;
    T update(I i, T t);
    void delete(I i);
}