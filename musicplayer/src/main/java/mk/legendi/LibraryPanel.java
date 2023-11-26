package mk.legendi;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class LibraryPanel extends JPanel {
    public final JScrollPane libraryScrollPane;
    private final Consumer<Path> onAdd;

    public LibraryPanel(Consumer<Path> onAdd) {
        this.onAdd = onAdd;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        libraryScrollPane = new JScrollPane();
        add(libraryScrollPane);
    }

    public void displayFiles(List<Path> files) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        for (int index = 0; index < files.size(); index++) {
            Path file = files.get(index);
            String trackLabelText = file.getFileName().toString();
            c.gridy = index;

            c.gridx = 0;
            c.weightx = 9;
            c.fill = GridBagConstraints.HORIZONTAL;
            JLabel trackLabel = new JLabel(trackLabelText);
            panel.add(trackLabel, c);

            c.gridx = 1;
            c.weightx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            JButton addButton = new JButton("+");
            addButton.addActionListener(e -> onAdd.accept(file));
            panel.add(addButton, c);
        }

        libraryScrollPane.setViewportView(panel);
    }
}
