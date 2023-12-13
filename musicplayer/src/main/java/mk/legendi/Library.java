package mk.legendi;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library implements Serializable {
    @Serial
    private static final long serialVersionUID = -6470090944414208497L;

    private final Database database;

    private String pathKey = "library-path";

    private Path path = null;

    private List<Track> tracks = Collections.emptyList();

    public Library(Database database) {
        this.database = database;

        String pathString = database.get(pathKey);
        if (pathString != null) {
            this.path = Path.of(pathString);
            this.tracks = scanPath();
        }
    }

    public Library(Database database, String path) {
        this.database = database;
        pathKey = path;

        String pathString = database.get(pathKey);
        if (pathString != null) {
            this.path = Path.of(pathString);
            this.tracks = scanPath();
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path newPath) {
        database.put(pathKey, newPath.toString());
        path = newPath;
        tracks = scanPath();

        System.out.println("Files after scan:");
        for (Track track : tracks) {
            System.out.println("  " + track.getPath().toAbsolutePath());
        }
    }

    public void removePath() {
        database.put(pathKey, "");
        path = null;
        tracks = Collections.emptyList();
    }

    public List<Track> getTracks() {
        return tracks;
    }

    private List<Track> scanPath() {
        if (path == null) return Collections.emptyList();

        List<Track> foundTracks = new ArrayList<>();

        SimpleFileVisitor<Path> fileVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                boolean isSupported = AudioManager.SUPPORTED_FORMATS.stream().anyMatch(
                        format -> file.getFileName().toString().endsWith("." + format));
                if (isSupported) {
                    foundTracks.add(new Track(file));
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path path, IOException exception) {
                return FileVisitResult.CONTINUE;
            }
        };

        System.out.println("Scanning library: " + path.toAbsolutePath());

        try {
            Files.walkFileTree(path, fileVisitor);
        } catch (IOException exception) {
            System.out.println("Scan failed");
            exception.printStackTrace();
        }

        return foundTracks;
    }

}
