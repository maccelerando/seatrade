import java.util.Random;
import java.util.Scanner;

public class ShipApp {

  // TODO move to UserInputHandler class and refactor this class
  private static final String[] HARBOR_NAMES = new String[] {"reykjavik", "lissabon", "dakar", "algier", "cotonau", "halifax", "plymouth", "brest", "new york", "carracas"};

  private static final String DEFAULT_COMPANYAPP_SERVER_ADDRESS = "localhost";
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final int DEFAULT_COMPANYAPP_PORT_NUMBER = 8152;
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8151;
  private static final Scanner scanner = new Scanner(System.in);
  private static volatile boolean exit = false;
  private static String userInput = "no-input";
  private static AutoComplete autoComplete = new AutoComplete();
  private static Communicator communicatorSeaTrade;
  private static Communicator communicatorCompanyApp;
  private static UserInputHandler userInputHandler = new UserInputHandler(scanner);
  private double seacoin = 0.0D;  // TODO: enhanced feature

  private static final String[] inputSelectionStrings = new String[] {"launch:", "moveto:", "loadcargo", "unloadcargo", "exit"};

  public static void main(String[] args) {
    String seatradeServerAddress = DEFAULT_SEATRADE_SERVER_ADDRESS;
    // Ship Name
    String shipName = userInputHandler.getUserInput("shipName");
    System.out.println("Ship name = " + shipName);
    // harbor name
    String harborNameLaunch = "";
    harborNameLaunch = userInputHandler.getUserInput("harborName");
    establishSeaTradeConnection(seatradeServerAddress, DEFAULT_SEATRADE_PORT_NUMBER, shipName, harborNameLaunch);

    // main routine
    while (!exit) {
      sendSeaTrade();
    }

    // TODO cleanup
  }

  // will replace sendToSeaTrade()
  private static void sendSeaTrade() {
    userInput = userInputHandler.getUserInput("seaTrade");
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

  public static void establishSeaTradeConnection(String address, int portNumber, String companyName, String harborName) {
    try {
      communicatorSeaTrade = new Communicator(address, portNumber);
      // TODO: make company chooseable, make harbor chooseable
      String serverMessage = inputSelectionStrings[0] + "Quickstart:" + harborName + ":" + companyName;
      System.out.println("debug establishSeaTradeConnection registerMessage = " + serverMessage);
      communicatorSeaTrade.getPrintWriter().println(serverMessage);
      System.out.println("Sent message to Server = " + serverMessage);
    } catch (Exception e) {
      System.out.println("Error establishSeaTradeConnection " + e.getMessage());
    }
  }

//  public static void establishCompanyAppConnection(String address, int portNumber) {
//    try {
//      communicatorCompanyApp = new Communicator(address, portNumber);
//      String messageCompanyApp =
//    }
//  }

}
