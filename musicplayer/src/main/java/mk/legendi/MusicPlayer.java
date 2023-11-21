package mk.legendi;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer extends JFrame {
    private static final int SIZE_W = 400;
    private static final int SIZE_H = 400;

    private final Library library;
    private final AudioManager audioManager;

    private final List<Path> queue = new ArrayList<>();

    private final NowPlayingPanel nowPlayingPanel;
    private final LibraryPanel libraryPanel;
    private final SettingsPanel settingsPanel;

    public MusicPlayer(Library library, AudioManager audioManager) {
        this.library = library;
        this.audioManager = audioManager;

        JPanel mainPanel = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        Dimension size = new Dimension(SIZE_W, SIZE_H);

        nowPlayingPanel = new NowPlayingPanel(this::removeFromQueue, this::startPlayback);
        nowPlayingPanel.playPauseButton.addActionListener(e -> playOrPause());
        nowPlayingPanel.previousButton.addActionListener(e -> playPrevious());
        nowPlayingPanel.nextButton.addActionListener(e -> playNext());
        nowPlayingPanel.setPreferredSize(size);
        tabbedPane.addTab("Now Playing", nowPlayingPanel);

        libraryPanel = new LibraryPanel(this::addToQueue);
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
        settingsPanel.setPreferredSize(size);
        tabbedPane.addTab("Settings", settingsPanel);

        mainPanel.add(tabbedPane);
        add(mainPanel);

        displayLibrary();
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

        List<Path> files = library.getFiles();
        libraryPanel.displayFiles(files);
    }

    private void addToQueue(Path path) {
        queue.add(path);
        nowPlayingPanel.displayQueue(queue);
    }

    private void removeFromQueue(Path path) {
        queue.remove(path);
        nowPlayingPanel.displayQueue(queue);
    }

    private void startPlayback(Path path) {
        audioManager.startPlayback(path);
    }

    private void playOrPause() {

    }

    private void playPrevious() {

    }

    private void playNext() {

    }
}
