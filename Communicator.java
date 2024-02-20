import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Communicator {
  private Socket socket;
  private OutputStream outputStream;
  private PrintWriter printWriter;
  private Listener listener;

  public Communicator(String address, int portNumber) {
    try {
      this.socket = new Socket(address, portNumber);
      System.out.println("Connected to server at address " + address + " on port number " + portNumber);
      this.outputStream = socket.getOutputStream();
      this.listener = new Listener(portNumber, socket);
      listener.start();
      this.printWriter = new PrintWriter(outputStream, true);
    } catch (UnknownHostException e) {
      System.out.println("ERROR: Communicator() " + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("ERROR: Communicator() " + e.getMessage());
      e.printStackTrace();
    }
  }
  
  public Communicator(Socket socket, String address, int portNumber) {
    try {
      this.socket = socket;
      System.out.println("Connected to server at address " + address + " on port number " + portNumber);
      this.outputStream = socket.getOutputStream();
      this.listener = new Listener(portNumber, socket);
      listener.start();
      this.printWriter = new PrintWriter(outputStream, true);
    } catch (UnknownHostException e) {
      System.out.println("ERROR: Communicator() " + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("ERROR: Communicator() " + e.getMessage());
      e.printStackTrace();
    }
  }

  public PrintWriter getPrintWriter() {
    return printWriter;
  }
  
  public Listener getListener() {
    return listener;
  }

  public void cleanup() {
    System.out.println("start Communicator cleanup");
    if (listener != null) {
      listener.interrupt();
      System.out.println("listener.inputStream set to null");
    }
    if (socket != null) {
      try {
        socket.close();
        System.out.println("Communicator socket closed");
      } catch (IOException e) {
        System.out.println("Error cleanup() socket.close() " + e.getMessage());
      }
    }
    if (printWriter != null) {
      printWriter.close();
      System.out.println("Communicator printWriter closed");
    }
    if (outputStream != null) {
      try {
        outputStream.close();
        System.out.println("Communicator outputStream closed");
      } catch (IOException e) {
        System.out.println("Error cleanup() outputStream.close() " + e.getMessage());
      }
    }
    System.out.println("Communicator cleanup finished");
  }
}
