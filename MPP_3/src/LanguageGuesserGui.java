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

    public LanguageGuesserGui() {
        JFrame frame = new JFrame("Language Guesser");
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        scrollPane.getVerticalScrollBar().setBackground(new Color(51, 56, 65));
        scrollPane.getVerticalScrollBar().setForeground(new Color(51, 56, 64));

        guessButton.setFocusable(false);

    }
}
