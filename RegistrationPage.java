import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegistrationPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public class RoundedBorder implements Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }

    public RegistrationPage() {
        setTitle("Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 80));
        setBackground(getForeground());
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton registerButton = new JButton("Register");
        JButton cancelButton = new JButton("Cancel");

        // Set rounded borders
        int borderRadius = 10; // Adjust radius as needed
        usernameField.setBorder(new RoundedBorder(borderRadius));
        passwordField.setBorder(new RoundedBorder(borderRadius));
        registerButton.setBorder(new RoundedBorder(borderRadius));
        cancelButton.setBorder(new RoundedBorder(borderRadius));

        // Set font and foreground color for labels and buttons
        Font font = new Font("SansSerif", Font.PLAIN, 18);
        usernameLabel.setFont(font);
        passwordLabel.setFont(font);
        registerButton.setFont(font);
        cancelButton.setFont(font);
        usernameLabel.setForeground(Color.blue);
        passwordLabel.setForeground(Color.blue);
        registerButton.setForeground(Color.WHITE);
        cancelButton.setForeground(Color.WHITE);

        // Set background color for text fields and buttons
        usernameField.setBackground(Color.WHITE);
        passwordField.setBackground(Color.WHITE);
        registerButton.setContentAreaFilled(true);
        registerButton.setBackground(new Color(36, 59, 85)); // Dark blue
        cancelButton.setContentAreaFilled(true);
        cancelButton.setBackground(new Color(36, 59, 85)); // Dark blue

        // Add action listener to the register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(RegistrationPage.this, "Username or password cannot be empty. Please enter valid credentials.");
                } else {
                    if (registerUser(username, password)) {
                        JOptionPane.showMessageDialog(RegistrationPage.this, "Registration Successful!");
                        dispose();
                        openLoginPage();
                    } else {
                        JOptionPane.showMessageDialog(RegistrationPage.this, "Failed to register. Please try again.");
                    }
                }
            }
        });

        // Add action listener to the cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openLoginPage();
                dispose();
            }
        });

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        add(cancelButton);

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private boolean registerUser(String username, String password) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("userpass.csv", true))) {
            bw.write(username + "," + password);
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openLoginPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegistrationPage();
            }
        });
    }

}
