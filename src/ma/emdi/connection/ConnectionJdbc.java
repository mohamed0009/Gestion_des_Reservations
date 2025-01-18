package ma.emdi.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionJdbc {
    private static final String URL = "jdbc:mysql://localhost:3306/reservation_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "IDRISSI@2001";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL chargé avec succès !");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur : Driver MySQL non trouvé.");
            e.printStackTrace();
        }
    }

    public static Connection getCnx() {
        try {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Erreur : Impossible d'obtenir une connexion.");
            e.printStackTrace();
            return null;
        }
    }
}
