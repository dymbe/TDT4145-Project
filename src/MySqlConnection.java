import java.sql.*;

public class MySqlConnection {

    private String dbName;
    private String user;
    private String password;
    private Connection connection;

    public MySqlConnection(String dbName, String user, String password) {
        this.dbName = dbName;
        this.user = user;
        this.password = password;
        connection = null;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Could not find driver");
        }
        String url = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false";

        connection = DriverManager.getConnection(url, user, password);
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public void close() {
        if (connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        MySqlConnection con = new MySqlConnection("tdt4145", "root", "root");
        ResultSet rs = null;
        try {
            rs = con.executeQuery("SELECT * FROM Apparat");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(rs);
    }

}