package com.cinemabook.show;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    private static final String CSV_PATH =
            System.getProperty("user.dir") + "/data/shows.csv";

    public void addShow(Show show) {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(CSV_PATH, true))) {

            writer.write(
                    show.getShowId() + "," +
                            show.getMovieId() + "," +
                            show.getDate() + "," +
                            show.getTime() + "," +
                            show.getHall() + "," +
                            show.getTotalSeats() + "," +
                            show.getAvailableSeats()
            );

            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Show> getAllShows() {

        List<Show> shows = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CSV_PATH))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                Show show = new Show(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        Integer.parseInt(data[5]),
                        Integer.parseInt(data[6])
                );

                shows.add(show);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return shows;
    }
}