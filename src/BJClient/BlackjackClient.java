package BJClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

public class BlackjackClient {
//	try (var listener = new ServerSocket(59090)) {
//        System.out.println("Listening on Port 59090");

	private String IPaddress() {
		String ip = "no IP address";

		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();

				if (n.isUp() && !n.isLoopback()) {

					Enumeration ee = n.getInetAddresses();
					while (ee.hasMoreElements()) {
						InetAddress i = (InetAddress) ee.nextElement();
						System.out.println(i.getHostAddress()); // testing
						if (i instanceof  java.net.Inet4Address && !i.isLoopbackAddress()) {
							return ip = i.getHostAddress();
						}
					}
				}
			}
			return ip; 
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	// driver code
	public static void main(String[] args) {

		String clientIp =IPaddress();

		// establish a connection by providing host and port number.
		try (Socket socket = new Socket(clientIp, 2121)) {

			// writing to server
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// reading from server
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// object of scanner class
			Scanner sc = new Scanner(System.in);
			String line = null;

			while (!"exit".equalsIgnoreCase(line)) {

				// reading from user
				line = sc.nextLine();

				// sending the user input to server
				out.println(line);
				out.flush();

				// displaying server reply
				System.out.println("Server replied " + in.readLine());
			}

			// closing the scanner object
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
