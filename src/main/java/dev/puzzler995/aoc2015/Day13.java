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
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day13 {

  private static final Pattern inputPattern =
      Pattern.compile(
          "(?<person>\\w.*) would (?<positive>\\w.*) (?<qty>\\d{1,2}) happiness units by sitting next to (?<nextto>\\w.*).");
  Map<String, Map<String, Integer>> happinessMap;

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
    Resource example1 = new ClassPathResource("day13/example.txt");
    Resource input = new ClassPathResource("day13/input.txt");
    part1(example1);
    part1(input);
    part2(input);
  }

  private String[] findHappiestTable() {
    String[] people = happinessMap.keySet().toArray(String[]::new);
    List<String[]> tables = permutations(people);
    String[] happiestTable = null;
    int maxHappiness = Integer.MIN_VALUE;
    for (String[] table : tables) {
      int happy = tableHappiness(table);
      if (happy > maxHappiness) {
        maxHappiness = happy;
        happiestTable = table;
      }
    }
    return happiestTable;
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    happinessMap = new HashMap<>();
    happinessMap.put("Me", new HashMap<>());
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = inputPattern.matcher(line);
        while (matcher.find()) {
          var person = matcher.group("person");
          var positive = matcher.group("positive");
          var qty = Integer.parseInt(matcher.group("qty"));
          var nextTo = matcher.group("nextto");
          if (StringUtils.equals("lose", positive)) {
            qty = qty * -1;
          }
          happinessMap.putIfAbsent(person, new HashMap<>());
          happinessMap.get(person).putIfAbsent(nextTo, qty);
          happinessMap.get(person).putIfAbsent("Me", 0);
          happinessMap.get("Me").putIfAbsent(person, 0);
        }
      }
      var happiestTable = findHappiestTable();
      log.info(logBase + "Happiest Table: " + tableHappiness(happiestTable));
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    happinessMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = inputPattern.matcher(line);
        while (matcher.find()) {
          var person = matcher.group("person");
          var positive = matcher.group("positive");
          var qty = Integer.parseInt(matcher.group("qty"));
          var nextTo = matcher.group("nextto");
          if (StringUtils.equals("lose", positive)) {
            qty = qty * -1;
          }
          happinessMap.putIfAbsent(person, new HashMap<>());
          happinessMap.get(person).putIfAbsent(nextTo, qty);
        }
      }
      var happiestTable = findHappiestTable();
      log.info(logBase + "Happiest Table: " + tableHappiness(happiestTable));
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private int tableHappiness(String[] table) {
    String last = table[0];
    int happiness = 0;
    for (String next : Arrays.copyOfRange(table, 1, table.length)) {
      happiness += happinessMap.get(last).get(next);
      happiness += happinessMap.get(next).get(last);
      last = next;
    }
    happiness += happinessMap.get(table[0]).get(table[table.length - 1]);
    happiness += happinessMap.get(table[table.length - 1]).get(table[0]);
    return happiness;
  }
}
