package mk.legendi;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Library {

    private final String pathKey = "library-path";

    private final Database database;

    private Path path = null;

    private List<Path> files = Collections.emptyList();

    public Library(Database database) {
        this.database = database;

        String pathString = database.get(pathKey);
        if (pathString != null) {
            this.path = Path.of(pathString);
            this.files = scanPath();
        }
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path newPath) {
        database.put(pathKey, newPath.toString());
        path = newPath;
        files = scanPath();

        System.out.println("Files after scan:");
        for (Path file : files) {
            System.out.println("  " + file.toAbsolutePath());
        }
    }

    public void removePath() {
        database.put(pathKey, "");
        path = null;
        files = Collections.emptyList();
    }

    public List<Path> getFiles() {
        return files;
    }

    private List<Path> scanPath() {
        if (path == null) return Collections.emptyList();

        List<Path> foundFiles = new ArrayList<>();

        SimpleFileVisitor<Path> fileVisitor = new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                boolean isSupported = AudioManager.SUPPORTED_FORMATS.stream().anyMatch(
                        format -> file.getFileName().toString().endsWith("." + format));
                if (isSupported) {
                    foundFiles.add(file);
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

        return foundFiles;
    }

}
