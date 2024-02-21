import java.util.Scanner;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ShipApp {

  private static final String DEFAULT_COMPANYAPP_SERVER_ADDRESS = "localhost";
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final int DEFAULT_COMPANYAPP_PORT_NUMBER = 8152;
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8151;
  private static final Scanner scanner = new Scanner(System.in);
  private static volatile boolean exit = false;  // TODO implement functionality
  private static String userInput = "no-input";
  private static Communicator communicatorSeaTrade;
  private static Communicator communicatorCompanyApp;
  private static UserInputHandler userInputHandler = new UserInputHandler(scanner);
  private static double credit = 0.0D;  // TODO: enhance feature
  private static String shipName;

  private static final String[] inputSelectionStrings = new String[] {"launch:", "moveto:", "loadcargo", "unloadcargo", "exit"};

  public static void main(String[] args) {

    String seatradeServerAddress = setServerAddress("SeaTrade");
    int seaTradePortNumber = setPortNumber();
    // ship name
    shipName = userInputHandler.getUserInput("shipName");
    // harbor name
    String harborNameLaunch = "";
    while (harborNameLaunch.isEmpty()) {
      harborNameLaunch = userInputHandler.getUserInput("harborName");
    }
    // company
    String companyName = userInputHandler.getUserInput("companyName");
    if (!companyName.equals("Quickstart")) {
      String companyAppServerAddress = setServerAddress("CompanyApp");
      int companyAppPortNumber = setPortNumber();
      establishCompanyAppConnection(companyAppServerAddress, companyAppPortNumber, shipName);
    }
    establishSeaTradeConnection(seatradeServerAddress, seaTradePortNumber, companyName, harborNameLaunch, shipName);

    // main routine
    while (!exit) {
      sendToSeaTrade();
    }

    // TODO cleanup and exit
  }
  
  private static void processInputFromSeaTrade(String input) {
    // process input
    String[] processedString = input.split(":");
    switch (processedString[0]) {
    case "registered":
      try {
        credit = Double.parseDouble(processedString[2]);
      } catch (NumberFormatException e) {
        System.out.println("Error processInputFromSeaTrade case registered" + e.getMessage());
        e.printStackTrace();
      }
      // TODO add update database here
      break;
    case "moved":  // TODO process case company app
      communicatorCompanyApp.getPrintWriter().println("creditupdate:" + shipName + ":-" + processedString[2]);
      break;
    case "reached":  // TODO process case company app
      communicatorCompanyApp.getPrintWriter().println(processedString[0] + ":" + shipName + ":" + processedString[1]);
      break;
    case "loaded":  // TODO process case company app
      // example: "loaded:898:shipName"
      String[] loadedInfo = processedString[1].split("|");
      communicatorCompanyApp.getPrintWriter().println(processedString[0] + ":" + loadedInfo[1] + ":" + shipName);
      break;
    case "unloaded":  // TODO process case company app
      // example: unloaded:60000:shipName
      communicatorCompanyApp.getPrintWriter().println(input + ":" + shipName);
      break;
    default:
      System.out.println("Error: processInputFromSeaTrade unreachable code");
    }
  }

  private static void sendToSeaTrade() {
    userInput = userInputHandler.getUserInput("ShipApp-SeaTrade");
    if (userInput.equals("exit")) {
      exit = true;
    }
    sendMessageToSeaTrade(userInput);
  }

  private static void sendMessageToSeaTrade(String message) {
    try {
      communicatorSeaTrade.getPrintWriter().println(message);
    } catch (Exception e) {
      System.out.println("Error sendMessageToSeaTrade: " + e.getMessage());
    }
  }

  // example: launch:companyname:harbourname:shipname
  public static void establishSeaTradeConnection(String address, int portNumber, String companyName, String harborName, String shipName) {
    try {
      communicatorSeaTrade = new Communicator(address, portNumber);
      // TODO: make company chooseable
      String messageSeaTrade = inputSelectionStrings[0] + companyName  + ":" + harborName + ":" + shipName;
      communicatorSeaTrade.getPrintWriter().println(messageSeaTrade);
      System.out.println("Sent message to Server = " + messageSeaTrade);
    } catch (Exception e) {
      System.out.println("Error establishSeaTradeConnection " + e.getMessage());
    }
  }

  // example: register:shipname
  public static void establishCompanyAppConnection(String address, int portNumber, String shipName) {
    try {
      communicatorCompanyApp = new Communicator(address, portNumber);
      String messageCompanyApp = "register:" + shipName;
      communicatorCompanyApp.getPrintWriter().println(messageCompanyApp);
      System.out.println("Sent message to CompanyApp = " + messageCompanyApp);
    } catch (Exception e) {
      System.out.println("Error establishCompanyAppConnection " + e.getMessage());
    }
  }
  
  public static String setServerAddress(String serverName) {
    String address = "";
    if (serverName.equals("SeaTrade")) {
      address = DEFAULT_SEATRADE_SERVER_ADDRESS;
    } else if (serverName.equals("CompanyApp")) {
      address = DEFAULT_COMPANYAPP_SERVER_ADDRESS;
    } else {
      System.out.println("Error: setServerAddress invalid usecase");
    }
    System.out.println("Enter " + serverName + " IPv4 server address. Default is " + DEFAULT_SEATRADE_SERVER_ADDRESS + ".");
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

}