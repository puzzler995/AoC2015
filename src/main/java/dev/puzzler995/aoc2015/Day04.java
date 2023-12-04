package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day04 {

  @Value("${settings.runLongRun}")
  private boolean runLongRun;

  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day04/example01.txt");
    Resource example2 = new ClassPathResource("day04/example02.txt");
    Resource input = new ClassPathResource("day04/input.txt");
    part1(example);
    part1(example2);
    if (runLongRun) {
      part1(input);
      part2(input);
    }
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        boolean found = false;
        int i = 0;
        while (!found) {
          i++;
          log.debug(logBase + "Trying: " + line + i);
          var trial = line + i;
          var hex = DigestUtils.md5Hex(trial);
          if (hex.startsWith("00000")) {
            log.info(logBase + "Found: " + trial);
            found = true;
          }
        }
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
        boolean found = false;
        int i = 0;
        while (!found) {
          i++;
          log.debug(logBase + "Trying: " + line + i);
          var trial = line + i;
          var hex = DigestUtils.md5Hex(trial);
          if (hex.startsWith("000000")) {
            log.info(logBase + "Found: " + trial);
            found = true;
          }
        }
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
