package evolution;

import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PairingDance {
    public interface pairingListener {
        public void startLoveMaking(PairingDance d);
        public void endLoveMaking(PairingDance d);
    }
    private List<pairingListener> listeners;

    private Creature wife;
    private Creature husband;

    private  BreedingSettings breedingSettings;
    private World world;

    private DoubleProperty centerX;
    private DoubleProperty centerY;
    private DoubleProperty distance;

    private boolean wifeArrived;
    private boolean husbandArrived;

    private boolean areMating;

    private int countdown;

    private boolean finished;
    public PairingDance(World world, Creature wife, Creature husband, BreedingSettings breedingSettings) {
        this.breedingSettings = breedingSettings;
        this.world = world;
        listeners = new LinkedList<>();

        this.wife = wife;
        this.husband = husband;
        wifeArrived = false;
        husbandArrived = false;
        areMating = false;
        finished = false;

        centerX = new SimpleDoubleProperty();
        centerX.bind(wife.xProperty().add(husband.xProperty()).divide(2));
        centerY = new SimpleDoubleProperty();
        centerY.bind(wife.yProperty().add(husband.yProperty()).divide(2));
        distance = new SimpleDoubleProperty();
        distance.bind(breedingSettings.breedingProximityProperty());

    }

    public void step() {
        if (finished) return;
        if (areMating) {
            if (countdown <= 0) {
                Creature child = Relationship.breed(world, getChildXPosition(), getChildYPosition(), wife, husband, breedingSettings.getAverageMutations(), breedingSettings.getCrossingOverProbability(), world.getEncoder());
                child.setParents(wife, husband);
                world.addCreature(child);
                finished = true;
                wife.stopPairingDance();
                husband.stopPairingDance();
                for (pairingListener l: listeners) {
                    l.endLoveMaking(this);
                }
            } else {
                countdown--;
            }
        } else if (wifeArrived && husbandArrived && !areMating) {
            areMating = true;
            for (pairingListener l: listeners) {
                l.startLoveMaking(this);
            }
            countdown = 100; //TODO make breeding time a setting
        }
    }

    public void arrived(Creature c) {
        if (c == wife) {
            wifeArrived = true;
        } else if (c == husband) {
            husbandArrived = true;
        }
    }

    public double getPairingXPosition(Creature c) {
        if (c == wife) {
            return centerX.get()-distance.get()/2;
        } else if (c == husband) {
            return centerX.get()+distance.get()/2;
        } else {
            return 0;//centerX.get();
        }
    }

    public double getPairingYPosition(Creature c) {
        return centerY.get();
    }

    public DoubleProperty centerXProperty() {
        return centerX;
    }

    public DoubleProperty centerYProperty() {
        return centerY;
    }

    public double getChildXPosition() {
        return centerX.get();
    }

    public double getChildYPosition() {
        return centerY.get()+distance.get()/2;
    }

    public double getDistance() {
        return distance.get();
    }

    public List<pairingListener> getListeners() {
        return listeners;
    }

    public Creature getWife() {
        return wife;
    }

    public Creature getHusband() {
        return husband;
    }

    public Creature getPartner(Creature c) {
        if (c == wife) {
            return husband;
        } else if (c == husband) {
            return wife;
        } else {
            return null;
        }
    }

    public boolean isFinished() {
        return finished;
    }
}
