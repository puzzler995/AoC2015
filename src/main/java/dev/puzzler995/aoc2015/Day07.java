package dev.puzzler995.aoc2015;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Day07 {
  private static final String regex =
      "((?<signal>[a-z0-9]{1,5})|((?<leftGateInput>[a-z0-9]{0,2}) {0,1}(?<command>\\w{1,6}) (?<rightGateInput>[a-z0-9]{0,2}))) -> (?<destination>\\w{1,2})";
  private static final Pattern commandPattern = Pattern.compile(regex);

  private static AbstractValuable getNode(String input, Node node, Map<String, Wire> wireMap) {
    AbstractValuable newNode;
    if (StringUtils.isNumeric(input)) {
      newNode = new Node(null, null, NodeType.EMITTER, Short.parseShort(input));
    } else {
      wireMap.putIfAbsent(input, new Wire(input));
      newNode = wireMap.get(input);
    }
    return newNode;
  }

  @PostConstruct
  public void init() {
    Resource example = new ClassPathResource("day07/example.txt");
    Resource input = new ClassPathResource("day07/input.txt");
    part1(example);
    part1(input);
    part2(input);
  }

  private void part1(Resource resource) {
    final String logBase = "Part 1 - " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    Map<String, Wire> wireMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = commandPattern.matcher(line);
        while (matcher.find()) {
          var signal = matcher.group("signal");
          var leftGateInput = matcher.group("leftGateInput");
          var rightGateInput = matcher.group("rightGateInput");
          var command = matcher.group("command");
          var destination = matcher.group("destination");
          if (StringUtils.isEmpty(destination)) {
            throw new IllegalArgumentException("lol wut");
          }
          wireMap.putIfAbsent(destination, new Wire(destination));
          var destinationWire = wireMap.get(destination);
          // This is code for a xxx -> yyy
          if (StringUtils.isNotEmpty(signal)) {
            if (StringUtils.isNumeric(signal)) {
              destinationWire.setInput(
                  new Node(null, null, NodeType.EMITTER, Short.parseShort(signal)));
            } else {
              wireMap.putIfAbsent(signal, new Wire(signal));
              var inWire = wireMap.get(signal);
              destinationWire.setInput(inWire);
              inWire.addOutput(destinationWire);
            }
          } else {
            Node node = new Node();
            // Code for Operations
            if (StringUtils.isNotEmpty(leftGateInput)) {
              node.setInput1(getNode(leftGateInput, node, wireMap));
            }
            if (StringUtils.isNotEmpty(rightGateInput)) {
              node.setInput2(getNode(rightGateInput, node, wireMap));
            }
            if (StringUtils.isNotEmpty(command)) {
              node.setNodeType(NodeType.valueOf(command));
            }
            destinationWire.setInput(node);
          }
        }
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
    log.debug(logBase + "Wires: " + wireMap);
    if (wireMap.containsKey("a")) {
      log.info(logBase + "Wire A: " + wireMap.get("a").getValue());
    }
  }

  private void part2(Resource resource) {
    final String logBase = "Part 2- " + resource.getFilename() + ": ";
    log.debug(logBase + "Start");
    Map<String, Wire> wireMap = new HashMap<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        log.debug(logBase + line);
        var matcher = commandPattern.matcher(line);
        while (matcher.find()) {
          var signal = matcher.group("signal");
          var leftGateInput = matcher.group("leftGateInput");
          var rightGateInput = matcher.group("rightGateInput");
          var command = matcher.group("command");
          var destination = matcher.group("destination");
          if (StringUtils.isEmpty(destination)) {
            throw new IllegalArgumentException("lol wut");
          }
          wireMap.putIfAbsent(destination, new Wire(destination));
          var destinationWire = wireMap.get(destination);
          // This is code for a xxx -> yyy
          if (StringUtils.isNotEmpty(signal)) {
            if (StringUtils.isNumeric(signal)) {
              destinationWire.setInput(
                  new Node(null, null, NodeType.EMITTER, Short.parseShort(signal)));
            } else {
              wireMap.putIfAbsent(signal, new Wire(signal));
              var inWire = wireMap.get(signal);
              destinationWire.setInput(inWire);
              inWire.addOutput(destinationWire);
            }
          } else {
            Node node = new Node();
            // Code for Operations
            if (StringUtils.isNotEmpty(leftGateInput)) {
              node.setInput1(getNode(leftGateInput, node, wireMap));
            }
            if (StringUtils.isNotEmpty(rightGateInput)) {
              node.setInput2(getNode(rightGateInput, node, wireMap));
            }
            if (StringUtils.isNotEmpty(command)) {
              node.setNodeType(NodeType.valueOf(command));
            }
            destinationWire.setInput(node);
          }
        }
      }
    } catch (Exception e) {
      log.error(logBase + "Error: " + e.getMessage());
    }
    log.debug(logBase + "Wires: " + wireMap);
    if (wireMap.containsKey("a")) {
      var wireA = wireMap.get("a");
      var wireB = wireMap.get("b");
      log.debug(logBase + "Wire A: " + wireA.getValue());
      log.debug(logBase+ "Wire B: " + wireB.getValue());
      var val = wireA.getValue();
      wireMap.values().forEach(w -> w.setValue(0));
      wireMap.values().forEach(w -> w.setNeedsForceUpdate(true));
      wireB.setValue(val);
      wireB.setNeedsForceUpdate(false);
      wireA.updateValue(true);
      log.info(logBase + "Wire A: " +wireA.getValue());
    }

  }

  private enum NodeType {
    EMITTER,
    LINK,
    AND,
    OR,
    NOT,
    LSHIFT,
    RSHIFT
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  private abstract static class AbstractValuable {
    protected int value;

    protected abstract void updateValue(boolean force);
  }

  @Data
  @AllArgsConstructor
  private static class NewNode {
    String name;
    NodeType nodeType;
    int value;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = true)
  private static class Node extends AbstractValuable {
    private AbstractValuable input1;
    private AbstractValuable input2;
    private NodeType nodeType;

    public Node(AbstractValuable input1, AbstractValuable input2, NodeType nodeType, int value) {
      this.nodeType = nodeType;
      this.input1 = input1;
      this.input2 = input2;
      this.value = value;
    }

    @Override
    public int getValue() {
      int val = 0;
      switch (nodeType) {
        case EMITTER -> val = value;
        case LINK -> val = input1.getValue();
        case AND -> val = input1.getValue() & input2.getValue();
        case OR -> val = input1.getValue() | input2.getValue();
        case NOT -> val = ~input2.getValue() & 0xFFFF;
        case LSHIFT -> val = input1.getValue() << input2.getValue();
        case RSHIFT -> val = input1.getValue() >> input2.getValue();
      }
      return val;
    }

    @Override
    protected void updateValue(boolean force) {
      if (input1 != null) {
        input1.updateValue(force);
      }
      if (input2 != null) {
        input2.updateValue(force);
      }
    }
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @AllArgsConstructor
  private static class Wire extends AbstractValuable {
    private final String name;
    private AbstractValuable input;
    private List<AbstractValuable> output = new ArrayList<>();
    private boolean needsForceUpdate;

    public Wire(String name) {
      this.name = name;
    }

    public void addOutput(AbstractValuable valuable) {
      output.add(valuable);
    }

    @Override
    public String toString() {
      return "Wire{" + "name=" + name + ", value=" + getValue() + "}";
    }

    @Override
    public int getValue() {
      if (value == 0 && input != null) {
        updateValue(false);
      }
      return this.value;
    }

    public void updateValue(boolean force) {
      if (input == null) {
        value = 0;
      } else if (value == 0 || (force && needsForceUpdate)) {
        log.debug("Updating " + this.name);
        input.updateValue(force);
        value = input.getValue();
        needsForceUpdate = false;
      }
    }
  }
}
