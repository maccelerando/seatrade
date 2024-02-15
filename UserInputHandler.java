import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInputHandler {

  private final String[] DEFAULT_HARBOR_NAMES = new String[] {"reykjavik", "lissabon", "dakar", "algier", "cotonau", "halifax", "plymouth", "brest", "new york", "carracas"};
  private final String[] DEFAULT_HARBOR_SYMBOLS = new String[] {"🏔️ ", "🏰", "🌅", "🕌", "🏝️ ", "🚢", "🌊", "🚤", "🗽", "🌄"};
  private static final String[] DEFAULT_COMPANY_NAMES = new String[]{"OceanLink", "AquaMarine", "SeaVista", "Nautical", "BlueHorizon", "SeaSwift", "Neptune's", "WaveCrest", "SeaHarbor", "Maritime", "AquaTrade", "Oceanic", "SeaSail", "BlueWave", "Horizon", "SeaCurrent", "Marisource", "AquaMeridian", "NautiLink", "OceanCraft"};
  private final String[] DEFAULT_SHIP_NAMES = new String[] {"AquaArrowCargo", "AquaBreezeTransport", "AquaGliderCarrier", "AquaGlowCarrier", "AquaLineTrader", "AquaRiderClipper", "AquaSailTransport", "AquaVistaClipper", "BlueBreezeFreighter", "BlueHavenFreighter", "BlueHorizonFreighter", "BlueMarlinTransporter", "BlueTideVoyager", "BlueVoyageMerchant", "CeruleanClipper", "CeruleanCruiseClipper", "DolphinDanceExplorer", "EmeraldEagleExpress", "FleetFeatherFreighter", "GoldenGaleGlider", "HarmonyHarborHauler", "HorizonCruiseTrader", "HorizonHarborTrader", "HorizonHauler", "HorizonSailCargo", "HorizonSwayTrader", "IvoryIsleExplorer", "JadeJourneyJet", "JubilantJourneyFreighter", "KaleidoKiteClipper", "LunarLagoonLiner", "MarineMistMerchant", "MarisMerchant", "MarisTradeClipper", "MaritimeMajesty", "MaritimeWhirlFreighter", "NautiQuestExplorer", "NautiVoyager", "NautiWaveExplorer", "NauticalLegacyCarrier", "NauticalNebulaNavigator", "Neptune'sCrestShip", "Neptune'sGrace", "Neptune'sLegacy", "Neptune'sPrideShip", "OceanCraftOdyssey", "OceanJourneyVessel", "OceanNavigator", "OceanOdysseyOrbit", "OceanPioneer", "OceanVanguard", "OceanVistaMariner", "PearlPreludePilot", "QuantumQuestQuasar", "QuasarQuestVoyager", "RoyalRippleRunner", "SapphireSkySail", "SeaBreezeClipper", "SeaCrestExplorer", "SeaCrestExpress", "SeaGliderExpress", "SeaHarmonyExplorer", "SeaLoomCarrier", "SeaLinkExpress", "SeaSereneNavigator", "SeaSailEmpress", "SeaSproutVoyager", "SeaStarVoyager", "SeaVoyageQuest", "TitanicTideTransport", "TradeTideMariner", "TradeWavesMariner", "TradeWindsDiscovery", "TradeWindsVoyager", "UtopiaUmbrellaClipper", "VividVoyageVessel", "WaveChaseTransport", "WaveCrestTrader", "WaveQuestVoyager", "WaveRunnerFreighter", "WaveSailMariner", "WaveWhispererWanderer", "XanaduXplorer", "XtraordinaryExplorer", "YellowYachtYonder", "ZephyrZephyrClipper", "ZealousZephyrZiggurat"};
  private final String[] SA_ST_COMMANDS = new String[] {"launch:", "moveto:", "loadcargo", "unloadcargo", "exit"};
  private final String[] SA_ST_SYMBOLS  = new String[] {"🚢", "⚓", "📦", "📦", "🚪"};
  private final String[] CA_ST_COMMANDS = new String[] {"register:", "getinfo:harbour", "getinfo:cargo", "exit"};
  private final String[] CA_ST_SYMBOLS  = new String[] {"🏢", "🏭", "🛃", "🚪"};

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
      case "companyName":
        inputSelectionStrings = DEFAULT_COMPANY_NAMES;
        System.out.println("Enter company name!");
        userInput = scanner.nextLine();
        if (!userInput.isEmpty()) {
          if (userInput.matches("\\d+")) {
            int companyNameIndex = Integer.parseInt(userInput) % DEFAULT_COMPANY_NAMES.length;
            processedInput = DEFAULT_COMPANY_NAMES[companyNameIndex];
          } else {
            processedInput = userInput;
          }
        } else {
          processedInput = DEFAULT_COMPANY_NAMES[random.nextInt(DEFAULT_COMPANY_NAMES.length)];
          System.out.print("No input. ");
        }
        System.out.println("Using company name " + processedInput + ".");
        break;
      case "shipName":
        inputSelectionStrings = DEFAULT_SHIP_NAMES;
        System.out.println("Enter ship name!");
        System.out.print("🚢 ");
        userInput = scanner.nextLine();
        if (userInput.isEmpty()) {
          processedInput = DEFAULT_SHIP_NAMES[random.nextInt(DEFAULT_SHIP_NAMES.length)];
          System.out.println("No input. Ship name chosen by ShipApp.");
          System.out.println("🚢 " + processedInput);
          break;
        }
        processedInput = userInput;
        break;
      case "harborName":
        inputSelectionStrings = DEFAULT_HARBOR_NAMES;
        for (int i = 0; i < DEFAULT_HARBOR_NAMES.length; i++) {
          System.out.println(DEFAULT_HARBOR_SYMBOLS[i] + " " + DEFAULT_HARBOR_NAMES[i]);
        }
        System.out.println("Enter harbor name!");
        System.out.print("⚓ ");
        userInput = scanner.nextLine();
        if (userInput.isEmpty()) {
          processedInput = DEFAULT_HARBOR_NAMES[random.nextInt(DEFAULT_HARBOR_NAMES.length)];
          System.out.println("No input. Harbor name chosen by ShipApp.");
          System.out.println("⚓ " + processedInput);
          break;
        }
        processedInput = autoComplete.autoCompleteInput(userInput, inputSelectionStrings);
        if (!userInput.equals(processedInput)) {
          System.out.println("⚓ " + processedInput);
        }
        break;
      case "ipv4":
        processedInput = "";
        while (processedInput.isEmpty()) {
          userInput = scanner.nextLine();
          if (userInput.isEmpty()) {
            processedInput = "";
            break;
          } else {
            // check for correct IPv4 syntax
            String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
            Pattern pattern = Pattern.compile(ipv4Pattern);
            Matcher matcher = pattern.matcher(userInput);
            if (matcher.matches()) {
              processedInput = userInput;
              break;
            } else {
              System.out.println("Wrong IPv4 format. Example: 127.0.0.1");
              continue;
            }
          }
        }
        break;
      case "portNumber":
        processedInput = "";
        while (processedInput.isEmpty()) {
          userInput = scanner.nextLine();
          if (userInput.isEmpty()) {
            processedInput = "";
            break;
          } else {
            // check for correct port number syntax
            try {
              int portNumber = Integer.parseInt(userInput);
              if (portNumber >= 0 && portNumber <= 65535) {
                processedInput = userInput;
                break;
              }
            } catch (NumberFormatException e) {
              System.out.println("Wrong format. Enter a number between 0 and 65535.");
              continue;
            }
          }
        }
        break;
      case "CompanyApp-SeaTrade":
        while (processedInput.isEmpty()) {
          System.out.println("Enter message to SeaTrade.\n-> getinfo:harbour\n-> getinfo:cargo\n-> exit");
          userInput = scanner.nextLine();
          if (userInput.isEmpty()) {
            System.out.println("No input.");
            continue;
          } else {
            processedInput = autoComplete.autoCompleteInput(userInput, CA_ST_COMMANDS);
            // TODO check for "register:"
          }
        }
        break;
      case "ShipApp-SeaTrade":
        while (processedInput.isEmpty()) {
          System.out.println("Enter message to SeaTrade.\n⚓ moveto\n📦 loadcargo\n📦 unloadcargo\n🚪 exit");
          userInput = scanner.nextLine();
          if (userInput.isEmpty()) {
            System.out.println("No input.");
            continue;
          } else {
            processedInput = autoComplete.autoCompleteInput(userInput, SA_ST_COMMANDS);
          }
          switch (processedInput) {
            // moveto: follow up commands
            case "moveto:":
              inputSelectionStrings = DEFAULT_HARBOR_NAMES;
              System.out.println("Enter harbor name!");
              System.out.print("⚓ ");
              userInput = scanner.nextLine();
              userInput = autoComplete.autoCompleteInput(userInput, DEFAULT_HARBOR_NAMES);
              if (userInput.isEmpty()) {
                System.out.println("No input. Harbor name will be chosen by ShipApp.");
                String harborName = DEFAULT_HARBOR_NAMES[random.nextInt(DEFAULT_HARBOR_NAMES.length)];
                processedInput = processedInput.concat(harborName);
              } else {
                processedInput = processedInput.concat(userInput);
              }
              break;
            case "loadcargo":
              // "loadcargo" does not need additional operations
              break;
            case "unloadcargo":
              // "unloadcargo" does not need additional operations
              break;
            case "exit":
              System.out.println("ShipApp ended." + SA_ST_SYMBOLS[SA_ST_SYMBOLS.length - 1]);
              break;
            default:
              System.out.println("Error: UserInputHandler getUserInput() unreachable code");
            }
          }
        break;
      default:
        System.out.println("Error: UserInputHandler getUserInput() unreachable code");
        return "exit";
    }
    return processedInput;
  }
}

