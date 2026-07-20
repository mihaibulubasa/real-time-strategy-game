package view;


import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private JTextField textField;
    private JButton connectPlayer;
    private JLabel error;

    public LoginPanel(){
        setBackground(Color.darkGray);
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new Insets(10, 10, 10, 10);
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("Enter username:");
        label.setForeground(Color.WHITE);
        textField = new JTextField(15);
        connectPlayer = new JButton("Connect");
        error = new JLabel("");
        error.setForeground(Color.RED);
        error.setAlignmentX(Component.CENTER_ALIGNMENT);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        add(label, gridBagConstraints);
        gridBagConstraints.gridy = 1;
        add(textField, gridBagConstraints);
        gridBagConstraints.gridy = 2;
        add(connectPlayer, gridBagConstraints);
        gridBagConstraints.gridy = 3;
        add(error, gridBagConstraints);
    }

    public JTextField getTextField(){
        return textField;
    }

    public JButton getConnectPlayer(){
        return connectPlayer;
    }

    public void onLogInFail() {
        SwingUtilities.invokeLater(() -> {
            error.setText("Invalid username");
            revalidate();
            repaint();
        });
    }

}
