package mk.legendi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class TrackPane extends JScrollPane {
    private final boolean showTrackNumbers;
    private final String buttonText;
    private Consumer<Integer> onButtonClick = null;
    private Consumer<Integer> onTrackClick = null;
    private List<JLabel> trackLabels = Collections.emptyList();
    private JLabel lastUnderlinedLabel = null;

    public TrackPane(boolean showTrackNumbers, String buttonText) {
        this.showTrackNumbers = showTrackNumbers;
        this.buttonText = buttonText;
    }

    public void setOnButtonClick(Consumer<Integer> onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public void setOnTrackClick(Consumer<Integer> onTrackClick) {
        this.onTrackClick = onTrackClick;
    }

    public void setTracks(List<Track> tracks) {
        trackLabels = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1.0;

        for (int index = 0; index < tracks.size(); index++) {
            int finalIndex = index;
            Track track = tracks.get(index);

            String nameLabelText;
            if (showTrackNumbers) {
                nameLabelText = String.format("%02d - %s", index + 1, track.getName());
            } else {
                nameLabelText = track.getName();
            }

            c.gridy = index;

            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            JLabel nameLabel = new JLabel(nameLabelText);
            nameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (onTrackClick != null) {
                        onTrackClick.accept(finalIndex);
                    }
                }
            });
            trackLabels.add(nameLabel);
            panel.add(nameLabel, c);

            c.gridx = 1;
            c.fill = GridBagConstraints.NONE;
            c.anchor = GridBagConstraints.EAST;
            JButton button = new JButton(buttonText);
            button.addActionListener(e -> {
                if (onButtonClick != null) {
                    onButtonClick.accept(finalIndex);
                }
            });
            panel.add(button, c);
        }

        setViewportView(panel);
    }

    public void setUnderlinedLabel(int index) {
        if (index < 0 || index >= trackLabels.size()) return;

        if (lastUnderlinedLabel != null) {
            makeUnderlined(lastUnderlinedLabel, false);
        }

        JLabel label = trackLabels.get(index);
        makeUnderlined(label, true);
        lastUnderlinedLabel = label;
    }

    private void makeUnderlined(JLabel label, boolean underlined) {
        Font font = label.getFont();

        Map<TextAttribute, Object> attributes = new HashMap<>(font.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, underlined ? TextAttribute.UNDERLINE_ON : -1);

        label.setFont(font.deriveFont(attributes));
    }
}
