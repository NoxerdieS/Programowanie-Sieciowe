import java.io.*;
import java.net.*;
import java.time.*;

public class Main {
    private static final String[] POLISH_DAYS = {
            "poniedziałek", "wtorek", "środa", "czwartek", "piątek", "sobota", "niedziela"
    };

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Serwer nasłuchuje na porcie 8080");
        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String firstLine = in.readLine();
                LocalDate today = LocalDate.now();
                DayOfWeek dayOfWeek = today.getDayOfWeek();
                String dayPolish = POLISH_DAYS[dayOfWeek.getValue() - 1];

                if (firstLine != null && firstLine.startsWith("GET")) {
                    out.println("HTTP/1.1 200 OK");
                    out.println("Content-Type: text/html; charset=UTF-8");
                    out.println();
                    out.println("<h3>" + dayPolish + "</h3>");
                } else {
                    out.println(dayPolish);
                }
            }
        }
    }
}