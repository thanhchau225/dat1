package SocketDemo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private Connection connection;
    private JLabel lblNewLabel;
    private JLabel lblNewLabel_1;
    private JLabel lblNewLabel_2;

    public Login() {
        setTitle("Login or Register");
        setSize(557, 695);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        usernameLabel.setForeground(new Color(0, 191, 255));
        usernameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        usernameLabel.setBounds(28, 221, 143, 40);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        passwordLabel.setForeground(new Color(0, 191, 255));
        passwordLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        passwordLabel.setBounds(28, 284, 143, 40);
        usernameField = new JTextField(10);
        usernameField.setBounds(201, 230, 258, 29);
        passwordField = new JPasswordField(10);
        passwordField.setBounds(201, 288, 258, 29);

        loginButton = new JButton("Login");
        loginButton.setForeground(new Color(0, 191, 255));
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
        loginButton.setBounds(201, 360, 143, 40);
        panel.setLayout(null);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);

        getContentPane().add(panel);
        
        lblNewLabel = new JLabel("LOGIN");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 40));
        lblNewLabel.setForeground(new Color(0, 191, 255));
        lblNewLabel.setBounds(190, 75, 143, 69);
        panel.add(lblNewLabel);
        
        lblNewLabel_1 = new JLabel("Bạn chưa có tài khoản? Đăng ký");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(135, 412, 218, 34);
        panel.add(lblNewLabel_1);
        
        lblNewLabel_2 = new JLabel("Tại đây");
        lblNewLabel_2.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		Register register = new Register();
    			register.setVisible(true);
        	}
        });
        lblNewLabel_2.setForeground(new Color(0, 191, 255));
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_2.setBounds(359, 412, 58, 34);
        panel.add(lblNewLabel_2);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/chatuser", "chau", "1234");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Không thể kết nối với cơ sở dữ liệu!");
        }

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                authenticateUser(username, password);
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
                ManagerChatter managerChatter = new ManagerChatter();
                managerChatter.setVisible(true);
                
                // Chờ cho ManagerChatter khởi động xong
                SwingUtilities.invokeLater(() -> {
                    ClientChater chatClient = new ClientChater();
                    chatClient.setVisible(true);
                });

                this.dispose();
            } else {
                JOptionPane.showMessageDialog(Login.this, "Sai username hoặc password");
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
            JOptionPane.showMessageDialog(Login.this, "Người dùng đã đăng kí thành công!");
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
                if (hex.length() == 1) hexString.append('0');
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
        	Login app = new Login();
            app.setVisible(true);
        });
    }
}
