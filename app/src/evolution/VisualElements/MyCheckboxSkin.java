package evolution.VisualElements;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.SkinBase;

public class MyCheckboxSkin extends SkinBase<CheckBox>{
    private Rectangle node;
    private Color selectedFillColor;
    private Color deselectedFillColor;
    private Color selectedBorderColor;
    private Color deselectedBorderColor;
    private SimpleBooleanProperty value;
    private SimpleDoubleProperty prefSizeProperty;
    public MyCheckboxSkin(CheckBox checkbox, Color selectedFill, Color deselectedFill, Color selectedBorder, Color deselectedBorder, boolean initialValue) {
        super(checkbox);
        node = new Rectangle();
        prefSizeProperty = new SimpleDoubleProperty(0);
        node.widthProperty().bind(prefSizeProperty);
        node.heightProperty().bind(prefSizeProperty);
        node.setStrokeWidth(3);
        node.setArcHeight(10);
        node.setArcWidth(10);
        getChildren().add(node);
        selectedFillColor = selectedFill;
        deselectedFillColor = deselectedFill;
        selectedBorderColor = selectedBorder;
        deselectedBorderColor = deselectedBorder;
        value = new SimpleBooleanProperty(initialValue);
        updateAppearance();
        value.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                updateAppearance();
            }
            
        });
        checkbox.selectedProperty().bind(value);
        node.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                value.set(!value.get());
            }
            
        });
    }



    private void updateAppearance() {
        if (value.get()) {
            node.setFill(selectedFillColor);
            node.setStroke(selectedBorderColor);
        } else {
            node.setFill(deselectedFillColor);
            node.setStroke(deselectedBorderColor);
        }
    }
    
    public Rectangle displayRectangle() {
        return node;
    }

    @Override
    public void dispose() {
        node.getParent().getChildrenUnmodifiable().remove(node);
    }

    public double getPrefSize() {
        return prefSizeProperty.get();
    }
    
    public void setPrefSize(double d) {
        prefSizeProperty.set(d);
    }

    public SimpleDoubleProperty prefSizeProperty() {
        return prefSizeProperty;
    }
}
