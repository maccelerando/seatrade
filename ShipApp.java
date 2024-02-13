import java.util.Random;
import java.util.Scanner;

public class ShipApp {
  private static final String DEFAULT_COMPANYAPP_SERVER_ADDRESS = "localhost";
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final int DEFAULT_COMPANYAPP_PORT_NUMBER = 8152;
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8151;
  private static final String[] DEFAULT_SHIP_NAMES = new String[] {"TradeWindsVoyager", "OceanNavigator", "MarisMerchant", "SeaHarmonyExplorer", "AquaVistaClipper", "BlueHorizonFreighter", "NauticalQuestCarrier", "WaveCrestTrader", "Neptune'sLegacy", "SeaSailEmpress", "HorizonHauler", "OceanPioneer", "AquaArrowCargo", "SeaBreezeClipper", "NautiVoyager", "BlueWaveMariner", "MaritimeMajesty", "SeaLinkExpress", "OceanCraftOdyssey", "TradeWindsDiscovery", "AquaGliderCarrier", "HorizonHarborTrader", "SeaSereneNavigator", "OceanJourneyVessel", "NautiQuestExplorer", "BlueMarlinTransporter", "SeaStarVoyager", "WaveRunnerFreighter", "Neptune'sPrideShip", "TradeWavesMariner", "AquaLineTrader", "HorizonSailCargo", "SeaGliderExpress", "OceanVanguard", "NauticalLegacyCarrier", "BlueVoyageMerchant", "MarisTradeClipper", "SeaCrestExplorer", "AquaSailTransport", "WaveQuestVoyager", "Neptune'sGrace", "SeaLoomCarrier", "HorizonCruiseTrader", "OceanVistaMariner", "NautiWaveExplorer", "BlueHavenFreighter", "AquaRiderClipper", "SeaSproutVoyager", "WaveChaseTransport", "TradeTideMariner", "AquaGlowCarrier", "HorizonSwayTrader", "OceanWhisperExplorer", "NautiWavesClipper", "BlueTideVoyager", "MaritimeWhirlFreighter", "SeaCrestExpress", "Neptune'sCrestShip", "AquaBreezeTransport", "WaveSailMariner"};
  private static final String[] HARBOR_NAMES = new String[] {"reykjavik", "lissabon", "dakar", "algier", "cotonau", "halifax", "plymouth", "brest", "new york", "carracas"};
  private static volatile boolean exit = false;
  private static Scanner scanner = new Scanner(System.in);
  private static String userInput = "no-input";
  private static String[] inputSelectionStrings = new String[]{"launch:", "moveto:", "loadcargo", "unloadcargo", "exit"};
  private static AutoComplete autoComplete = new AutoComplete(inputSelectionStrings);
  private static Communicator communicatorSeaTrade;
  private static Communicator communicatorCompanyApp;
  private double seacoin = 0.0D;  // TODO: enhanced feature

  public static void main(String[] args) {
    String seatradeServerAddress = DEFAULT_SEATRADE_SERVER_ADDRESS;
    String shipName = setShipName();
    System.out.println("Ship name = " + shipName);
    establishSeaTradeConnection(seatradeServerAddress, DEFAULT_SEATRADE_PORT_NUMBER, shipName);

    // main routine
    while (!exit) {
      sendToSeaTrade();
    }

    // TODO cleanup
  }

  private static void sendToSeaTrade() {
    System.out.println("Enter message to SeaTrade. Suggestions: \n-> moveto:harbour\n-> loadcargo\n-> unloadcargo\n-> exit");
    String input = "default";

    try {
      input = scanner.nextLine();
      if (!input.isEmpty()) {
        input = autoComplete.autoCompleteInput(input);
        if (input.equals("moveto:")) {
          System.out.println("Enter harbor name");
          String harborName = scanner.nextLine();
          AutoComplete harborNameComplete = new AutoComplete(HARBOR_NAMES);
          harborName = harborNameComplete.autoCompleteInput(harborName);
          input = input.concat(harborName);
          System.out.println("debug: harborName = " + harborName);
          System.out.println("debug: input = " + input);
        }
      } else {
        input = "exit";
      }
    } catch (Exception e) {
      System.out.println("Error with input: " + e.getMessage());
    }
    if (input.equals("exit")) {
      exit = true;
    }
    sendMessageToSeaTrade(input);
  }

  private static void sendMessageToSeaTrade(String message) {
    try {
      communicatorSeaTrade.getPrintWriter().println(message);
    } catch (Exception e) {
      System.out.println("Error sendMessageToSeaTrade: " + e.getMessage());
    }
  }

  public static void establishSeaTradeConnection(String address, int portNumber, String companyName) {
    try {
      communicatorSeaTrade = new Communicator(address, portNumber);
      // TODO: make company chooseable, make harbour chooseable
      String serverMessage = inputSelectionStrings[0] + "Quickstart:lissabon:" + companyName;
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

  public static String setShipName() {
    String shipName = DEFAULT_SHIP_NAMES[0];
    Random random = new Random();
    int shipNamesIndex = random.nextInt(DEFAULT_SHIP_NAMES.length);
    shipName = DEFAULT_SHIP_NAMES[shipNamesIndex];
    return shipName;
  }

}
