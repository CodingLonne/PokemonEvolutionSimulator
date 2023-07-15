package evolution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class BreedingSettings {
    private static final double defaultAverageMutations = 3;
    private static final double defaultCrossingOverProbability = 0.05;
    private static final double defaultAttraction = 0.32;
    private static final double defaultBreedingProximity = 50;
    private static final int defaultIncestPrevention = 3;

    private DoubleProperty averageMutations;
    private DoubleProperty crossingOverProbability;
    private DoubleProperty attraction;
    private DoubleProperty breedingProximity;
    private IntegerProperty incestPrevention;
    public BreedingSettings(double averageMutations, double crossingOverProbability, double attraction, double breedingProximity) {
        if (crossingOverProbability<0 || crossingOverProbability>1) {
            throw new IllegalArgumentException("invalid crossing over probability: " + Double.toString(crossingOverProbability));
        }
        if (attraction<0 || attraction>1) {
            throw new IllegalArgumentException("invalid attraction: " + Double.toString(attraction) + ". attraction needs to be between 0 and 1.");
        }
        this.averageMutations = new SimpleDoubleProperty(averageMutations);
        this.crossingOverProbability = new SimpleDoubleProperty(crossingOverProbability);
        this.attraction = new SimpleDoubleProperty(attraction);
        this.breedingProximity = new SimpleDoubleProperty(breedingProximity);
        this.incestPrevention = new SimpleIntegerProperty(defaultIncestPrevention);
    }
    public BreedingSettings() {
        this(defaultAverageMutations, defaultCrossingOverProbability, defaultAttraction, defaultBreedingProximity);
    }
    
    //getters
    public double getAverageMutations() {
        return averageMutations.get();
    }

    public double getCrossingOverProbability() {
        return crossingOverProbability.get();
    }

    public double getAttraction() {
        return attraction.get();
    }

    public double getBreedingProximity() {
        return breedingProximity.get();
    }

    public int getIncestPrevention() {
        return incestPrevention.get();
    }

    //setters
    public void setAverageMutations(double d) {
        averageMutations.set(d);
    }

    public void setCrossingOverProbability(double d) {
        crossingOverProbability.set(d);
    }

    public void setAttraction(double d) {
        attraction.set(d);
    }

    public void setBreedingProximity(double d) {
        breedingProximity.set(d);
    }

    public void setIncestPrevention(int i) {
        incestPrevention.set(i);
    }

    //properties
    public DoubleProperty averageMutationsProperty() {
        return averageMutations;
    }

    public DoubleProperty crossingOverProbabilityProperty() {
        return crossingOverProbability;
    }

    public DoubleProperty attractionProperty() {
        return attraction;
    }

    public DoubleProperty breedingProximityProperty() {
        return breedingProximity;
    }

    public IntegerProperty incestPreventionProperty() {
        return incestPrevention;
    }
}
