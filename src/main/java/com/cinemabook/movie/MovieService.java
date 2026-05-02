package com.cinemabook.movie;

import org.springframework.stereotype.Service;
import java.io.*;
import java.util.*;

@Service
public class MovieService {

    private static final String CSV_PATH = System.getProperty("user.dir") + "/src/main/resources/data/movies.csv";
    private static final String HEADER = "movieId,title,genre,duration,rating,description,imageUrl,status";

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();
        File file = new File(CSV_PATH);
        if (!file.exists()) return movies;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; }
                String[] p = parseCSVLine(line);
                if (p.length == 8)
                    movies.add(new Movie(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7]));
            }
        } catch (IOException e) { e.printStackTrace(); }
        return movies;
    }

    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                fields.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        fields.add(current.toString().trim());
        return fields.toArray(new String[0]);
    }

    public Optional<Movie> getMovieById(String movieId) {
        return getAllMovies().stream()
                .filter(m -> m.getMovieId().equals(movieId))
                .findFirst();
    }

    public List<Movie> getMoviesByStatus(String status) {
        return getAllMovies().stream()
                .filter(m -> m.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    public void saveMovie(Movie movie) {
        movie.setMovieId("MOV" + System.currentTimeMillis());
        File file = new File(CSV_PATH);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (file.length() == 0) bw.write(HEADER + "\n");
            bw.write(toCsvLine(movie) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void updateMovie(Movie updated) {
        List<Movie> movies = getAllMovies();
        for (int i = 0; i < movies.size(); i++) {
            if (movies.get(i).getMovieId().equals(updated.getMovieId())) {
                movies.set(i, updated);
                break;
            }
        }
        writeAll(movies);
    }

    public void deleteMovie(String movieId) {
        List<Movie> movies = getAllMovies();
        movies.removeIf(m -> m.getMovieId().equals(movieId));
        writeAll(movies);
    }

    private void writeAll(List<Movie> movies) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_PATH, false))) {
            bw.write(HEADER + "\n");
            for (Movie m : movies) bw.write(toCsvLine(m) + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String toCsvLine(Movie m) {
        return String.join(",", m.getMovieId(), m.getTitle(), m.getGenre(),
                m.getDuration(), m.getRating(), m.getDescription(),
                m.getImageUrl(), m.getStatus());
    }
}
