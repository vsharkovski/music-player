package mk.legendi;

import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class NowPlayingPanel extends JPanel {
    private static final String PLAY_ICON = "▶";
    private static final String PAUSE_ICON = "⏸";

    public final JScrollPane queueScrollPane;
    public final JLabel trackLabel;
    public final JSlider progressSlider;
    public final JButton playPauseButton;
    public final JButton previousButton;
    public final JButton nextButton;

    private final Consumer<Integer> onRemove;
    private final Consumer<Integer> onSelect;

    private List<JLabel> lastQueueLabels = Collections.emptyList();
    private JLabel lastUnderlinedLabel = null;

    public NowPlayingPanel(Consumer<Integer> onRemove, Consumer<Integer> onSelect) {
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
            c.weightx = 1;

            c.gridx = 0;
            c.gridwidth = 3;

            c.gridy = 0;
            trackLabel = new JLabel();
            controlPanel.add(trackLabel, c);

            c.gridy = 1;
            progressSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
            controlPanel.add(progressSlider, c);

            c.gridy = 2;
            c.gridwidth = 1;

            c.gridx = 0;
            previousButton = new JButton("<<");
            controlPanel.add(previousButton, c);

            c.gridx = 1;
            playPauseButton = new JButton(PLAY_ICON);
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

        lastQueueLabels = new ArrayList<>();

        for (int index = 0; index < queue.size(); index++) {
            int finalIndex = index;
            Path file = queue.get(index);
            String nameLabelText = String.format("%02d - %s", index + 1, file.getFileName().toString());
            c.gridy = index;

            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            JLabel nameLabel = new JLabel(nameLabelText);
            nameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onSelect.accept(finalIndex);
                }
            });
            lastQueueLabels.add(nameLabel);
            panel.add(nameLabel, c);

            c.gridx = 1;
            c.fill = GridBagConstraints.NONE;
            JButton removeButton = new JButton("x");
            removeButton.addActionListener(e -> onRemove.accept(finalIndex));
            panel.add(removeButton, c);
        }

        queueScrollPane.setViewportView(panel);
    }

    public void play(int index) {
        playPauseButton.setText(PAUSE_ICON);

        if (lastUnderlinedLabel != null) {
            setUnderlined(lastUnderlinedLabel, false);
        }

        if (index >= 0 && index < lastQueueLabels.size()) {
            JLabel label = lastQueueLabels.get(index);
            setUnderlined(label, true);
            lastUnderlinedLabel = label;
            trackLabel.setText(label.getText());
        }
    }

    public void pause() {
        playPauseButton.setText(PLAY_ICON);
    }

    public void setTime(Duration currentTime, Duration stopTime) {
        progressSlider.setMaximum((int) Math.round(stopTime.toSeconds()));
        progressSlider.setValue((int) Math.round(currentTime.toSeconds()));
    }

    private void setUnderlined(JLabel label, boolean underlined) {
        Font font = label.getFont();

        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, underlined ? TextAttribute.UNDERLINE_ON : -1);

        label.setFont(font.deriveFont(attributes));
    }
}
