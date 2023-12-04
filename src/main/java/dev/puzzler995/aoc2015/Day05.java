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
public class Day05 {
  private static boolean containsDoubleLetter(String string) {
    for (int i = 0; i < string.length() - 1; i++) {
      if (string.charAt(i) == string.charAt(i + 1)) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsDoublePair(String string) {
    for (int i = 0; i < string.length() - 1; i++) {
      var pair = string.substring(i, i + 2);
      if (string.indexOf(pair, i + 2) != -1) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsForbiddenStrings(String string) {
    return string.contains("ab")
        || string.contains("cd")
        || string.contains("pq")
        || string.contains("xy");
  }

  private static boolean containsSandwich(String string) {
    for (int i = 0; i < string.length() - 2; i++) {
      if (string.charAt(i) == string.charAt(i + 2)) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsThreeVowels(String string) {
    return string.chars().filter(c -> "aeiou".indexOf(c) != -1).count() >= 3;
  }

  private static boolean isNiceP1(String string) {
    return containsThreeVowels(string)
        && containsDoubleLetter(string)
        && !containsForbiddenStrings(string);
  }

  private static boolean isNiceP2(String string) {
    return containsDoublePair(string) && containsSandwich(string);
  }

  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day05/example.txt");
    Resource example2 = new ClassPathResource("day05/example02.txt");
    Resource input = new ClassPathResource("day05/input.txt");
    part1(example);
    part1(input);
    part2(example2);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");

    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      var niceCount = 0;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        if (isNiceP1(line)) {
          log.debug(logBase + "Nice: " + line);
          niceCount++;
        }
      }
      log.info(logBase + "Nice Count: " + niceCount);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");

    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      var niceCount = 0;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        if (isNiceP2(line)) {
          log.debug(logBase + "Nice: " + line);
          niceCount++;
        }
      }
      log.info(logBase + "Nice Count: " + niceCount);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
