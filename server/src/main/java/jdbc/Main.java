package jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.utils.JDBCUtils;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = JDBCUtils.getNewConnection()) {
            JDBCUtils.printArticleList(connection);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}