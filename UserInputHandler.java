import java.util.Random;
import java.util.Scanner;

public class UserInputHandler {

  private final String[] DEFAULT_HARBOR_NAMES = new String[] {"reykjavik", "lissabon", "dakar", "algier", "cotonau", "halifax", "plymouth", "brest", "new york", "carracas"};
  private final String[] DEFAULT_SHIP_NAMES = new String[] {"AquaArrowCargo", "AquaBreezeTransport", "AquaGliderCarrier", "AquaGlowCarrier", "AquaLineTrader", "AquaRiderClipper", "AquaSailTransport", "AquaVistaClipper", "BlueBreezeFreighter", "BlueHavenFreighter", "BlueHorizonFreighter", "BlueMarlinTransporter", "BlueTideVoyager", "BlueVoyageMerchant", "CeruleanClipper", "CeruleanCruiseClipper", "DolphinDanceExplorer", "EmeraldEagleExpress", "FleetFeatherFreighter", "GoldenGaleGlider", "HarmonyHarborHauler", "HorizonCruiseTrader", "HorizonHarborTrader", "HorizonHauler", "HorizonSailCargo", "HorizonSwayTrader", "IvoryIsleExplorer", "JadeJourneyJet", "JubilantJourneyFreighter", "KaleidoKiteClipper", "LunarLagoonLiner", "MarineMistMerchant", "MarisMerchant", "MarisTradeClipper", "MaritimeMajesty", "MaritimeWhirlFreighter", "NautiQuestExplorer", "NautiVoyager", "NautiWaveExplorer", "NauticalLegacyCarrier", "NauticalNebulaNavigator", "Neptune'sCrestShip", "Neptune'sGrace", "Neptune'sLegacy", "Neptune'sPrideShip", "OceanCraftOdyssey", "OceanJourneyVessel", "OceanNavigator", "OceanOdysseyOrbit", "OceanPioneer", "OceanVanguard", "OceanVistaMariner", "PearlPreludePilot", "QuantumQuestQuasar", "QuasarQuestVoyager", "RoyalRippleRunner", "SapphireSkySail", "SeaBreezeClipper", "SeaCrestExplorer", "SeaCrestExpress", "SeaGliderExpress", "SeaHarmonyExplorer", "SeaLoomCarrier", "SeaLinkExpress", "SeaSereneNavigator", "SeaSailEmpress", "SeaSproutVoyager", "SeaStarVoyager", "SeaVoyageQuest", "TitanicTideTransport", "TradeTideMariner", "TradeWavesMariner", "TradeWindsDiscovery", "TradeWindsVoyager", "UtopiaUmbrellaClipper", "VividVoyageVessel", "WaveChaseTransport", "WaveCrestTrader", "WaveQuestVoyager", "WaveRunnerFreighter", "WaveSailMariner", "WaveWhispererWanderer", "XanaduXplorer", "XtraordinaryExplorer", "YellowYachtYonder", "ZephyrZephyrClipper", "ZealousZephyrZiggurat"};

  private AutoComplete autoComplete;
  private Random random;
  private Scanner scanner;
  private String userInput = "";
  private String[] inputSelectionStrings;


  public UserInputHandler(Scanner scanner) {
    this.autoComplete = new AutoComplete();
    this.random = new Random();
    this.scanner = scanner;
    this.userInput = userInput;
  }

  // TODO String kindOfInput should only be things like "shipNames", "harborNames", ... maybe there is a better way for this like an enum for example
  public String getUserInput(String kindOfInput) {
    String processedInput = "";
    switch (kindOfInput) {
      case "shipName":
        inputSelectionStrings = DEFAULT_SHIP_NAMES;
        System.out.println("Enter ship name.");
        userInput = scanner.nextLine();
        if (userInput.isEmpty()) {
          System.out.println("No input. Ship name will be chosen by ShipApp.");
          processedInput = DEFAULT_SHIP_NAMES[random.nextInt(DEFAULT_SHIP_NAMES.length)];
          break;
        }
        processedInput = autoComplete.autoCompleteInput(userInput, inputSelectionStrings);
        break;
      case "harborName":
        inputSelectionStrings = DEFAULT_HARBOR_NAMES;
        System.out.println("Enter harbor name.");
        userInput = scanner.nextLine();
        if (userInput.isEmpty()) {
          System.out.println("No input. Harbor name will be chosen by ShipApp.");
          processedInput = DEFAULT_HARBOR_NAMES[random.nextInt(DEFAULT_HARBOR_NAMES.length)];
          break;
        }
        processedInput = autoComplete.autoCompleteInput(userInput, inputSelectionStrings);
        break;
      default:
        System.out.println("Error: UserInputHandler getUserInput() unreachable code");
    }
    return processedInput;
  }
}
