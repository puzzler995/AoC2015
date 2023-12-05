package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day14 {
  private static final String regex =
      "(?<name>\\w.*) can fly (?<speed>\\d{1,2}) km/s for (?<ftime>\\d{1,2}) seconds, but then must rest for (?<rtime>\\d{1,3}) seconds\\.";
  private static final Pattern pattern = Pattern.compile(regex);

  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day14/example.txt");
    Resource input = new ClassPathResource("day14/input.txt");
    part1(example, 1000);
    part1(input, 2503);
    part2(example, 1000);
    part2(input, 2503);
  }

  private void part1(Resource resource, Integer length) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      var maxDistance = 0;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = pattern.matcher(line);
        while (matcher.find()) {
          var name = matcher.group("name");
          var speed = Integer.parseInt(matcher.group("speed"));
          var ftime = Integer.parseInt(matcher.group("ftime"));
          var rtime = Integer.parseInt(matcher.group("rtime"));
          var distancePer = speed * ftime;
          var interval = ftime + rtime;
          var intervalM = length % interval;
          var intervals = (length - intervalM) / interval;
          var distance = intervals * distancePer;
          if (intervalM > ftime) {
            distance += distancePer;
          } else if (intervalM > 0) {
            distance += (intervalM * speed);
          }
          if (distance > maxDistance) {
            maxDistance = distance;
          }
          log.debug(logBase + name + ": " + distance);
        }
      }
      log.info(logBase + "Max Distance: " + maxDistance);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource, Integer length) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    List<Reindeer> reindeerList = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = pattern.matcher(line);
        while (matcher.find()) {
          var name = matcher.group("name");
          var speed = Integer.parseInt(matcher.group("speed"));
          var ftime = Integer.parseInt(matcher.group("ftime"));
          var rtime = Integer.parseInt(matcher.group("rtime"));
          reindeerList.add(new Reindeer(ftime, name, rtime, speed));
        }
      }
      var q = findWinner(reindeerList, length);
      log.info(logBase + "Max Points: " + q.getScore());
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private Reindeer findWinner(List<Reindeer> reindeerList, Integer length) {
    for (int i = 1; i <= length; i++) {
      reindeerList.forEach(Reindeer::proceed);
      int maxDistance = reindeerList.stream().mapToInt(Reindeer::getDistance).max().orElse(0);
      reindeerList.stream().filter(r -> r.getDistance() == maxDistance).forEach(Reindeer::awardPoint);
    }
    return reindeerList.stream().max(Comparator.comparingInt(Reindeer::getScore)).get();
  }

  @Data
  @AllArgsConstructor
  @RequiredArgsConstructor
  private static class Reindeer {
    private final Integer flightTime;
    private final String name;
    private final Integer restTime;
    private final Integer speed;
    private Integer currentTimeElapsed = 0;
    private Integer distance = 0;
    private boolean isFlying = true;
    private Integer score = 0;

    public void awardPoint() {
      score++;
    }

    public void proceed() {
      currentTimeElapsed++;
      if (isFlying) {
        distance += speed;
        if (currentTimeElapsed >= flightTime) {
          isFlying = false;
          currentTimeElapsed = 0;
        }
      } else {
        if (currentTimeElapsed >= restTime) {
          isFlying = true;
          currentTimeElapsed = 0;
        }
      }
    }
  }
}
