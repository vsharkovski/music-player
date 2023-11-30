package mk.legendi;

import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;

public class NowPlayingPanel extends JPanel {
    private static final String PLAY_ICON = "▶";
    private static final String PAUSE_ICON = "⏸";

    public final TrackPane trackPane;
    public final JButton clearTracksButton;
    public final JButton playPauseButton;
    public final JButton previousButton;
    public final JButton nextButton;
    public final JSlider progressSlider;

    private final JLabel trackLabel;
    private final JLabel timeLabel;

    public NowPlayingPanel() {
        setLayout(new BorderLayout());

        JPanel tracksPanel = new JPanel();
        tracksPanel.setLayout(new BorderLayout());
        {
            trackPane = new TrackPane(true, "x");
            tracksPanel.add(trackPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new BorderLayout());
            {
                buttonPanel.add(Box.createHorizontalGlue(), BorderLayout.LINE_START);

                clearTracksButton = new JButton("\uD83D\uDDD1️");
                buttonPanel.add(clearTracksButton, BorderLayout.LINE_END);

                buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            }
            tracksPanel.add(buttonPanel, BorderLayout.PAGE_END);
        }
        add(tracksPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        {
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(5, 5, 5, 5);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3;
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            {
                trackLabel = new JLabel();
                trackLabel.setHorizontalAlignment(SwingConstants.LEFT);
                panel.add(trackLabel, BorderLayout.CENTER);

                timeLabel = new JLabel();
                timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                panel.add(timeLabel, BorderLayout.LINE_END);
            }
            controlPanel.add(panel, c);

            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 3;
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
        add(controlPanel, BorderLayout.SOUTH);
    }

    public void play(Track track, int index) {
        playPauseButton.setText(PAUSE_ICON);
        trackPane.setUnderlinedLabel(index);
        trackLabel.setText(track.getName());
    }

    public void pause() {
        playPauseButton.setText(PLAY_ICON);
    }

    public void setTime(Duration currentTime, Duration stopTime) {
        int currentTimeSeconds = (int) Math.round(currentTime.toSeconds());
        int stopTimeSeconds = (int) Math.round(stopTime.toSeconds());

        progressSlider.setValue(currentTimeSeconds);
        progressSlider.setMaximum(stopTimeSeconds);

        String timeText = String.format("%s / %s", formatTime(currentTimeSeconds), formatTime(stopTimeSeconds));
        timeLabel.setText(timeText);
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%d:%02d", minutes, remainingSeconds);
    }
}
