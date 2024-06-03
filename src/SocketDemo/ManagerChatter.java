
package SocketDemo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import java.awt.Color;

public class ManagerChatter extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtServerPort;
	private JTabbedPane tabbedPane;
	
	ServerSocket srvSocket = null;
	BufferedReader bf = null;
	Thread t;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerChatter frame = new ManagerChatter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ManagerChatter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(0, 153, 255));
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Manager Port: ");
		lblNewLabel.setForeground(new Color(204, 0, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);
		
		txtServerPort = new JTextField();
		txtServerPort.setBackground(new Color(0, 153, 255));
		txtServerPort.setForeground(new Color(204, 0, 0));
		txtServerPort.setText("12345");
		panel.add(txtServerPort);
		txtServerPort.setColumns(10);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		this.setSize(600, 300);
		int serverPort = Integer.parseInt(txtServerPort.getText());
		try {
			srvSocket = new ServerSocket(serverPort);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		t = new Thread(this);
		t.start();
	}
	public void run() {
		while(true) {
			try {
				Socket aStaffSocket = srvSocket.accept();
				if(aStaffSocket != null) {
					bf = new BufferedReader(new InputStreamReader(aStaffSocket.getInputStream()));
					String S = bf.readLine();
					int pos = S.indexOf(":");
					String staffName = S.substring(pos+1);
					ChatPanel p = new ChatPanel(aStaffSocket, "Manager", staffName);
					tabbedPane.add(staffName, p);
					p.updateUI();
				}
				Thread.sleep(100);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}