package mk.legendi;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private Map<String, String> store;

    public Database() {
        store = new HashMap<>();
    }

    public String get(String key) {
        if (!store.containsKey(key)) return null;
        return store.get(key);
    }

    public void put(String key, String value) {
        store.put(key, value);
    }

    public boolean loadStore(Path path) {
        System.out.println("Attempting to load store from: " + path.toAbsolutePath());

        if (Files.notExists(path)) {
            System.out.println("File does not exist");
            return false;
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object object = objectInputStream.readObject();
            store = (Map<String, String>) object;
            System.out.println("Loaded store successfully");
            return true;
        } catch (Exception exception) {
            System.err.println("Failed to load store");
            exception.printStackTrace();
            return false;
        }
    }

    public boolean writeStore(Path path) {
        System.out.println("Attempting to write store to: " + path.toAbsolutePath());

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(store);
            objectOutputStream.flush();
            System.out.println("Wrote store successfully");
            return true;
        } catch (Exception exception) {
            System.err.println("Failed to write store");
            exception.printStackTrace();
            return false;
        }
    }
}