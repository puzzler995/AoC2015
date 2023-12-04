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
public class Day08 {
  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day08/example.txt");
    Resource input = new ClassPathResource("day08/input.txt");
    part1(example);
    part1(input);
    part2(example);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      int totalCharactersOfCode = 0;
      int totalCharactersInMemory = 0;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var inside = line.substring(1, line.length() - 1);
        boolean currentlyEscaping = false;
        boolean currentlyHexing = false;
        int hexStop = 0;
        StringBuilder hexCache = new StringBuilder();
        StringBuilder escapedCache = new StringBuilder();
        for (int i = 0; i < inside.length(); i++) {
          char c = inside.charAt(i);
          if (c == '\\' && (!currentlyEscaping)) {
            currentlyEscaping = true;
            continue;
          }
          if (c == 'x' && currentlyEscaping) {
            currentlyHexing = true;
            hexCache = new StringBuilder();
            hexStop = i + 2;
            continue;
          }
          if (currentlyEscaping && currentlyHexing) {
            hexCache.append(c);
            if (i == hexStop) {
              char h = (char) Integer.parseInt(hexCache.toString(), 16);
              escapedCache.append(h);
              currentlyHexing = false;
              currentlyEscaping = false;
            }
            continue;
          }
          escapedCache.append(c);
          currentlyEscaping = false;
        }
        var escapedString = escapedCache.toString();
        log.debug(logBase + "Escaped String: " + escapedString);
        log.debug(logBase + "Characters in Code: " + line.length());
        totalCharactersOfCode += line.length();
        log.debug(logBase + "Characters in Memory: " + escapedString.length());
        totalCharactersInMemory += escapedString.length();
      }
      log.debug(logBase + "Total Characters in Code: " + totalCharactersOfCode);
      log.debug(logBase + "Total Characters in Memory: " + totalCharactersInMemory);
      log.info(logBase + "Difference: " + (totalCharactersOfCode - totalCharactersInMemory));
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      int totalCharactersOfCode = 0;
      int totalCharactersInMemory = 0;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        StringBuilder unescapedCache = new StringBuilder();
        unescapedCache.append('"');
        for (int i = 0; i < line.length(); i++) {
          char c = line.charAt(i);
          if (c == '"' || c == '\\') {
            unescapedCache.append("\\");
          }
          unescapedCache.append(c);
        }
        unescapedCache.append('"');
        var unescapedString = unescapedCache.toString();
        log.debug(logBase + "Unescaped String: " + unescapedString);
        log.debug(logBase + "Characters in Code: " + line.length());
        totalCharactersOfCode += line.length();
        log.debug(logBase + "Characters in Encoded: " + unescapedString.length());
        totalCharactersInMemory += unescapedString.length();
      }
      log.debug(logBase + "Total Characters in Code: " + totalCharactersOfCode);
      log.debug(logBase + "Total Characters in Memory: " + totalCharactersInMemory);
      log.info(logBase + "Difference: " + (totalCharactersInMemory - totalCharactersOfCode));
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
