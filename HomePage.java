import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FlashCard {
    private String question;
    private String[] options;
    private int correctOptionIndex;

    // public class GradientPanel extends JPanel {
    //     @Override
    //     protected void paintComponent(Graphics g) {
    //         super.paintComponent(g);
    //         Graphics2D g2d = (Graphics2D) g;

    //         // Define gradient colors (dark blue to light blue)
    //         Color color1 = new Color(36, 59, 85); // Dark blue
    //         Color color2 = new Color(51, 102, 153); // Light blue

    //         // Define gradient start and end points
    //         int width = getWidth();
    //         int height = getHeight();
    //         GradientPaint gradient = new GradientPaint(0, 0, color1, width, height, color2);
    //         g2d.setPaint(gradient);
    //         g2d.fillRect(0, 0, width, height);
    //     }
    // }

    public FlashCard(String question, String[] options, int correctOptionIndex) {
        this.question = question;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

class FlashCardQuizUI extends JFrame {
    private List<FlashCard> flashCards;
    private JTextArea questionArea;
    private JRadioButton[] optionButtons;
    private JButton nextButton;
    private int currentCardIndex;
    private int score;

    private List<String> userData; // List to store user data

    public FlashCardQuizUI(String title) {
        setTitle(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());
        setResizable(true);
        setLocationRelativeTo(null);

        flashCards = new ArrayList<>();
        currentCardIndex = 0;
        score = 0;
        userData = new ArrayList<>(); // Initialize user data list

        questionArea = new JTextArea();
        questionArea.setFont(new Font("Arial", Font.BOLD, 16));
        questionArea.setBackground(new Color(36, 59, 85)); // Dark blue
        questionArea.setForeground(Color.WHITE);
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);

        JScrollPane questionScrollPane = new JScrollPane(questionArea);
        questionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        questionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        optionButtons = new JRadioButton[4];
        ButtonGroup optionGroup = new ButtonGroup();
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionButtons[i].setForeground(Color.BLACK);
            optionButtons[i].setBackground(new Color(36, 59, 85)); // Dark blue
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setForeground(Color.WHITE);
        nextButton.setBackground(new Color(36, 59, 85)); // Dark blue
        nextButton.addActionListener(new NextButtonListener());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(questionScrollPane, BorderLayout.NORTH);
        mainPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(nextButton, BorderLayout.SOUTH);

        Border blackLine = BorderFactory.createLineBorder(Color.BLACK);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(blackLine, BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        add(mainPanel, BorderLayout.CENTER);
    }

    public void addFlashCard(FlashCard flashCard) {
        flashCards.add(flashCard);
    }

    public void startQuiz() {
        Collections.shuffle(flashCards);
        showNextFlashCard();
    }

    private void showNextFlashCard() {
        if (currentCardIndex < flashCards.size()) {
            FlashCard flashCard = flashCards.get(currentCardIndex);
            questionArea.setText(flashCard.getQuestion());
            String[] options = flashCard.getOptions();
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setSelected(false);
            }
            currentCardIndex++;
        } else {
            JOptionPane.showMessageDialog(this, "Quiz ended. Your score: " + score);
            saveUserData(); // Save user data when quiz ends
            dispose();
        }
    }

    private class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FlashCard flashCard = flashCards.get(currentCardIndex - 1);
            for (int i = 0; i < 4; i++) {
                if (optionButtons[i].isSelected()) {
                    if (i == flashCard.getCorrectOptionIndex()) {
                        JOptionPane.showMessageDialog(FlashCardQuizUI.this, "Correct!");
                        score++;
                    } else {
                        JOptionPane.showMessageDialog(FlashCardQuizUI.this, "Incorrect. The correct answer is: " + flashCard.getOptions()[flashCard.getCorrectOptionIndex()]);
                    }
                    // Store user's answer and correct answer in user data list
                    userData.add(flashCard.getQuestion() + "," + flashCard.getOptions()[i] + "," + flashCard.getOptions()[flashCard.getCorrectOptionIndex()]);
                    showNextFlashCard();
                    return;
                }
            }
            JOptionPane.showMessageDialog(FlashCardQuizUI.this, "Please select an option.");
        }
    }

    private void saveUserData() {
        try {
            FileWriter writer = new FileWriter("userdata.csv");
            // Write user data to CSV file
            for (String data : userData) {
                writer.write(data + "\n");
            }
            // Write total score
            writer.write("Total Score:," + score + "\n");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

public class HomePage extends JFrame {

    public HomePage() {
        setTitle("Java Quiz and Notes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setLayout(new FlowLayout(FlowLayout.CENTER, 150, 70));
        setBackground(new Color(36, 59, 85)); // Dark blue
        setLocationRelativeTo(null);

        JButton notesButton = new JButton("Notes");
        notesButton.setFont(new Font("Arial", Font.BOLD, 16));
        notesButton.setForeground(Color.WHITE);
        notesButton.setBackground(new Color(36, 59, 85)); // Dark blue

        // JPanel quizPanel = new JPanel(new FlowLayout());
        // quizPanel.setBackground(new Color(36, 59, 85)); // Dark blue

        JButton quiz1Button = new JButton("Quiz 1");
        quiz1Button.setFont(new Font("Arial", Font.BOLD, 16));
        quiz1Button.setForeground(Color.WHITE);
        quiz1Button.setBackground(new Color(36, 59, 85));
        JButton quiz2Button = new JButton("Quiz 2");
        quiz2Button.setFont(new Font("Arial", Font.BOLD, 16));
        quiz2Button.setForeground(Color.WHITE);
        quiz2Button.setBackground(new Color(36, 59, 85));
        JButton quiz3Button = new JButton("Quiz 3");
        quiz3Button.setFont(new Font("Arial", Font.BOLD, 16));
        quiz3Button.setForeground(Color.WHITE);
        quiz3Button.setBackground(new Color(36, 59, 85));
        quiz1Button.setFont(new Font("Arial", Font.BOLD, 16));
        quiz1Button.setForeground(Color.WHITE);
        quiz1Button.setBackground(new Color(36, 59, 85)); // Dark blue
        quiz2Button.setFont(new Font("Arial", Font.BOLD, 16));
        quiz2Button.setForeground(Color.WHITE);
        quiz2Button.setBackground(new Color(36, 59, 85)); // Dark blue
        quiz3Button.setFont(new Font("Arial", Font.BOLD, 16));
        quiz3Button.setForeground(Color.WHITE);
        quiz3Button.setBackground(new Color(36, 59, 85)); // Dark blue

        notesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNotes();
            }
        });

        quiz1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openQuiz("Quiz 1");
            }
        });

        quiz2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openQuiz("Quiz 2");
            }
        });

        quiz3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openQuiz("Quiz 3");
            }
        });

        add(quiz1Button);
        add(quiz2Button);
        add(quiz3Button);

        add(notesButton);
            }

    private void openNotes() {
        JFrame notesFrame = new JFrame("Notes");
        JTextArea notesArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(notesArea);
        notesFrame.add(scrollPane);
        notesFrame.setSize(400, 300);
        notesFrame.setLocationRelativeTo(null);
        notesFrame.setVisible(true);
    }

    private void openQuiz(String title) {
        FlashCardQuizUI quizUI = new FlashCardQuizUI(title);
        if (title.equals("Quiz 1")) {
            quizUI.addFlashCard(new FlashCard("Which of the following is not a primitive data type in Java?", new String[]{"int", "double", "String", "boolean"}, 2));
            quizUI.addFlashCard(new FlashCard("What is the output of the following code snippet?\n\n" +
                    "```java\n" +
                    "int x = 10;\n" +
                    "System.out.println(x++);\n" +
                    "```\n", new String[]{"10", "11", "9", "Compilation Error"}, 0));
            quizUI.addFlashCard(new FlashCard("Which of the following is a reference data type in Java?", new String[]{"int", "float", "char", "String"}, 3));
            quizUI.addFlashCard(new FlashCard("Which loop is guaranteed to execute at least one time in Java?", new String[]{"for", "while", "do-while", "if"}, 2));
            quizUI.addFlashCard(new FlashCard("What is the result of `7 % 3` in Java?", new String[]{"2", "3", "1", "0"}, 0));
            quizUI.addFlashCard(new FlashCard("What is the output of `System.out.println(5 == 5.0)` in Java?", new String[]{"true", "false", "Compilation Error", "Runtime Error"}, 0));
            quizUI.addFlashCard(new FlashCard("What is the output of the following code snippet?\n\n" +
                    "```java\n" +
                    "String str = \"Java\";\n" +
                    "System.out.println(str.substring(1, 3));\n" +
                    "```\n", new String[]{"Jav", "av", "ava", "va"}, 1));
            quizUI.addFlashCard(new FlashCard("Which of the following is not a valid Java identifier?", new String[]{"myVariable", "123variable", "_variable", "variable123"}, 1));
            quizUI.addFlashCard(new FlashCard("What will happen if you try to divide an integer by zero in Java?", new String[]{"Compilation Error", "Runtime Error", "Infinity", "NaN"}, 1));
            quizUI.addFlashCard(new FlashCard("What is the default value of a boolean variable in Java?", new String[]{"true", "false", "0", "null"}, 1));
        } else if (title.equals("Quiz 2")) {
            quizUI.addFlashCard(new FlashCard("What is the superclass of all classes in Java?", new String[]{"Object", "Superclass", "Parent", "Root"}, 0));
            quizUI.addFlashCard(new FlashCard("Which keyword is used to define a constant in Java?", new String[]{"final", "const", "static", "constant"}, 0));
            quizUI.addFlashCard(new FlashCard("What is the access level of a method without any modifier in Java?", new String[]{"public", "private", "protected", "default"}, 3));
            quizUI.addFlashCard(new FlashCard("What does the 'break' keyword do in a loop in Java?", new String[]{"Exits the loop and continues to the next iteration", "Exits the loop entirely", "Skips the current iteration and continues to the next", "None of the above"}, 1));
            quizUI.addFlashCard(new FlashCard("Which of the following is an example of a compile-time error in Java?", new String[]{"Division by zero", "ArrayIndexOutOfBoundsException", "NullPointerException", "Syntax Error"}, 3));
            quizUI.addFlashCard(new FlashCard("What is the result of `Math.pow(2, 3)` in Java?", new String[]{"4", "6", "8", "16"}, 3));
            quizUI.addFlashCard(new FlashCard("What is the output of `System.out.println(3 | 5)` in Java?", new String[]{"0", "1", "3", "5"}, 2));
            quizUI.addFlashCard(new FlashCard("Which of the following correctly declares an array of integers in Java?", new String[]{"int[] arr;", "int arr[];", "int arr[10];", "int[10] arr;"}, 1));
            quizUI.addFlashCard(new FlashCard("What is the purpose of the 'finally' block in a try-catch-finally statement in Java?", new String[]{"To catch exceptions", "To execute code regardless of whether an exception occurs or not", "To specify code that should be executed before throwing an exception", "To specify code that should be executed after throwing an exception"}, 1));
        } else if (title.equals("Quiz 3")) {
            quizUI.addFlashCard(new FlashCard("Which of the following is a valid identifier in Java?", new String[]{"1variable", "_variable", "variable-1", "variable$"}, 1));
            quizUI.addFlashCard(new FlashCard("What is the return type of the 'main' method in Java?", new String[]{"void", "int", "String", "main"}, 0));
            quizUI.addFlashCard(new FlashCard("What is the output of the following code snippet?\n\n" +
                    "```java\n" +
                    "int[] arr = {1, 2, 3};\n" +
                    "System.out.println(arr[3]);\n" +
                    "```\n", new String[]{"Compilation Error", "0", "1", "ArrayIndexOutOfBoundsException"}, 3));
            quizUI.addFlashCard(new FlashCard("How do you declare a constant in Java?", new String[]{"Using the `const` keyword", "Using the `final` keyword", "Using the `static` keyword", "Constants cannot be declared in Java"}, 1));
            quizUI.addFlashCard(new FlashCard("What is the purpose of the `break` statement in Java?", new String[]{"To terminate a loop or switch statement", "To skip the current iteration of a loop", "To throw an exception", "To define a constant"}, 0));
            quizUI.addFlashCard(new FlashCard("What is the result of `Math.sqrt(16)` in Java?", new String[]{"4", "6", "8", "16"}, 0));
            quizUI.addFlashCard(new FlashCard("What is the output of `System.out.println(3 & 5)` in Java?", new String[]{"0", "1", "3", "5"}, 0));
            quizUI.addFlashCard(new FlashCard("Which of the following is a correct way to declare and initialize a String in Java?", new String[]{"String str = new String(\"Hello\");", "String str = \"Hello\";", "String str();", "String str; str = \"Hello\";"}, 1));
            quizUI.addFlashCard(new FlashCard("What is the purpose of the 'finally' block in a try-catch-finally statement in Java?", new String[]{"To catch exceptions", "To execute code regardless of whether an exception occurs or not", "To specify code that should be executed before throwing an exception", "To specify code that should be executed after throwing an exception"}, 1));
        }
        quizUI.startQuiz();
        quizUI.setVisible(true);
    }

    public static void main(String[] args) {
        // Set look and feel to Nimbus for better UI appearance
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HomePage homePage = new HomePage();
        homePage.setVisible(true);
    }
}
