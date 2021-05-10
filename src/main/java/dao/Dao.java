package dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    void create(T t) throws DaoException;

    void delete(T t) throws DaoException;

    void update(T t) throws DaoException;

    Optional<List<T>> findAll() throws DaoException;
}
