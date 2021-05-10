package dao.jdbc;

import dao.DaoException;
import dao.DaoFactory;
import dao.DaoPresent;
import data.Present;
import data.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestJdbcPresent {
    private static final DaoFactory factory = DaoFactory.getInstance();

    @BeforeEach
    public  void initDatabase() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = factory.getConnection();
            String sql = getSqlFileQueries("src/test/resources/schema.sql");
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterEach
    public  void clearDataBase() throws DaoException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection  = factory.getConnection();
            String sql = getSqlFileQueries("src/test/resources/clearDB.sql");
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert statement != null;
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getSqlFileQueries(String fileRoute) throws IOException {
        StringBuilder queries = new StringBuilder();
        File file = new File(fileRoute);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String string;
        while ((string = br.readLine()) != null) {
            queries.append(string);
        }
        return queries.toString();
    }

    @Test
    void shouldNotThrowExceptionForCreatingPresent() throws DaoException {
        new JdbcWorker().create(new Worker("",""));
        assertDoesNotThrow(() -> new JdbcPresent().create(new Present(1,"time",true)));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingEmptyPresent() {
        assertThrows(DaoException.class,() -> new JdbcPresent().create(new Present()));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingPresentWhenThereIsNoWorkers() {
        assertThrows(DaoException.class,() -> new JdbcPresent().create(new Present(1,"time",true)));
    }

    @Test
    void shouldNotThrowDaoExceptionForDeletingPresent() throws DaoException {
        new JdbcWorker().create(new Worker("",""));
        new JdbcPresent().create(new Present(1,"time",true));
        assertDoesNotThrow(() -> new JdbcPresent().delete(new Present(1,"time",true)));
    }

    @Test
    void shouldShowAddedPresents() throws DaoException {
        DaoPresent daoPresent = new JdbcPresent();
        List<Present> expected = new ArrayList<>();
        new JdbcWorker().create(new Worker("",""));
        int amount = 3;
        for(int i = 1; i <= amount; i++){
            daoPresent.create(new Present(1,"time",true));
            expected.add(new Present(1,"time",true));
        }
        assertEquals(expected,daoPresent.findAll().get());
    }
}
