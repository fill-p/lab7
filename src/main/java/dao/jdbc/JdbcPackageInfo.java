package dao.jdbc;

import dao.DaoException;
import dao.DaoFactory;
import dao.DaoPackageInfo;
import data.PackageInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPackageInfo implements DaoPackageInfo {

    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private static final String CREATE_PRESENT = "insert into package_info(sender,receiver) values(?,?);";

    private static final String DELETE_PRESENT = "delete from package_info " +
            "where id = ?;";

    private static final String UPDATE_PRESENT = "update package_info set " +
            "sender = ?," +
            "receiver = ?" +
            "where id = ?;";

    private static final String FIND_PRESENT = "select * from package_info;";


    @Override
    public void create(PackageInfo packageInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(CREATE_PRESENT);
            statement.setString(1, packageInfo.getSender());
            statement.setString(2, packageInfo.getReceiver());
            if(statement.execute()){
                throw new DaoException("PackageInfo was not created");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void delete(PackageInfo packageInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(DELETE_PRESENT);
            statement.setLong(1, packageInfo.getId());
            if(statement.execute()){
                throw new DaoException("PackageInfo was not deleted");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public void update(PackageInfo packageInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(UPDATE_PRESENT);
            statement.setString(1, packageInfo.getSender());
            statement.setString(2, packageInfo.getReceiver());
            statement.setLong(3, packageInfo.getId());
            if(statement.execute()){
                throw new DaoException("PackageInfo was not updated");
            }
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
    }

    @Override
    public Optional<List<PackageInfo>> findAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<PackageInfo> packageInfoList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_PRESENT);
            packageInfoList = readDataFromResultSet(statement.executeQuery());
        } catch (DaoException | SQLException e){
            throw new DaoException(e.getMessage(),e);
        } finally {
            try {
                if(connection != null){
                    connection.close();
                }
                if(statement != null){
                    statement.close();
                }
            }catch (SQLException e){
                throw new DaoException("Cannot close connection",e);
            }
        }
        return Optional.of(packageInfoList);
    }

    private List<PackageInfo> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<PackageInfo> packageInfoList = new ArrayList<>();
        try {
            while(resultSet.next()){
                packageInfoList.add(new PackageInfo(
                        resultSet.getInt("id"),
                        resultSet.getString("sender"),
                        resultSet.getString("receiver")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return packageInfoList;
    }
}
