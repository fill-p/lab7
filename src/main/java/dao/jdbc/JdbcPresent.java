package dao.jdbc;

import dao.DaoException;
import dao.DaoFactory;
import dao.DaoPresent;
import data.Present;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPresent implements DaoPresent {

    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private static final String CREATE_PRESENT = "insert into presents(worker_id,date,is_present) values(?,?,?);";

    private static final String DELETE_PRESENT = "delete from presents " +
            "where worker_id = ?;";

    private static final String UPDATE_PRESENT = "update presents set " +
            "date = ?," +
            "is_present = ?" +
            "where worker_id = ?;";

    private static final String FIND_PRESENT = "select * from presents;";


    @Override
    public void create(Present present) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(CREATE_PRESENT);
            statement.setLong(1, present.getWorkerId());
            statement.setString(2, present.getDate());
            statement.setBoolean(3, present.isPresent());
            if(statement.execute()){
                throw new DaoException("Present was not created");
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
    public void delete(Present present) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(DELETE_PRESENT);
            statement.setLong(1, present.getWorkerId());
            if(statement.execute()){
                throw new DaoException("Present was not deleted");
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
    public void update(Present present) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(UPDATE_PRESENT);
            statement.setString(1, present.getDate());
            statement.setBoolean(2, present.isPresent());
            statement.setLong(2, present.getWorkerId());
            if(!statement.execute()){
                throw new DaoException("Present was not updated");
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
    public Optional<List<Present>> findAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Present> presentList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_PRESENT);
            presentList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(presentList);
    }

    private List<Present> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<Present> presentList = new ArrayList<>();
        try {
            while(resultSet.next()){
                presentList.add(new Present(
                        resultSet.getInt("worker_id"),
                        resultSet.getString("date"),
                        resultSet.getBoolean("is_present")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return presentList;
    }
}
