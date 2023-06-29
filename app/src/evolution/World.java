package evolution;

import java.util.LinkedList;

import evolution.Interfaces.CreaturePlaceField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class World implements CreaturePlaceField {
    SimpleDoubleProperty worldSize;

    LinkedList<Creature> allCreatures;
    LinkedList<CreatureListener> creatureListeners;
    LinkedList<WorldListener> worldListeners;
    LinkedList<CreatureClickListener> creatureClickListeners;

    proteinEncodingManager geneEncoder;

    SimpleIntegerProperty day;

    interface CreatureListener{
        void onCreatureCreate(Creature c);
        void onCreatureDelete(Creature c);
        void onCreatureUpdate(Creature c);
    }
    interface WorldListener{
        void setupNeededConnections(SimpleIntegerProperty day, SimpleDoubleProperty worldSize);
    }
    interface CreatureClickListener{
        void OnCreatureClick(Creature c);
    }

    World(double size, proteinEncodingManager encoder) {
        worldSize = new SimpleDoubleProperty(size);
        allCreatures = new LinkedList<>();
        creatureListeners = new LinkedList<>();
        worldListeners = new LinkedList<>();
        creatureClickListeners = new LinkedList<>();
        geneEncoder = encoder;
        day = new SimpleIntegerProperty(0);
    }

    public void initialize() {
        for (WorldListener w: worldListeners) {
            w.setupNeededConnections(day, worldSize);
        }
    }

    public void addCreature(double x, double y) {
        Creature newCreature = new Creature(this, x, y, geneEncoder);
        allCreatures.add(newCreature);
        for (CreatureListener listener : creatureListeners) {
            listener.onCreatureCreate(newCreature);
        }
        for (CreatureClickListener listener : creatureClickListeners) {
            newCreature.addCreatureClickListener(listener);
        }
    }

    public void addCreature(double x, double y, Dna dna) {
        Creature newCreature = new Creature(this, x, y, dna, geneEncoder);
        allCreatures.add(newCreature);
        for (CreatureListener listener : creatureListeners) {
            listener.onCreatureCreate(newCreature);
        }
        for (CreatureClickListener listener : creatureClickListeners) {
            newCreature.addCreatureClickListener(listener);
        }
    }

    public void addCreature(Creature c) {
        allCreatures.add(c);
        for (CreatureListener listener : creatureListeners) {
            listener.onCreatureCreate(c);
        }
        for (CreatureClickListener listener : creatureClickListeners) {
            c.addCreatureClickListener(listener);
        }
    }

    public void updateCreature(Creature c) {
        for (CreatureListener listener : creatureListeners) {
            listener.onCreatureUpdate(c);
        }
    }

    public void removeCreature(Creature c) {
        for (CreatureListener listener : creatureListeners) {
            listener.onCreatureDelete(c);
        }
        allCreatures.remove(c);
    }

    public Creature getCreature(int i) {
        return allCreatures.get(i);
    }

    public LinkedList<Creature> getCreatures() {
        return allCreatures;
    }

    public double getWorldSize() {
        return worldSize.get();
    }

    public void setWorldSize(double d) {
        worldSize.set(d);
    }

    public DoubleProperty worldSizeProperty() {
        return worldSize;
    }
    
    public void addCreatureListener(CreatureListener listener) {
        creatureListeners.add(listener);
    }
    
    public void addWorldListener(WorldListener listener) {
        worldListeners.add(listener);
    }

    public void addCreatureClickListener(CreatureClickListener listener) {
        creatureClickListeners.add(listener);
    }

    public proteinEncodingManager getEncoder() {
        return geneEncoder;
    }

}
