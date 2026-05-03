package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import javax.swing.*;
import javax.imageio.ImageIO;

// dealer-side table view
// used to manage the table (open, deal, insurance, close)
// most controls are still local placeholders
// server should own actual game state
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

    private Map<String, BufferedImage> cardImageMap;
    private String cardImageFolderPath = "cards";

    private HandDisplayPanel dealerHandPanel;
    private HandDisplayPanel tableHandPanel;

    private String dealerId;
    private String ipAddress;
    private int turnTimeSeconds;
    private int minimumBet;
    private int maximumBet;
    private boolean tableOpen;
    private boolean endTableNextTurn;

    private Socket socket;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    public BlackjackDealerTableGUI()
    {
        this("DealerTest", "localhost", null, null, null);
    }

    public BlackjackDealerTableGUI(String dealerId)
    {
        this(dealerId, "localhost", null, null, null);
    }

    public BlackjackDealerTableGUI(String dealerId, String ipAddress)
    {
        this(dealerId, ipAddress, null, null, null);
    }

    public BlackjackDealerTableGUI(String dealerId, String ipAddress,
            Socket socket, ObjectInputStream objIn, ObjectOutputStream objOut)
    {
        super("Blackjack Dealer Table");

        this.dealerId = dealerId;
        this.ipAddress = ipAddress;
        this.turnTimeSeconds = 45;
        this.minimumBet = 10;
        this.maximumBet = 500;
        this.tableOpen = false;
        this.endTableNextTurn = false;
        this.socket = socket;
        this.objIn = objIn;
        this.objOut = objOut;

        loadCardImages();
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


    private void loadCardImages()
    {
        // card image setup
        // expected folder example:
        // project/cards/ace_of_spades.png
        // project/cards/10_of_hearts.png
        // project/cards/back.png
        cardImageMap = new HashMap<String, BufferedImage>();

        String[] values =
        {
            "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "jack", "queen", "king", "ace"
        };

        String[] suits =
        {
            "spades", "hearts", "diamonds", "clubs"
        };

        for (String value : values)
        {
            for (String suit : suits)
            {
                String imageKey = value + "_of_" + suit;
                loadCardImage(imageKey, cardImageFolderPath + "/" + imageKey + ".png");
            }
        }

        loadCardImage("back", cardImageFolderPath + "/back.png");
        loadCardImage("?", cardImageFolderPath + "/back.png");
    }

    private void loadCardImage(String imageKey, String filePath)
    {
        try
        {
            BufferedImage image = ImageIO.read(new java.io.File(filePath));

            if (image != null)
            {
                cardImageMap.put(imageKey.toLowerCase(), image);
            }
        }
        catch (IOException e)
        {
            // image files are optional for now
            // missing images fall back to simple drawn cards
        }
    }

    private String normalizeCardKey(String cardText)
    {
        if (cardText == null)
        {
            return "";
        }

        return cardText.trim().toLowerCase()
                .replace(" ", "_")
                .replace("-", "_");
    }

    private String cardToImageKey(Card card)
    {
        // later server/game hook
        // Card.toString() already returns values like ace_of_spades
        if (card == null)
        {
            return "?";
        }

        return card.toString();
    }

    private void addCards(JPanel mainPanel)
    {
        // same scalable card system as player side
        // dealer + table hand areas
        // currently filled with dummy values
dealerHandPanel = new HandDisplayPanel();
        dealerHandPanel.setBounds(520, 100, 380, 180);
        dealerHandPanel.setHands(singleHand("back", "10_of_hearts"));

        tableHandPanel = new HandDisplayPanel();
        tableHandPanel.setBounds(520, 455, 410, 205);
        tableHandPanel.setHands(singleHand("8_of_spades", "7_of_hearts"));

        mainPanel.add(dealerHandPanel);
        mainPanel.add(tableHandPanel);
    }

    private java.util.List<java.util.List<String>> singleHand(String... cards)
    {
        java.util.List<java.util.List<String>> hands = new ArrayList<java.util.List<String>>();
        hands.add(Arrays.asList(cards));
        return hands;
    }

    private java.util.List<java.util.List<String>> splitHands(String[] firstHand, String[] secondHand)
    {
        java.util.List<java.util.List<String>> hands = new ArrayList<java.util.List<String>>();
        hands.add(Arrays.asList(firstHand));
        hands.add(Arrays.asList(secondHand));
        return hands;
    }

    private JButton buildActionButton(String text, int x, int y, int w, int h, boolean enabledStyle)
    {
        JButton button = new JButton("<html><center>" + text.replace("\n", "<br>") + "</center></html>");
        button.setBounds(x, y, w, h);
        button.setFont(new Font("Monospaced", Font.PLAIN, text.length() > 12 ? 18 : 23));
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
        // lets dealer configure table settings (timer, min/max bet)
        // values are stored locally for now
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
        // marks table as open and enables controls
        // real version should confirm with server first
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
        // placeholder for dealing a card
        // currently just updates UI for testing
        // server should control actual card distribution
        if (!tableOpen)
        {
            tableLabel.setText("Open table first");
            return;
        }

        tableLabel.setText("Deal card requested");

        // quick visual demo only
        tableHandPanel.setHands(singleHand("8_of_spades", "7_of_hearts", "2_of_clubs", "ace_of_spades"));
    }

    private void offerInsurance()
    {
        // prompts dealer to offer insurance
        // should only be valid when dealer shows an Ace
        // validation should come from server
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
        // flags the table to end after current round
        // currently just updates local state
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
        // closes table locally and disables controls
        // server should handle actual table removal + player updates
        tableOpen = false;
        endTableNextTurn = false;
        tableLabel.setText("Table Closed");
        setTableControlsEnabled(false);
        openTableButton.setEnabled(true);
    }

    private class HandDisplayPanel extends JPanel
    {
        // draws cards dynamically based on how many are in the hand
        // supports both normal hands and split hands
        // cards shrink and reposition automatically as count increases
        private java.util.List<java.util.List<String>> hands;

        public HandDisplayPanel()
        {
            hands = new ArrayList<java.util.List<String>>();
            setOpaque(false);
        }

        public void setHands(java.util.List<java.util.List<String>> hands)
        {
            // main entry point for updating what cards are shown
            // call this whenever the hand changes
            this.hands = hands;
            repaint();
        }

        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            if (hands == null || hands.isEmpty())
            {
                g2.dispose();
                return;
            }

            int handCount = hands.size();
            int rowHeight = getHeight() / handCount;

            for (int row = 0; row < handCount; row++)
            {
                drawHand(g2, hands.get(row), 0, row * rowHeight, getWidth(), rowHeight);
            }

            g2.dispose();
        }

        private void drawHand(Graphics2D g2, java.util.List<String> cards,
                int areaX, int areaY, int areaWidth, int areaHeight)
        {
            if (cards == null || cards.isEmpty())
            {
                return;
            }

            int cardCount = cards.size();
            int maxCardHeight = Math.max(70, areaHeight - 18);
            int maxCardWidth = (int) (maxCardHeight * 0.72);
            int gap = 14;
            int neededWidth = cardCount * maxCardWidth + (cardCount - 1) * gap;

            int cardWidth = maxCardWidth;
            int cardHeight = maxCardHeight;

            if (neededWidth > areaWidth)
            {
                gap = Math.max(4, areaWidth / Math.max(cardCount * 8, 1));
                cardWidth = (areaWidth - (cardCount - 1) * gap) / cardCount;
                cardWidth = Math.max(42, cardWidth);
                cardHeight = (int) (cardWidth / 0.72);
            }

            if (cardHeight > areaHeight - 10)
            {
                cardHeight = areaHeight - 10;
                cardWidth = (int) (cardHeight * 0.72);
            }

            int totalWidth = cardCount * cardWidth + (cardCount - 1) * gap;
            int startX = areaX + Math.max(0, (areaWidth - totalWidth) / 2);
            int startY = areaY + Math.max(0, (areaHeight - cardHeight) / 2);

            for (int i = 0; i < cardCount; i++)
            {
                int x = startX + i * (cardWidth + gap);
                drawCard(g2, cards.get(i), x, startY, cardWidth, cardHeight);
            }
        }


        private void drawCard(Graphics2D g2, String label, int x, int y, int width, int height)
        {
            String imageKey = normalizeCardKey(label);
            BufferedImage cardImage = cardImageMap.get(imageKey);

            if (cardImage != null)
            {
                // scales the PNG into whatever size the hand layout calculated
                g2.drawImage(cardImage, x, y, width, height, null);
                return;
            }

            // fallback card if the PNG is missing
            // this keeps the GUI usable while images are being added
            g2.setColor(CARD_WHITE);
            g2.fillRoundRect(x, y, width, height, 10, 10);

            g2.setColor(new Color(235, 235, 235));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width, height, 10, 10);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(10, width / 6)));

            String displayText = label;

            if (displayText != null && displayText.contains("_of_"))
            {
                displayText = displayText.substring(0, displayText.indexOf("_of_"));
            }

            FontMetrics metrics = g2.getFontMetrics();
            int textX = x + (width - metrics.stringWidth(displayText)) / 2;
            int textY = y + (height + metrics.getAscent()) / 2 - 4;

            g2.drawString(displayText, textX, textY);
        }

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
