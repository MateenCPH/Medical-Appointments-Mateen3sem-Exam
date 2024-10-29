package dat.daos;

import dat.entities.Plant;

import java.util.List;

public interface IDAO<T, I> {

    T readById(I i);
    List<T> readAll();
//    List<T> readByType(T s);
    T create(T t);
    T update(I i, T t);
    void delete(I i);
    boolean validatePrimaryKey(I i);

}
