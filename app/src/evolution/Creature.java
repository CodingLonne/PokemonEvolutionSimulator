package evolution;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import evolution.Relationship.Relation;
import evolution.World.CreatureClickListener;
import evolution.World.CreatureUpdateListener;
import evolution.proteinEncodingManager.proteinChangeListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class Creature implements proteinChangeListener{
    public final static double defaultSize = 16;
    public final static double defaultSpeed = 2;
    public final static double defaultSense = 100;//100;
    public final static double defaultMaxHealth = 100;
    public final static double defaultMaxEnergy = 200;
    private SimpleDoubleProperty maxHealth;
    private SimpleDoubleProperty maxEnergy;

    private Dna dna;
    private World world;

    private SimpleStringProperty name;
    private Creature mother;
    private Creature father;

    private SimpleDoubleProperty health;
    private SimpleDoubleProperty energy;
    private SimpleIntegerProperty age;

    private SimpleBooleanProperty alive;
    private SimpleBooleanProperty isSleeping;
    private int horniness;

    private SimpleDoubleProperty xProperty;
    private SimpleDoubleProperty yProperty;
    private SimpleDoubleProperty size = new SimpleDoubleProperty(defaultSize);
    private SimpleDoubleProperty speed = new SimpleDoubleProperty(defaultSpeed);
    private SimpleDoubleProperty sense = new SimpleDoubleProperty(defaultSense);

    private LinkedList<Creature> loves;
    private LinkedList<Creature> enemies;
    private LinkedList<Creature> children;
    private SimpleIntegerProperty childrenAmount;
    private HashMap<Creature, Relation> relationships;
    private SimpleIntegerProperty kills;

    private Comparator<Creature> byDistance = (c1, c2) -> (int) Math.signum(this.getDistanceTo(c1)-this.getDistanceTo(c2));

    private HashMap<Type, Integer> proteinDefense;
    private HashMap<Type, Integer> proteinOffense;

    private LinkedList<CreatureClickListener> creatureClickListeners;
    private LinkedList<CreatureUpdateListener> creatureUpdateListeners;

    private double facingDirection; //radians

    private PairingDance pairingDance;
    private boolean isPairing;

    public Creature(World world, double x, double y, Dna dna, double health, double energy, proteinEncodingManager encoder) {
        xProperty = new SimpleDoubleProperty(x);
        yProperty = new SimpleDoubleProperty(y);
        this.dna = dna;
        name = new SimpleStringProperty("unnamed");
        this.age = new SimpleIntegerProperty(0);

        maxHealth = new SimpleDoubleProperty(defaultMaxHealth);
        maxEnergy = new SimpleDoubleProperty(defaultMaxEnergy);
        this.health = new SimpleDoubleProperty(health);
        this.energy = new SimpleDoubleProperty(energy);

        this.alive = new SimpleBooleanProperty(true);
        this.isPairing = false;
        this.horniness = 0;
        this.isSleeping = new SimpleBooleanProperty(false);
        this.isSleeping.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                setSleeping(newValue);
            }
            
        });

        proteinDefense = dna.getDefenseMap();
        proteinOffense = dna.getOffenseMap();

        relationships = new HashMap<>();
        loves = new LinkedList<Creature>();
        enemies = new LinkedList<Creature>();
        world.getBreedingSettings().attractionProperty().addListener((p, ov, nv) -> relationships.clear());
        world.getBreedingSettings().incestPreventionProperty().addListener((p, ov, nv) -> relationships.clear());
        world.getFightingSettings().agressivityProperty().addListener((p, ov, nv) -> relationships.clear());
        world.getFightingSettings().infightingProtectionProperty().addListener((p, ov, nv) -> relationships.clear());

        children = new LinkedList<Creature>();
        childrenAmount = new SimpleIntegerProperty(0);
        kills = new SimpleIntegerProperty(0);

        encoder.addListener(this);
        creatureClickListeners = new LinkedList<>();
        creatureUpdateListeners = new LinkedList<>();
        this.world = world;

        facingDirection = angleTowards(0, 0);
    }

    public Creature(World world, double x, double y, Dna dna, proteinEncodingManager encoder) {
        this(world, x, y, dna, defaultMaxHealth, defaultMaxEnergy, encoder);
    }

    public Creature(World world, double x, double y, proteinEncodingManager encoder) {
        this(world, x, y, Dna.randomDna(encoder), defaultMaxHealth, defaultMaxEnergy, encoder);
    }

    public void observe() {
        if (this.isSleeping.get()) return;

        loves.clear();
        enemies.clear();
        for (Creature c: world.getCreatures()) {
            if (!c.getSleeping() && getDistanceTo(c)<sense.get()) {
                if (relationships.containsKey(c)) {
                    if (relationships.get(c) == Relation.LOVE && !c.isPairing()) loves.add(c);
                    else if (relationships.get(c) == Relation.HATE) enemies.add(c);
                } else {
                    Relation relation = Relationship.evaluate(this, c, world.getDay(), world.getBreedingSettings().getIncestPrevention(), 0, world.getBreedingSettings().getAttraction(), world.getFightingSettings().getAgressivity());
                    relationships.put(c, relation);
                    if (relation == Relation.LOVE && !c.isPairing()) loves.add(c);
                    else if (relation == Relation.HATE) enemies.add(c);
                }
            }
        }
        loves.sort(byDistance);
        enemies.sort(byDistance);
        this.updateCreature();
    }

    public void act() {
        if (this.isSleeping.get()) return;

        Relation feeling = getFeelingPriority();
        if (isPairing) {
            if (getDistanceTo(pairingDance.getPairingXPosition(this), pairingDance.getPairingYPosition(this)) < this.speed.get()) {
                setX(pairingDance.getPairingXPosition(this));
                setY(pairingDance.getPairingYPosition(this));
                pairingDance.arrived(this);
            } else {
                facingDirection = angleTowards(pairingDance.getPairingXPosition(this), pairingDance.getPairingYPosition(this));
                move();
            }
        } else if (feeling == Relation.HATE) {
            if (getDistanceTo(enemies.getFirst())<world.getFightingSettings().getMaximumAttackRange()*0.5) {
                facingDirection = angleTowards(enemies.getFirst());
            } else if (getDistanceTo(enemies.getFirst())<world.getFightingSettings().getMaximumAttackRange()) {
                facingDirection = angleTowards(enemies.getFirst());
                move();
            } else {
                facingDirection = angleTowards(enemies.getFirst());
                move();
            }
        } else if (feeling == Relation.LOVE) {
            if (world.attemptToStartPairingDance(this, loves.getFirst())) {
                facingDirection = angleTowards(pairingDance.getPairingXPosition(this), pairingDance.getPairingYPosition(this));
                move();
            } else {
                facingDirection = angleTowards(loves.getFirst());
                move();
            }
        } else if (isOutsideWall()) {
            facingDirection = angleTowards(0, 0);
            move();
        } else {
            move();
        }
    }

    public Type mostProminentType() {
        int amount = 0;
        Type gene = Type.BUG;
        for(Type t : Type.allTypes()) {
            if (proteinDefense.get(t) + proteinOffense.get(t) > amount) {
                amount = proteinDefense.get(t) + proteinOffense.get(t);
                gene = t;
            }
        }
        return gene;
    }
    //commands
    public void startPairingDance(PairingDance pairingDance) {
        this.pairingDance = pairingDance;
        this.isPairing = true;
    }

    public void stopPairingDance() {
        this.pairingDance = null;
        this.isPairing = false;
    }

    //small calculations to help thinking
    public double getDistanceTo(double x, double y) {
        return Math.sqrt(Math.pow(this.getX()-x, 2)+Math.pow(this.getY()-y, 2));
    }

    public double getDistanceTo(Creature other) {
        return Math.sqrt(Math.pow(this.getX()-other.getX(), 2)+Math.pow(this.getY()-other.getY(), 2));
    }

    private boolean isOutsideWall() {
        return Math.pow(getX(), 2)+Math.pow(getY(), 2)>Math.pow(world.getWorldSize(), 2);
    }

    private double angleTowards(double x, double y) {
        return Math.atan2(y-this.getY(), x-this.getX());
    }

    private double angleTowards(Creature c) {
        return angleTowards(c.getX(), c.getY());
    }

    private void move() {
        setX(getX()+Math.cos(facingDirection)*getSpeed());
        setY(getY()+Math.sin(facingDirection)*getSpeed());
    }

    private Relation getFeelingPriority() {
        if (loves.isEmpty() && enemies.isEmpty()) {
            return Relation.NEUTRAL;
        } else if (!loves.isEmpty() && !enemies.isEmpty()) {
            if (this.getDistanceTo(loves.get(0))<this.getDistanceTo(enemies.get(0))) {
                return Relation.LOVE;
            } else {
                return Relation.HATE;
            }
        } else if (loves.isEmpty()) {
            return Relation.HATE;
        } else {
            return Relation.LOVE;
        }
    }

    //getters
    public String getName() {
        return name.get();
    }

    public Dna getDna() {
        return dna;
    }

    public double getX() {
        return xProperty.getValue();
    }

    public double getY() {
        return yProperty.getValue();
    }

    public double getSize() {
        return size.get();
    }

    public double getSpeed() {
        return speed.get();
    }

    public double getSense() {
        return sense.get();
    }

    public double getHealth() {
        return health.get();
    }

    public double getEnergy() {
        return energy.get();
    }

    public double getMaxHealth() {
        return maxHealth.get();
    }

    public double getMaxEnergy() {
        return maxEnergy.get();
    }

    public int getAge() {
        return age.get();
    }

    public boolean getAlive() {
        return alive.get();
    }

    public boolean getSleeping() {
        return isSleeping.get();
    }

    public HashMap<Type, Integer> getDefenseMap() {
        return proteinDefense;
    }

    public HashMap<Type, Integer> getOffenseMap() {
        return proteinOffense;
    }

    public LinkedList<Creature> getLoves() {
        return loves;
    }

    public LinkedList<Creature> getEnemies() {
        return enemies;
    }

    public LinkedList<Creature> getChildren() {
        return children;
    }

    public int getChildrenAmount() {
        return childrenAmount.get();
    }

    public int getKills() {
        return kills.get();
    }

    public Creature getMother() {
        return mother;
    }

    public Creature getFather() {
        return father;
    }

    public boolean isPairing() {
        return isPairing;
    }

    //setters
    public void giveName(String name) {
        this.name.set(name);
    }

    public void setX(double x) {
        xProperty.setValue(x);
    }

    public void setY(double y) {
        yProperty.setValue(y);
    }

    public void setSize(double s) {
        size.set(s);
    }

    public void setSpeed(double s) {
        speed.set(s);
    }

    public void setSense(double s) {
        sense.set(s);
    }

    public void setHealth(double h) {
        health.set(h);
    }

    public void setEnergy(double e) {
        energy.set(e);
    }

    public void setAge(int a) {
        age.set(a);
    }

    public void setMaxHealth(double h) {
        maxHealth.set(h);
    }

    public void setMaxEnergy(double e) {
        maxEnergy.set(e);
    }

    public void setAlive(boolean b) {
        alive.set(b);
    }

    public void setSleeping(boolean b) {
        if (b) {
            fallAsleep();
        } else {
            wakeUp();
        }
    }

    public void wakeUp() {
        facingDirection = angleTowards(0, 0);
        isSleeping.set(false);
    }

    public void fallAsleep() {
        isSleeping.set(true);
        loves.clear();
        enemies.clear();
    } 

    public void setParents(Creature m, Creature f) {
        mother = m;
        father = f;
    }

    //adders
    public void addLove(Creature c) {
        loves.add(c);
    }

    public void addEnemy(Creature c) {
        enemies.add(c);
    }

    public void addChild(Creature c) {
        children.add(c);
        childrenAmount.set(childrenAmount.get()+1);
    }

    public void addKill() {
        kills.set(kills.get()+1);
    }

    //property getters
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty xProperty() {
        return xProperty;
    }

    public SimpleDoubleProperty yProperty() {
        return yProperty;
    }

    public DoubleProperty sizeProperty() {
        return size;
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public DoubleProperty senseProperty() {
        return sense;
    }

    public SimpleDoubleProperty healthProperty() {
        return health;
    }

    public SimpleDoubleProperty energyProperty() {
        return energy;
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public SimpleDoubleProperty maxHealthProperty() {
        return maxHealth;
    }

    public SimpleDoubleProperty maxEnergyProperty() {
        return maxEnergy;
    }

    public SimpleBooleanProperty aliveProperty() {
        return alive;
    }

    public SimpleBooleanProperty sleepingProperty() {
        return isSleeping;
    }

    public ReadOnlyIntegerProperty childrenAmountProperty() {
        return childrenAmount;
    }

    public ReadOnlyIntegerProperty killProperty() {
        return kills;
    }

    //listeners
    public void addCreatureClickListener(CreatureClickListener listener) {
        creatureClickListeners.add(listener);
    }

    public void addCreatureUpdateListener(CreatureUpdateListener listener) {
        creatureUpdateListeners.add(listener);
    }

    public void removeCreatureUpdateListener(CreatureUpdateListener listener) {
        creatureUpdateListeners.remove(listener);
    }
    
    //events
    @Override
    public void onProteinChange() {
        dna.update();
        proteinDefense = dna.getDefenseMap();
        proteinOffense = dna.getOffenseMap();
        this.updateCreature();
    }

    public void updateCreature() {
        for (CreatureUpdateListener listener : creatureUpdateListeners) {
            listener.onCreatureUpdate(this);
        }
    }

    @Override
    public void onProteinChangeFinished() {
        return;
    }

    public void gotClickedOn() {
        for (CreatureClickListener listener : creatureClickListeners) {
            listener.OnCreatureClick(this);
        }
    }
    //tostring

    public String toString() {
        return "creature: \n\n" + dna.toString();
    }
}
