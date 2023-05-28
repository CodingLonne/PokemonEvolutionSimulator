package evolution;

import java.util.HashMap;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class VisualWorld extends Pane implements World.CreatureListener, World.WorldListener{
    private SimpleIntegerProperty centerX;
    private SimpleIntegerProperty centerY;
    private SimpleIntegerProperty radius;
    private HashMap<Creature, CreatureBody> creatureBodies;
    private SimpleIntegerProperty day;

    private int backgroundFieldLayer = 10;
    private int heartLayer = 5;
    private int attackLayer = 4;
    private int creatureLayer = 3;

    VisualWorld(int size) {
        this.setPrefWidth(size);
        this.setPrefHeight(size);
        centerX = new SimpleIntegerProperty();
        centerY = new SimpleIntegerProperty();
        centerX.bind(widthProperty().divide(2));
        centerY.bind(heightProperty().divide(2));
        Circle field = new Circle();
        field.centerXProperty().bind(centerX);
        field.centerYProperty().bind(centerY);
        radius = new SimpleIntegerProperty();
        radius.bind(Bindings.min(this.widthProperty(), this.heightProperty()).divide(2));
        field.radiusProperty().bind(radius);
        field.setFill(Color.rgb(159, 229, 162));
        field.setViewOrder(backgroundFieldLayer);
        this.getChildren().add(field);
        creatureBodies = new HashMap<Creature, CreatureBody>();
        this.day = new SimpleIntegerProperty(0);
    }
    public void setupNeededConnections(SimpleIntegerProperty day) {
        this.day.bind(day);
    }
    @Override
    public void onCreatureCreate(Creature c) {
        CreatureBody body = new CreatureBody(c, c.getX(), c.getY(), 10, c.mostProminentType().getColor());
        body.setStroke(Color.BLACK);
        body.centerXProperty().bind(c.getXProperty());
        body.centerYProperty().bind(c.getYProperty());
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

class CreatureBody extends Circle {
    private Creature host;
    CreatureBody(Creature host) {
        super();
        this.host = host;
    }
    CreatureBody(Creature host, double radius) {
        super(radius);
        this.host = host;
    }
    CreatureBody(Creature host, double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        this.host = host;
    }
    CreatureBody(Creature host, double centerX, double centerY, double radius, Paint fill) {
        super(centerX, centerY, radius, fill);
        this.host = host;
    }
    CreatureBody(Creature host, double radius, Paint fill) {
        super(radius, fill);
        this.host = host;
    }
    public Creature getHost() {
        return host;
    }
}
