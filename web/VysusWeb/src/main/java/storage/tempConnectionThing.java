import java.sql.*;

    String url = "jdbc:mysql://vysus1.cw2j7jythjii.eu-west-2.rds.amazonaws.com:3306/vysusdb";
    String username = "foxtrot";
    String password = "vysusfoxtrotprojectdatabasepassword";

    Connection connection = null;
    try {
        connection = (Connection) DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
        //catch this
    }
    if (connection != null) {
        //use

        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

