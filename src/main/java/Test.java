import java.sql.*;

public class Test {

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://77.55.192.67:3306/wsx22";
    private String login = "wsx22";
    private String password = "wsx22";

    private Connection con;
    private PreparedStatement ps;
    private String sql;
    private ResultSet rs;

    public Test() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, login, password);
        }
        catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
