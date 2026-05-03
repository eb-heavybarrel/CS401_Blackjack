package main;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

// player lobby
// shows available tables and basic account info
// currently uses placeholder table data
// connection is stored here for when table messaging gets added
public class BlackjackLobbyGUI extends JFrame
{
    private static final Color OUTER_GRAY = new Color(68, 68, 68);
    private static final Color DARK_RED = new Color(132, 18, 13);
    private static final Color BROWN = new Color(111, 80, 34);
    private static final Color GOLD_TEXT = new Color(198, 174, 83);
    private static final Color PANEL_GOLD = new Color(166, 151, 82);
    private static final Color BUTTON_GOLD = new Color(198, 174, 86);
    private static final Color GREEN_DOT = new Color(62, 222, 62);
    private static final Color RED_DOT = new Color(250, 0, 0);

    private JLabel userLabel;
    private JLabel ipLabel;
    private JLabel creditsLabel;
    private JLabel statusLabel;
    private JTextArea infoArea;

    private DefaultListModel<TableInfo> tableListModel;
    private JList<TableInfo> tableList;

    private String username;
    private UserRole role;
    private float credits;
    private String ipAddress;

    private Socket socket;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;

    public BlackjackLobbyGUI()
    {
        this("Guest", UserRole.PLAYER, 1000.0f, "localhost", null, null, null);
    }

    public BlackjackLobbyGUI(String username, UserRole role, float credits)
    {
        this(username, role, credits, "localhost", null, null, null);
    }

    public BlackjackLobbyGUI(String username, UserRole role, float credits, String ipAddress)
    {
        this(username, role, credits, ipAddress, null, null, null);
    }

    public BlackjackLobbyGUI(String username, UserRole role, float credits, String ipAddress,
            Socket socket, ObjectInputStream objIn, ObjectOutputStream objOut)
    {
        super("Blackjack Lobby");

        this.username = username;
        this.role = role;
        this.credits = credits;
        this.ipAddress = ipAddress;
        this.socket = socket;
        this.objIn = objIn;
        this.objOut = objOut;

        buildFrame();
        loadPlaceholderTables();
        configureForPlayer();
    }

    private void buildFrame()
    {
        JPanel outerPanel = new JPanel(new GridBagLayout());
        outerPanel.setBackground(OUTER_GRAY);

        JPanel redPanel = new JPanel(new BorderLayout(18, 0));
        redPanel.setBackground(DARK_RED);
        redPanel.setBorder(BorderFactory.createEmptyBorder(12, 14, 24, 14));
        redPanel.setPreferredSize(new Dimension(1030, 610));

        redPanel.add(buildTablePanel(), BorderLayout.WEST);
        redPanel.add(buildRightPanel(), BorderLayout.CENTER);

        outerPanel.add(redPanel);
        add(outerPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1220, 700);
        setLocationRelativeTo(null);
    }

