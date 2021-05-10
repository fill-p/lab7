package dao.jdbc;

import dao.DaoFactory;
import dao.DaoWorker;
import data.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import dao.DaoException;

public class JdbcWorker implements DaoWorker {

    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private static final String CREATE_WORKER = "insert into worker(name,surname) values(?,?);";

    private static final String DELETE_WORKER = "delete from worker " +
            "where id = ?;";

    private static final String UPDATE_WORKER = "update worker set " +
            "name = ?," +
            "surname = ?" +
            "where id = ?;";

    private static final String FIND_WORKERS = "select * from worker;";


    @Override
    public void create(Worker worker) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(CREATE_WORKER);
            statement.setString(1, worker.getName());
            statement.setString(2, worker.getSurname());
            if(statement.execute()){
                throw new DaoException("Worker was not created");
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
    public void delete(Worker worker) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(DELETE_WORKER);
            statement.setLong(1, worker.getId());
            if(statement.execute()){
                throw new DaoException("Worker was not deleted");
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
    public void update(Worker worker) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(UPDATE_WORKER);
            statement.setString(1, worker.getName());
            statement.setString(2, worker.getSurname());
            statement.setLong(2, worker.getId());
            if(!statement.execute()){
                throw new DaoException("Worker was not updated");
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
    public Optional<List<Worker>> findAll() throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        List<Worker> workerList;
        try {
            connection = daoFactory.getConnection();
            statement = connection.prepareStatement(FIND_WORKERS);
            workerList = readDataFromResultSet(statement.executeQuery());
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
        return Optional.of(workerList);
    }



    private List<Worker> readDataFromResultSet(ResultSet resultSet) throws DaoException {
        List<Worker> workerList = new ArrayList<>();
        try {
            while(resultSet.next()){
                workerList.add(new Worker(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname")
                ));
            }
        } catch (SQLException e) {
            throw new DaoException("Reading from result set is failed",e);
        }
        return workerList;
    }
}
