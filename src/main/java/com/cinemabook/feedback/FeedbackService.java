package com.cinemabook.feedback;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    // Points to src/main/resources/data/feedback.csv
    private static final String CSV_PATH = "src/main/resources/data/feedback.csv";

    private final List<Feedback> feedbacks = new ArrayList<>();
    private int nextId = 1;

    // Constructor — loads CSV when Spring starts
    public FeedbackService() {
        loadFromCsv();
    }

    // ─── LOAD FROM CSV ───────────────────────────────────────────
    private void loadFromCsv() {
        feedbacks.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; }
                String[] p = line.split(",", 7); // max 7 parts (comment may have commas — limit split)
                if (p.length < 7) continue;
                int id     = Integer.parseInt(p[0].trim());
                String usr = p[1].trim();
                String mov = p[2].trim();
                int rat    = Integer.parseInt(p[3].trim());
                String com = p[4].trim();
                String dat = p[5].trim();
                int lik    = Integer.parseInt(p[6].trim());

                feedbacks.add(new Feedback(id, usr, mov, rat, com, dat, lik));
                if (id >= nextId) nextId = id + 1;
            }
        } catch (IOException e) {
            System.err.println("⚠ Could not load feedback.csv: " + e.getMessage());
        }
    }

    // ─── SAVE TO CSV ─────────────────────────────────────────────
    private void saveToCsv() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_PATH))) {
            pw.println("id,username,movieTitle,rating,comment,date,likes");
            for (Feedback f : feedbacks) {
                pw.printf("%d,%s,%s,%d,%s,%s,%d%n",
                        f.getId(), f.getUsername(), f.getMovieTitle(),
                        f.getRating(), f.getComment(), f.getDate(), f.getLikes());
            }
        } catch (IOException e) {
            System.err.println("⚠ Could not save feedback.csv: " + e.getMessage());
        }
    }

    // ─── CREATE ──────────────────────────────────────────────────
    public void addFeedback(String username, String movieTitle,
                            int rating, String comment, String date) {
        Feedback f = new Feedback(nextId++, username, movieTitle, rating, comment, date, 0);
        feedbacks.add(f);
        saveToCsv();
    }

    // ─── READ ────────────────────────────────────────────────────
    public List<Feedback> getAll() {
        return Collections.unmodifiableList(feedbacks);
    }

    public List<Feedback> filterBySentiment(String sentiment) {
        return feedbacks.stream()
                .filter(f -> f.getSentiment().equalsIgnoreCase(sentiment))
                .collect(Collectors.toList());
    }

    // ─── UPDATE (like) ───────────────────────────────────────────
    public boolean likeFeedback(int id) {
        for (Feedback f : feedbacks) {
            if (f.getId() == id) {
                f.setLikes(f.getLikes() + 1);
                saveToCsv();
                return true;
            }
        }
        return false;
    }

    // ─── DELETE ──────────────────────────────────────────────────
    public boolean deleteFeedback(int id) {
        boolean removed = feedbacks.removeIf(f -> f.getId() == id);
        if (removed) saveToCsv();
        return removed;
    }

    // ─── STATS ───────────────────────────────────────────────────
    public double getAverageRating() {
        return feedbacks.stream()
                .mapToInt(Feedback::getRating)
                .average()
                .orElse(0.0);
    }

    public long countBySentiment(String sentiment) {
        return feedbacks.stream()
                .filter(f -> f.getSentiment().equalsIgnoreCase(sentiment))
                .count();
    }
}
