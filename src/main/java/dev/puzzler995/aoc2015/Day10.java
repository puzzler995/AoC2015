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
public class Day10 {

  private static String processString(String string, String logBase) {
    int currentNum = 0;
    int currentQty = 0;
    StringBuilder cache = new StringBuilder();
    for (int i = 0; i < string.length(); i++) {
      var c = Integer.parseInt(string.substring(i, i + 1));
      log.debug(logBase + c);
      if (currentNum == c) {
        currentQty++;
      } else {
        if (currentNum != 0 && currentQty != 0) {
          cache.append(currentQty).append(currentNum);
        }
        currentNum = c;
        currentQty = 1;
      }
    }
    cache.append(currentQty).append(currentNum);
    return cache.toString();
  }

  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day10/example.txt");
    Resource input = new ClassPathResource("day10/input.txt");
    part1(example);
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
        String startString = line;
        for (int i = 0; i < 40; i++) {
          startString = processString(startString, logBase);
        }
        log.info(logBase + "Final Line Length: " + startString.length());
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
        String startString = line;
        for (int i = 0; i < 50; i++) {
          startString = processString(startString, logBase);
        }
        log.info(logBase + "Final Line Length: " + startString.length());
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
