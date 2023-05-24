package evolution;

import java.util.HashMap;
import java.util.LinkedList;

import evolution.Relationship.Relation;
import evolution.World.CreatureClickListener;
import evolution.proteinEncodingManager.proteinChangeListener;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Creature implements proteinChangeListener{
    private Dna dna;
    private World world;

    private SimpleStringProperty name;
    private Creature mother;
    private Creature father;
    private SimpleDoubleProperty health;
    private SimpleDoubleProperty energy;
    private SimpleIntegerProperty age;
    private double horniness;
    private SimpleDoubleProperty xProperty;
    private SimpleDoubleProperty yProperty;

    private LinkedList<Creature> loves;
    private LinkedList<Creature> enemies;
    private HashMap<Creature, Relation> relationships;

    private HashMap<Type, Integer> proteinDefense;
    private HashMap<Type, Integer> proteinOffense;

    private LinkedList<CreatureClickListener> creatureClickListeners;

    Creature(World world, double x, double y, Dna dna, double health, double energy, proteinEncodingManager encoder) {
        xProperty = new SimpleDoubleProperty(x);
        yProperty = new SimpleDoubleProperty(y);
        this.dna = dna;
        name = new SimpleStringProperty("unnamed");
        this.age = new SimpleIntegerProperty(0);
        this.health = new SimpleDoubleProperty(health);
        this.energy = new SimpleDoubleProperty(energy);
        this.horniness = 0d;

        proteinDefense = dna.getDefenseMap();
        proteinOffense = dna.getOffenseMap();

        relationships = new HashMap<>();
        loves = new LinkedList<Creature>();
        enemies = new LinkedList<Creature>();

        encoder.addListener(this);
        creatureClickListeners = new LinkedList<>();
        this.world = world;
    }

    Creature(World world, double x, double y, Dna dna, proteinEncodingManager encoder) {
        this(world, x, y, dna, 100, 200, encoder);
    }

    Creature(World world, double x, double y, proteinEncodingManager encoder) {
        this(world, x, y, Dna.randomDna(encoder), 100, 200, encoder);
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

    public double getHealth() {
        return health.get();
    }

    public double getEnergy() {
        return energy.get();
    }

    public int getAge() {
        return age.get();
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

    public void setHealth(double h) {
        health.set(h);
    }

    public void setEnergy(double e) {
        energy.set(e);
    }

    public void setAge(int a) {
        age.set(a);
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

    //property getters
    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty getXProperty() {
        return xProperty;
    }

    public SimpleDoubleProperty getYProperty() {
        return yProperty;
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