    private JPanel buildTablePanel()
    {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(BROWN);
        panel.setBorder(BorderFactory.createEmptyBorder(4, 26, 22, 26));
        panel.setPreferredSize(new Dimension(462, 0));

        JLabel titleLabel = new JLabel("Tables", SwingConstants.CENTER);
        titleLabel.setForeground(GOLD_TEXT);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 38));
        titleLabel.setPreferredSize(new Dimension(0, 54));

        tableListModel = new DefaultListModel<TableInfo>();
        tableList = new JList<TableInfo>(tableListModel);
        tableList.setCellRenderer(new TableCellRenderer());
        tableList.setFixedCellHeight(56);
        tableList.setBackground(BROWN);
        tableList.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BROWN);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildRightPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(0, 24));
        panel.setBackground(DARK_RED);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 20));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 32, 0));
        buttonPanel.setBackground(DARK_RED);
        buttonPanel.setPreferredSize(new Dimension(0, 76));

        JButton fundsButton = makeTopButton("Add Funds/\nCash Out", 24);
        fundsButton.addActionListener(event -> addFunds());

        JButton rulesButton = makeTopButton("Rules", 34);
        rulesButton.addActionListener(event -> showRules());

        JButton signOutButton = makeTopButton("Sign Out", 32);
        signOutButton.addActionListener(event -> confirmSignOut());

        buttonPanel.add(fundsButton);
        buttonPanel.add(rulesButton);
        buttonPanel.add(signOutButton);

        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setBackground(PANEL_GOLD);
        infoArea.setForeground(Color.BLACK);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 30));
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setBorder(BorderFactory.createEmptyBorder(62, 28, 24, 24));
        infoArea.setText("*view funds + fund actions\n\n*rule display\n\n*sign out confirmation");

        JPanel statusPanel = buildStatusPanel();

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(infoArea, BorderLayout.CENTER);
        panel.add(statusPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildStatusPanel()
    {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setBackground(DARK_RED);
        panel.setPreferredSize(new Dimension(0, 42));

        JPanel labels = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        labels.setBackground(DARK_RED);

        userLabel = makeStatusLabel("User: " + username);
        ipLabel = makeStatusLabel("IP: " + ipAddress);
        creditsLabel = makeStatusLabel("Credits: " + credits);
        statusLabel = makeStatusLabel("Select an OPEN table.");

        labels.add(userLabel);
        labels.add(ipLabel);
        labels.add(creditsLabel);
        labels.add(statusLabel);

        JButton joinButton = makeSmallGoldButton("Join Table");
        joinButton.addActionListener(event -> joinSelectedTable());

        panel.add(labels, BorderLayout.CENTER);
        panel.add(joinButton, BorderLayout.EAST);

        return panel;
    }

    private JLabel makeStatusLabel(String text)
    {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return label;
    }

    private JButton makeTopButton(String text, int fontSize)
    {
        JButton button = new JButton("<html><center>" + text.replace("\n", "<br>") + "</center></html>");
        button.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        button.setBackground(BUTTON_GOLD);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private JButton makeSmallGoldButton(String text)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 18));
        button.setBackground(BUTTON_GOLD);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 38));
        return button;
    }

    private void loadPlaceholderTables()
    {
        // fake table list so the UI has something to render
        // this should eventually come from the server
tableListModel.clear();
        tableListModel.addElement(new TableInfo(1, "Table 1", 45, 100, 6));
        tableListModel.addElement(new TableInfo(2, "Table 2", 45, 25, 6));
        tableListModel.addElement(new TableInfo(3, "Table 3", 15, 10, 3));
        tableListModel.addElement(new TableInfo(4, "Table 4", 45, 650, 6));
        tableListModel.addElement(new TableInfo(5, "Table 5", 25, 850, 2));
        tableListModel.addElement(new TableInfo(6, "Table 6", 30, 200, 5));
        tableListModel.addElement(new TableInfo(7, "Table 7", 20, 50, 6));
    }

    private void configureForPlayer()
    {
        if (role != UserRole.PLAYER)
        {
            statusLabel.setText("Dealers skip the lobby.");
        }
    }

    private void joinSelectedTable()
    {
        TableInfo selectedTable = tableList.getSelectedValue();

        if (selectedTable == null)
        {
            statusLabel.setText("Select a table first.");
            return;
        }

        if (!selectedTable.isAvailable())
        {
            statusLabel.setText(selectedTable.name + " is FULL.");
            return;
        }
        // checks selected table and blocks if FULL
        // right now just opens the player table locally
        // later this should ask the server before allowing entry
BlackjackPlayerTableGUI playerTable = new BlackjackPlayerTableGUI(
                username,
                credits,
                selectedTable,
                socket,
                objIn,
                objOut);

        playerTable.setVisible(true);
        dispose();
    }

    private void showRules()
    {
        infoArea.setText(
                "Rules\n\n"
                + "- Try to get closer to 21 than the dealer.\n\n"
                + "- Going over 21 is a bust.\n\n"
                + "- Face cards are worth 10.\n\n"
                + "- Aces can count as 1 or 11.\n\n"
                + "- Actions: hit, stand, double down, split, surrender, insurance.");
    }

    private void addFunds()
    {
        // temporary local credit change
        // server-side credit handling isn’t wired yet
String amountText = JOptionPane.showInputDialog(this, "Amount to add:");

        if (amountText == null)
        {
            return;
        }

        try
        {
            float amount = Float.parseFloat(amountText);

            if (amount <= 0)
            {
                statusLabel.setText("Amount must be greater than 0.");
                return;
            }

            credits += amount;
            creditsLabel.setText("Credits: " + credits);
            infoArea.setText("Funds updated locally.\n\nCurrent credits: " + credits);
        }
        catch (NumberFormatException e)
        {
            statusLabel.setText("Enter a valid number.");
        }
    }

    private void confirmSignOut()
    {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Sign out and return to login?",
                "Sign Out",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION)
        {
            closeQuietly();
            BlackjackLoginGUI login = new BlackjackLoginGUI();
            login.setVisible(true);
            dispose();
        }
    }

    private void closeQuietly()
    {
        if (socket == null)
        {
            return;
        }

        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static class TableInfo
    {
        // lightweight table representation for the lobby
        // holds basic info like player count and bet amount
        // will likely be replaced or synced with server-side table data
        public int tableId;
        public String name;
        public int turnSeconds;
        public int bet;
        public int playerCount;

        public TableInfo(int tableId, String name, int turnSeconds, int bet, int playerCount)
        {
            this.tableId = tableId;
            this.name = name;
            this.turnSeconds = turnSeconds;
            this.bet = bet;
            this.playerCount = playerCount;
        }

        public boolean isAvailable()
        {
            // OPEN means fewer than 6 players; FULL means 6/6
            return playerCount < 6;
        }

        public String getStatusText()
        {
            return isAvailable() ? "OPEN" : "FULL";
        }

        public String toString()
        {
            return name + " | " + getStatusText() + " | Turn: " + turnSeconds
                    + "s | Bet: " + bet + " | Players: " + playerCount + "/6";
        }
    }

    private class TableCellRenderer extends JPanel implements ListCellRenderer<TableInfo>
    {
        // controls how table rows look
        // FULL tables get greyed out here
        private JLabel dotLabel;
        private JLabel nameLabel;
        private JLabel infoLabel;

        public TableCellRenderer()
        {
            setLayout(null);
            setBackground(BROWN);

            dotLabel = new JLabel("●", SwingConstants.CENTER);
            dotLabel.setOpaque(true);
            dotLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

            nameLabel = new JLabel("", SwingConstants.LEFT);
            nameLabel.setOpaque(true);
            nameLabel.setFont(new Font("Monospaced", Font.PLAIN, 26));
            nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

            infoLabel = new JLabel("", SwingConstants.LEFT);
            infoLabel.setOpaque(true);
            infoLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
            infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

            add(dotLabel);
            add(nameLabel);
            add(infoLabel);
        }

        public Component getListCellRendererComponent(
                JList<? extends TableInfo> list,
                TableInfo value,
                int index,
                boolean isSelected,
                boolean cellHasFocus)
        {
            setPreferredSize(new Dimension(420, 56));
            dotLabel.setBounds(0, 8, 34, 34);
            nameLabel.setBounds(58, 8, 136, 34);
            infoLabel.setBounds(210, 8, 176, 34);

            boolean available = value.isAvailable();

            dotLabel.setText("●");
            dotLabel.setForeground(available ? GREEN_DOT : RED_DOT);
            nameLabel.setText(value.name);
            infoLabel.setText(value.getStatusText() + " | " + value.playerCount + "/6");

            if (available)
            {
                dotLabel.setBackground(Color.WHITE);
                nameLabel.setBackground(Color.WHITE);
                infoLabel.setBackground(Color.WHITE);
                nameLabel.setForeground(Color.BLACK);
                infoLabel.setForeground(Color.BLACK);
            }
            else
            {
                dotLabel.setBackground(new Color(190, 190, 190));
                nameLabel.setBackground(new Color(190, 190, 190));
                infoLabel.setBackground(new Color(190, 190, 190));
                nameLabel.setForeground(new Color(95, 95, 95));
                infoLabel.setForeground(new Color(95, 95, 95));
            }

            if (isSelected && available)
            {
                setBackground(new Color(145, 105, 45));
            }
            else if (isSelected)
            {
                setBackground(new Color(120, 120, 120));
            }
            else
            {
                setBackground(BROWN);
            }

            return this;
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new BlackjackLobbyGUI("PlayerTest", UserRole.PLAYER, 1000.0f, "localhost").setVisible(true);
            }
        });
    }
}
