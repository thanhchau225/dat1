package SocketDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Register extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton registerButton;
	private Connection connection;
	private JLabel lblEmail;
	private JTextField textField;
	private JRadioButton rdbtnNewRadioButton;
	private JRadioButton rdbtnNewRadioButton_1;
	private JLabel lblNewLabel;

	public Register() {
		setTitle("Login or Register");
		setSize(555, 693);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		usernameLabel.setForeground(new Color(0, 191, 255));
		usernameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		usernameLabel.setBounds(56, 243, 110, 28);
		JLabel passwordLabel = new JLabel("Password:");
		passwordLabel.setForeground(new Color(0, 191, 255));
		passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		passwordLabel.setBounds(56, 301, 110, 28);
		usernameField = new JTextField(10);
		usernameField.setBounds(206, 247, 270, 28);
		passwordField = new JPasswordField(10);
		passwordField.setBounds(206, 305, 270, 28);
		registerButton = new JButton("Register");
		registerButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		registerButton.setForeground(new Color(0, 191, 255));
		registerButton.setBounds(199, 431, 110, 46);
		panel.setLayout(null);

		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(passwordLabel);
		panel.add(passwordField);
		panel.add(registerButton);

		getContentPane().add(panel);
		
		lblEmail = new JLabel("Email:");
		lblEmail.setHorizontalAlignment(SwingConstants.TRAILING);
		lblEmail.setForeground(new Color(0, 191, 255));
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblEmail.setBounds(56, 186, 110, 28);
		panel.add(lblEmail);
		
		textField = new JTextField(10);
		textField.setBounds(206, 190, 270, 28);
		panel.add(textField);
		
		rdbtnNewRadioButton = new JRadioButton("Male");
		rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		rdbtnNewRadioButton.setForeground(new Color(0, 191, 255));
		rdbtnNewRadioButton.setBounds(206, 372, 103, 21);
		panel.add(rdbtnNewRadioButton);
		
		rdbtnNewRadioButton_1 = new JRadioButton("Female");
		rdbtnNewRadioButton_1.setForeground(new Color(0, 191, 255));
		rdbtnNewRadioButton_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		rdbtnNewRadioButton_1.setBounds(321, 372, 103, 21);
		panel.add(rdbtnNewRadioButton_1);
		
		lblNewLabel = new JLabel("REGISTRATION");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
		lblNewLabel.setForeground(new Color(0, 191, 255));
		lblNewLabel.setBounds(128, 52, 296, 81);
		panel.add(lblNewLabel);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatuser", "chau", "1234");
		} catch (ClassNotFoundException | SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Không thể kết nối với cơ sở dữ liệu!");
		}

		registerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText();
				String password = String.valueOf(passwordField.getPassword());
				registerUser(username, password);
			}
		});
	}

	private void authenticateUser(String username, String password) {
		try {
			String query = "SELECT * FROM chatuser.loginchat WHERE `username`=? AND `password`=?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, encryptPassword(password));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				JOptionPane.showMessageDialog(Register.this, "Welcome ");
			} else {
				JOptionPane.showMessageDialog(Register.this, "Sai username hoặc password");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private void registerUser(String username, String password) {
		try {
			String query = "INSERT INTO chatuser.loginchat (`username`, `password`) VALUES (?, ?)";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, username);
			statement.setString(2, encryptPassword(password));
			statement.executeUpdate();
			JOptionPane.showMessageDialog(Register.this, "Người dùng đã đăng kí thành công!");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	private String encryptPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return password;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			Register app = new Register();
			app.setVisible(true);
		});
	}
}
