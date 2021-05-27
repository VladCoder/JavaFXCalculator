package ru.vlsoft;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtils {

    private static Connection connection;

    protected static Connection getConnection() throws SQLException {

        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:h2:~/calcdb", "sa", "");
        }

        return connection;
    }

    protected static void initDatabase() throws SQLException {

        Statement statement = getConnection().createStatement();
        statement.execute("" +
                "create table if not exists history(" +
                "id int primary key auto_increment," +
                "entry varchar(200))");
        statement.close();

    }

    protected static void log(String entry) throws SQLException {

        PreparedStatement statement = getConnection().prepareStatement("" +
                "insert into history(entry)" +
                "values(?)");
        statement.setString(1, entry);
        statement.execute();
        statement.close();

    }

    protected static List<String> getLog() throws SQLException {

        Statement statement = getConnection().createStatement();
        ResultSet rs = statement.executeQuery("select entry from history order by id desc limit 10");

        List<String> result = new ArrayList<String>();

        while (rs.next()) {
            result.add(rs.getString("entry"));
        }

        rs.close();
        statement.close();

        return result;

    }

}
