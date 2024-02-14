import java.util.Scanner;

public class ShipApp {

  private static final String DEFAULT_COMPANYAPP_SERVERADDRESS = "localhost";
  private static final String DEFAULT_SEATRADE_SERVERADDRESS = "localhost";
  private static final int DEFAULT_COMPANYAPP_PORTNUMBER = 8152;
  private static final int DEFAULT_SEATRADE_PORTNUMBER = 8151;
  private static final Scanner scanner = new Scanner(System.in);
  private static final String DEFAULT_COMPANY_NAME = "Quickstart";
  private static String companyName = DEFAULT_COMPANY_NAME;
  private static volatile boolean exit = false;
  private static String userInput = "";
  private static AutoComplete autoComplete = new AutoComplete();
  private static Communicator communicatorSeaTrade;
  private static Communicator communicatorCompanyApp;
  private static UserInputHandler userInputHandler = new UserInputHandler(scanner);
  private double seacoin = 0.0D;  // TODO: enhanced feature


  public static void main(String[] args) {
    // TODO make these choosable
    String seatradeServerAddress = DEFAULT_SEATRADE_SERVERADDRESS;
    int seatradePortNumber = DEFAULT_SEATRADE_PORTNUMBER;
    String companyAppServerAddress = DEFAULT_COMPANYAPP_SERVERADDRESS;
    int companyAppPortNumber = DEFAULT_COMPANYAPP_PORTNUMBER;

    // ship name
    String shipName = userInputHandler.getUserInput("shipName");

    // choose company
//    establishCompanyAppConnection(companyAppServerAddress, companyAppPortNumber, shipName);

    // harbor name
    String harborNameLaunch = "";
    while (harborNameLaunch.isEmpty()) {
      harborNameLaunch = userInputHandler.getUserInput("harborName");
    }
    establishSeaTradeConnection(seatradeServerAddress, seatradePortNumber, companyName, harborNameLaunch, shipName);

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

  public static void establishSeaTradeConnection(String address, int portNumber, String companyName, String harborName, String shipName) {
    try {
      communicatorSeaTrade = new Communicator(address, portNumber);
      // TODO: make company chooseable
      String messageSeaTrade = "launch:" + companyName + ":" + harborName + ":" + shipName;
      communicatorSeaTrade.getPrintWriter().println(messageSeaTrade);
      System.out.println("Sent message to SeaTrade = " + messageSeaTrade);
    } catch (Exception e) {
      System.out.println("Error establishSeaTradeConnection " + e.getMessage());
    }
  }

//  public static void establishCompanyAppConnection(String address, int portNumber, String shipName) {
//    try {
//      communicatorCompanyApp = new Communicator(address, portNumber);
//      String messageCompanyApp =  "launch:" + shipName;
//    } catch (Exception e) {
//      System.out.println("Error establishCompanyAppConnection " + e.getMessage());
//    }
//  }

}
