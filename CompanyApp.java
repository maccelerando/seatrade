import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CompanyApp {
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8150;
  private static final int DEFAULT_SHIPAPP_PORT_NUMBER = 8152;
  private static Scanner scanner = new Scanner(System.in);
  private static String userInput = "no-input";
  private static volatile boolean exit = false;
  private static String companyName;
  private double credit = 0.0D;
  private static Communicator communicatorSeaTrade;
  private static UserInputHandler userInputHandler = new UserInputHandler(scanner);

  public static void main(String[] args) {
    createDatabaseTables();

    String seaTradeServerAddress = setSeaTradeServerAddress();
    int seaTradePortNumber = setPortNumber();
    // wip
    companyName = setCompanyName();
    establishSeaTradeConnection(seaTradeServerAddress, seaTradePortNumber);

    // main routine
    while (!exit) {
      sendToSeaTrade();
    }
    cleanup();
    System.exit(0);
  }

  private static void sendToSeaTrade() {
    userInput = userInputHandler.getUserInput("CompanyApp-SeaTrade");
    if (userInput.equals(exit)) {
      exit = true;
    }
    sendMessageToSeaTrade(userInput);
  }

  private static void sendMessageToSeaTrade(String message) {
    try {
      communicatorSeaTrade.getPrintWriter().println(message);
      System.out.println("Sent message to Server = " + message);
    } catch (Exception e) {
      System.out.println("Error sendMessageToSeaTrade: " + e.getMessage());
    }
  }

  private static void establishSeaTradeConnection(String address, int portNumber) {
    try {
      communicatorSeaTrade = new Communicator(address, portNumber);
      String serverMessage = "register:".concat(companyName);
      communicatorSeaTrade.getPrintWriter().println(serverMessage);
      System.out.println("Sent message to Server = " + serverMessage);
    } catch (Exception e) {
      System.out.println("Error establishing connection with the server: " + e.getMessage());
    }
  }

  public static String setSeaTradeServerAddress() {
    String address = DEFAULT_SEATRADE_SERVER_ADDRESS;
    System.out.println("Enter SeaTrade IPv4 server address. Default is " + DEFAULT_SEATRADE_SERVER_ADDRESS + ".");
    String userInput = userInputHandler.getUserInput("ipv4");
    if (!userInput.isEmpty()) {
      address = userInput;
    } else {
      System.out.println("No input. Using default address " + address + ".");
    }
    return address;
  }

  public static int setPortNumber() {
    int portNr = DEFAULT_SEATRADE_PORT_NUMBER;
    System.out.println("Enter port number. Default is " + portNr + ".");
    String userInput = userInputHandler.getUserInput("portNumber");
    if (!userInput.isEmpty()) {
      portNr = Integer.parseInt(userInput);
    } else {
      System.out.println("No input. Using default port number " + portNr + ".");
    }
    return portNr;
  }

  public static String setCompanyName() {
    companyName = userInputHandler.getUserInput("companyName");
    return companyName;
  }

  public static void cleanup() {
    System.out.println("start cleanup");
    if (communicatorSeaTrade != null) {
      communicatorSeaTrade.cleanup();
    }
    if (scanner != null) {
      scanner.close();
      System.out.println("CompanyApp scanner closed");
    }
    System.out.println("cleanup complete");
    System.out.println("program ended");
  }

  private static void createDatabaseTables() {
    try (Connection con = Connect.getConnection()) {
      if (con != null) {
        System.out.println("Connected to database, creating tables...");

        // SQL-Befehl zum Löschen der Tabellen, falls sie bereits existieren
        String dropHarbour = "DROP TABLE IF EXISTS harbour;";
        String dropShip = "DROP TABLE IF EXISTS ship;";

        // SQL-Befehl zum Erstellen der Tabelle harbour
        String createHarbour = "CREATE TABLE harbour (" + "cargos INT, " + "position VARCHAR(255), " + "name VARCHAR(255)" + ");";

        // SQL-Befehl zum Erstellen der Tabelle ship
        String createShip = "CREATE TABLE ship (" + "credit INT, " + "position VARCHAR(255), " + "name VARCHAR(255), " + "routes INT" + ");";

        Statement stmt = con.createStatement();

        // Ausführen der Lösch- und Erstellungsbefehle
        stmt.execute(dropHarbour);
        stmt.execute(dropShip);
        stmt.execute(createHarbour);
        stmt.execute(createShip);

        System.out.println("Tables created successfully");
      }
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("Error creating database tables: " + e.getMessage());
    }
  }
}
