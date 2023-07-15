package evolution.Visual.VisualElements;

import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MultiFacedDisplay<T> extends StackPane {
    private Map<T, Node> faces;
    private Node defaultFace;
    private ObjectProperty<T> value;
    public MultiFacedDisplay(Map<T, Node> faces, Node defaultFace, T initialValue) {
        super();
        this.faces = faces;
        this.defaultFace = defaultFace;
        this.value = new SimpleObjectProperty<>(initialValue);
        this.value.addListener((p, ov, nv) -> changeValueFromTo(ov, nv));
        if (this.faces.containsKey(value.get())) {
            this.getChildren().add(this.faces.get(value.get()));
        } else {
            this.getChildren().add(defaultFace);
        }
    }

    private void changeValueFromTo(T ov, T nv) {
        if (faces.containsKey(ov)) {
            this.getChildren().remove(faces.get(ov));
        } else {
            this.getChildren().remove(defaultFace);
        }
        if (faces.containsKey(nv)) {
            this.getChildren().add(faces.get(nv));
        } else {
            this.getChildren().add(defaultFace);
        }
    }

    //getters
    public Map<T, Node> getFaces() {
        return faces;
    }

    public Node getDefaultFace() {
        return defaultFace;
    }

    public T getValue() {
        return value.get();
    }

    //setters
    public void setDefaultFace(Node f) {
        defaultFace = f;
    }

    public void setValue(T value) {
        this.value.set(value);
    }

    //properties
    public ObjectProperty<T> valueProperty() {
        return value;
    }

}
