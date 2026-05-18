package com.cinemabook.movie;



import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeaturedPosterService {

    private static final String FILE_PATH =
            System.getProperty("user.dir") + "/src/main/resources/data/featured_posters.txt";
    private static final int MAX_FEATURED = 6;

    private final MovieService movieService;

    public FeaturedPosterService(MovieService movieService) {
        this.movieService = movieService;
    }

    /** Returns up to 6 featured movies. Falls back to NOW_SHOWING if nothing is saved. */
    public List<Movie> getFeaturedMovies() {
        List<String> ids = readIds();
        if (ids.isEmpty()) {
            List<Movie> nowShowing = movieService.getMoviesByStatus("NOW_SHOWING");
            return nowShowing.stream().limit(MAX_FEATURED).collect(Collectors.toList());
        }
        // Resolve IDs → Movie objects (skip any deleted movies)
        return ids.stream()
                .map(id -> movieService.getMovieById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /** Returns the currently saved featured movie IDs. */
    public List<String> getFeaturedIds() {
        return readIds();
    }

    /** Saves up to 6 selected movie IDs. */
    public void saveFeaturedIds(List<String> ids) {
        List<String> toSave = ids.stream().limit(MAX_FEATURED).collect(Collectors.toList());
        File file = new File(FILE_PATH);
        file.getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            bw.write(String.join(",", toSave));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> readIds() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line == null || line.isBlank()) return new ArrayList<>();
            return Arrays.stream(line.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
