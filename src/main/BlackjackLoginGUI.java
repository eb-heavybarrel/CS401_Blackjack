package main;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

// login screen
// handles connecting to server + authenticating user
// sends USER_LOGIN and receives a User object back
// routes based on role (player / dealer / developer)
public class BlackjackLoginGUI extends JFrame
{
    private static final int SERVER_PORT = 2121;

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
    private JTextField createIpField;

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

    private void buildFrame()
    {
        cardLayout = new CardLayout();
        screenPanel = new JPanel(cardLayout);

        add(screenPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 590);
        setLocationRelativeTo(null);
    }

    private void buildLoginScreen()
    {
        JPanel page = buildBasePage("Login");

        JPanel centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.setOpaque(false);

        JPanel loginBox = buildFormBox(176);

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

        loginStatusLabel = makeStatusLabel("**Login Success/Failure**", 28);

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
        JPanel page = buildBasePage("Create Account");

        JPanel centerStack = new JPanel();
        centerStack.setLayout(new BoxLayout(centerStack, BoxLayout.Y_AXIS));
        centerStack.setOpaque(false);

        JPanel createBox = buildFormBox(230);

        createUsernameField = makeInputField("Username");
        createPasswordField = makePasswordField("Password");
        confirmPasswordField = makePasswordField("Confirm Password");
        createIpField = makeInputField("IP");

        JPanel fields = new JPanel(new GridLayout(4, 1, 0, 12));
        fields.setOpaque(false);
        fields.add(createUsernameField);
        fields.add(createPasswordField);
        fields.add(confirmPasswordField);
        fields.add(createIpField);

        JButton createButton = makeArrowButton();
        createButton.addActionListener(event -> createAccount());

        createBox.add(fields, BorderLayout.CENTER);
        createBox.add(createButton, BorderLayout.EAST);

        JButton backButton = makeTextLink("<Back to Login");
        backButton.addActionListener(event -> showScreen("LOGIN"));

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 14));
        backPanel.setOpaque(false);
        backPanel.add(backButton);

        createStatusLabel = makeStatusLabel("Server creates new users when username is new.", 20);

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

    private JPanel buildBasePage(String tabText)
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

    private JPanel buildFormBox(int height)
    {
        JPanel box = new JPanel(new BorderLayout(28, 0));
        box.setBackground(BOX_GRAY);
        box.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        box.setPreferredSize(new Dimension(500, height));
        box.setMaximumSize(new Dimension(500, height));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        return box;
    }

    private JTextField makeInputField(String placeholder)
    {
        JTextField field = new JTextField(placeholder);
        field.setPreferredSize(new Dimension(330, 43));
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
        field.setPreferredSize(new Dimension(330, 43));
        field.setFont(new Font("Monospaced", Font.PLAIN, 34));
        field.setForeground(PLACEHOLDER_GRAY);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        return field;
    }

    private JButton makeArrowButton()
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

    private JButton makeTextLink(String text)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.PLAIN, 28));
        button.setForeground(Color.WHITE);
        button.setBackground(PAGE_GRAY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private JLabel makeStatusLabel(String text, int fontSize)
    {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.BOLD, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private void login()
    {
        String username = loginUsernameField.getText().trim();
        String password = new String(loginPasswordField.getPassword()).trim();
        String ipAddress = loginIpField.getText().trim();

        if (!validLoginFields(username, password, ipAddress))
        {
            loginStatusLabel.setText("**Enter Username/Password/IP**");
            return;
        }

        connectAndLogin(username, password, ipAddress, loginStatusLabel);
    }

    private void createAccount()
    {
        String username = createUsernameField.getText().trim();
        String password = new String(createPasswordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String ipAddress = createIpField.getText().trim();

        if (!validCreateFields(username, password, confirmPassword, ipAddress))
        {
            createStatusLabel.setText("All fields are required.");
            return;
        }

        if (!password.equals(confirmPassword))
        {
            createStatusLabel.setText("Passwords do not match.");
            return;
        }

        connectAndLogin(username, password, ipAddress, createStatusLabel);
    }

    private boolean validLoginFields(String username, String password, String ipAddress)
    {
        return !(username.equalsIgnoreCase("Username")
                || password.equalsIgnoreCase("Password")
                || ipAddress.equalsIgnoreCase("IP")
                || ipAddress.equalsIgnoreCase("IP Address")
                || username.isEmpty()
                || password.isEmpty()
                || ipAddress.isEmpty());
    }

    private boolean validCreateFields(String username, String password, String confirmPassword, String ipAddress)
    {
        return !(username.equalsIgnoreCase("Username")
                || password.equalsIgnoreCase("Password")
                || confirmPassword.equalsIgnoreCase("Confirm Password")
                || ipAddress.equalsIgnoreCase("IP")
                || ipAddress.equalsIgnoreCase("IP Address")
                || username.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()
                || ipAddress.isEmpty());
    }

    private void connectAndLogin(String username, String password, String ipAddress, JLabel statusLabel)
    {
        // opens socket to server using entered IP
        // sends username + password as a USER_LOGIN message
        // server returns a User object on success
        // keeps socket + streams alive and passes them forward
        Socket socket = null;

        try
        {
            socket = new Socket(ipAddress, SERVER_PORT);

            ObjectOutputStream objOut = new ObjectOutputStream(socket.getOutputStream());
            objOut.flush();

            ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());

            List<Object> data = new ArrayList<Object>();
            data.add(username);
            data.add(password);

            Message loginMessage = new Message(
                    MessageClass.USER,
                    MessageType.USER_LOGIN,
                    data);

            objOut.writeObject(loginMessage);
            objOut.flush();

            Message response = (Message) objIn.readObject();

            if (response != null
                    && response.getmType() == MessageType.USER_LOGIN
                    && response.getmStatus() == MessageStatus.SUCCESS)
            {
                User user = (User) response.getmData().get(0);
                openNextScreen(user, ipAddress, socket, objIn, objOut);
            }
            else
            {
                statusLabel.setText("**Login Failed**");
                closeQuietly(socket);
            }
        }
        catch (IOException e)
        {
            statusLabel.setText("**Could Not Connect**");
            closeQuietly(socket);
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            statusLabel.setText("**Bad Server Response**");
            closeQuietly(socket);
            e.printStackTrace();
        }
    }

    private void openNextScreen(User user, String ipAddress,
            Socket socket, ObjectInputStream objIn, ObjectOutputStream objOut)
    {
        // decides where the user goes after login
        // developers get prompted to choose player or dealer
        // passes connection forward so later screens can keep using it
        UserRole role = user.getRole();

        if (role == UserRole.DEVELOPER)
        {
            String[] options =
            {
                "Player",
                "Dealer"
            };

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
                role = UserRole.DEALER;
            }
            else if (choice == 0)
            {
                role = UserRole.PLAYER;
            }
            else
            {
                loginStatusLabel.setText("**Role Choice Cancelled**");
                closeQuietly(socket);
                return;
            }
        }

        if (role == UserRole.DEALER)
        {
            BlackjackDealerTableGUI dealerTable = new BlackjackDealerTableGUI(
                    user.getUserName(),
                    ipAddress,
                    socket,
                    objIn,
                    objOut);

            dealerTable.setVisible(true);
        }
        else
        {
            BlackjackLobbyGUI lobby = new BlackjackLobbyGUI(
                    user.getUserName(),
                    UserRole.PLAYER,
                    user.getCredits(),
                    ipAddress,
                    socket,
                    objIn,
                    objOut);

            lobby.setVisible(true);
        }

        dispose();
    }

    private void closeQuietly(Socket socket)
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

    private void showScreen(String screenName)
    {
        cardLayout.show(screenPanel, screenName);
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
