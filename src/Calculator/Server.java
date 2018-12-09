package Calculator;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Server implements Runnable {
	private static DatagramSocket serverSocket;
	private DatagramPacket receivePacket, sendPacket;
	private InetAddress IPAddress;
	private static Integer port = 9876;
	private byte[] receiveData;
	private byte[] sendData;
	static String input, output;
	static ScriptEngineManager sem;
	static ScriptEngine engine;
	static boolean isRunning = false;
	static JLabel activeThread;
	static JButton btnS;
	static JTextField portTf;
	static JLabel portLabel;
	private static JLabel lblThcHnhLp;

	public Server() {
		sem = new ScriptEngineManager();
		engine = sem.getEngineByName("JavaScript");
	}

	public static void main(String[] args) throws IOException {
		JFrame frmServer = new JFrame("Server - Hoàng Thị Bích");
		frmServer.setBounds(200, 200, 479, 124);
		frmServer.setResizable(false);
		frmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		portLabel = new JLabel("Cổng");
		portLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		portLabel.setBounds(23, 46, 44, 31);
		portTf = new JTextField("9876");
		portTf.setBounds(84, 48, 115, 31);
		activeThread = new JLabel("0");
		activeThread.setBounds(231, 48, 85, 31);
		btnS = new JButton();
		btnS.setBounds(337, 46, 96, 31);
		btnS.setText("Start");
		frmServer.getContentPane().setLayout(null);
		frmServer.getContentPane().add(portLabel);
		frmServer.getContentPane().add(portTf);
		frmServer.getContentPane().add(activeThread);
		frmServer.getContentPane().add(btnS);

		lblThcHnhLp = new JLabel("Thực Hành Lập Trình Mạng - Calculator");
		lblThcHnhLp.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 18));
		lblThcHnhLp.setBounds(65, 10, 338, 22);
		frmServer.getContentPane().add(lblThcHnhLp);
		frmServer.setVisible(true);
		Thread thread = new Thread(new Server());

		btnS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (portTf.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Bạn chưa nhập Port");
				} else {
					if (isRunning) {
						isRunning = false;
						thread.interrupt();
						serverSocket.close();
						JOptionPane.showMessageDialog(null, "Đã ngừng Server");
					} else {
						port = Integer.parseInt(portTf.getText());
						try {
							serverSocket = new DatagramSocket(port);
							JOptionPane.showMessageDialog(null, "Đã khởi động Server");
						} catch (IOException e2) {
							JOptionPane.showMessageDialog(null, "Lỗi khi khởi động Server");
						}
						isRunning = true;
						thread.start();
					}
					portTf.setEditable(true);
					btnS.setText("Start");
					activeThread.setText("Luồng: " + Thread.activeCount());
				}
			}
		});
	}

	@Override
	public void run() {
		while (isRunning) {
			btnS.setText("Stop");
			portTf.setEditable(false);
			activeThread.setText("Luồng: " + Thread.activeCount());
			try {
				receiveData = new byte[1024];
				receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				IPAddress = receivePacket.getAddress();
				port = receivePacket.getPort();
				input = new String(receivePacket.getData()).trim();
				output = String.valueOf(engine.eval(input));
				sendData = new byte[1024];
				sendData = output.getBytes();
				sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
			} catch (ScriptException e) {
				output = "Lỗi cú pháp";
				try {
					sendData = new byte[1024];
					sendData = output.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
				} catch (IOException e1) {
				}
				continue;
			} catch (IOException e) {
				output = "Lỗi kết nối";
				try {
					sendData = new byte[1024];
					sendData = output.getBytes();
					sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
					serverSocket.send(sendPacket);
				} catch (IOException e1) {
				}
				continue;
			}
		}
	}

}
