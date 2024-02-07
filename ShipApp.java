import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class ShipApp {
  private static final String DEFAULT_COMPANYAPP_SERVER_ADDRESS = "localhost";
  private static final String DEFAULT_SEATRADE_SERVER_ADDRESS = "localhost";
  private static final int DEFAULT_COMPANYAPP_PORT_NUMBER = 8152;
  private static final int DEFAULT_SEATRADE_PORT_NUMBER = 8151;
  private static final String[] DEFAULT_SHIP_NAMES = new String[]{"TradeWindsVoyager", "OceanNavigator", "MarisMerchant", "SeaHarmonyExplorer", "AquaVistaClipper", "BlueHorizonFreighter", "NauticalQuestCarrier", "WaveCrestTrader", "Neptune'sLegacy", "SeaSailEmpress", "HorizonHauler", "OceanPioneer", "AquaArrowCargo", "SeaBreezeClipper", "NautiVoyager", "BlueWaveMariner", "MaritimeMajesty", "SeaLinkExpress", "OceanCraftOdyssey", "TradeWindsDiscovery", "AquaGliderCarrier", "HorizonHarborTrader", "SeaSereneNavigator", "OceanJourneyVessel", "NautiQuestExplorer", "BlueMarlinTransporter", "SeaStarVoyager", "WaveRunnerFreighter", "Neptune'sPrideShip", "TradeWavesMariner", "AquaLineTrader", "HorizonSailCargo", "SeaGliderExpress", "OceanVanguard", "NauticalLegacyCarrier", "BlueVoyageMerchant", "MarisTradeClipper", "SeaCrestExplorer", "AquaSailTransport", "WaveQuestVoyager", "Neptune'sGrace", "SeaLoomCarrier", "HorizonCruiseTrader", "OceanVistaMariner", "NautiWaveExplorer", "BlueHavenFreighter", "AquaRiderClipper", "SeaSproutVoyager", "WaveChaseTransport", "TradeTideMariner", "AquaGlowCarrier", "HorizonSwayTrader", "OceanWhisperExplorer", "NautiWavesClipper", "BlueTideVoyager", "MaritimeWhirlFreighter", "SeaCrestExpress", "Neptune'sCrestShip", "AquaBreezeTransport", "WaveSailMariner"};
  private static Scanner scanner;
  private static String userInput;
  private static Socket socket;
  private static OutputStream outputStream;
  private static PrintWriter printWriter;
  private static Listener listener;
  private static volatile boolean exit;
  private static String[] inputSelectionStrings;
  private double seacoin = 0.0D;  // TODO: enhanced feature
  private static AutoComplete autoComplete;

  public static void main(String[] args) {
    String seatradeServerAddress = DEFAULT_SEATRADE_SERVER_ADDRESS;
    boolean var2 = true;
    String shipName = setShipName();
    System.out.println("Ship name = " + shipName);
    establishSeaTradeConnection(seatradeServerAddress, DEFAULT_SEATRADE_PORT_NUMBER, shipName);

  }

  public static void establishSeaTradeConnection(String address, int portNr, String companyName) {
    // TODO: make company chooseable, make harbour chooseable
    String serverMessage = inputSelectionStrings[0] + "Quickstart:lissabon:" + companyName;
    System.out.println("debug establishSeaTradeConnection registerMessage = " + serverMessage);

    try {
      socket = new Socket(address, portNr);
      System.out.println("Connected to server on port " + portNr + ".");
      outputStream = socket.getOutputStream();
      listener = new Listener(portNr, socket);
      listener.start();
      printWriter = new PrintWriter(outputStream, true);
      printWriter.println(serverMessage);
      System.out.println("Sent message to Server = " + serverMessage);
    } catch (Exception e) {
      System.out.println("Error establishSeaTradeConnection " + e.getMessage());
    }

  }

//  private static String autoCompleteInput(String input) {
//    String completed = "";
//    for (int i = 0; i < inputSelectionStrings.length; i++) {
//      for (int j = 0; j < inputSelectionStrings[i].length(); j++) {
//        if (input.equals(inputSelectionStrings[i].substring(0, j + 1))) {
//          completed = inputSelectionStrings[i];
//          break;
//        }
//      }
//    }
//    return completed;
//  }

  public static String setShipName() {
    String shipName = DEFAULT_SHIP_NAMES[0];
    Random random = new Random();
    int shipNamesIndex = random.nextInt(DEFAULT_SHIP_NAMES.length);
    shipName = DEFAULT_SHIP_NAMES[shipNamesIndex];
    return shipName;
  }

  static {
    scanner = new Scanner(System.in);
    userInput = "no-input";
    socket = null;
    outputStream = null;
    printWriter = null;
    listener = null;
    exit = false;
    inputSelectionStrings = new String[]{"launch:", "moveto:harbour", "loadcargo", "unloadcargo", "exit"};
    autoComplete = new AutoComplete(inputSelectionStrings);
  }
}
