package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day11 {

  private static boolean containsForbiddenLetters(String string) {
    return string.contains("i") || string.contains("o") || string.contains("l");
  }

  private static boolean containsIncreasingStraight(String string) {
    for (int i = 0; i < string.length() - 2; i++) {
      if ((string.charAt(i) + 1) == (string.charAt(i + 1))
          && (string.charAt(i) + 2) == (string.charAt(i + 2))) {
        return true;
      }
    }
    return false;
  }

  private static boolean containsTwoPair(String string) {
    String pairFound = "";
    for (int i = 0; i < string.length() - 1; i++) {
      if (string.charAt(i) == string.charAt(i + 1)) {
        if (StringUtils.isEmpty(pairFound)) {
          pairFound = string.substring(i, i + 2);
        } else if (!StringUtils.equals(pairFound, string.substring(i, i + 2))) {
          return true;
        }
      }
    }
    return false;
  }

  private static String findNewPassword(String string) {
    var newPass = string;
    while (!StringUtils.equals(newPass, "zzzzzzzz")) {
      newPass = increment(newPass);
      if (!containsForbiddenLetters(newPass)
          && containsTwoPair(newPass)
          && containsIncreasingStraight(newPass)) {
        return newPass;
      }
    }
    return "zzzzzzzz";
  }

  private static String increment(String string) {
    var c = string.charAt(string.length() - 1);
    if (c == 'z') {
      var sub = increment(string.substring(0, string.length() - 1));
      return sub + "a";
    }
    return string.substring(0, string.length() - 1) + String.valueOf((char) (c + 1));
  }

  @PostConstruct
  public void init() {
    Resource example4 = new ClassPathResource("day11/example4.txt");
    Resource example5 = new ClassPathResource("day11/example5.txt");
    Resource input = new ClassPathResource("day11/input.txt");
    Resource input2 = new ClassPathResource("day11/input2.txt");
    part1(example4);
    part1(example5);
    part1(input);
    part1(input2);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var newPass = findNewPassword(line);
        log.info(logBase + "New Password: " + newPass);
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
