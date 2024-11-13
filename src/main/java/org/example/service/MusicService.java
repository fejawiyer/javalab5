package org.example.service;

import org.example.model.Music;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MusicService {
    private Connection connection;

    public MusicService(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }

    public Optional<List<Music>> findAll() {
        List<Music> musicList = new ArrayList<>();
        String sql = "select * from study.music";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                musicList.add(new Music(resultSet.getInt("id"), resultSet.getString("name")));
            }

            return Optional.of(musicList);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    public Optional<List<Music>> findMusicWOSymbols(String symbols) throws SQLException {
        if (symbols.isEmpty())
        {
            return Optional.empty();
        }
        StringBuilder sql = new StringBuilder("select * from study.music where ");
        for (int i = 0; i < symbols.length(); i++) {
            sql.append("lower(name) not like ?");
            if (i < symbols.length() - 1) {
                sql.append(" and ");
            }
        }
        PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
        for (int i = 0; i<symbols.length(); i++)
        {
            preparedStatement.setString(i+1, "%" + symbols.charAt(i)+ "%");
        }
        List<Music> musicList = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            musicList.add(new Music(resultSet.getInt("id"), resultSet.getString("name")));
        }

        return musicList.isEmpty() ? Optional.empty() : Optional.of(musicList);
    }
    public void addMusic(int id, String composition) throws SQLException {
        String sql = "insert into study.music (id,name) values (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, composition);
        preparedStatement.execute();
    }
}