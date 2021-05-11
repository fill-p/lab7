package dao.jdbc;

import dao.DaoFactory;
import dao.DaoReceiveInfo;
import data.ReceiveInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DaoException;

public class JdbcReceiveInfo implements DaoReceiveInfo {

    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private static final String CREATE_RECEIVE_INFO = "insert into receive_info(package_id,receive_date,is_receive) values(?,?,?);";

    private static final String DELETE_RECEIVE_INFO = "delete from receive_info " +
            "where package_id = ?;";

    private static final String UPDATE_RECEIVE_INFO = "update receive_info set " +
            "receive_date = ?," +
            "is_receive = ?" +
            "where package_id = ?;";

    private static final String FIND_RECEIVE_INFO = "select * from receive_info;";


    @Override
    public void create(ReceiveInfo receiveInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(CREATE_RECEIVE_INFO);
            statement.setLong(1, receiveInfo.getPackageId());
            statement.setString(2, receiveInfo.getReceiveDate());
            statement.setBoolean(3,receiveInfo.isReceive());
            if(statement.execute()){
                throw new DaoException("ReceiveInfo was not created");
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
    public void delete(ReceiveInfo receiveInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(DELETE_RECEIVE_INFO);
            statement.setLong(1, receiveInfo.getPackageId());
            if(statement.execute()){
                throw new DaoException("ReceiveInfo was not deleted");
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
    public void update(ReceiveInfo receiveInfo) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(UPDATE_RECEIVE_INFO);
            statement.setBoolean(2, receiveInfo.isReceive());
            statement.setString(1, receiveInfo.getReceiveDate());
            statement.setLong(3, receiveInfo.getPackageId());
            if(statement.execute()){
                throw new DaoException("ReceiveInfo was not updated");
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
    public Optional<List<ReceiveInfo>> findAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<ReceiveInfo> receiveInfoList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_RECEIVE_INFO);
            receiveInfoList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(receiveInfoList);
    }



    private List<ReceiveInfo> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<ReceiveInfo> receiveInfoList = new ArrayList<>();
        try {
            while(resultSet.next()){
                receiveInfoList.add(new ReceiveInfo(
                        resultSet.getInt("package_id"),
                        resultSet.getString("receive_date"),
                        resultSet.getBoolean("is_receive")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return receiveInfoList;
    }
}
