package evolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import evolution.Relationship.Relation;
import evolution.World.CreatureClickListener;
import evolution.proteinEncodingManager.proteinChangeListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Creature implements proteinChangeListener{
    public final static double defaultSize = 16;
    public final static double defaultSpeed = 2;
    public final static double defaultSense = 100;
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
    private double horniness;

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

    private HashMap<Type, Integer> proteinDefense;
    private HashMap<Type, Integer> proteinOffense;

    private LinkedList<CreatureClickListener> creatureClickListeners;

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
        this.horniness = 0d;
        this.isSleeping = new SimpleBooleanProperty(false);

        proteinDefense = dna.getDefenseMap();
        proteinOffense = dna.getOffenseMap();

        relationships = new HashMap<>();
        loves = new LinkedList<Creature>();
        enemies = new LinkedList<Creature>();
        children = new LinkedList<Creature>();
        childrenAmount = new SimpleIntegerProperty(0);
        kills = new SimpleIntegerProperty(0);

        encoder.addListener(this);
        creatureClickListeners = new LinkedList<>();
        this.world = world;
        //TODO remove this
        Random random = new Random();
        this.health.set(random.nextDouble(maxHealth.get()));
        this.energy.set(random.nextDouble(maxEnergy.get()));
        this.age.set(random.nextInt(12));
    }

    public Creature(World world, double x, double y, Dna dna, proteinEncodingManager encoder) {
        this(world, x, y, dna, defaultMaxHealth, defaultMaxEnergy, encoder);
    }

    public Creature(World world, double x, double y, proteinEncodingManager encoder) {
        this(world, x, y, Dna.randomDna(encoder), defaultMaxHealth, defaultMaxEnergy, encoder);
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


    public double getDistanceTo(Creature other) {
        return Math.sqrt(Math.pow(this.getX()-other.getX(), 2)+Math.pow(this.getY()-other.getY(), 2));
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
        isSleeping.set(b);
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
    
    //events
    @Override
    public void onProteinChange() {
        dna.update();
        proteinDefense = dna.getDefenseMap();
        proteinOffense = dna.getOffenseMap();
        this.world.updateCreature(this);
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
