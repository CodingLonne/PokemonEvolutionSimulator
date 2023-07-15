package evolution.Visual.VisualElements;

import evolution.Creature;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class CreatureLabel extends Label {
    private ObjectProperty<Creature> creature;
    private SimpleBooleanProperty alive;
    public CreatureLabel(Creature c) {
        super();
        creature = new SimpleObjectProperty<>(c);
        creature.addListener(new ChangeListener<Creature>() {

            @Override
            public void changed(ObservableValue<? extends Creature> observable, Creature oldValue, Creature newValue) {
                updateCreature(newValue);
            }
            
        });
        alive = new SimpleBooleanProperty(true);
        setCreature(c);
        updateCreature(c);
    }
    public CreatureLabel(String s, Creature c) {
        super(s);
        creature = new SimpleObjectProperty<>();
        creature.addListener(new ChangeListener<Creature>() {

            @Override
            public void changed(ObservableValue<? extends Creature> observable, Creature oldValue, Creature newValue) {
                updateCreature(newValue);
            }
            
        });
        alive = new SimpleBooleanProperty();
        setCreature(c);
        updateCreature(c);
    }
    private void updateCreature(Creature c) {
        this.textProperty().unbind();
        this.alive.unbind();
        this.textFillProperty().unbind();
        this.alive.set(false);
        if (c != null) {
            this.textProperty().bind(c.nameProperty().concat(Bindings.when(alive).then("").otherwise("☠")));
            this.textFillProperty().bind(Bindings.when(alive).then(c.mostProminentType().getColor()).otherwise(c.mostProminentType().getColor().desaturate()));
            alive.bind(c.aliveProperty());
        } else {
            this.setText("-");
            this.setTextFill(Color.BLACK);
        }
    }

    public Creature getCreature() {
        return creature.get();
    }

    public void setCreature(Creature c) {
        creature.set(c);
    }

    public ObjectProperty<Creature> creatureProperty() {
        return creature;
    }

    public SimpleBooleanProperty aliveProperty() {
        return alive;
    }

    public boolean getAlive() {
        return alive.get();
    }
}
