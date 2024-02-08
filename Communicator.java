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
      System.out.println("ERROR: Communicytor() " + e.getMessage());
      e.printStackTrace();
    } catch (IOException e) {
      System.out.println("ERROR: Communicytor() " + e.getMessage());
      e.printStackTrace();
    }
  }

  public PrintWriter getPrintWriter() {
    return printWriter;
  }
}
