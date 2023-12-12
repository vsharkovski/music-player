package mk.legendi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends JFrame {
    private static final int SIZE_W = 400;
    private static final int SIZE_H = 400;

    private final Library library;
    private final AudioManager audioManager;

    private final List<Track> queue = new ArrayList<>();

    private int lastPlayedIndex = -1;
    private Track lastPlayedTrack = null;

    private final NowPlayingPanel nowPlayingPanel;
    private final LibraryPanel libraryPanel;
    public final SettingsPanel settingsPanel;

    public MusicPlayer(Library library, AudioManager audioManager) {
        this.library = library;
        this.audioManager = audioManager;

        JPanel mainPanel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        Dimension size = new Dimension(SIZE_W, SIZE_H);

        nowPlayingPanel = new NowPlayingPanel();
        nowPlayingPanel.trackPane.setOnTrackClick(this::startPlayback);
        nowPlayingPanel.trackPane.setOnButtonClick(this::removeFromQueue);
        nowPlayingPanel.clearTracksButton.addActionListener(e -> clearQueue());
        nowPlayingPanel.playPauseButton.addActionListener(e -> playOrPause());
        nowPlayingPanel.previousButton.addActionListener(e -> playPrevious());
        nowPlayingPanel.nextButton.addActionListener(e -> playNext());
        nowPlayingPanel.progressSlider.addChangeListener(e -> {
            if (!nowPlayingPanel.progressSlider.getValueIsAdjusting()) return;
            double value = nowPlayingPanel.progressSlider.getValue();
            double max = nowPlayingPanel.progressSlider.getMaximum();
            setPlayTime(value / max);
        });
        nowPlayingPanel.setPreferredSize(size);
        tabbedPane.addTab("Now Playing", nowPlayingPanel);

        libraryPanel = new LibraryPanel();
        libraryPanel.trackPane.setOnButtonClick(this::addToQueue);
        libraryPanel.setPreferredSize(size);
        tabbedPane.addTab("Library", libraryPanel);

        settingsPanel = new SettingsPanel();
        settingsPanel.chooseLibraryButton.addActionListener(e -> {
            Path path = chooseLibraryPath();
            if (path != null) {
                this.library.setPath(path);
                displayLibrary();
            }
        });
        settingsPanel.clearLibraryButton.addActionListener(e -> {
            this.library.removePath();
            displayLibrary();
        });
        /*
        settingsPanel.logoutButton.addActionListener(e -> {
            playOrPause();
            this.dispose();
            LogIn logIn = new LogIn();
            logIn.frmLogin.setVisible(true);
        });*/


        settingsPanel.setPreferredSize(size);
        tabbedPane.addTab("Settings", settingsPanel);

        mainPanel.add(tabbedPane);
        add(mainPanel);

        displayLibrary();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener Exit = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //JFrame.dispose();
                LogIn login = new LogIn();
                login.frmLogin.setVisible(true);
                dispose();
                //super.windowClosing(e);
            }
        };
        this.addWindowListener(Exit);
    }

    private Path chooseLibraryPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File directory = fileChooser.getSelectedFile();
            return Path.of(directory.getAbsolutePath());
        } else {
            return null;
        }
    }

    private void displayLibrary() {
        Path path = library.getPath();
        if (path == null) {
            settingsPanel.libraryLocationField.setText("");
        } else {
            settingsPanel.libraryLocationField.setText(path.toString());
        }

        List<Track> tracks = library.getTracks();
        libraryPanel.trackPane.setTracks(tracks);
    }

    private void addToQueue(int index) {
        Track track = library.getTracks().get(index);
        queue.add(track);
        nowPlayingPanel.trackPane.setTracks(queue);
        updatePlayStatus();
    }

    private void removeFromQueue(int index) {
        queue.remove(index);
        nowPlayingPanel.trackPane.setTracks(queue);

        if (lastPlayedIndex != -1) {
            if (lastPlayedIndex == index) {
                lastPlayedIndex = -1;
            } else if (lastPlayedIndex > index) {
                lastPlayedIndex--;
            }
        }

        updatePlayStatus();
    }

    private void clearQueue() {
        queue.clear();
        nowPlayingPanel.trackPane.setTracks(queue);
        lastPlayedIndex = -1;
        updatePlayStatus();
    }

    private void startPlayback(int index) {
        if (index < 0 || index >= queue.size()) return;

        Track track = queue.get(index);
        lastPlayedIndex = index;
        lastPlayedTrack = track;
        audioManager.startPlayback(track.getPath(), nowPlayingPanel::setTime, this::updatePlayStatus);
        updatePlayStatus();
    }

    public void playOrPause() {
        audioManager.playOrPause();
        updatePlayStatus();
    }

    private void playPrevious() {
        if (lastPlayedTrack == null || lastPlayedIndex == 0) return;
        startPlayback(lastPlayedIndex - 1);
    }

    private void playNext() {
        if (lastPlayedTrack == null || lastPlayedIndex == queue.size() - 1) return;
        startPlayback(lastPlayedIndex + 1);
    }

    private void setPlayTime(double fraction) {
        audioManager.seek(fraction);
        updatePlayStatus();
    }

    private void updatePlayStatus() {
        switch (audioManager.getStatus()) {
            case PLAYING:
                if (lastPlayedTrack != null) {
                    nowPlayingPanel.play(lastPlayedTrack, lastPlayedIndex);
                }
                break;
            case PAUSED:
            case NONE:
            case ERROR:
                nowPlayingPanel.pause();
                break;
            case ENDED:
                if (lastPlayedIndex < queue.size() - 1) {
                    playNext();
                } else {
                    nowPlayingPanel.pause();
                }
                break;
        }
    }
}
