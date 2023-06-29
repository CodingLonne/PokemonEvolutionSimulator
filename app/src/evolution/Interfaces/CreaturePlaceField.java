package evolution.Interfaces;

import evolution.Creature;
import evolution.Dna;
import evolution.proteinEncodingManager;

public interface CreaturePlaceField {
    public void addCreature(double x, double y);
    public void addCreature(double x, double y, Dna dna);
    public void addCreature(Creature c);
    public double getWorldSize();
    public proteinEncodingManager getEncoder();
}

/* methods to call when a (probably visual) class wants to add a creature to the world */
