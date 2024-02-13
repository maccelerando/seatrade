import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CompanyApp {
  private static final String[] COMPANY_NAMES = new String[]{"OceanLink", "AquaMarine", "SeaVista", "Nautical", "BlueHorizon", "SeaSwift", "Neptune's", "WaveCrest", "SeaHarbor", "Maritime", "AquaTrade", "Oceanic", "SeaSail", "BlueWave", "Horizon", "SeaCurrent", "Marisource", "AquaMeridian", "NautiLink", "OceanCraft"};
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final String DEFAULT_SHIPAPP_ADDRESS = "localhost";
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8150;
  private static final int DEFAULT_SHIPAPP_PORT_NUMBER = 8152;
  private static Scanner scanner = new Scanner(System.in);
  private static String userInput = "no-input";
  private static volatile boolean exit = false;
  private static String[] inputSelectionStrings = new String[] {"register:", "getinfo:harbour", "getinfo:cargo", "exit"};
  private double credit = 0.0D;
  private static AutoComplete autoComplete = new AutoComplete(inputSelectionStrings);
  private static Communicator communicator;

  public static void main(String[] args) {
    createDatabaseTables();
    String seatradeServerAddress = setServerAddress("SeaTrade");
    int portNr = setPortNumber();
    String companyName = setCompanyName();
    inputSelectionStrings[0] = inputSelectionStrings[0].concat(companyName);
    establishConnection(seatradeServerAddress, portNr);

    // main routine
    while (!exit) {
      sendToSeaTrade();
    }
    cleanup();
    System.exit(0);
  }

  private static void sendToSeaTrade() {
    System.out.println("Enter message to server. Suggestions: \n-> register:companyname\n-> getinfo:harbour\n-> getinfo:cargo\n-> exit");
    String input = "default";

    try {
      input = scanner.nextLine();
      if (!input.isEmpty()) {
        input = autoComplete.autoCompleteInput(input);
      } else {
        input = "exit";
      }
    } catch (Exception e) {
      System.out.println("Error with input: " + e.getMessage());
    }
    if (input.equals("exit")) {
      exit = true;
    }
    sendMessageToServer(input);
  }

  private static void sendMessageToServer(String message) {
    try {
      communicator.getPrintWriter().println(message);
      System.out.println("Sent message to Server = " + message);
    } catch (Exception e) {
      System.out.println("Error sendMessageToServer: " + e.getMessage());
    }

  }

  private static void establishConnection(String address, int portNumber) {
    try {
      communicator = new Communicator(address, portNumber);
      String serverMessage = inputSelectionStrings[0];
      communicator.getPrintWriter().println(serverMessage);
      System.out.println("Sent message to Server = " + serverMessage);
    } catch (Exception e) {
      System.out.println("Error establishing connection with the server: " + e.getMessage());
    }
  }

  // serverName should be "SeaTrade" or "ShipApp"
  public static String setServerAddress(String serverName) {
    String address = "";
    switch (serverName) {
    case "SeaTrade":
      address = DEFAULT_SEATRADE_SERVER_ADDRESS;
      break;
    case "ShipApp":
      address = DEFAULT_SHIPAPP_ADDRESS;
      break;
    default:
      System.out.println("Error: SetServerAddress switch fall through to not reachable code");
    }
    System.out.println("Enter " + serverName + " IPv4 address. Default is " + address + ".");
    String userInput = scanner.nextLine();
    if (!userInput.isEmpty()) {
      try {
        address = userInput;  // TODO: check if user input is in valid server address format
      } catch (Exception e) {
        System.out.println("Input error.\n Using default server address " + DEFAULT_SEATRADE_SERVER_ADDRESS + ".");
      }
    } else {
      System.out.println("No input.\nUsing default server address " + DEFAULT_SEATRADE_SERVER_ADDRESS + ".");
    }
    return address;
  }

  public static int setPortNumber() {
    int portNr = DEFAULT_SEATRADE_PORT_NUMBER;
    System.out.println("Enter port number. Default is " + portNr + ".");
    String userInput = scanner.nextLine();
    if (!userInput.isEmpty()) {
      try {
        portNr = Integer.parseInt(userInput);
        System.out.println("Port number = " + portNr);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("No input. \nUsing default port number " + portNr + ".");
    }

    return portNr;
  }

  public static String setCompanyName() {
    String companyName = COMPANY_NAMES[0];
    System.out.println("Enter company name.");
    try {
      userInput = scanner.nextLine();
      if (!userInput.isEmpty()) {
        if (userInput.matches("\\d+")) {
          int companyNameIndex = Integer.parseInt(userInput) % COMPANY_NAMES.length;
          companyName = COMPANY_NAMES[companyNameIndex];
        } else {
          companyName = userInput;
        }
      }
    } catch (Exception e) {
      System.out.println("Error with user input.");
    }
    return companyName;
  }

  public static void cleanup() {
    System.out.println("start cleanup");
    if (communicator != null) {
      communicator.cleanup();
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
