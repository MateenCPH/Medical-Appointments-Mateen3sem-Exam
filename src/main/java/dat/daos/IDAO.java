package dat.daos;

import java.util.List;

public interface IDAO<T, I, Ld1, Ld2, S> {

    T readById(I i) throws Exception;
    List<T> readAll() throws Exception;
    List<T> readBySpeciality(S s) throws Exception;
    List<T> readByBirthdayRange(Ld1 ld1, Ld2 ld2) throws Exception;
    T create(T t) throws Exception;
    T update(I i, T t) throws Exception;
    void delete(I i) throws Exception;
    boolean validatePrimaryKey(I i);

}
