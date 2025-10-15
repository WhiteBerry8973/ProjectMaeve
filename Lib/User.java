package Lib;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class User {

    public static class Rec {
        public String username, password;
        public int points;
        Rec(String u, String p, int pts){ username=u; password=p; points=pts; }
    }

    private final Path path;
    private final Map<String, Rec> map = new HashMap<>();

    public User(String filePath) {
        this.path = Paths.get(filePath);
        load();
    }

    public synchronized boolean authenticate(String username, String password) {
        Rec r = map.get(username);
        return r != null && Objects.equals(r.password, password);
    }

    public synchronized void upsertUser(String username, String password, int initialPoints) {
        Rec r = map.get(username);
        if (r == null) {
            r = new Rec(username, password, Math.max(0, initialPoints));
            map.put(username, r);
        } else {
            r.password = password;
            r.points   = Math.max(r.points, initialPoints);
        }
        save();
    }

    public synchronized int getPoints(String username) {
        Rec r = map.get(username);
        return r == null ? 0 : r.points;
    }

    public synchronized void setPoints(String username, int pts) {
        Rec r = map.computeIfAbsent(username, newUser -> new Rec(newUser, "", 0));
        r.points = Math.max(0, pts);
        save();
    }

    public synchronized void addPoints(String username, int delta) {
        if (username == null || username.trim().isEmpty()) return;
        Rec r = map.computeIfAbsent(username, newUser -> new Rec(newUser, "", 0));
        r.points = Math.max(0, r.points + delta);
        save();
    }

    private void load() {
        map.clear();
        try {
            if (!Files.exists(path)) return;
            for (String line : Files.readAllLines(path)) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split(",", -1);
                if (p.length >= 2) {
                    String u = p[0].trim();
                    String pw = p[1].trim();
                    int pts = 0;
                    if (p.length >= 3 && !p[2].trim().isEmpty()) {
                        try { pts = Integer.parseInt(p[2].trim()); } catch (NumberFormatException ignored) {}
                    }
                    map.put(u, new Rec(u, pw, pts));
                }
            }
        } catch (IOException ignored) {}
    }

    private void save() {
        try {
            if (path.getParent() != null) Files.createDirectories(path.getParent());
            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                bw.write("# username,password,points"); bw.newLine();
                for (Rec r : map.values()) {
                    bw.write(r.username + "," + r.password + "," + r.points);
                    bw.newLine();
                }
            }
        } catch (IOException ignored) {}
    }
}
