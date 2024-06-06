import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    // private boolean passwordVisible = false;

    // Custom rounded border class
    public class RoundedBorder implements Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.BLACK);
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

    public LoginPage() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 80));
        setBackground(getForeground());
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
       // JButton togglePasswordButton = new JButton(new ImageIcon("eye.png")); // Add your eye icon path here

        // Set rounded borders
        int borderRadius = 10; // Adjust radius as needed
        usernameField.setBorder(new RoundedBorder(borderRadius));
        passwordField.setBorder(new RoundedBorder(borderRadius));
        loginButton.setBorder(new RoundedBorder(borderRadius));
        registerButton.setBorder(new RoundedBorder(borderRadius));
        //togglePasswordButton.setBorder(null);

        // Set font and foreground color for labels and buttons
        Font font = new Font("SansSerif", Font.PLAIN, 18);
        usernameLabel.setFont(font);
        passwordLabel.setFont(font);
        loginButton.setFont(font);
        registerButton.setFont(font);
        //togglePasswordButton.setFont(font);
        usernameLabel.setForeground(Color.BLUE);
        passwordLabel.setForeground(Color.BLUE);
        loginButton.setForeground(Color.WHITE);
        registerButton.setForeground(Color.WHITE);
        //togglePasswordButton.setContentAreaFilled(false); // Remove button background

        // Set background color for text fields and buttons
        usernameField.setBackground(Color.WHITE);
        passwordField.setBackground(Color.WHITE);
        loginButton.setContentAreaFilled(true);
        loginButton.setBackground(new Color(36, 59, 85)); // Dark blue
        registerButton.setContentAreaFilled(true);
        registerButton.setBackground(new Color(36, 59, 85)); // Dark blue

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (verifyLogin(username, password)) {
                    JOptionPane.showMessageDialog(LoginPage.this, "Login Successful!");
                    openHomePage();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginPage.this, "Invalid username or password. Please try again.");
                    usernameField.setText("");
                    passwordField.setText("");
                }
            }
        });

        // Add action listener to the register button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationPage();
                dispose();
            }
        });

        // Add mouse listener to toggle password visibility
        // togglePasswordButton.addMouseListener(new MouseAdapter() {
        //     @Override
        //     public void mouseClicked(MouseEvent e) {
        //         super.mouseClicked(e);
        //         passwordVisible = !passwordVisible;
        //         if (passwordVisible) {
        //             passwordField.setEchoChar((char) 0); // Show password
        //         } else {
        //             passwordField.setEchoChar('\u2022'); // Hide password
        //         }
        //     }
        // });

        // Add components to the frame
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        //add(togglePasswordButton);
        add(loginButton);
        add(registerButton);

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private boolean verifyLogin(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader("userpass.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void openHomePage() {
        HomePage homePage = new HomePage();
        homePage.setVisible(true);
    }

    private void openRegistrationPage() {
        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginPage();
            }
        });
    }
}
