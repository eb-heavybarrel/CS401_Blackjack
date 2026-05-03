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

// player-side table view
// shows cards, actions, and betting
// scalable card rendering is already implemented
// actions are still local placeholders
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

    private Map<String, BufferedImage> cardImageMap;
    private String cardImageFolderPath = "cards";

    private HandDisplayPanel dealerHandPanel;
    private HandDisplayPanel playerHandPanel;

    private String username;
    private BlackjackLobbyGUI.TableInfo tableInfo;
    private float credits;
    private int currentBet;

    private Socket socket;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    public BlackjackPlayerTableGUI()
    {
        this("Guest", 1000.0f, new BlackjackLobbyGUI.TableInfo(1, "Table 1", 45, 100, 3), null, null, null);
    }

    public BlackjackPlayerTableGUI(String username, float credits, BlackjackLobbyGUI.TableInfo tableInfo)
    {
        this(username, credits, tableInfo, null, null, null);
    }

    public BlackjackPlayerTableGUI(String username, float credits, BlackjackLobbyGUI.TableInfo tableInfo,
            Socket socket, ObjectInputStream objIn, ObjectOutputStream objOut)
    {
        super("Blackjack Player Table");

        this.username = username;
        this.credits = credits;
        this.tableInfo = tableInfo;
        this.currentBet = 0;
        this.socket = socket;
        this.objIn = objIn;
        this.objOut = objOut;

        loadCardImages();
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
        // sets up the card display regions
        // dealer + player hands render inside scalable panels
        // cards are currently hardcoded for visual testing
dealerHandPanel = new HandDisplayPanel();
        dealerHandPanel.setBounds(520, 100, 380, 180);
        dealerHandPanel.setHands(singleHand("back", "10_of_hearts"));

        playerHandPanel = new HandDisplayPanel();
        playerHandPanel.setBounds(520, 455, 410, 205);
        playerHandPanel.setHands(singleHand("8_of_spades", "7_of_hearts"));

        mainPanel.add(dealerHandPanel);
        mainPanel.add(playerHandPanel);
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

    private JButton buildActionButton(String text, int x, int y, int w, int h, boolean enabledStyle, String action)
    {
        JButton button = new JButton("<html><center>" + text.replace("-", "-<br>") + "</center></html>");
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
        // local bet handling for now
        // deducts credits and updates labels
        // real version should go through the server
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
        // handles button presses like hit/stand/etc
        // currently just updates UI text and demo hand changes
        // later this should send actions to the server
        betLabel.setText("Action: " + action);

        // quick visual demo only: show that the scalable region can handle 5+ cards
        if (action.equals("HIT"))
        {
            playerHandPanel.setHands(singleHand("8_of_spades", "7_of_hearts", "2_of_clubs", "3_of_diamonds", "ace_of_spades"));
        }
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
                new BlackjackPlayerTableGUI().setVisible(true);
            }
        });
    }
}
