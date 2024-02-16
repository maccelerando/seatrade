import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Listener extends Thread {
  int port;
  String input = "nothing";
  BufferedReader inputStream = null;

  Listener(int portNumber, Socket socket) {
    this.port = portNumber;

    try {
      this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      System.out.println("Error Thread inputStream initialisation " + e.getMessage());
    }

  }

  public void run() {
    System.out.println("Listener running.");

    while(!interrupted()) {
      try {
        this.input = this.inputStream.readLine();
        if (this.input == null) {
          System.out.println("End listening. The stream is closed.");
          break;
        }
        System.out.println("Thread answer from server = " + this.input);
      } catch (IOException e) {
        if (!interrupted()) {
          System.out.println("Error Thread inputStream.readLine() " + e.getMessage());
        }
      }
    }

    if (this.inputStream != null) {
      try {
        this.inputStream.close();
      } catch (IOException e) {
        System.out.println("Error in thread inputStream.close() " + e.getMessage());
      }
    }
    System.out.println("Listener interrupted");
  }
}
