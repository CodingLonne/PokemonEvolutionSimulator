package evolution.Visual;

import java.util.HashMap;
import java.util.Map;

import evolution.Creature;
import evolution.PairingDance;
import evolution.World;
import evolution.PairingDance.pairingListener;
import evolution.Visual.VisualElements.CreatureBody;
import evolution.Visual.VisualElements.HeartShape;
import evolution.Visual.VisualElements.MyColors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Scale;

public class VisualWorld extends Pane implements World.CreatureListener, World.CreatureUpdateListener, World.WorldListener, pairingListener{
    private Circle field;
    private SimpleDoubleProperty centerX;
    private SimpleDoubleProperty centerY;
    private SimpleDoubleProperty radius;
    private SimpleDoubleProperty scale;
    private Map<Creature, CreatureBody> creatureBodies;
    private Map<PairingDance, HeartShape> pairVisuals;
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
        field = new Circle();
        field.centerXProperty().bind(centerX);
        field.centerYProperty().bind(centerY);
        radius = new SimpleDoubleProperty();
        radius.bind(Bindings.min(this.widthProperty(), this.heightProperty()).divide(2));
        field.radiusProperty().bind(radius);
        field.setFill(MyColors.celadon);
        field.setViewOrder(backgroundFieldLayer);
        this.getChildren().add(field);
        creatureBodies = new HashMap<Creature, CreatureBody>();
        pairVisuals = new HashMap<PairingDance, HeartShape>();
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
        c.addCreatureUpdateListener(this);
    }
    @Override
    public void onCreatureUpdate(Creature c) {
        creatureBodies.get(c).setFill(c.mostProminentType().getColor());
    }
    @Override
    public void onCreatureDelete(Creature c) {
        this.getChildren().remove(creatureBodies.get(c));
        creatureBodies.remove(c);
        c.removeCreatureUpdateListener(this);
    }
    @Override
    public void startLoveMaking(PairingDance d) {
        //HeartShape heart = new HeartShape(d.getWife().getX(), d.getWife().getY(), d.getHusband().getX(), d.getHusband().getY(), Color.RED);
        HeartShape heart = new HeartShape(d.getDistance()*2, Color.RED);
        heart.getShapeToShow().setFill(MyColors.bittersweet);
        heart.getShapeToShow().setStroke(MyColors.tomato);
        heart.getShapeToShow().setStrokeWidth(4);
        heart.getShapeToShow().setOpacity(0.75);
        Scale scaleTransfrom = new Scale();
        scaleTransfrom.xProperty().bind(scale);
        scaleTransfrom.yProperty().bind(scale);
        scaleTransfrom.setPivotX(0);
        scaleTransfrom.setPivotY(0);
        //scaleTransfrom.pivotXProperty().bind(centerX);
        //scaleTransfrom.pivotYProperty().bind(centerY);
        heart.getShapeToShow().getTransforms().add(scaleTransfrom);
        heart.getShapeToShow().layoutXProperty().bind(centerX.add(d.centerXProperty().multiply(scale)));
        heart.getShapeToShow().layoutYProperty().bind(centerY.add(d.centerYProperty().multiply(scale)));
        heart.getShapeToShow().setViewOrder(heartLayer);
        pairVisuals.put(d, heart);
        this.getChildren().add(heart.getShapeToShow());
    }
    @Override
    public void endLoveMaking(PairingDance d) {
        this.getChildren().remove(pairVisuals.get(d).getShapeToShow());
        pairVisuals.remove(d);
    }
}

