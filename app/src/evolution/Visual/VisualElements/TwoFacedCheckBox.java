package evolution.Visual.VisualElements;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.SkinBase;

public class TwoFacedCheckBox extends SkinBase<CheckBox>{
    private SimpleBooleanProperty value;
    private Node selectedNode;
    private Node unselectedNode;
    public TwoFacedCheckBox(CheckBox checkbox, Node unselected, Node selected, boolean initialValue) {
        super(checkbox);
        this.selectedNode = selected;
        this.unselectedNode = unselected;
        value = new SimpleBooleanProperty(initialValue);
        checkbox.selectedProperty().bind(value);
        updateAppearance(value.get());
        value.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                updateAppearance(newValue);
            }
            
        });
        checkbox.selectedProperty().bind(value);
        selectedNode.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                value.set(false);
            }
            
        });
        unselectedNode.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                value.set(true);
            }
            
        });
    }



    private void updateAppearance(boolean newValue) {
        if (newValue) {
            this.getChildren().remove(unselectedNode);
            this.getChildren().add(selectedNode);
        } else {
            this.getChildren().remove(selectedNode);
            this.getChildren().add(unselectedNode);
        }
    }

    @Override
    public void dispose() {
        this.getChildren().removeAll(unselectedNode, selectedNode);
    }
}