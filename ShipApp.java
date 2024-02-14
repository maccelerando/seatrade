import java.util.Scanner;

public class ShipApp {

  private static final String DEFAULT_COMPANYAPP_SERVER_ADDRESS = "localhost";
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final int DEFAULT_COMPANYAPP_PORT_NUMBER = 8152;
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8151;
  private static final Scanner scanner = new Scanner(System.in);
  private static volatile boolean exit = false;
  private static String userInput = "";
  private static AutoComplete autoComplete = new AutoComplete();
  private static Communicator communicatorSeaTrade;
  private static Communicator communicatorCompanyApp;
  private static UserInputHandler userInputHandler = new UserInputHandler(scanner);
  private double seacoin = 0.0D;  // TODO: enhanced feature

  private static final String[] inputSelectionStrings = new String[] {"launch:", "moveto:", "loadcargo", "unloadcargo", "exit"};

  public static void main(String[] args) {
    String seatradeServerAddress = DEFAULT_SEATRADE_SERVER_ADDRESS;
    // ship name
    String shipName = userInputHandler.getUserInput("shipName");
    // harbor name
    String harborNameLaunch = "";
    while (harborNameLaunch.isEmpty()) {
      harborNameLaunch = userInputHandler.getUserInput("harborName");
    }
    establishSeaTradeConnection(seatradeServerAddress, DEFAULT_SEATRADE_PORT_NUMBER, shipName, harborNameLaunch);

    // main routine
    while (!exit) {
      sendToSeaTrade();
    }

    // TODO cleanup
  }

  private static void sendToSeaTrade() {
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
      // TODO: make company chooseable
      String serverMessage = inputSelectionStrings[0] + "Quickstart:" + harborName + ":" + companyName;
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
