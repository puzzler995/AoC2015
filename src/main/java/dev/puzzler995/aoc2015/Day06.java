package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day06 {

  private static final Pattern instructionPattern =
      Pattern.compile(
          "(?<instruction>turn off|toggle|turn on) (?<corner1x>\\d{1,3}),(?<corner1y>\\d{1,3}) through (?<corner2x>\\d{1,3}),(?<corner2y>\\d{1,3})");

  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day06/example.txt");
    Resource example2 = new ClassPathResource("day06/example2.txt");
    Resource input = new ClassPathResource("day06/input.txt");
    part1(example);
    part1(input);
    part2(example2);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    Light[][] lights = new Light[1000][1000];
    for (int i = 0; i < lights.length; i++) {
      for (int j = 0; j < lights[i].length; j++) {
        lights[i][j] = new Light();
      }
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var match = instructionPattern.matcher(line);
        while (match.find()) {
          var instruction = match.group("instruction");
          var corner1x = Integer.parseInt(match.group("corner1x"));
          var corner1y = Integer.parseInt(match.group("corner1y"));
          var corner2x = Integer.parseInt(match.group("corner2x"));
          var corner2y = Integer.parseInt(match.group("corner2y"));

          for (int x = corner1x; x <= corner2x; x++) {
            for (int y = corner1y; y <= corner2y; y++) {
              switch (instruction) {
                case "turn on":
                  lights[x][y].turnOn();
                  break;
                case "turn off":
                  lights[x][y].turnOff();
                  break;
                case "toggle":
                  lights[x][y].toggle();
                  break;
                default:
                  throw new IllegalArgumentException("Wrong instruction");
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
    var onLights = Arrays.stream(lights).flatMap(Arrays::stream).filter(Light::isOn).count();
    log.info(logBase + "On Lights: " + onLights);
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    DimmableLight[][] lights = new DimmableLight[1000][1000];
    for (int i = 0; i < lights.length; i++) {
      for (int j = 0; j < lights[i].length; j++) {
        lights[i][j] = new DimmableLight();
      }
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var match = instructionPattern.matcher(line);
        while (match.find()) {
          var instruction = match.group("instruction");
          var corner1x = Integer.parseInt(match.group("corner1x"));
          var corner1y = Integer.parseInt(match.group("corner1y"));
          var corner2x = Integer.parseInt(match.group("corner2x"));
          var corner2y = Integer.parseInt(match.group("corner2y"));

          for (int x = corner1x; x <= corner2x; x++) {
            for (int y = corner1y; y <= corner2y; y++) {
              switch (instruction) {
                case "turn on":
                  lights[x][y].turnOn();
                  break;
                case "turn off":
                  lights[x][y].turnOff();
                  break;
                case "toggle":
                  lights[x][y].toggle();
                  break;
                default:
                  throw new IllegalArgumentException("Wrong instruction");
              }
            }
          }
        }
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
    var onLights =
        Arrays.stream(lights).flatMap(Arrays::stream).mapToInt(DimmableLight::getOn).sum();
    log.info(logBase + "On Lights: " + onLights);
  }

  @Getter
  private static class DimmableLight {
    private int on;

    @Override
    public String toString() {
      return "Light: " + on;
    }

    public void toggle() {
      on = on + 2;
    }

    public void turnOff() {
      if (on >= 1) {
        on--;
      }
    }

    public void turnOn() {
      on++;
    }
  }

  @Getter
  private static class Light {
    private boolean on;

    @Override
    public String toString() {
      return "Light: " + on;
    }

    public void toggle() {
      on = !on;
    }

    public void turnOff() {
      on = false;
    }

    public void turnOn() {
      on = true;
    }
  }
}
