import java.io.*;
import java.net.*;

public class Main {
    public static void main(String[] args) {
        int port = 5000;
        System.out.println("ECHO server start na porcie " + port);

        try (ServerSocket server = new ServerSocket(port)) {
            try (Socket socket = server.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                 PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)) {

                System.out.println("Klient połączony: " + socket.getRemoteSocketAddress());
                out.println("Połączono z ECHO. Wpisz tekst (albo /quit)."); // mała „powitalna”

                String line;
                while ((line = in.readLine()) != null) {
                    if ("/quit".equalsIgnoreCase(line)) {
                        out.println("Koniec.");
                        break;
                    }
                    out.println(line.toUpperCase());
                }
                System.out.println("Zamykam połączenie z klientem.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
