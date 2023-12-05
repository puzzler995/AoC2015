package dev.puzzler995.aoc2015;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.StreamSupport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day12 {
  ObjectMapper objectMapper;

  private static Integer part2Sum(JsonNode json) {
    Integer total = 0;
    var q = json.elements();
    for (Iterator<JsonNode> it = q; it.hasNext(); ) {
      JsonNode p = it.next();
      if (p.isObject()) {
        if (StreamSupport.stream(p.spliterator(), false)
            .filter(Objects::nonNull)
            .noneMatch(value -> StringUtils.equals(value.asText(), "red"))) {
          total += part2Sum(p);
        }
      } else if (p.isArray()) {
        total += part2Sum(p);
      } else if (p.isNumber()) {
        total += p.asInt();
      }
    }
    return total;
  }

  private static Integer sum(JsonNode json) {
    Integer total = 0;
    var q = json.elements();
    for (Iterator<JsonNode> it = q; it.hasNext(); ) {
      JsonNode p = it.next();
      if (p.isArray() || p.isObject()) {
        total += sum(p);
      } else if (p.isNumber()) {
        total += p.asInt();
      }
    }
    return total;
  }

  @PostConstruct
  public void init() {
    objectMapper = new ObjectMapper();
    Resource example1 = new ClassPathResource("day12/example1.txt");
    Resource input = new ClassPathResource("day12/input.txt");
    part1(input);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try {
      var json = objectMapper.readTree(new FileReader(resource.getFile()));
      log.debug(logBase + "Json Loaded");
      var total = sum(json);
      log.info(logBase + "Total: " + total);

    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    try {
      var json = objectMapper.readTree(new FileReader(resource.getFile()));
      log.debug(logBase + "Json Loaded");
      var total = part2Sum(json);
      log.info(logBase + "Total: " + total);

    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
  }
}
