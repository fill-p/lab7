package dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestDaoFactory{

    @Test
    void shouldReturnDifferentConnections() throws DaoException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        assertNotEquals(daoFactory.getConnection(),daoFactory.getConnection());
    }
}
