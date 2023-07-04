package evolution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class BreedingSettings {
    private static final double defaultAverageMutations = 3;
    private static final double defaultCrossingOverProbability = 0.05;
    private static final double defaultAttraction = 0.32;
    private static final double defaultBreedingProximity = 50;

    private DoubleProperty averageMutations;
    private DoubleProperty crossingOverProbability;
    private DoubleProperty attraction;
    private DoubleProperty breedingProximity;
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

    //properties
    public DoubleProperty averageMutationsProperty() {
        return averageMutations;
    }

    public DoubleProperty CrossingOverProbabilityProperty() {
        return crossingOverProbability;
    }

    public DoubleProperty AttractionProperty() {
        return attraction;
    }

    public DoubleProperty BreedingProximityProperty() {
        return breedingProximity;
    }
}
