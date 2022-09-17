package com.geekbrains.cloud.netty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbAuthenticationProvider {
    private BdConnect dbConnection;
    private Statement stmt;

    public void init() {
        dbConnection = new BdConnect();
    }

    public String getNicknameByLoginAndPassword(String login, String password) {
        String query = String.format("select * from users where name = '%s' and score = '%s';", login, password);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            while (rs.next()) {
                System.out.println(rs.getString(2));
                return (rs.getString(2));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return " Логин и пароль введены неверно ";
    }
    public boolean booleanNicknameByLoginAndPassword(String login, String password) {
        String query = String.format("select * from users where name = '%s' and score = '%s';", login, password);
        try (ResultSet rs = dbConnection.getStmt().executeQuery(query)) {
            while (rs.next()) {
                System.out.println(rs.getString(2));
                return true;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    // записывсем нового пользователя в базу данных
    public void executeUser (String responceLogin, String responcePassword) throws SQLException {
       dbConnection.getStmt().executeUpdate(String.format("insert into users (name, score) values ('%s', '%s');", responceLogin, responcePassword));
    }
}
