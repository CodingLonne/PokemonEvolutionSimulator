package evolution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class FightingSettings {
    private static final double defaultAdvantageMutiplier = 1.6;

    private DoubleProperty advantageMultiplier;

    public FightingSettings(double advantageMultiplier) {
        this.advantageMultiplier = new SimpleDoubleProperty(advantageMultiplier);
    }

    public FightingSettings() {
        this(defaultAdvantageMutiplier);
    }

    //getters
    public double getAdvantageMultiplier() {
        return advantageMultiplier.get();
    }

    //setters
    public void setAdvantageMultiplier(double adv) {
        advantageMultiplier.set(adv);
    }

    //properties
    public DoubleProperty advantageMultiplierProperty() {
        return advantageMultiplier;
    }
}
