package dao.jdbc;

import dao.DaoException;
import dao.DaoFactory;
import dao.DaoPackageInfo;
import data.PackageInfo;
import data.ReceiveInfo;
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

public class TestJdbcPackageInfo {
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
    void shouldNotThrowExceptionForCreatingPackageInfo() throws DaoException {
        assertDoesNotThrow(() -> new JdbcPackageInfo().create(new PackageInfo("","")));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingEmptyPackageInfo() {
        assertThrows(DaoException.class,() -> new JdbcPackageInfo().create(new PackageInfo()));
    }

    @Test
    void shouldNotThrowDaoExceptionForDeletingPackageInfo() throws DaoException {
        new JdbcPackageInfo().create(new PackageInfo("",""));
        assertDoesNotThrow(() -> new JdbcPackageInfo().delete(new PackageInfo(1,"","")));
    }

    @Test
    void shouldNotThrowDaoExceptionForUpdatingPackageInfoAndShowNewValue() throws DaoException {
        new JdbcPackageInfo().create(new PackageInfo("",""));
        System.out.println(new JdbcPackageInfo().findAll().get());
        assertDoesNotThrow(() -> new JdbcPackageInfo().update(new PackageInfo(1,"New sender","New receiver")));
        List<PackageInfo> expected = new ArrayList<>();
        expected.add(new PackageInfo(1,"New sender","New receiver"));
        assertEquals(expected,new JdbcPackageInfo().findAll().get());
    }


    @Test
    void shouldShowAddedPackageInfos() throws DaoException {
        DaoPackageInfo daoPackageInfo = new JdbcPackageInfo();
        List<PackageInfo> expected = new ArrayList<>();
        int amount = 3;
        for(int i = 1; i <= amount; i++){
            daoPackageInfo.create(new PackageInfo("",""));
            expected.add(new PackageInfo(i,"",""));
        }
        assertEquals(expected, daoPackageInfo.findAll().get());
    }
}
