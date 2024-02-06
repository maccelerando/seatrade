import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CompanyApp {
   private static final String[] COMPANY_NAMES = new String[]{"OceanLink", "AquaMarine", "SeaVista", "Nautical", "BlueHorizon", "SeaSwift", "Neptune's", "WaveCrest", "SeaHarbor", "Maritime", "AquaTrade", "Oceanic", "SeaSail", "BlueWave", "Horizon", "SeaCurrent", "Marisource", "AquaMeridian", "NautiLink", "OceanCraft"};
   private static final String DEFAULT_SERVER_ADDRESS = "localhost";
   private static final int DEFAULT_PORT_NUMBER = 8150;
   private static Scanner scanner;
   private static String userInput;
   private static volatile boolean exit;
   private static Socket socket;
   private static OutputStream outputStream;
   private static PrintWriter printWriter;
   private static Listener listener;
   private static String[] inputSelectionStrings;
   private double credit = 0.0D;

   public static void main(String[] args) {
      String address = setServerAddress();
      int portNr = setPortNumber();
      String companyName = setCompanyName();
      inputSelectionStrings[0] = inputSelectionStrings[0].concat(companyName);
      establishConnection(address, portNr);

      // main routine
      while(!exit) {
         sendToServer();
      }

      cleanup();
      System.exit(0);
   }

   private static void sendToServer() {
      System.out.println("Enter message to server. Suggestions: \n-> register:companyname\n-> getinfo:harbour\n-> getinfo:cargo\n-> exit");
      String input = "default";

      try {
         input = scanner.nextLine();
         if (!input.isEmpty()) {
            input = autoCompleteInput(input);
         } else {
            input = "exit";
         }
      } catch (Exception e) {
         System.out.println("Error with input: " + e.getMessage());
      }

      sendMessageToServer(input);
      if (input.equals("exit")) {
         exit = true;
      }

   }

   private static void sendMessageToServer(String message) {
      try {
         printWriter.println(message);
         System.out.println("Sent message to Server = " + message);
      } catch (Exception e) {
         System.out.println("Error sendMessageToServer: " + e.getMessage());
      }

   }

   private static String autoCompleteInput(String input) {
      String completed = "";

      for(int i = 0; i < inputSelectionStrings.length; ++i) {
         for(int j = 0; j < inputSelectionStrings[i].length(); ++j) {
            if (input.equals(inputSelectionStrings[i].substring(0, j + 1))) {
               completed = inputSelectionStrings[i];
               break;
            }
         }
      }

      if (!completed.isEmpty()) {
         System.out.println("Assume input = " + completed);
      } else {
         System.out.println("Wrong input: " + input);
      }

      return completed;
   }

   private static void establishConnection(String address, int portNr) {
      try {
         String serverMessage = inputSelectionStrings[0];
         System.out.println("debug establishConnection registerMessage = " + serverMessage);
         socket = new Socket(address, portNr);
         System.out.println("Connected to server on port " + portNr + ".");
         outputStream = socket.getOutputStream();
         listener = new Listener(DEFAULT_PORT_NUMBER, socket);
         listener.start();
         printWriter = new PrintWriter(outputStream, true);
         printWriter.println(serverMessage);
         System.out.println("Sent message to Server = " + serverMessage);
      } catch (Exception e) {
         System.out.println("Error establishing connection with the server: " + e.getMessage());
      }

   }

   public static String setServerAddress() {
      String address = DEFAULT_SERVER_ADDRESS;
      System.out.println("Enter server address. Default is " + address + ".");
      String userInput = scanner.nextLine();
      if (!userInput.isEmpty()) {
         try {
            address = userInput;  // TODO: check if user input is in valid server address format
         } catch (Exception e) {
            System.out.println("Input error.\n Using default server address " + DEFAULT_SERVER_ADDRESS + ".");
         }
      } else {
         System.out.println("No input.\nUsing default server address " + DEFAULT_SERVER_ADDRESS + ".");
      }

      return address;
   }

   public static int setPortNumber() {
      int portNr = DEFAULT_PORT_NUMBER;
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
      if (scanner != null) {
         scanner.close();
      }

      if (listener != null) {
         listener.interrupt();

         try {
            listener.join();
         } catch (InterruptedException e) {
            System.out.println("Error cleanup() listener.join() " + e.getMessage());
         }
      }

      if (printWriter != null) {
         printWriter.close();
      }

      if (socket != null) {
         try {
            socket.close();
         } catch (IOException e) {
            System.out.println("Error cleanup() socket.close() " + e.getMessage());
         }
      }

      if (outputStream != null) {
         try {
            outputStream.close();
         } catch (IOException e) {
            System.out.println("Error cleanup() outputStream.close() " + e.getMessage());
         }
      }

   }

   static {
      scanner = new Scanner(System.in);
      userInput = "no-input";
      exit = false;
      socket = null;
      outputStream = null;
      printWriter = null;
      listener = null;
      inputSelectionStrings = new String[]{"register:", "getinfo:harbour", "getinfo:cargo", "exit"};
   }
}