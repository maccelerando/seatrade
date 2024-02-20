import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class CompanyApp {
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8150;
  private static final int DEFAULT_SHIPAPP_PORT_NUMBER = 8152;
  private static Scanner scanner = new Scanner(System.in);
  private static String userInput = "no-input";
  private static volatile boolean exit = false;
  private static String companyName;
  private static double credit = 0.0D;
  private static Communicator communicatorSeaTrade;
  private static UserInputHandler userInputHandler = new UserInputHandler(scanner);
  private static CompanyServer companyServer;

  public static void main(String[] args) {


    // initialize
    String seaTradeServerAddress = setSeaTradeServerAddress();
    int seaTradePortNumber = setPortNumber();
    companyName = setCompanyName();
    establishSeaTradeConnection(seaTradeServerAddress, seaTradePortNumber);
    
    // database
    createDatabaseTables();
    initializeOrUpdateShip(companyName, credit);

    // company server
    companyServer = new CompanyServer(8152);
    companyServer.run();

    // main routine
    while (!exit) {
      sendToSeaTrade();
    }
    // program end
    cleanup();
    System.exit(0);
  }

  private static void processInputFromSeaTrade(String input) {
    // process input
    String[] processedString = input.split(":");
    switch (processedString[0]) {
    case "registered":
      credit = Double.parseDouble(processedString[2]);
      // TODO add update database here
      initializeOrUpdateShip(companyName, credit);
      break;
    case "":  // TODO when ship arrives in harbour
    default:
      System.out.println("Error: processInputFromSeaTrade unreachable code");
    }
  }

  private static void sendToSeaTrade() {
    userInput = userInputHandler.getUserInput("CompanyApp-SeaTrade");
    if (userInput.equals("exit")) {
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
    //
    new Thread(() -> {
      String input = "";
      while(!exit) {
        input = communicatorSeaTrade.getListener().getInput();
        if (!input.isEmpty()) {
          communicatorSeaTrade.getListener().resetInput();
          processInputFromSeaTrade(input);
        }
      }
      // TODO interrupt this thread here
    }).start();
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

        String dropHarbour = "DROP TABLE IF EXISTS harbour;";
        String dropShip = "DROP TABLE IF EXISTS ship;";

        String createHarbour = "CREATE TABLE harbour (" +
            "cargos INT, " +
            "position VARCHAR(255), " +
            "name VARCHAR(255)" +
            ");";

        String createShip = "CREATE TABLE ship (" +
            "credit DOUBLE, " +
            "position VARCHAR(255), " +
            "name VARCHAR(255) PRIMARY KEY, " +
            "routes INT" +
            ");";

        Statement stmt = con.createStatement();

        stmt.execute(dropHarbour);
        stmt.execute(dropShip);
        stmt.execute(createHarbour);
        stmt.execute(createShip);

        System.out.println("Tables and initial ship record created successfully");
      }
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("Error creating database tables: " + e.getMessage());
    }
  }

  public static void initializeOrUpdateShip(String companyName, double credit) {
    try (Connection con = Connect.getConnection()) {
      if (con != null) {
        // Überprüfen, ob das Schiff bereits existiert
        String checkShipExists = "SELECT count(*) FROM ship WHERE name = ?";
        boolean exists = false;
        try (PreparedStatement pstmt = con.prepareStatement(checkShipExists)) {
          pstmt.setString(1, companyName);
          ResultSet rs = pstmt.executeQuery();
          if (rs.next()) {
            exists = rs.getInt(1) > 0;
          }
        }

        if (exists) {
          // Update existing ship credit
          String updateShip = "UPDATE ship SET credit = ? WHERE name = ?";
          try (PreparedStatement pstmt = con.prepareStatement(updateShip)) {
            pstmt.setDouble(1, credit);
            pstmt.setString(2, companyName);
            pstmt.executeUpdate();
          }
        } else {
          // Insert new ship with initial credit
          String insertShip = "INSERT INTO ship (name, credit, position, routes) VALUES (?, ?, '', 0);";
          try (PreparedStatement pstmt = con.prepareStatement(insertShip)) {
            pstmt.setString(1, companyName);
            pstmt.setDouble(2, credit);
            pstmt.executeUpdate();
          }
        }

        System.out.println("Ship record initialized or updated successfully");
      }
    } catch (SQLException | ClassNotFoundException e) {
      System.out.println("Error initializing or updating ship: " + e.getMessage());
    }
  }


}
