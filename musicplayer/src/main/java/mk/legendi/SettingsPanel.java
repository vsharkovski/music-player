package mk.legendi;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {
    public final JTextField libraryLocationField;
    public final JButton chooseLibraryButton;
    public final JButton clearLibraryButton;

    public SettingsPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        add(new JLabel("Music Library Location"), c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        libraryLocationField = new JTextField();
        libraryLocationField.setEditable(false);
        add(libraryLocationField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        chooseLibraryButton = new JButton("Choose new...");
        add(chooseLibraryButton, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        clearLibraryButton = new JButton("Clear");
        add(clearLibraryButton, c);
    }
}