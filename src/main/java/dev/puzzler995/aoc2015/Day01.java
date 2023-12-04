package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;

@Component
@Slf4j
public class Day01 {
  @PostConstruct
  public void init() {
    Resource input = new ClassPathResource("day01/input.txt");
    part1(input);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        int floor = 0;
        for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          if (c == '(') {
            floor++;
          } else if (c == ')') {
            floor--;
          }
        }
        log.info(logBase + "Floor: " + floor);
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        int floor = 0;
        for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          if (c == '(') {
            floor++;
          } else if (c == ')') {
            floor--;
          }
          if (floor == -1) {
            log.info(logBase + "Basement: " + (i + 1));
            break;
          }
        }
        log.info(logBase + "Floor: " + floor);
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
