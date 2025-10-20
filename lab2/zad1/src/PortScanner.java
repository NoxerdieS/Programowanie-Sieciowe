import java.net.Socket;
import java.net.InetSocketAddress;

public class PortScanner {
    public static void main(String[] args) {
        String host = "interia.pl";
        int[] ports = {80, 443, 21, 22, 25};
        int timeout = 2000;

        for (int port : ports) {
            try {
                Socket s = new Socket();
                s.connect(new InetSocketAddress(host, port), timeout);
                System.out.println("Port " + port + " OTWARTY");
                s.close();
            } catch (Exception e) {
                System.out.println("Port " + port + " ZAMKNIĘTY");
            }
        }
    }
}