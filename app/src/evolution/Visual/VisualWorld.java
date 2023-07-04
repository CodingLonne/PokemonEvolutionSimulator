package evolution.Visual;

import java.util.HashMap;

import evolution.Creature;
import evolution.World;
import evolution.Visual.VisualElements.CreatureBody;
import evolution.Visual.VisualElements.MyColors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VisualWorld extends Pane implements World.CreatureListener, World.WorldListener{
    private SimpleDoubleProperty centerX;
    private SimpleDoubleProperty centerY;
    private SimpleDoubleProperty radius;
    private SimpleDoubleProperty scale;
    private HashMap<Creature, CreatureBody> creatureBodies;
    private SimpleIntegerProperty day;

    private int backgroundFieldLayer = 10;
    private int heartLayer = 5;
    private int attackLayer = 4;
    private int creatureLayer = 3;
    private int arrowLayer = 2;

    public VisualWorld(int size) {
        this.setPrefWidth(size);
        this.setPrefHeight(size);
        centerX = new SimpleDoubleProperty();
        centerY = new SimpleDoubleProperty();
        centerX.bind(widthProperty().divide(2));
        centerY.bind(heightProperty().divide(2));
        scale = new SimpleDoubleProperty(1);
        Circle field = new Circle();
        field.centerXProperty().bind(centerX);
        field.centerYProperty().bind(centerY);
        radius = new SimpleDoubleProperty();
        radius.bind(Bindings.min(this.widthProperty(), this.heightProperty()).divide(2));
        field.radiusProperty().bind(radius);
        field.setFill(MyColors.celadon);
        field.setViewOrder(backgroundFieldLayer);
        this.getChildren().add(field);
        creatureBodies = new HashMap<Creature, CreatureBody>();
        this.day = new SimpleIntegerProperty(0);
    }
    public void setupNeededConnections(SimpleIntegerProperty day, SimpleDoubleProperty worldSize) {
        this.day.bind(day);
        scale.bind(radius.divide(worldSize));
    }
    @Override
    public void onCreatureCreate(Creature c) {
        CreatureBody body = new CreatureBody(c, c.getX(), c.getY(), 10, c.mostProminentType().getColor());
        body.setStroke(Color.BLACK);
        //c.getXProperty()
        body.centerXProperty().bind(centerX.add(c.xProperty().multiply(scale)));
        body.centerYProperty().bind(centerY.add(c.yProperty().multiply(scale)));
        body.radiusProperty().bind(c.sizeProperty().multiply(scale));
        body.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                ((CreatureBody) event.getTarget()).getHost().gotClickedOn();
                event.consume();
            }
            
        });
        body.setViewOrder(creatureLayer);
        creatureBodies.put(c, body);
        this.getChildren().add(body);
    }
    @Override
    public void onCreatureUpdate(Creature c) {
        creatureBodies.get(c).setFill(c.mostProminentType().getColor());
    }
    @Override
    public void onCreatureDelete(Creature c) {
        this.getChildren().remove(creatureBodies.get(c));
        creatureBodies.remove(c);
    }
}

