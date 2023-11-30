package mk.legendi;

import javax.swing.*;

public class LibraryPanel extends JPanel {
    public final TrackPane trackPane;

    public LibraryPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        trackPane = new TrackPane(false, "+");
        add(trackPane);
    }
}
