import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

  public static Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.cj.jdbc.Driver");
    return DriverManager.getConnection("jdbc:mysql://localhost:3306/SeaTradeDB", "root", "");
  }

  public static void main(String[] args) {
    try {
      Connection con = getConnection(); // getConnection innerhalb der main-Methode
      if (con != null) {
        System.out.println("Database connected");
        // Hier könnte man weitere Tests durchführen, z.B. eine Abfrage ausführen
      }
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }
}
