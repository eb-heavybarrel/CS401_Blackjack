package main;

import java.awt.*;
import javax.swing.*;

public class BlackjackDealerTableGUI extends JFrame
{
    private static final Color TABLE_GREEN = new Color(49, 119, 43);
    private static final Color PLAYER_PANEL_GREEN = new Color(31, 80, 33);
    private static final Color PLAYER_LABEL_GREEN = new Color(99, 135, 94);
    private static final Color BUTTON_RED = new Color(151, 30, 30);
    private static final Color DISABLED_RED = new Color(110, 75, 75);
    private static final Color TIMER_YELLOW = new Color(226, 207, 57);
    private static final Color CARD_WHITE = new Color(246, 246, 240);

    private JLabel timerLabel;
    private JLabel dealerLabel;
    private JLabel tableLabel;
    private JLabel betRangeLabel;
    private JLabel ipLabel;

    private JButton openTableButton;
    private JButton dealCardButton;
    private JButton insuranceButton;
    private JButton endNextTurnButton;
    private JButton closeTableButton;

    private String dealerId;
    private String ipAddress;
    private int turnTimeSeconds;
    private int minimumBet;
    private int maximumBet;
    private boolean tableOpen;
    private boolean endTableNextTurn;

    public BlackjackDealerTableGUI()
    {
        this("DealerTest", "localhost");
    }

    public BlackjackDealerTableGUI(String dealerId)
    {
        this(dealerId, "localhost");
    }

