package evolution;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import evolution.Interfaces.CreaturePlaceField;
import evolution.PairingDance.pairingListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class World implements CreaturePlaceField {
    private SimpleDoubleProperty worldSize;
    private SimpleIntegerProperty day;
    private double defaultfps = 50;

    private proteinEncodingManager geneEncoder;
    private BreedingSettings breedingSettings;
    private FightingSettings fightingSettings;

    private List<CreatureListener> creatureListeners;
    private List<WorldListener> worldListeners;
    private List<CreatureClickListener> creatureClickListeners;
    private List<pairingListener> pairingListeners;

    private Timeline timeline;
    private DayTimeManager dayTime;

    private List<Creature> allCreatures;
    private List<PairingDance> pairingDances;
    public interface CreatureListener{
        void onCreatureCreate(Creature c);
        void onCreatureDelete(Creature c);
    }
    public interface CreatureUpdateListener {
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
        this.pairingDances = new LinkedList<>();
        this.creatureListeners = new LinkedList<>();
        this.worldListeners = new LinkedList<>();
        this.creatureClickListeners = new LinkedList<>();
        this.pairingListeners = new LinkedList<>();
        this.geneEncoder = encoder;
        this.breedingSettings = breedingSettings;
        this.fightingSettings = fightingSettings;
        this.day = new SimpleIntegerProperty(0);
        this.dayTime = new DayTimeManager();
        this.timeline = new Timeline(new KeyFrame(new Duration(1000d/defaultfps), e -> step()));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void initialize() {
        for (WorldListener w: worldListeners) {
            w.setupNeededConnections(day, worldSize);
        }
        timeline.play();
    }

    public void shutdown() {
        timeline.stop();
    }

    private synchronized void step() {
        dayTime.step();
        for (Creature c: allCreatures) {
            if (!c.getSleeping()) {
                c.observe();
            }
        }
        for (Creature c: allCreatures) {
            if (!c.getSleeping()) {
                c.act();
            }
        }
        Iterator<PairingDance> danceIterator = pairingDances.iterator();
        while (danceIterator.hasNext()) {
            PairingDance d = danceIterator.next();
            d.step();
            if (d.isFinished()) {
                danceIterator.remove();
            }
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

    public void removeCreature(Creature c) {
        for (CreatureListener listener : creatureListeners) {
            listener.onCreatureDelete(c);
        }
        allCreatures.remove(c);
    }
    //creature requests
    public boolean attemptToStartPairingDance(Creature c1, Creature c2) {
        //if they are both each others focus
        if (!c1.isPairing() && !c2.isPairing() && 
            !c1.getLoves().isEmpty() && !c2.getLoves().isEmpty() && 
             c1.getLoves().getFirst() == c2 && c2.getLoves().getFirst() == c1 && 
            (c1.getEnemies().isEmpty() || c1.getDistanceTo(c1.getLoves().getFirst()) < c1.getDistanceTo(c1.getEnemies().getFirst())) && 
            (c2.getEnemies().isEmpty() || c2.getDistanceTo(c2.getLoves().getFirst()) < c2.getDistanceTo(c2.getEnemies().getFirst()))) {
            PairingDance dance;
            if (c1.getX() < c2.getX()) {
                dance = new PairingDance(this, c1, c2, breedingSettings);
            } else {
                dance = new PairingDance(this, c2, c1, breedingSettings);
            }
            dance.getListeners().addAll(pairingListeners);
            pairingDances.add(dance);
            c1.startPairingDance(dance);
            c2.startPairingDance(dance);
            return true;
        } else {
            return false;
        }
    }

    //getters
    public Creature getCreature(int i) {
        return allCreatures.get(i);
    }

    public List<Creature> getCreatures() {
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

    public BreedingSettings getBreedingSettings() {
        return breedingSettings;
    }

    public FightingSettings getFightingSettings() {
        return fightingSettings;
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

    public void addPairingListener(pairingListener listener) {
        pairingListeners.add(listener);
    }

    //properties
    public DoubleProperty worldSizeProperty() {
        return worldSize;
    }

    public ReadOnlyIntegerProperty dayProperty() {
        return day;
    }
}
