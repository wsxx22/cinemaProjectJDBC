package utils;

import javax.xml.transform.Result;
import java.sql.*;

public class sql {


    private static String url = "jdbc:mysql://77.55.192.67:3306/wsx22";
    private static String login = "wsx22";
    private static String password = "wsx22";

    private sql () { }

    public static ResultSet connectToDatabase (String sql ) {

        try {

            Connection connection = DriverManager.getConnection(url, login, password);
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();

            return resultSet;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
