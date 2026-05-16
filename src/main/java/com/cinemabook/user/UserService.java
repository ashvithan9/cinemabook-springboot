package com.cinemabook.user;

import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class UserService {

    private static final String CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/data/users.csv";
    private static final String HEADER = "userId,name,email,password,phone,role";

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(CSV_PATH);
        if (!file.exists()) return users;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                if (p.length == 6)
                    users.add(new User(p[0].trim(), p[1].trim(), p[2].trim(),
                            p[3].trim(), p[4].trim(), p[5].trim()));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return users;
    }

    public Optional<User> getUserById(String userId) {
        return getAllUsers().stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        return getAllUsers().stream()
                .filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password))
                .findFirst();
    }

    public boolean emailExists(String email) {
        return getAllUsers().stream().anyMatch(u -> u.getEmail().equals(email));
    }

    // Self-registration: always CUSTOMER
    public void saveUser(User user) {
        user.setUserId("USR" + System.currentTimeMillis());
        user.setRole("CUSTOMER");
        appendUser(user);
    }

    // Admin-created user: keeps whatever role was set
    public void createUser(User user) {
        user.setUserId("USR" + System.currentTimeMillis());
        if (user.getRole() == null || user.getRole().isEmpty()) user.setRole("CUSTOMER");
        appendUser(user);
    }

    private void appendUser(User user) {
        File file = new File(CSV_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() == 0) bw.write(HEADER + "\n");
            bw.write(toCsvLine(user) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void updateUser(User updated) {
        List<User> users = getAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(updated.getUserId())) {
                users.set(i, updated);
                break;
            }
        }
        writeAll(users);
    }

    public void deleteUser(String userId) {
        List<User> users = getAllUsers();
        users.removeIf(u -> u.getUserId().equals(userId));
        writeAll(users);
    }

    private void writeAll(List<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH, false))) {
            bw.write(HEADER + "\n");
            for (User u : users) bw.write(toCsvLine(u) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String toCsvLine(User u) {
        return String.join(",", u.getUserId(), u.getName(), u.getEmail(),
                u.getPassword(), u.getPhone(), u.getRole());
    }
}
