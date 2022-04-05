package jdbc.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {
    public static Connection getNewConnection() throws SQLException {
        final String dbURL = "jdbc:postgresql://localhost:5435/home_budget_db";
        final String userName = "postgres";
        final String password = "123456";

        Connection connection = DriverManager.getConnection(dbURL, userName, password);
        if (connection.isValid(1)) {
            System.out.println("Connection successful");
        }
        return connection;
    }

    //Test function
    public static void printArticleList(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM articles")
        ) {
            while (resultSet.next()) {
                final int articleId = resultSet.getInt("id");
                final String articleName = resultSet.getString("name");
                System.out.println(articleId + " " + articleName);
            }
        }
    }
}
