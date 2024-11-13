package org.example.service;

import org.example.model.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PersonService {
    private Connection connection;
    public PersonService(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }
    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS person (
                    id INTEGER,
                    name VARCHAR(100) NOT NULL,
                    surname VARCHAR(100) NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    subscribed BOOLEAN
                );""";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }
    public void insert(List<Person> Persons) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into person (id, name, surname, phone, subscribed) values (?, ?, ?, ?, ?)");
        for (int i = 0; i<Persons.size(); i++){
            preparedStatement.setInt(1,i);
            preparedStatement.setString(2, Persons.get(i).getName());
            preparedStatement.setString(3, Persons.get(i).getSurname());
            preparedStatement.setString(4, Persons.get(i).getPhone());
            preparedStatement.setBoolean(5, Persons.get(i).getSubscribed());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }
    public void insert(int id, String name, String surname, String phone, Boolean subscribed) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into person (id, name, surname, phone, subscribed) values (?, ?, ?, ?, ?)");
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, surname);
        preparedStatement.setString(4,phone);
        preparedStatement.setBoolean(5, subscribed);
        preparedStatement.execute();
    }
    public void drop() throws SQLException {
        String sql = "DROP TABLE IF EXISTS person";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }
    public Optional<Person> findById(int id) {
        try {
            String sql = "select * from study.person where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Person(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("phone"),
                        resultSet.getBoolean("subcribed")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
