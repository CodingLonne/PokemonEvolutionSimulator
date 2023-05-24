package evolution;

import java.util.LinkedList;

import javafx.beans.property.SimpleIntegerProperty;

public class World {
    double width;
    double height;

    LinkedList<Creature> allCreatures;
    LinkedList<CreatureListener> creatureListeners;
    LinkedList<WorldListener> worldListeners;
    LinkedList<CreatureClickListener> creatureClickListeners;

    proteinEncodingManager geneEncoder;

    interface CreatureListener{
        void onCreatureCreate(Creature c);
        void onCreatureDelete(Creature c);
        void onCreatureUpdate(Creature c);
    }
    interface WorldListener{
        void setupNeededConnections(SimpleIntegerProperty day);
    }
    interface CreatureClickListener{
        void OnCreatureClick(Creature c);
    }

    World(double size, proteinEncodingManager encoder) {
        this.width = size;
        this.height = size;
        allCreatures = new LinkedList<>();
        creatureListeners = new LinkedList<>();
        worldListeners = new LinkedList<>();
        creatureClickListeners = new LinkedList<>();
        geneEncoder = encoder;
    }

    public void addCreature(int x, int y) {
        Creature newCreature = new Creature(this, x, y, geneEncoder);
        allCreatures.add(newCreature);
        for (CreatureListener listener : creatureListeners) {
            listener.onCreatureCreate(newCreature);
        }
        for (CreatureClickListener listener : creatureClickListeners) {
            newCreature.addCreatureClickListener(listener);
        }
    }

    public void addCreature(int x, int y, Dna dna) {
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
    
    public void addCreatureListener(CreatureListener listener) {
        creatureListeners.add(listener);
    }
    
    public void addWorldListener(WorldListener listener) {
        worldListeners.add(listener);
    }

    public void addCreatureClickListener(CreatureClickListener listener) {
        creatureClickListeners.add(listener);
    }

}
