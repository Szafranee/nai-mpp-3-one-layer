import javax.swing.*;
import java.awt.*;

public class LanguageGuesserGui {
    private JPanel panel1;
    private JLabel Title;
    private JTextArea textInputArea;
    private JPanel textAreaPanel;
    private JPanel infoPanel;
    private JScrollPane scrollPane;
    private JButton guessButton;
    private JPanel buttonPanel;
    private JLabel guessedLanguage;

    public LanguageGuesserGui() {
        JFrame frame = new JFrame("Language Guesser");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        scrollPane.getVerticalScrollBar().setBackground(new Color(51, 56, 65));
        scrollPane.getVerticalScrollBar().setForeground(new Color(51, 56, 64));

        guessButton.setFocusable(false);

        guessButton.addActionListener(e -> {
            String text = textInputArea.getText();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter some text to guess the language.");
            } else {
                String language = Main.testPerceptrons(Main.perceptrons, text);
                guessedLanguage.setText(language);
//                Main.trainPerceptrons(Main.perceptrons, Main.trainingVectorsList, Main.epochs);
            }
        });

        // procced when enter is pressed
    }
}
