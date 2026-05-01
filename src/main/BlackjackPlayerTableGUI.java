package main;

import java.awt.*;
import javax.swing.*;

public class BlackjackPlayerTableGUI extends JFrame
{
    private static final Color TABLE_GREEN = new Color(49, 119, 43);
    private static final Color PLAYER_PANEL_GREEN = new Color(31, 80, 33);
    private static final Color PLAYER_LABEL_GREEN = new Color(99, 135, 94);
    private static final Color BUTTON_RED = new Color(151, 30, 30);
    private static final Color DISABLED_RED = new Color(110, 75, 75);
    private static final Color TIMER_YELLOW = new Color(226, 207, 57);
    private static final Color CARD_WHITE = new Color(246, 246, 240);

    private JLabel timerLabel;
    private JLabel tableLabel;
    private JLabel creditsLabel;
    private JLabel betLabel;

    private JSpinner betSpinner;

    private String username;
    private BlackjackLobbyGUI.TableInfo tableInfo;
    private float credits;
    private int currentBet;

    public BlackjackPlayerTableGUI()
    {
        this("Guest", 1000.0f, new BlackjackLobbyGUI.TableInfo("Table 1", 45, 100, 3));
    }

    public BlackjackPlayerTableGUI(String username, float credits, BlackjackLobbyGUI.TableInfo tableInfo)
    {
        super("Blackjack Player Table");

        this.username = username;
        this.credits = credits;
        this.tableInfo = tableInfo;
        this.currentBet = 0;

        buildFrame();
    }

    private void buildFrame()
    {
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(TABLE_GREEN);
        mainPanel.setPreferredSize(new Dimension(1180, 700));

        mainPanel.add(buildTimerPanel());
        mainPanel.add(buildPlayerPanel());

        addCards(mainPanel);

        mainPanel.add(buildActionButton("Hit", 1010, 210, 205, 62, true, "HIT"));
        mainPanel.add(buildActionButton("Stand", 1010, 292, 205, 62, true, "STAND"));
        mainPanel.add(buildActionButton("Surrender", 1010, 374, 205, 62, true, "SURRENDER"));
        mainPanel.add(buildActionButton("Double-Down", 1010, 456, 205, 62, false, "DOUBLE_DOWN"));

        mainPanel.add(buildBetPanel());
        mainPanel.add(buildInfoPanel());

        add(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildTimerPanel()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBounds(486, 0, 210, 74);
        panel.setBackground(TIMER_YELLOW);

        timerLabel = new JLabel(formatTimer(tableInfo.turnSeconds), SwingConstants.CENTER);
        timerLabel.setForeground(Color.BLACK);
        timerLabel.setFont(new Font("SansSerif", Font.PLAIN, 46));

        panel.add(timerLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildPlayerPanel()
    {
        JPanel panel = new JPanel(null);
        panel.setBounds(18, 34, 445, 630);
        panel.setBackground(PLAYER_PANEL_GREEN);

        int y = 20;

        for (int i = 1; i <= 6; i++)
        {
            JLabel row = new JLabel("Player " + i + " | Score: XXX |");
            row.setOpaque(true);
            row.setBackground(PLAYER_LABEL_GREEN);
            row.setForeground(Color.WHITE);
            row.setFont(new Font("Monospaced", Font.BOLD, 20));
            row.setBounds(12, y, 416, 34);
            panel.add(row);

            y += 104;
        }

        return panel;
    }

    private void addCards(JPanel mainPanel)
    {
        mainPanel.add(makeCard(565, 105));
        mainPanel.add(makeCard(710, 105));
        mainPanel.add(makeCard(572, 485));
        mainPanel.add(makeCard(712, 485));
    }

    private JPanel makeCard(int x, int y)
    {
        JPanel card = new JPanel();
        card.setBounds(x, y, 120, 160);
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(235, 235, 235), 2));
        return card;
    }

    private JButton buildActionButton(String text, int x, int y, int w, int h, boolean enabledStyle, String action)
    {
        JButton button = new JButton(text);
        button.setBounds(x, y, w, h);
        button.setFont(new Font("Monospaced", Font.PLAIN, text.length() > 10 ? 24 : 32));
        button.setForeground(Color.WHITE);
        button.setBackground(enabledStyle ? BUTTON_RED : DISABLED_RED);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(event -> playerAction(action));
        return button;
    }

    private JPanel buildBetPanel()
    {
        JPanel panel = new JPanel(null);
        panel.setBounds(905, 570, 230, 110);
        panel.setOpaque(false);

        JLabel label = new JLabel("Bet", SwingConstants.CENTER);
        label.setBounds(30, 0, 120, 45);
        label.setFont(new Font("SansSerif", Font.PLAIN, 42));
        label.setForeground(Color.BLACK);

        JPanel yellowBox = new JPanel(null);
        yellowBox.setBounds(0, 44, 136, 70);
        yellowBox.setBackground(TIMER_YELLOW);

        betSpinner = new JSpinner(new SpinnerNumberModel(tableInfo.bet, 1, 5000, 5));
        betSpinner.setBounds(16, 16, 104, 38);
        betSpinner.setFont(new Font("SansSerif", Font.PLAIN, 22));
        yellowBox.add(betSpinner);

        JButton allInButton = new JButton("All in");
        allInButton.setBounds(136, 66, 72, 34);
        allInButton.setBackground(new Color(135, 130, 55));
        allInButton.setForeground(Color.WHITE);
        allInButton.setBorderPainted(false);
        allInButton.setFocusPainted(false);
        allInButton.addActionListener(event ->
        {
            betSpinner.setValue((int) credits);
            placeBet();
        });

        JButton placeButton = new JButton("Place");
        placeButton.setBounds(136, 44, 72, 24);
        placeButton.setFont(new Font("SansSerif", Font.PLAIN, 11));
        placeButton.setBackground(TIMER_YELLOW);
        placeButton.setBorderPainted(false);
        placeButton.setFocusPainted(false);
        placeButton.addActionListener(event -> placeBet());

        panel.add(label);
        panel.add(yellowBox);
        panel.add(placeButton);
        panel.add(allInButton);

        return panel;
    }

    private JPanel buildInfoPanel()
    {
        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBounds(470, 620, 330, 60);
        panel.setOpaque(false);

        tableLabel = makeBottomLabel(tableInfo.name + " | " + username);
        creditsLabel = makeBottomLabel("Credits: " + credits);
        betLabel = makeBottomLabel("Bet: " + currentBet);

        panel.add(tableLabel);
        panel.add(creditsLabel);
        panel.add(betLabel);

        return panel;
    }

    private JLabel makeBottomLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    private String formatTimer(int seconds)
    {
        return "0:" + String.format("%02d", seconds);
    }

    private void placeBet()
    {
        int bet = (Integer) betSpinner.getValue();

        if (bet > credits)
        {
            betLabel.setText("Not enough credits");
            return;
        }

        currentBet = bet;
        credits -= bet;

        creditsLabel.setText("Credits: " + credits);
        betLabel.setText("Bet: " + currentBet);
    }

    private void playerAction(String action)
    {
        betLabel.setText("Action: " + action);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new BlackjackPlayerTableGUI().setVisible(true);
            }
        });
    }
}
