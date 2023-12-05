package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day09 {
  private static final Pattern tablePattern =
      Pattern.compile("(?<from>\\w.*) to (?<to>\\w.*) = (?<distance>\\d.*)");
  Map<String, Map<String, Integer>> distances;

  public static <T> void swap(T[] array, int first, int second) {
    T temp = array[first];
    array[first] = array[second];
    array[second] = temp;
  }

  private static <T> void allPermutationsHelper(T[] permutation, List<T[]> permutations, int n) {
    if (n <= 0) {
      permutations.add(permutation);
      return;
    }
    T[] tempPermutation = Arrays.copyOf(permutation, permutation.length);
    for (int i = 0; i < n; i++) {
      swap(tempPermutation, i, n - 1);
      allPermutationsHelper(tempPermutation, permutations, n - 1);
      swap(tempPermutation, i, n - 1);
    }
  }

  private static <T> List<T[]> permutations(T[] original) {
    List<T[]> permutations = new ArrayList<>();
    allPermutationsHelper(original, permutations, original.length);
    return permutations;
  }

  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day09/example.txt");
    Resource input = new ClassPathResource("day09/input.txt");
    part1(example);
    part1(input);
    part2(example);
    part2(input);
  }

  private String[] findLongestPath() {
    String[] cities = distances.keySet().toArray(String[]::new);
    List<String[]> paths = permutations(cities);
    String[] longestPath = null;
    int maxDistance = Integer.MIN_VALUE;
    for (String[] path : paths) {
      int distance = pathDistance(path);
      if (distance > maxDistance) {
        maxDistance = distance;
        longestPath = path;
      }
    }
    return longestPath;
  }

  private String[] findShortestPath() {
    String[] cities = distances.keySet().toArray(String[]::new);
    List<String[]> paths = permutations(cities);
    String[] shortestPath = null;
    int minDistance = Integer.MAX_VALUE;
    for (String[] path : paths) {
      int distance = pathDistance(path);
      if (distance < minDistance) {
        minDistance = distance;
        shortestPath = path;
      }
    }
    return shortestPath;
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    distances = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = tablePattern.matcher(line);
        while (matcher.find()) {
          var from = matcher.group("from");
          var to = matcher.group("to");
          var distance = Integer.parseInt(matcher.group("distance"));
          distances.putIfAbsent(from, new HashMap<>());
          distances.putIfAbsent(to, new HashMap<>());
          distances.get(from).putIfAbsent(to, distance);
          distances.get(to).putIfAbsent(from, distance);
        }
      }
      var shortestPath = findShortestPath();
      int distance = pathDistance(shortestPath);
      log.debug(logBase + "Shortest Path: " + Arrays.toString(shortestPath));
      log.info(logBase + "Length: " + distance);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    distances = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = tablePattern.matcher(line);
        while (matcher.find()) {
          var from = matcher.group("from");
          var to = matcher.group("to");
          var distance = Integer.parseInt(matcher.group("distance"));
          distances.putIfAbsent(from, new HashMap<>());
          distances.putIfAbsent(to, new HashMap<>());
          distances.get(from).putIfAbsent(to, distance);
          distances.get(to).putIfAbsent(from, distance);
        }
      }
      var longestPath = findLongestPath();
      int distance = pathDistance(longestPath);
      log.debug(logBase + "Longest Path: " + Arrays.toString(longestPath));
      log.info(logBase + "Length: " + distance);
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private int pathDistance(String[] path) {
    String last = path[0];
    int distance = 0;
    for (String next : Arrays.copyOfRange(path, 1, path.length)) {
      distance += distances.get(last).get(next);
      last = next;
    }
    return distance;
  }
}
