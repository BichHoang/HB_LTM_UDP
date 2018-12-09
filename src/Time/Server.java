package Time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class Server {
		public static void main(String[] args) throws Exception {
			//Gan cong 9876 cho chuong trinh
			DatagramSocket serverSocket = new DatagramSocket(9876);
			//Tao cac mang byte de chua dl gui va nhan
			System.out.println("Server is started");
			byte[] receiveData = new byte[1024];
			byte[] sendData = new byte[1024];
			while(true) {
				//Tao goi ong de nhan du lieu tu client
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				//Nhan du lieu tu client
				serverSocket.receive(receivePacket);
				//Lay dia chi IP cua may client
				InetAddress IPAddress = receivePacket.getAddress();
				//Lay port cua chuong trinh client
				int port = receivePacket.getPort();
				//Lay ngay gio de gui nguoc lai client
				String request = new String(receivePacket.getData());
				System.out.println(request);
				if (request.trim().equals("getDate"))
				{
					sendData= new Date().toString().getBytes();
				}
				else sendData = "Server not know what you want?".getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				//Gui dl lai cho client
				serverSocket.send(sendPacket);
			}
		}
}


