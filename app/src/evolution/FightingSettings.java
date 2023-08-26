package evolution;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
//import org.apache.commons.math3;
//import org.apache.commons.math3.distribution.BinomialDistribution;

public class FightingSettings {
    private static final double defaultAdvantageMutiplier = 1.6;
    private static final double defaultMaximumAttackReach = 60;
    private static final double defaultAgressivity = 0.7;
    private static final int defaultInfightingProtection = 3;

    private DoubleProperty advantageMultiplier;
    private DoubleProperty maximumAttackReach;
    private DoubleProperty agressivity;
    private IntegerProperty infightingProtection;

    //private BinomialDistribution BinomialDistribution;

    public FightingSettings(double advantageMultiplier, double maxAttackReach, double agressivity, int infightingProtection) {
        this.advantageMultiplier = new SimpleDoubleProperty(advantageMultiplier);
        this.maximumAttackReach = new SimpleDoubleProperty(maxAttackReach);
        this.agressivity = new SimpleDoubleProperty(agressivity);
        this.infightingProtection = new SimpleIntegerProperty(infightingProtection);
    }

    public FightingSettings() {
        this(defaultAdvantageMutiplier, defaultMaximumAttackReach, defaultAgressivity, defaultInfightingProtection);
    }

    //getters
    public double getAdvantageMultiplier() {
        return advantageMultiplier.get();
    }

    public double getMaximumAttackRange() {
        return maximumAttackReach.get();
    }

    public double getAgressivity() {
        return agressivity.get();
    }

    public int getInfightingProtection() {
        return infightingProtection.get();
    }

    //setters
    public void setAdvantageMultiplier(double adv) {
        advantageMultiplier.set(adv);
    }

    public void setMaximumAttackReach(double r) {
        maximumAttackReach.set(r);
    }

    public void setAgressivity(double a) {
        agressivity.set(a);
    }

    public void setInfightingProtection(int i) {
        infightingProtection.set(i);
    }

    //properties
    public DoubleProperty advantageMultiplierProperty() {
        return advantageMultiplier;
    }

    public DoubleProperty maximumAttackReachProperty() {
        return maximumAttackReach;
    }

    public DoubleProperty agressivityProperty() {
        return agressivity;
    }

    public IntegerProperty infightingProtectionProperty() {
        return infightingProtection;
    }
}
