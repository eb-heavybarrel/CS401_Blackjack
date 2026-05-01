package main;

import java.awt.*;
import java.io.*;
import javax.swing.*;

public class BlackjackLoginGUI extends JFrame
{
	// handles login + account creation flow
	// routes users based on role (player / dealer / developer)
	// uses local text file for now (no server yet)
	
    private static final String USER_FILE_NAME = "users.txt";

    private static final Color PAGE_GRAY = new Color(53, 53, 53);
    private static final Color TOP_BAR_GRAY = new Color(96, 96, 96);
    private static final Color BOX_GRAY = new Color(105, 105, 105);
    private static final Color BUTTON_GRAY = new Color(170, 170, 170);
    private static final Color PLACEHOLDER_GRAY = new Color(165, 165, 165);

    private CardLayout cardLayout;
    private JPanel screenPanel;

    private JTextField loginUsernameField;
    private JPasswordField loginPasswordField;
    private JTextField loginIpField;

    private JTextField createUsernameField;
    private JPasswordField createPasswordField;
    private JPasswordField confirmPasswordField;

    private JLabel loginStatusLabel;
    private JLabel createStatusLabel;

    public BlackjackLoginGUI()
    {
        super("Blackjack Login");

        buildFrame();
        buildLoginScreen();
        buildCreateAccountScreen();

        showScreen("LOGIN");
    }

    private void buildFrame() // sets up main window + card layout container
    {
        cardLayout = new CardLayout();
        screenPanel = new JPanel(cardLayout);

        add(screenPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
    }

    private void buildLoginScreen() 
    {
    	// builds the login UI (username / password / ip)
    	// includes login button + link to create account
        JPanel page = buildBasePage("Login");

        JPanel centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.setOpaque(false);

        JPanel loginBox = buildFormBox();

        loginUsernameField = makeInputField("Username");
        loginPasswordField = makePasswordField("Password");
        loginIpField = makeInputField("IP");

        JPanel fields = new JPanel(new GridLayout(3, 1, 0, 12));
        fields.setOpaque(false);
        fields.add(loginUsernameField);
        fields.add(loginPasswordField);
        fields.add(loginIpField);

        JButton loginButton = makeArrowButton();
        loginButton.addActionListener(event -> login());

        loginBox.add(fields, BorderLayout.CENTER);
        loginBox.add(loginButton, BorderLayout.EAST);

        JButton createButton = makeTextLink(">Create Account");
        createButton.addActionListener(event -> showScreen("CREATE"));

        JPanel createPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        createPanel.setOpaque(false);
        createPanel.add(createButton);

        loginStatusLabel = new JLabel("**Login Success/Failure**", SwingConstants.CENTER);
        loginStatusLabel.setForeground(Color.WHITE);
        loginStatusLabel.setFont(new Font("Monospaced", Font.BOLD, 28));
        loginStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerStack.add(loginBox);
        centerStack.add(createPanel);
        centerStack.add(Box.createVerticalStrut(32));
        centerStack.add(loginStatusLabel);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(PAGE_GRAY);
        body.add(centerStack);

        page.add(body, BorderLayout.CENTER);
        screenPanel.add(page, "LOGIN");
    }

    private void buildCreateAccountScreen()
    {
    	// builds account creation screen
    	// simple validation + writes new users to file
        JPanel page = buildBasePage("Create Account");

        JPanel centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.setOpaque(false);

        JPanel createBox = buildFormBox();

        createUsernameField = makeInputField("Username");
        createPasswordField = makePasswordField("Password");
        confirmPasswordField = makePasswordField("Confirm Password");

        JPanel fields = new JPanel(new GridLayout(3, 1, 0, 12));
        fields.setOpaque(false);
        fields.add(createUsernameField);
        fields.add(createPasswordField);
        fields.add(confirmPasswordField);

        JButton createButton = makeArrowButton();
        createButton.addActionListener(event -> createAccount());

        createBox.add(fields, BorderLayout.CENTER);
        createBox.add(createButton, BorderLayout.EAST);

        JButton backButton = makeTextLink("<Back to Login");
        backButton.addActionListener(event -> showScreen("LOGIN"));

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        backPanel.setOpaque(false);
        backPanel.add(backButton);

        createStatusLabel = new JLabel("New accounts start as PLAYER with 1000 credits.", SwingConstants.CENTER);
        createStatusLabel.setForeground(Color.WHITE);
        createStatusLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        createStatusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerStack.add(createBox);
        centerStack.add(backPanel);
        centerStack.add(Box.createVerticalStrut(30));
        centerStack.add(createStatusLabel);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(PAGE_GRAY);
        body.add(centerStack);

        page.add(body, BorderLayout.CENTER);
        screenPanel.add(page, "CREATE");
    }

    private JPanel buildBasePage(String tabText) // shared layout (top bar + background)
    {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(PAGE_GRAY);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(TOP_BAR_GRAY);
        topBar.setPreferredSize(new Dimension(0, 44));

        JLabel tab = new JLabel(tabText, SwingConstants.CENTER);
        tab.setOpaque(true);
        tab.setBackground(TOP_BAR_GRAY);
        tab.setForeground(Color.WHITE);
        tab.setFont(new Font("Monospaced", Font.PLAIN, 30));
        tab.setPreferredSize(new Dimension(tabText.equals("Login") ? 165 : 300, 44));

        JPanel tabHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 55, 0));
        tabHolder.setOpaque(false);
        tabHolder.add(tab);

        topBar.add(tabHolder, BorderLayout.WEST);
        page.add(topBar, BorderLayout.NORTH);

