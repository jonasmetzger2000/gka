package de.haw_hamburg.gka.aufg1.storage;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class GrphReader {

    private final Scanner scanner;
    private final Pattern HEADER_PATTERN = Pattern.compile("#(?<type>(directed)|(undirected)):(?<name>.+?);");
    private final Pattern BODY_PATTERN = Pattern.compile("(?<node1>[^-:]+?)(?::(?<attr1>[^-:]+?))?(?:\\s*-\\s*(?<node2>[^-:]+?)(?::(?<attr2>[^-:]+?))?(?:\\s+\\((?<edge>[^-:]+?)\\))?(?:::(?<weight>\\d+))?)?\\s*;");
    private final GrphStructure graph = new GrphStructure();

    public GrphStructure parseGrphFile() {
        scanner.useDelimiter(System.lineSeparator());
        parseHeader();
        parseNodesAndEdges();
        return graph;
    }

    private void parseHeader() {
        final Matcher matcher = HEADER_PATTERN.matcher(scanner.next());
        if (matcher.find()) {
            // read whether graph is directed or undirected
            switch (matcher.group("type")) {
                case "directed" -> graph.setDirected(true);
                case "undirected" -> graph.setDirected(false);
                default ->
                        throw new IllegalArgumentException(String.format("                                             graph type '%s'", matcher.group("type")));
            }
            // read graph name
            graph.setName(sanitize(matcher.group("name")));
        } else throw new IllegalArgumentException("Cant read Grph Header structure");
    }

    private void parseNodesAndEdges() {
        graph.setGrphLines(new ArrayList<>());
        while (scanner.hasNext()) {
            final Matcher matcher = BODY_PATTERN.matcher(scanner.next());
            if (matcher.find()) {
                if (Objects.isNull(matcher.group("node1"))) {
                    System.err.printf("Cannot parse line '%s'%n", matcher.group());
                    continue;
                }
                final GrphLine line = new GrphLine();
                line.setNode1(sanitize(matcher.group("node1")));
                line.setAttr1(sanitize(matcher.group("attr1")));
                line.setNode2(sanitize(matcher.group("node2")));
                line.setAttr2(sanitize(matcher.group("attr2")));
                line.setEdge(sanitize(matcher.group("edge")));
                final String weight = sanitize(matcher.group("weight"));
                if (Objects.nonNull(weight)) {
                    try {
                        line.setWeight(Integer.parseInt(weight));
                    } catch (NumberFormatException ex) {
                        System.err.printf("Cannot parse line '%s' due to weight not being a number%n", matcher.group());
                        continue;
                    }
                }
                graph.getGrphLines().add(line);
            }
        }
    }

    private String sanitize(String str) {
        if (Objects.nonNull(str)) return str.trim();
        else return null;
    }
}
