package dao.jdbc;

import dao.DaoException;
import dao.DaoFactory;
import dao.DaoPackageInfo;
import dao.DaoReceiveInfo;
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

public class TestJdbcReceiveInfo {

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
    void shouldNotThrowExceptionForCreatingReceiveInfo() throws DaoException {
        new JdbcPackageInfo().create(new PackageInfo("",""));
        assertDoesNotThrow(() -> new JdbcReceiveInfo().create(new ReceiveInfo(1,"",true)));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingEmptyReceiveInfo() {
        assertThrows(DaoException.class, () -> new JdbcReceiveInfo().create(new ReceiveInfo()));
    }

    @Test
    void shouldThrowDaoExceptionForCreatingReceiveInfoWithoutExistingPackage() {
        assertThrows(DaoException.class, () -> new JdbcReceiveInfo().create(new ReceiveInfo(1,"",true)));
    }

    @Test
    void shouldNotThrowDaoExceptionForDeletingReceiveInfo() throws DaoException {
        new JdbcPackageInfo().create(new PackageInfo("",""));
        assertDoesNotThrow(() -> new JdbcReceiveInfo().delete(new ReceiveInfo(1,"",true)));
    }

    @Test
    void shouldNotThrowDaoExceptionForUpdatingReceiveInfoAndShowNewData() throws DaoException {
        DaoReceiveInfo receiveInfo = new JdbcReceiveInfo();
        new JdbcPackageInfo().create(new PackageInfo("",""));
        receiveInfo.create(new ReceiveInfo(1,"",false));
        assertDoesNotThrow(() -> receiveInfo.update(new ReceiveInfo(1,"",true)));
        List<ReceiveInfo> expected = new ArrayList<>();
        expected.add(new ReceiveInfo(1,"",true));
        assertEquals(expected,receiveInfo.findAll().get());
    }


    @Test
    void shouldShowAddedReceiveInfos() throws DaoException {
        DaoReceiveInfo daoReceiveInfo = new JdbcReceiveInfo();
        List<ReceiveInfo> expected = new ArrayList<>();
        new JdbcPackageInfo().create(new PackageInfo("",""));
        int amount = 3;
        for(int i = 1; i <= amount; i++){
            daoReceiveInfo.create(new ReceiveInfo(1,"",true));
            expected.add(new ReceiveInfo(1,"",true));
        }
        assertEquals(expected, daoReceiveInfo.findAll().get());
    }
}
