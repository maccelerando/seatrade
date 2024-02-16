import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class CompanyServer extends Thread {

  private int portNumber;
  private ServerSocket serverSocket;
  private HashMap<String, Socket> shipAppSockets;
  private boolean exit;
  // console output from this server has different color for better differentiation
  public static final String ANSI_PURPLE = "\u001B[35m";
  public static final String ANSI_RESET = "\u001B[0m";

  public CompanyServer(int portNumber) {
    this.portNumber = portNumber;
    try {
      this.serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      printlnPurple("Error: server socket not created. " + e.getMessage());
    }
    this.shipAppSockets = new HashMap<String, Socket>();
    this.exit = true;
  }

  @Override
  public void run() {
    printlnPurple("Server is waiting for client connection on port " + portNumber);

    try {
      shipAppSockets.put("socket1", serverSocket.accept());
    } catch (IOException e) {
      printlnPurple("Error: socket1 not put " + e.getMessage());
    }
    printlnPurple(shipAppSockets.get("socket1").toString());

  }

  public void printlnPurple(String s) {
    System.out.println(ANSI_PURPLE + s + ANSI_RESET);
  }

  // TODO Socket clientSocket = serverSocket.accept() method
  // TODO close()  // closes this socket

}
