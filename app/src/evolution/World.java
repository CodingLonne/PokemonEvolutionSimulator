package evolution;

import java.util.LinkedList;

import evolution.Interfaces.CreaturePlaceField;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class World implements CreaturePlaceField {
    private SimpleDoubleProperty worldSize;

    private LinkedList<Creature> allCreatures;
    private LinkedList<CreatureListener> creatureListeners;
    private LinkedList<WorldListener> worldListeners;
    private LinkedList<CreatureClickListener> creatureClickListeners;

    private proteinEncodingManager geneEncoder;
    private BreedingSettings breedingSettings;
    private FightingSettings fightingSettings;

    private SimpleIntegerProperty day;

    public interface CreatureListener{
        void onCreatureCreate(Creature c);
        void onCreatureDelete(Creature c);
        void onCreatureUpdate(Creature c);
    }
    public interface WorldListener{
        void setupNeededConnections(SimpleIntegerProperty day, SimpleDoubleProperty worldSize);
    }
    public interface CreatureClickListener{
        void OnCreatureClick(Creature c);
    }

    public World(double size, proteinEncodingManager encoder, BreedingSettings breedingSettings, FightingSettings fightingSettings) {
        this.worldSize = new SimpleDoubleProperty(size);
        this.allCreatures = new LinkedList<>();
        this.creatureListeners = new LinkedList<>();
        this.worldListeners = new LinkedList<>();
        this.creatureClickListeners = new LinkedList<>();
        this.geneEncoder = encoder;
        this.breedingSettings = breedingSettings;
        this.fightingSettings = fightingSettings;
        this.day = new SimpleIntegerProperty(0);
    }

    public void initialize() {
        for (WorldListener w: worldListeners) {
            w.setupNeededConnections(day, worldSize);
        }
    }

    //creature adding/removing
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

    //getters
    public Creature getCreature(int i) {
        return allCreatures.get(i);
    }

    public LinkedList<Creature> getCreatures() {
        return allCreatures;
    }

    public double getWorldSize() {
        return worldSize.get();
    }

    public int getDay() {
        return day.get();
    }

    public proteinEncodingManager getEncoder() {
        return geneEncoder;
    }

    //setters
    public void setWorldSize(double d) {
        worldSize.set(d);
    }

    //adders
    public void addCreatureListener(CreatureListener listener) {
        creatureListeners.add(listener);
    }
    
    public void addWorldListener(WorldListener listener) {
        worldListeners.add(listener);
    }

    public void addCreatureClickListener(CreatureClickListener listener) {
        creatureClickListeners.add(listener);
    }

    //properties
    public DoubleProperty worldSizeProperty() {
        return worldSize;
    }

    public ReadOnlyIntegerProperty dayProperty() {
        return day;
    }
}
