package org.example.service;

import org.example.model.Book;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookService {
    private Connection connection;
    public BookService(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }
    public void createTable() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS books (
                    id INTEGER,
                    name VARCHAR(100) NOT NULL,
                    author VARCHAR(100) NOT NULL,
                    publishingYear INTEGER,
                    ISBN VARCHAR(100),
                    publisher VARCHAR(100)
                );""";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }
    public void insert(List<Book> Books) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into books (id, name, author, publishingYear, ISBN, publisher) values (?, ?, ?, ?, ?, ?)");
        for (int i = 0; i<Books.size(); i++) {
            preparedStatement.setInt(1, i);
            preparedStatement.setString(2, Books.get(i).getName());
            preparedStatement.setString(3, Books.get(i).getAuthor());
            preparedStatement.setInt(4, Books.get(i).getPublishingYear());
            preparedStatement.setString(5, Books.get(i).getIsbn());
            preparedStatement.setString(6, Books.get(i).getPublisher());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
    }
    public void insert(int id, String name, String author, int publishingYear, String ISBN, String publisher) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("insert into books (id, name, author, publishingYear, ISBN, publisher) values (?, ?, ?, ?, ?, ?)");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, author);
        preparedStatement.setInt(4, publishingYear);
        preparedStatement.setString(5, ISBN);
        preparedStatement.setString(6, publisher);
        preparedStatement.execute();
    }
    public void drop() throws SQLException {
        String sql = "DROP TABLE IF EXISTS books";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.execute();
    }
    public Optional<List<Book>> selectSorted() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "select id, name, author, publishingYear, ISBN, publisher from study.books order by publishingYear";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            books.add(new Book(resultSet.getString("name"), resultSet.getString("author"),
                    resultSet.getInt("publishingYear"), resultSet.getString("isbn"),
                    resultSet.getString("publisher")));
        }
        return books.isEmpty() ? Optional.empty() : Optional.of(books);
    }
    public Optional<List<Book>> selectYear(int year) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "select id, name, author, publishingYear, ISBN, publisher from study.books where publishingYear > ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, year);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            books.add(new Book(resultSet.getString("name"), resultSet.getString("author"),
                    resultSet.getInt("publishingYear"), resultSet.getString("isbn"),
                    resultSet.getString("publisher")));
        }
        return books.isEmpty() ? Optional.empty() : Optional.of(books);
    }
    public Optional<Book> findById(int id) {
        try {
            String sql = "select * from study.book where id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(new Book(resultSet.getString("name"), resultSet.getString("author"), resultSet.getInt("publishingYear"),
                        resultSet.getString("ISBN"), resultSet.getString("publisher")));
            }
            return Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
}
