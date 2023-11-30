package mk.legendi;

import java.nio.file.Path;

public class Track {
    private final Path path;
    private final String name;

    public Track(Path path) {
        this.path = path;

        String filename = path.getFileName().toString();
        int extensionStartIndex = filename.lastIndexOf(".");
        if (extensionStartIndex != -1) {
            filename = filename.substring(0, extensionStartIndex);
        }
        this.name = filename;
    }

    public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }
}
