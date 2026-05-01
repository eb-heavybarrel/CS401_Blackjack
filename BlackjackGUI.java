import javax.swing.*;
import java.awt.*;

public class BlackjackGUI extends JFrame
{
    private CardLayout cardLayout;
    private JPanel mainPanel;

    //currently 3 primary screens, no dealer-specific variation of table, GUIs primarily player focused
    
    public BlackjackGUI()
    {
        setTitle("Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        
        //three primary screens for interfacing
        mainPanel.add(createLoginPanel(), "Login");
        mainPanel.add(createLobbyPanel(), "Lobby");
        mainPanel.add(createTablePanel(), "Table");

        add(mainPanel);

        setVisible(true);
    }

    
    //Login screen with fields for user & pw, button for login, create account, success or fail status
    private JPanel createLoginPanel()
    {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(50, 50, 50));

        JLabel title = new JLabel("Login");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Monospaced", Font.BOLD, 34));
        title.setBounds(70, 25, 200, 50);
        panel.add(title);

        JPanel loginBox = new JPanel(null);
        loginBox.setBackground(new Color(100, 100, 100));
        loginBox.setBounds(330, 220, 550, 260);
        panel.add(loginBox);

        JTextField usernameField = new JTextField("Username");
        usernameField.setFont(new Font("Monospaced", Font.PLAIN, 30));
        usernameField.setBounds(45, 35, 360, 55);
        loginBox.add(usernameField);

        JPasswordField passwordField = new JPasswordField("Password");
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 30));
        passwordField.setBounds(45, 105, 360, 55);
        loginBox.add(passwordField);

        JTextField ipField = new JTextField("IP");
        ipField.setFont(new Font("Monospaced", Font.PLAIN, 30));
        ipField.setBounds(45, 175, 360, 55);
        loginBox.add(ipField);

        JButton loginButton = new JButton(">>");
        loginButton.setFont(new Font("Monospaced", Font.BOLD, 28));
        loginButton.setBounds(440, 95, 80, 60);
        loginBox.add(loginButton);

        JButton createAccountButton = new JButton(">Create Account");
        createAccountButton.setFont(new Font("Monospaced", Font.PLAIN, 26));
        createAccountButton.setForeground(Color.WHITE);
        createAccountButton.setContentAreaFilled(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setBounds(420, 500, 350, 60);
        panel.add(createAccountButton);

        JLabel loginStatus = new JLabel("Login Success/Failure");
        loginStatus.setFont(new Font("Monospaced", Font.BOLD, 30));
        loginStatus.setForeground(Color.WHITE);
        loginStatus.setBounds(330, 590, 600, 50);
        panel.add(loginStatus);

        loginButton.addActionListener(e ->
        {
            // Later: validate username/password/IP with server
            cardLayout.show(mainPanel, "Lobby");
        });

        return panel;
    }

    private JPanel createLobbyPanel()
    {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(65, 65, 65));

        JPanel background = new JPanel(null);
        background.setBackground(new Color(130, 20, 15));
        background.setBounds(40, 40, 1120, 620);
        panel.add(background);

        JPanel tableListPanel = new JPanel(null);
        tableListPanel.setBackground(new Color(105, 75, 30));
        tableListPanel.setBounds(20, 20, 520, 580);
        background.add(tableListPanel);

        JLabel tablesTitle = new JLabel("Tables", SwingConstants.CENTER);
        tablesTitle.setFont(new Font("Monospaced", Font.BOLD, 34));
        tablesTitle.setForeground(new Color(210, 180, 90));
        tablesTitle.setBounds(120, 10, 280, 50);
        tableListPanel.add(tablesTitle);

        for (int i = 0; i < 7; i++)  //lobby capacity indicator
        {
            int y = 80 + (i * 65);

            JLabel statusDot = new JLabel("●", SwingConstants.CENTER);
            statusDot.setOpaque(true);
            statusDot.setBackground(Color.WHITE);
            statusDot.setForeground(i == 2 || i == 4 || i == 5 ? Color.GREEN : Color.RED);
            statusDot.setFont(new Font("Arial", Font.BOLD, 28));
            statusDot.setBounds(35, y, 40, 40);
            tableListPanel.add(statusDot);

            JButton tableButton = new JButton("Table " + (i + 1));
            tableButton.setFont(new Font("Monospaced", Font.PLAIN, 24));
            tableButton.setBounds(100, y, 150, 40);
            tableListPanel.add(tableButton);

            JLabel tableInfo = new JLabel("Turn: 45s | Bet: " + ((i + 1) * 25));
            tableInfo.setFont(new Font("Monospaced", Font.PLAIN, 16));
            tableInfo.setOpaque(true);
            tableInfo.setBackground(Color.WHITE);
            tableInfo.setBounds(270, y, 190, 40);
            tableListPanel.add(tableInfo);

            tableButton.addActionListener(e ->
            {
                // Later: send join-table request to server
                cardLayout.show(mainPanel, "Table");
            });
        }

        JButton fundsButton = new JButton("Add Funds / Cash Out");
        fundsButton.setFont(new Font("Monospaced", Font.PLAIN, 20));
        fundsButton.setBounds(570, 30, 170, 70);
        background.add(fundsButton);

        JButton rulesButton = new JButton("Rules");
        rulesButton.setFont(new Font("Monospaced", Font.PLAIN, 32));
        rulesButton.setBounds(770, 30, 160, 70);
        background.add(rulesButton);

        JButton signOutButton = new JButton("Sign Out");
        signOutButton.setFont(new Font("Monospaced", Font.PLAIN, 30));
        signOutButton.setBounds(965, 30, 170, 70);
        background.add(signOutButton);

        JTextArea displayArea = new JTextArea();
        displayArea.setText(
            "* view funds + fund actions\n\n" +
            "* rule display\n\n" +
            "* sign out confirmation"
        );
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 28));
        displayArea.setBackground(new Color(165, 150, 80));
        displayArea.setBounds(580, 130, 530, 450);
        displayArea.setEditable(false);
        background.add(displayArea);

        fundsButton.addActionListener(e ->
            displayArea.setText("Funds Menu\n\nCurrent Balance: $1000\n\nAdd Funds\nCash Out")
        );

        rulesButton.addActionListener(e ->
            displayArea.setText("Blackjack Rules\n\nHit: take another card\nStand: keep hand\nDouble Down: double bet\nSurrender: forfeit half bet")
        );

        signOutButton.addActionListener(e ->
            cardLayout.show(mainPanel, "Login")
        );

        return panel;
    }

    private JPanel createTablePanel()
    {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(45, 115, 45));

        JPanel playerList = new JPanel(null);
        playerList.setBackground(new Color(30, 80, 30));
        playerList.setBounds(35, 40, 440, 590);
        panel.add(playerList);

        for (int i = 0; i < 6; i++)   //player labels and scores on table
        {
            JLabel playerLabel = new JLabel("Player " + (i + 1) + " | Score: XXX |");
            playerLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
            playerLabel.setForeground(Color.WHITE);
            playerLabel.setOpaque(true);
            playerLabel.setBackground(new Color(105, 140, 100));
            playerLabel.setBounds(20, 25 + (i * 95), 400, 45);
            playerList.add(playerLabel);
        }

        JLabel timer = new JLabel("0:45", SwingConstants.CENTER);
        timer.setOpaque(true);
        timer.setBackground(new Color(220, 200, 50));
        timer.setFont(new Font("Monospaced", Font.BOLD, 42));
        timer.setBounds(560, 15, 250, 75);
        panel.add(timer);

        JPanel dealerCard1 = createCardPanel();
        dealerCard1.setBounds(625, 120, 120, 150);
        panel.add(dealerCard1);
        													//dealer cards
        JPanel dealerCard2 = createCardPanel();
        dealerCard2.setBounds(775, 120, 120, 150);
        panel.add(dealerCard2);

        
        
        JPanel playerCard1 = createCardPanel();
        playerCard1.setBounds(635, 490, 120, 150);
        panel.add(playerCard1);
        													//player cards
        JPanel playerCard2 = createCardPanel();
        playerCard2.setBounds(785, 490, 120, 150);
        panel.add(playerCard2);

        
        
        
        JButton hitButton = createActionButton("Hit");
        hitButton.setBounds(1030, 230, 210, 70);
        panel.add(hitButton);

        JButton standButton = createActionButton("Stand");
        standButton.setBounds(1030, 320, 210, 70);
        panel.add(standButton);

        JButton surrenderButton = createActionButton("Surrender");
        surrenderButton.setBounds(1030, 410, 210, 70);
        panel.add(surrenderButton);

        JButton doubleButton = createActionButton("Double-Down");
        doubleButton.setBounds(1030, 500, 210, 70);
        panel.add(doubleButton);

        JLabel betLabel = new JLabel("Bet", SwingConstants.CENTER);
        betLabel.setFont(new Font("Monospaced", Font.BOLD, 38));
        betLabel.setBounds(920, 570, 150, 60);
        panel.add(betLabel);

        JTextField betField = new JTextField("100");
        betField.setFont(new Font("Monospaced", Font.PLAIN, 22));
        betField.setBounds(950, 630, 130, 45);
        panel.add(betField);

        JButton allInButton = new JButton("All in");
        allInButton.setFont(new Font("Monospaced", Font.PLAIN, 16));
        allInButton.setBounds(1085, 640, 95, 40);
        panel.add(allInButton);

        JButton backButton = new JButton("Lobby");
        backButton.setBounds(20, 660, 100, 35);
        panel.add(backButton);
        															//actions
        
        
        backButton.addActionListener(e ->
            cardLayout.show(mainPanel, "Lobby")
        );

        hitButton.addActionListener(e ->
            System.out.println("Hit selected")
        );

        standButton.addActionListener(e ->
            System.out.println("Stand selected")
        );

        surrenderButton.addActionListener(e ->
            System.out.println("Surrender selected")
        );

        doubleButton.addActionListener(e ->
            System.out.println("Double-down selected")
        );

        allInButton.addActionListener(e ->
            betField.setText("ALL")
        );

        return panel;
    }

    private JPanel createCardPanel()
    {
        JPanel card = new JPanel();
        card.setBackground(new Color(245, 245, 240));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        return card;
    }

    private JButton createActionButton(String text)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 28));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(140, 25, 25));
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(BlackjackGUI::new);
    }
}