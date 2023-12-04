package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day02 {
  @PostConstruct
  public void init() {
    Resource input = new ClassPathResource("day02/input.txt");
    part1(input);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      int totalArea = 0;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var s1 = line.split("x");
        var l = Integer.parseInt(s1[0]);
        var w = Integer.parseInt(s1[1]);
        var h = Integer.parseInt(s1[2]);
        var side1 = l * w;
        var side2 = w * h;
        var side3 = h * l;
        var slack = Math.min(Math.min(side1, side2), side3);
        var area = 2 * side1 + 2 * side2 + 2 * side3 + slack;
        log.debug(logBase + "Area: " + area);
        totalArea += area;
      }
      log.info(logBase + "Total Area: " + totalArea);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      int totalLength = 0;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var s1 = line.split("x");
        var l = Integer.parseInt(s1[0]);
        var w = Integer.parseInt(s1[1]);
        var h = Integer.parseInt(s1[2]);
        var side1 = 2 * l + 2 * w;
        var side2 = 2 * w + 2 * h;
        var side3 = 2 * h + 2 * l;
        var smallest = Math.min(Math.min(side1, side2), side3);
        var volume = l * w * h;
        var length = smallest + volume;
        log.debug(logBase + "Length: " + length);
        totalLength += length;
      }
      log.info(logBase + "Total Length: " + totalLength);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
