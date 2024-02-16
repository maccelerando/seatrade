import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class CompanyServer extends Thread {

  private int portNumber;
  private ServerSocket serverSocket;
  private HashMap<String, Socket> shipAppSockets;
  private boolean exit;

  public CompanyServer(int portNumber) {
    this.portNumber = portNumber;
    try {
      this.serverSocket = new ServerSocket(portNumber);
    } catch (IOException e) {
      System.out.println("Error: server socket not created. " + e.getMessage());
    }
    this.shipAppSockets = new HashMap<String, Socket>();
    this.exit = true;
  }

  @Override
  public void run() {
    System.out.println("Server is waiting for client connection on port " + portNumber);
    try {
      shipAppSockets.put("socket1", serverSocket.accept());
    } catch (IOException e) {
      System.out.println("Error: socket1 not put " + e.getMessage());
    }
    System.out.println(shipAppSockets.get("socket1"));

  }

  // TODO Socket clientSocket = serverSocket.accept() method
  // TODO close()  // closes this socket

}
