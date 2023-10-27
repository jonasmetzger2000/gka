package de.haw_hamburg.gka.algo;

import de.haw_hamburg.gka.serializer.GrphGraphSerializer;
import lombok.SneakyThrows;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.junit.jupiter.api.Test;


import static de.haw_hamburg.gka.TestHelper.getFile;
import static org.assertj.core.api.Assertions.assertThat;

public class DjikstraAlgorithmTest {

    private final GrphGraphSerializer serializer = new GrphGraphSerializer();

    @Test
    @SneakyThrows
    public void allEdgesAreConsidered() {
        final Graph graph = serializer.readFrom(getFile("djikstra/allEdgesAreConsidered.grph")).toGraph();
        final DjikstraAlgorithm algorithm = new DjikstraAlgorithm(graph);
        algorithm.calculate(graph.getNode("A"));

        vorgaenger(graph, "A", "A");
        vorgaenger(graph, "B", "A");
        vorgaenger(graph, "D", "B");
        vorgaenger(graph, "E", "D");
        vorgaenger(graph, "F", "D");
        vorgaenger(graph, "C", "F");

        entfernung(graph, "A", 0);
        entfernung(graph, "B", 2);
        entfernung(graph, "D", 4);
        entfernung(graph, "E", 1004);
        entfernung(graph, "F", 6);
        entfernung(graph, "C", 9);
    }

    @Test
    @SneakyThrows
    public void firstPathCanOverrideLongPath() {
        final Graph graph = serializer.readFrom(getFile("djikstra/firstPathCanOverrideLongPath.grph")).toGraph();
        final DjikstraAlgorithm algorithm = new DjikstraAlgorithm(graph);
        algorithm.calculate(graph.getNode("A"));

        vorgaenger(graph, "A", "A");
        vorgaenger(graph, "B", "A");
        vorgaenger(graph, "D", "B");
        vorgaenger(graph, "E", "D");
        vorgaenger(graph, "F", "D");
        vorgaenger(graph, "C", "A");

        entfernung(graph, "A", 0);
        entfernung(graph, "B", 2);
        entfernung(graph, "D", 4);
        entfernung(graph, "E", 1004);
        entfernung(graph, "F", 7);
        entfernung(graph, "C", 10);
    }

    @Test
    @SneakyThrows
    public void shouldNotTraverseInDirectedGraph() {
        final Graph graph = serializer.readFrom(getFile("djikstra/shouldNotTraverseInDirectedGraph.grph")).toGraph();
        final DjikstraAlgorithm algorithm = new DjikstraAlgorithm(graph);
        algorithm.calculate(graph.getNode("A"));

        vorgaenger(graph, "A", "A");
        vorgaenger(graph, "B", null);
        vorgaenger(graph, "C", null);

        entfernung(graph, "A", 0);
        entfernung(graph, "B", Integer.MAX_VALUE);
        entfernung(graph, "C", Integer.MAX_VALUE);
    }

    @Test
    @SneakyThrows
    public void noPathExists() {
        final Graph graph = serializer.readFrom(getFile("djikstra/noPathExists.grph")).toGraph();
        final DjikstraAlgorithm algorithm = new DjikstraAlgorithm(graph);
        algorithm.calculate(graph.getNode("A"));

        vorgaenger(graph, "A", "A");
        vorgaenger(graph, "B", "A");
        vorgaenger(graph, "C", null);
        vorgaenger(graph, "D", null);

        entfernung(graph, "A", 0);
        entfernung(graph, "B", 1);
        entfernung(graph, "C", Integer.MAX_VALUE);
        entfernung(graph, "D", Integer.MAX_VALUE);
    }

    private void vorgaenger(Graph graph, String node, String expectedVorgaenger) {
        assertThat(graph.getNode(node).getAttribute("vorgänger", Node.class)).isEqualTo(graph.getNode(expectedVorgaenger));
    }

    private void entfernung(Graph graph, String node, Integer entfernung) {
        assertThat(graph.getNode(node).getAttribute("abstand")).isEqualTo(entfernung);
    }

}