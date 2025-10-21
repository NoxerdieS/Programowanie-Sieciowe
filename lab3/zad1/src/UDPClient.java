import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        byte[] sendData = "hej".getBytes();

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddr = InetAddress.getLocalHost();
            DatagramPacket outPacket = new DatagramPacket(
                    sendData, sendData.length, serverAddr, PORT
            );
            socket.send(outPacket);
            System.out.println("UDPClient: wysłano pakiet do " + serverAddr + ":" + PORT);

            byte[] buf = new byte[1024];
            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            socket.receive(inPacket);

            String response = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println("UDPClient: odpowiedź serwera: " + response);
        }
    }
}