    public BlackjackDealerTableGUI(String dealerId, String ipAddress)
    {
        super("Blackjack Dealer Table");

        this.dealerId = dealerId;
        this.ipAddress = ipAddress;
        this.turnTimeSeconds = 45;
        this.minimumBet = 10;
        this.maximumBet = 500;
        this.tableOpen = false;
        this.endTableNextTurn = false;

        buildFrame();
        setTableControlsEnabled(false);

        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                showTableSetupDialog();
            }
        });
    }

    private void buildFrame()
    {
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(TABLE_GREEN);
        mainPanel.setPreferredSize(new Dimension(1180, 700));

        mainPanel.add(buildTimerPanel());
        mainPanel.add(buildPlayerPanel());

        addCards(mainPanel);

        JButton settingsButton = buildActionButton("Settings", 1010, 145, 205, 50, true);
        settingsButton.addActionListener(event -> showTableSetupDialog());

        openTableButton = buildActionButton("Open Table", 1010, 210, 205, 50, true);
        openTableButton.addActionListener(event -> openTable());

        dealCardButton = buildActionButton("Deal Card", 1010, 275, 205, 50, true);
        dealCardButton.addActionListener(event -> dealCard());

        insuranceButton = buildActionButton("Offer Insurance", 1010, 340, 205, 50, true);
        insuranceButton.addActionListener(event -> offerInsurance());

        endNextTurnButton = buildActionButton("End Table\nNext Turn", 1010, 405, 205, 64, true);
        endNextTurnButton.addActionListener(event -> promptEndTableNextTurn());

        closeTableButton = buildActionButton("Close Table", 1010, 486, 205, 50, false);
        closeTableButton.addActionListener(event -> closeTable());

        mainPanel.add(settingsButton);
        mainPanel.add(openTableButton);
        mainPanel.add(dealCardButton);
        mainPanel.add(insuranceButton);
        mainPanel.add(endNextTurnButton);
        mainPanel.add(closeTableButton);

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

        timerLabel = new JLabel(formatTimer(turnTimeSeconds), SwingConstants.CENTER);
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

    private JButton buildActionButton(String text, int x, int y, int w, int h, boolean enabledStyle)
    {
        JButton button = new JButton("<html><center>" + text.replace("\n", "<br>") + "</center></html>");
        button.setBounds(x, y, w, h);
        button.setFont(new Font("Monospaced", Font.PLAIN, text.length() > 12 ? 19 : 23));
        button.setForeground(Color.WHITE);
        button.setBackground(enabledStyle ? BUTTON_RED : DISABLED_RED);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private JPanel buildInfoPanel()
    {
        JPanel panel = new JPanel(new GridLayout(4, 1));
        panel.setBounds(470, 604, 390, 76);
        panel.setOpaque(false);

        dealerLabel = makeBottomLabel("Dealer: " + dealerId);
        ipLabel = makeBottomLabel("IP: " + ipAddress);
        tableLabel = makeBottomLabel("Table setup required");
        betRangeLabel = makeBottomLabel("Bet Range: " + minimumBet + " - " + maximumBet);

        panel.add(dealerLabel);
        panel.add(ipLabel);
        panel.add(tableLabel);
        panel.add(betRangeLabel);

        return panel;
    }

    private JLabel makeBottomLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    private void showTableSetupDialog()
    {
        if (tableOpen)
        {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Changing settings while the table is open is only a placeholder.\nContinue?",
                    "Table Settings",
                    JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION)
            {
                return;
            }
        }

        JDialog setupDialog = new JDialog(this, "Table Setup", true);
        setupDialog.setSize(390, 270);
        setupDialog.setLocationRelativeTo(this);

        JPanel setupPanel = new JPanel(new BorderLayout(10, 10));
        setupPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel fieldsPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JSpinner turnTimeSpinner = new JSpinner(new SpinnerNumberModel(turnTimeSeconds, 10, 120, 5));
        JSpinner minBetSpinner = new JSpinner(new SpinnerNumberModel(minimumBet, 1, 10000, 5));
        JSpinner maxBetSpinner = new JSpinner(new SpinnerNumberModel(maximumBet, 1, 10000, 25));

        fieldsPanel.add(new JLabel("Turn time"));
        fieldsPanel.add(turnTimeSpinner);

        fieldsPanel.add(new JLabel("Minimum bet"));
        fieldsPanel.add(minBetSpinner);

        fieldsPanel.add(new JLabel("Maximum bet"));
        fieldsPanel.add(maxBetSpinner);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> setupDialog.dispose());

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(event ->
        {
            int newTurnTime = (Integer) turnTimeSpinner.getValue();
            int newMinBet = (Integer) minBetSpinner.getValue();
            int newMaxBet = (Integer) maxBetSpinner.getValue();

            if (newMinBet > newMaxBet)
            {
                JOptionPane.showMessageDialog(setupDialog,
                        "Minimum bet cannot be greater than maximum bet.");
                return;
            }

            turnTimeSeconds = newTurnTime;
            minimumBet = newMinBet;
            maximumBet = newMaxBet;

            timerLabel.setText(formatTimer(turnTimeSeconds));
            betRangeLabel.setText("Bet Range: " + minimumBet + " - " + maximumBet);
            tableLabel.setText("Settings confirmed");
            openTableButton.setEnabled(true);

            setupDialog.dispose();
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        setupPanel.add(new JLabel("Confirm table settings before opening."), BorderLayout.NORTH);
        setupPanel.add(fieldsPanel, BorderLayout.CENTER);
        setupPanel.add(buttonPanel, BorderLayout.SOUTH);

        setupDialog.add(setupPanel);
        setupDialog.setVisible(true);
    }

    private String formatTimer(int seconds)
    {
        return "0:" + String.format("%02d", seconds);
    }

    private void openTable()
    {
        tableOpen = true;
        tableLabel.setText("Table Open");
        setTableControlsEnabled(true);
        openTableButton.setEnabled(false);
    }

    private void setTableControlsEnabled(boolean enabled)
    {
        dealCardButton.setEnabled(enabled);
        insuranceButton.setEnabled(enabled);
        endNextTurnButton.setEnabled(enabled);
        closeTableButton.setEnabled(enabled);
    }

    private void dealCard()
    {
        if (!tableOpen)
        {
            tableLabel.setText("Open table first");
            return;
        }

        tableLabel.setText("Deal card requested");
    }

    private void offerInsurance()
    {
        if (!tableOpen)
        {
            tableLabel.setText("Open table first");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Offer insurance to players?\nThis should only be used when the dealer shows an Ace.",
                "Offer Insurance",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION)
        {
            tableLabel.setText("Insurance offered");
        }
    }

    private void promptEndTableNextTurn()
    {
        if (!tableOpen)
        {
            tableLabel.setText("Open table first");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "End this table after the next turn/round?",
                "End Table Next Turn",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION)
        {
            endTableNextTurn = true;
            tableLabel.setText("Ending after next turn");
        }
    }

    private void closeTable()
    {
        tableOpen = false;
        endTableNextTurn = false;
        tableLabel.setText("Table Closed");
        setTableControlsEnabled(false);
        openTableButton.setEnabled(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new BlackjackDealerTableGUI("DealerTest", "localhost").setVisible(true);
            }
        });
    }
}
