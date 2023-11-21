package mk.legendi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

public class NowPlayingPanel extends JPanel {
    public final JScrollPane queueScrollPane;
    public final JLabel trackLabel;
    public final JSlider progressSlider;
    public final JButton playPauseButton;
    public final JButton previousButton;
    public final JButton nextButton;

    private final Consumer<Path> onRemove;
    private final Consumer<Path> onSelect;

    public NowPlayingPanel(Consumer<Path> onRemove, Consumer<Path> onSelect) {
        this.onRemove = onRemove;
        this.onSelect = onSelect;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        queueScrollPane = new JScrollPane();
        add(queueScrollPane);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        {
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
            c.fill = GridBagConstraints.HORIZONTAL;

            c.gridx = 0;
            c.gridwidth = 3;

            c.gridy = 0;
            trackLabel = new JLabel();
            controlPanel.add(trackLabel, c);

            c.gridy = 1;
            progressSlider = new JSlider();
            controlPanel.add(progressSlider, c);

            c.gridy = 2;
            c.gridwidth = 1;

            c.gridx = 0;
            previousButton = new JButton("<<");
            controlPanel.add(previousButton, c);

            c.gridx = 1;
            playPauseButton = new JButton("▶");
            controlPanel.add(playPauseButton, c);

            c.gridx = 2;
            nextButton = new JButton(">>");
            controlPanel.add(nextButton, c);
        }
        add(controlPanel);
    }

    public void displayQueue(List<Path> queue) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);

        for (int index = 0; index < queue.size(); index++) {
            Path file = queue.get(index);
            String nameLabelText = String.format("%02d - %s", index, file.getFileName().toString());
            c.gridy = index;

            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            JLabel nameLabel = new JLabel(nameLabelText);
            nameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onSelect.accept(file);
                }
            });
            panel.add(nameLabel, c);

            c.gridx = 1;
            c.fill = GridBagConstraints.NONE;
            JButton removeButton = new JButton("x");
            removeButton.addActionListener(e -> onRemove.accept(file));
            panel.add(removeButton, c);
        }

        queueScrollPane.setViewportView(panel);
    }
}
