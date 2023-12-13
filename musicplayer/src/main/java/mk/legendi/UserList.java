package mk.legendi;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class UserList {
    private static UserList userlist;
    private final Path path = Path.of("accounts_list.db");
    ArrayList<User> accounts;

    public static UserList getInstance() {
        if (userlist == null) {
            userlist = new UserList();
        }
        return UserList.userlist;
    }

    private UserList() {
        accounts = new ArrayList<>();
    }

    public ArrayList<User> getAccounts() {
        return accounts;
    }

    public boolean writeUsers() {
        System.out.println("Attempting to write store to: " + path.toAbsolutePath());

        try (OutputStream outputStream = Files.newOutputStream(path)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(accounts);
            objectOutputStream.flush();
            System.out.println("Wrote store successfully");
            return true;
        } catch (Exception exception) {
            System.err.println("Failed to write store");
            exception.printStackTrace();
            return false;
        }
    }

    public boolean loadUsers(Path path) {
        System.out.println("Attempting to load store from: " + path.toAbsolutePath());

        if (Files.notExists(path)) {
            System.out.println("File does not exist");
            return false;
        }

        try (InputStream inputStream = Files.newInputStream(path)) {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object object = objectInputStream.readObject();
            accounts = (ArrayList<User>) object;
            System.out.println("Loaded store successfully");
            return true;
        } catch (Exception exception) {
            System.err.println("Failed to load store");
            exception.printStackTrace();
            return false;
        }
    }
}
