package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day03 {

  @PostConstruct
  public void init() {
    Resource input = new ClassPathResource("day03/input.txt");
    Resource example = new ClassPathResource("day03/example.txt");
    part1(example);
    part1(input);
    part2(example);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    List<House> houses = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        int x = 0;
        int y = 0;
        House house = new House(x, y, 1);
        houses.add(house);
        for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          switch (c) {
            case '^':
              y++;
              break;
            case 'v':
              y--;
              break;
            case '>':
              x++;
              break;
            case '<':
              x--;
              break;
          }
          int finalX = x;
          int finalY = y;
          var q = houses.stream().filter(h -> h.getX() == finalX && h.getY() == finalY).findFirst();
          if (q.isPresent()) {
            q.get().deliverPresent();
          } else {
            houses.add(new House(x, y, 1));
          }
        }
      }
      var l = houses.stream().filter(h -> h.getPresents() > 0).count();
      log.info(logBase + "Houses: " + l);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    List<House> houses = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        Santa santa = new Santa(0, 0);
        Santa roboSanta = new Santa(0, 0);
        House house = new House(2, 0, 0);
        houses.add(house);
        for (int i = 0; i < line.length(); i++) {
          Optional<House> q;
          Santa curSanta = i % 2 == 0 ? santa : roboSanta;
          char c = line.charAt(i);
          switch (c) {
            case '^':
              curSanta.moveNorth();
              break;
            case 'v':
              curSanta.moveSouth();
              break;
            case '>':
              curSanta.moveEast();
              break;
            case '<':
              curSanta.moveWest();
              break;
          }
          q =
              houses.stream()
                  .filter(h -> h.getX() == curSanta.getX() && h.getY() == curSanta.getY())
                  .findFirst();
          if (q.isPresent()) {
            q.get().deliverPresent();
          } else {
            houses.add(new House(1, curSanta.getX(), curSanta.getY()));
          }
        }
      }
      var l = houses.stream().filter(h -> h.getPresents() > 0).count();
      log.info(logBase + "Houses: " + l);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  @Data
  @AllArgsConstructor
  private static class House {
    private int presents;
    private int x;
    private int y;

    public void deliverPresent() {
      presents++;
    }
  }

  @Data
  @AllArgsConstructor
  private static class Santa {
    private int x;
    private int y;

    public void moveEast() {
      x++;
    }

    public void moveNorth() {
      y++;
    }

    public void moveSouth() {
      y--;
    }

    public void moveWest() {
      x--;
    }
  }
}
