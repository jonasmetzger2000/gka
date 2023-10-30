package de.haw_hamburg.gka.gui.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.graphstream.graph.Node;

import java.io.File;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GraphControlModel {

    private ObjectProperty<File> file = new SimpleObjectProperty<>();
    private ObservableList<Node> nodes = FXCollections.observableArrayList();
    private ObjectProperty<Node> source = new SimpleObjectProperty<>();
    private ObjectProperty<Node> target = new SimpleObjectProperty<>();
    private IntegerProperty length = new SimpleIntegerProperty();

}
