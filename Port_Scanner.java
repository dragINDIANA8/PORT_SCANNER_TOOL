import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PortScanner {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid amount of arguments.");
            System.out.println("Syntax: java PortScanner <ip>");
            System.exit(1);
        }

        String target = args[0];

        System.out.println("-".repeat(50));
        System.out.println("Scanning Target: " + target);
        System.out.println("Time Started: " + java.time.LocalDateTime.now());
        System.out.println("-".repeat(50));

        List<Integer> openPorts = new ArrayList<>();

        int[] commonPorts = {21, 22, 23, 25, 53, 80, 110, 143, 443, 445, 587, 993, 995};

        try (Scanner scanner = new Scanner(System.in)) {

            // Scan common ports first
            System.out.println("Scanning common ports...");
            for (int port : commonPorts) {
                if (isPortOpen(target, port, 1000)) {
                    openPorts.add(port);
                    System.out.println("Port " + port + " is open");
                }
            }

            // Scan the rest of the ports
            System.out.print("Enter start port for full scan: ");
            int startPort = scanner.nextInt();
            System.out.print("Enter end port for full scan: ");
            int endPort = scanner.nextInt();

            // Port range validation
            if (startPort > endPort) {
                System.err.println("Start port must be less than or equal to end port");
                System.exit(1);
            }

            System.out.println("Scanning all ports...");
            for (int port = startPort; port <= endPort; port++) {
                if (contains(commonPorts, port)) continue;
                if (isPortOpen(target, port, 1000)) {
                    openPorts.add(port);
                    System.out.println("Port " + port + " is open");
                }
            }

            System.out.println("-".repeat(50));
            System.out.println("Scanning Completed: " + java.time.LocalDateTime.now());
            System.out.println("-".repeat(50));
            System.out.println("Open Ports: " + openPorts);

        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }

    private static boolean isPortOpen(String ip, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean contains(int[] arr, int item) {
        for (int n : arr) {
            if (n == item) return true;
        }
        return false;
    }
}
