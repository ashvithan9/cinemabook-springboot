package com.cinemabook.show;

import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class ShowService {

    private static final String CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/data/shows.csv";
    private static final String HEADER = "showId,movieId,movieTitle,date,time,hall,totalSeats,availableSeats,price";

    public List<Show> getAllShows() {
        List<Show> shows = new ArrayList<>();
        File file = new File(CSV_PATH);
        if (!file.exists()) return shows;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                if (line.trim().isEmpty()) continue;
                String[] p = line.split(",", -1);
                if (p.length == 9) {
                    shows.add(new Show(
                        p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                        p[4].trim(), p[5].trim(),
                        Integer.parseInt(p[6].trim()),
                        Integer.parseInt(p[7].trim()),
                        p[8].trim()
                    ));
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return shows;
    }

    public Optional<Show> getShowById(String showId) {
        return getAllShows().stream()
                .filter(s -> s.getShowId().equals(showId))
                .findFirst();
    }

    public List<Show> getShowsByMovieId(String movieId) {
        return getAllShows().stream()
                .filter(s -> s.getMovieId().equals(movieId))
                .toList();
    }

    public void saveShow(Show show) {
        show.setShowId("SHW" + System.currentTimeMillis());
        show.setAvailableSeats(show.getTotalSeats());
        File file = new File(CSV_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() == 0) bw.write(HEADER + "\n");
            bw.write(toCsvLine(show) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void updateShow(Show updated) {
        List<Show> shows = getAllShows();
        for (int i = 0; i < shows.size(); i++) {
            if (shows.get(i).getShowId().equals(updated.getShowId())) {
                shows.set(i, updated);
                break;
            }
        }
        writeAll(shows);
    }

    public boolean reduceSeats(String showId, int seats) {
        List<Show> shows = getAllShows();
        for (Show s : shows) {
            if (s.getShowId().equals(showId)) {
                if (s.getAvailableSeats() < seats) return false;
                s.setAvailableSeats(s.getAvailableSeats() - seats);
                writeAll(shows);
                return true;
            }
        }
        return false;
    }

    public void restoreSeats(String showId, int seats) {
        List<Show> shows = getAllShows();
        for (Show s : shows) {
            if (s.getShowId().equals(showId)) {
                s.setAvailableSeats(Math.min(s.getAvailableSeats() + seats, s.getTotalSeats()));
                writeAll(shows);
                return;
            }
        }
    }

    public void deleteShow(String showId) {
        List<Show> shows = getAllShows();
        shows.removeIf(s -> s.getShowId().equals(showId));
        writeAll(shows);
    }

    private void writeAll(List<Show> shows) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH, false))) {
            bw.write(HEADER + "\n");
            for (Show s : shows) bw.write(toCsvLine(s) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String toCsvLine(Show s) {
        return String.join(",",
                s.getShowId(), s.getMovieId(), s.getMovieTitle(),
                s.getDate(), s.getTime(), s.getHall(),
                String.valueOf(s.getTotalSeats()),
                String.valueOf(s.getAvailableSeats()),
                s.getPrice());
    }
}
