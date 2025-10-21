import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        final int PORT = 5000;
        byte[] buf = new byte[1024];

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDPServer: nasłuch na porcie " + PORT + " ...");

            DatagramPacket inPacket = new DatagramPacket(buf, buf.length);
            socket.receive(inPacket);

            String reply = "Serwer data/czas: " + LocalDateTime.now();
            byte[] out = reply.getBytes();

            DatagramPacket outPacket = new DatagramPacket(
                    out, out.length,
                    inPacket.getAddress(),
                    inPacket.getPort()
            );
            socket.send(outPacket);
            System.out.println("UDPServer: wysłano odpowiedź do " +
                    inPacket.getAddress() + ":" + inPacket.getPort());

        }
    }
}