        return page;
    }

    private JPanel buildFormBox() // centered box that holds input fields + button
    {
        JPanel box = new JPanel(new BorderLayout(28, 0));
        box.setBackground(BOX_GRAY);
        box.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        box.setPreferredSize(new Dimension(470, 176));
        box.setMaximumSize(new Dimension(470, 176));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        return box;
    }

 // creates styled input fields with placeholder text
    
    private JTextField makeInputField(String placeholder)
    {
        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(310, 43));
        field.setFont(new Font("Monospaced", Font.PLAIN, 34));
        field.setForeground(PLACEHOLDER_GRAY);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        return field;
    }

    private JPasswordField makePasswordField(String placeholder)
    {
        JPasswordField field = new JPasswordField(placeholder);
        field.setEchoChar((char) 0);
        field.setPreferredSize(new Dimension(310, 43));
        field.setFont(new Font("Monospaced", Font.PLAIN, 34));
        field.setForeground(PLACEHOLDER_GRAY);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        return field;
    }

    private JButton makeArrowButton() // small submit button (>>)
    {
        JButton button = new JButton(">>");
        button.setFont(new Font("Monospaced", Font.BOLD, 28));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_GRAY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(74, 44));
        return button;
    }

    private JButton makeTextLink(String text) // clickable text button (used for navigation)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.PLAIN, 28));
        button.setForeground(Color.WHITE);
        button.setBackground(PAGE_GRAY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    
 // validates login inputs
 // checks user file
 // routes user to next screen
    private void login()
    {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();
        String ipAddress = loginIpField.getText().trim();

        if (username.equalsIgnoreCase("Username") || password.equalsIgnoreCase("Password")
                || ipAddress.equalsIgnoreCase("IP") || ipAddress.equalsIgnoreCase("IP Address")
                || username.isEmpty() || password.isEmpty() || ipAddress.isEmpty())
        {
            loginStatusLabel.setText("**Enter Username/Password/IP**");
            return;
        }

        UserRecord record = findUser(username);

        if (record == null)
        {
            loginStatusLabel.setText("**No Account Found**");
            return;
        }

        if (!record.password.equals(password))
        {
            loginStatusLabel.setText("**Incorrect Password**");
            return;
        }

        openNextScreen(record, ipAddress);
    }

    
 // handles role routing:
 // player → lobby
 // dealer → table
 // developer → choose role
    private void openNextScreen(UserRecord record, String ipAddress)
    {
        String role = record.role;

        if (role.equalsIgnoreCase("DEVELOPER"))
        {
            String[] options = {"Player", "Dealer"};

            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Developer login detected.\nChoose which interface to launch.",
                    "Developer Role",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 1)
            {
                role = "DEALER";
            }
            else if (choice == 0)
            {
                role = "PLAYER";
            }
            else
            {
                loginStatusLabel.setText("**Role Choice Cancelled**");
                return;
            }
        }

        if (role.equalsIgnoreCase("DEALER"))
        {
            BlackjackDealerTableGUI dealerTable = new BlackjackDealerTableGUI(record.username, ipAddress);
            dealerTable.setVisible(true);
        }
        else
        {
            BlackjackLobbyGUI lobby = new BlackjackLobbyGUI(record.username, UserRole.PLAYER, record.credits, ipAddress);
            lobby.setVisible(true);
        }

        dispose();
    }

    
 // validates new account
 // writes to users.txt
    private void createAccount()
    {
        String username = createUsernameField.getText().trim();
        String password = new String(createPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();

        if (username.equalsIgnoreCase("Username") || password.equalsIgnoreCase("Password")
                || confirmPassword.equalsIgnoreCase("Confirm Password")
                || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
        {
            createStatusLabel.setText("All fields are required.");
            return;
        }

        if (username.contains(",") || password.contains(","))
        {
            createStatusLabel.setText("Commas are not allowed.");
            return;
        }

        if (!password.equals(confirmPassword))
        {
            createStatusLabel.setText("Passwords do not match.");
            return;
        }

        if (findUser(username) != null)
        {
            createStatusLabel.setText("That username already exists.");
            return;
        }

        try
        {
            writeUser(username, password, "PLAYER", 1000.0f);
            createStatusLabel.setText("Account created. Log in when ready.");
            showScreen("LOGIN");
            loginStatusLabel.setText("**Account Created**");
        }
        catch (IOException e)
        {
            createStatusLabel.setText("Could not save account.");
        }
    }

    private UserRecord findUser(String username) // looks up user in file
    {
        File userFile = new File(USER_FILE_NAME);

        if (!userFile.exists())
        {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(userFile)))
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(",");

                if (parts.length >= 4 && parts[0].equalsIgnoreCase(username))
                {
                    return new UserRecord(parts[0], parts[1], parts[2], Float.parseFloat(parts[3]));
                }
            }
        }
        catch (IOException | NumberFormatException e)
        {
            loginStatusLabel.setText("**Could Not Read User File**");
        }

        return null;
    }

 // appends new user to file
    private void writeUser(String username, String password, String role, float credits) throws IOException
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE_NAME, true)))
        {
            writer.write(username + "," + password + "," + role + "," + credits);
            writer.newLine();
        }
    }

    private void showScreen(String screenName)
    {
        cardLayout.show(screenPanel, screenName);
    }

    private static class UserRecord
    {
        private String username;
        private String password;
        private String role;
        private float credits;

        public UserRecord(String username, String password, String role, float credits)
        {
            this.username = username;
            this.password = password;
            this.role = role;
            this.credits = credits;
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new BlackjackLoginGUI().setVisible(true);
            }
        });
    }
}
