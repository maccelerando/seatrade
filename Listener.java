import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Listener extends Thread {
  int port = 8150;
  String input = "nothing";
  BufferedReader inputStream = null;

  Listener(int var1, Socket var2) {
    this.port = var1;

    try {
      this.inputStream = new BufferedReader(new InputStreamReader(var2.getInputStream()));
    } catch (IOException var4) {
      System.out.println("Error Thread inputStream initialisation " + var4.getMessage());
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
      } catch (IOException var3) {
        if (!interrupted()) {
          System.out.println("Error Thread inputStream.readLine() " + var3.getMessage());
        }
      }
    }

    if (this.inputStream != null) {
      try {
        this.inputStream.close();
      } catch (IOException var2) {
        System.out.println("Error in thread inputStream.close() " + var2.getMessage());
      }
    }
    System.out.println("Listener interrupted");
  }
}
