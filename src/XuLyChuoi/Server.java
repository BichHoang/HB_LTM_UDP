package XuLyChuoi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) throws IOException {
		DatagramSocket serverSocket = new DatagramSocket(9876);
		System.out.println("HB - UDP: Server localhost:9876 started...");
		while(true) {
			byte[] receiveData = new byte[1024];
			DatagramPacket sendPacket;
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String st = new String(receivePacket.getData());
			String chThuong = "Chuỗi thường: " + st.toLowerCase();
			String chHoa = "Chuỗi hoa: " + st.toUpperCase();
			String hv = "Chuỗi HT - TH: " + hvHoaThuong(st);
			String soTu = "Số từ của chuỗi: " + demSoTu(st);
			byte[] sendChThuong= chThuong.getBytes();
			sendPacket = new DatagramPacket(sendChThuong, sendChThuong.length, IPAddress, port);
			serverSocket.send(sendPacket);
			byte[] sendChHoa= chHoa.getBytes();
			sendPacket = new DatagramPacket(sendChHoa, sendChHoa.length, IPAddress, port);
			serverSocket.send(sendPacket);
			byte[] sendHV= hv.getBytes();
			sendPacket = new DatagramPacket(sendHV, sendHV.length, IPAddress, port);
			serverSocket.send(sendPacket);
			byte[] sendSoTu= soTu.getBytes();
			sendPacket = new DatagramPacket(sendSoTu, sendSoTu.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}

	private static int demSoTu(String st) {
		st = st.trim();
		while(st.indexOf("  ") > 0) {
			st = st.replaceAll("  ", " ");
		}
		
		return st.split(" ").length;
	}
	private static String hvHoaThuong(String s) {
		String rs = "";
		char[] c = s.toCharArray();
		for (char d : c) {
			String tmp = "" + d;
			if(tmp.equals(tmp.toLowerCase())) {
				rs += tmp.toUpperCase();
			}else rs += tmp.toLowerCase();
		}
		return rs;
	}

}
