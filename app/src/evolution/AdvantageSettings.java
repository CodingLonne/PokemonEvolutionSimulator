package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class AdvantageSettings {
    public enum AttackStrenght {
        STRONG, NORMAL, WEAK;
        public AttackStrenght opposite() {
            switch (this) {
                case STRONG:
                    return WEAK;
                case WEAK:
                    return STRONG;
                default:
                    return NORMAL;
            }
        }
    }
    public static AdvantageSettings settings = new AdvantageSettings();
    private File homefolder;
    private Random random = new Random();

    //outer map attack, inner defense
    //represents attack force
    private Map<Type, Map<Type, ObjectProperty<AttackStrenght>>> advantages;
    protected AdvantageSettings() {
        homefolder = new File("typeConfigurations");
        homefolder.mkdir();
        advantages = new HashMap<Type, Map<Type, ObjectProperty<AttackStrenght>>>();
        for (Type a : Type.allTypes()) {
            advantages.put(a, new HashMap<Type, ObjectProperty<AttackStrenght>>());
            for (Type d : Type.allTypes()) {
                advantages.get(a).put(d, new SimpleObjectProperty<>(AttackStrenght.NORMAL));
            }
        }
        putAllOn(AttackStrenght.NORMAL);
        saveToFile("All normal");
        putOnDefault();
        saveToFile("Default config");
    }
    
    public void putAllOn(AttackStrenght strenght) {
        for (Type a : Type.allTypes()) {
            for (Type d : Type.allTypes()) {
                advantages.get(a).get(d).set(strenght);
            }
        }
    }

    public void putOnDefault() {
        putAllOn(AttackStrenght.NORMAL);
        //bug 
            //strenghts
        advantages.get(Type.BUG).get(Type.GRASS).set(AttackStrenght.STRONG);
        advantages.get(Type.BUG).get(Type.PSYCHIC).set(AttackStrenght.STRONG);
        advantages.get(Type.BUG).get(Type.DARK).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.BUG).get(Type.FIGHTING).set(AttackStrenght.WEAK);
        advantages.get(Type.BUG).get(Type.FLYING).set(AttackStrenght.WEAK);
        advantages.get(Type.BUG).get(Type.POISON).set(AttackStrenght.WEAK);
        advantages.get(Type.BUG).get(Type.GHOST).set(AttackStrenght.WEAK);
        advantages.get(Type.BUG).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.BUG).get(Type.FIRE).set(AttackStrenght.WEAK);
        advantages.get(Type.BUG).get(Type.FAIRY).set(AttackStrenght.WEAK);

        //dark 
            //strenghts
        advantages.get(Type.DARK).get(Type.GHOST).set(AttackStrenght.STRONG);
        advantages.get(Type.DARK).get(Type.PSYCHIC).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.DARK).get(Type.FIGHTING).set(AttackStrenght.WEAK);
        advantages.get(Type.DARK).get(Type.DARK).set(AttackStrenght.WEAK);
        advantages.get(Type.DARK).get(Type.FAIRY).set(AttackStrenght.WEAK);

        //dragon 
            //strenghts
        advantages.get(Type.DRAGON).get(Type.DRAGON).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.DRAGON).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.DRAGON).get(Type.FAIRY).set(AttackStrenght.WEAK);

        //electric 
            //strenghts
        advantages.get(Type.ELECTRIC).get(Type.FLYING).set(AttackStrenght.STRONG);
        advantages.get(Type.ELECTRIC).get(Type.WATER).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.ELECTRIC).get(Type.GROUND).set(AttackStrenght.WEAK);
        advantages.get(Type.ELECTRIC).get(Type.GRASS).set(AttackStrenght.WEAK);
        advantages.get(Type.ELECTRIC).get(Type.ELECTRIC).set(AttackStrenght.WEAK);
        advantages.get(Type.ELECTRIC).get(Type.DRAGON).set(AttackStrenght.WEAK);

        //fairy 
            //strenghts
        advantages.get(Type.FAIRY).get(Type.FIGHTING).set(AttackStrenght.STRONG);
        advantages.get(Type.FAIRY).get(Type.DRAGON).set(AttackStrenght.STRONG);
        advantages.get(Type.FAIRY).get(Type.DARK).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.FAIRY).get(Type.POISON).set(AttackStrenght.WEAK);
        advantages.get(Type.FAIRY).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.FAIRY).get(Type.FIRE).set(AttackStrenght.WEAK);

        //fighting 
            //strenghts
        advantages.get(Type.FIGHTING).get(Type.NORMAL).set(AttackStrenght.STRONG);
        advantages.get(Type.FIGHTING).get(Type.ROCK).set(AttackStrenght.STRONG);
        advantages.get(Type.FIGHTING).get(Type.STEEL).set(AttackStrenght.STRONG);
        advantages.get(Type.FIGHTING).get(Type.ICE).set(AttackStrenght.STRONG);
        advantages.get(Type.FIGHTING).get(Type.DARK).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.FIGHTING).get(Type.FLYING).set(AttackStrenght.WEAK);
        advantages.get(Type.FIGHTING).get(Type.POISON).set(AttackStrenght.WEAK);
        advantages.get(Type.FIGHTING).get(Type.PSYCHIC).set(AttackStrenght.WEAK);
        advantages.get(Type.FIGHTING).get(Type.BUG).set(AttackStrenght.WEAK);
        advantages.get(Type.FIGHTING).get(Type.GHOST).set(AttackStrenght.WEAK);
        advantages.get(Type.FIGHTING).get(Type.FAIRY).set(AttackStrenght.WEAK);

        //fire 
            //strenghts
        advantages.get(Type.FIRE).get(Type.BUG).set(AttackStrenght.STRONG);
        advantages.get(Type.FIRE).get(Type.STEEL).set(AttackStrenght.STRONG);
        advantages.get(Type.FIRE).get(Type.GRASS).set(AttackStrenght.STRONG);
        advantages.get(Type.FIRE).get(Type.ICE).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.FIRE).get(Type.ROCK).set(AttackStrenght.WEAK);
        advantages.get(Type.FIRE).get(Type.FIRE).set(AttackStrenght.WEAK);
        advantages.get(Type.FIRE).get(Type.WATER).set(AttackStrenght.WEAK);
        advantages.get(Type.FIRE).get(Type.DRAGON).set(AttackStrenght.WEAK);

        //flying 
            //strenghts
        advantages.get(Type.FLYING).get(Type.FIGHTING).set(AttackStrenght.STRONG);
        advantages.get(Type.FLYING).get(Type.BUG).set(AttackStrenght.STRONG);
        advantages.get(Type.FLYING).get(Type.GRASS).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.FLYING).get(Type.ROCK).set(AttackStrenght.WEAK);
        advantages.get(Type.FLYING).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.FLYING).get(Type.ELECTRIC).set(AttackStrenght.WEAK);

        //ghost 
            //strenghts
        advantages.get(Type.GHOST).get(Type.GHOST).set(AttackStrenght.STRONG);
        advantages.get(Type.GHOST).get(Type.PSYCHIC).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.GHOST).get(Type.NORMAL).set(AttackStrenght.WEAK);
        advantages.get(Type.GHOST).get(Type.DARK).set(AttackStrenght.WEAK);

        //grass 
            //strenghts
        advantages.get(Type.GRASS).get(Type.GROUND).set(AttackStrenght.STRONG);
        advantages.get(Type.GRASS).get(Type.ROCK).set(AttackStrenght.STRONG);
        advantages.get(Type.GRASS).get(Type.WATER).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.GRASS).get(Type.FLYING).set(AttackStrenght.WEAK);
        advantages.get(Type.GRASS).get(Type.POISON).set(AttackStrenght.WEAK);
        advantages.get(Type.GRASS).get(Type.BUG).set(AttackStrenght.WEAK);
        advantages.get(Type.GRASS).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.GRASS).get(Type.FIRE).set(AttackStrenght.WEAK);
        advantages.get(Type.GRASS).get(Type.GRASS).set(AttackStrenght.WEAK);
        advantages.get(Type.GRASS).get(Type.DRAGON).set(AttackStrenght.WEAK);

        //ground 
            //strenghts
        advantages.get(Type.GROUND).get(Type.POISON).set(AttackStrenght.STRONG);
        advantages.get(Type.GROUND).get(Type.ROCK).set(AttackStrenght.STRONG);
        advantages.get(Type.GROUND).get(Type.STEEL).set(AttackStrenght.STRONG);
        advantages.get(Type.GROUND).get(Type.FIRE).set(AttackStrenght.STRONG);
        advantages.get(Type.GROUND).get(Type.ELECTRIC).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.GROUND).get(Type.FLYING).set(AttackStrenght.WEAK);
        advantages.get(Type.GROUND).get(Type.BUG).set(AttackStrenght.WEAK);
        advantages.get(Type.GROUND).get(Type.GRASS).set(AttackStrenght.WEAK);

        //ice 
            //strenghts
        advantages.get(Type.ICE).get(Type.FLYING).set(AttackStrenght.STRONG);
        advantages.get(Type.ICE).get(Type.GROUND).set(AttackStrenght.STRONG);
        advantages.get(Type.ICE).get(Type.GRASS).set(AttackStrenght.STRONG);
        advantages.get(Type.ICE).get(Type.DRAGON).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.ICE).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.ICE).get(Type.FIRE).set(AttackStrenght.WEAK);
        advantages.get(Type.ICE).get(Type.WATER).set(AttackStrenght.WEAK);
        advantages.get(Type.ICE).get(Type.ICE).set(AttackStrenght.WEAK);

        //normal 
            //strenghts
            //weaknesses
        advantages.get(Type.NORMAL).get(Type.ROCK).set(AttackStrenght.WEAK);
        advantages.get(Type.NORMAL).get(Type.GHOST).set(AttackStrenght.WEAK);
        advantages.get(Type.NORMAL).get(Type.STEEL).set(AttackStrenght.WEAK);

        //poison 
            //strenghts
        advantages.get(Type.POISON).get(Type.GRASS).set(AttackStrenght.STRONG);
        advantages.get(Type.POISON).get(Type.FAIRY).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.POISON).get(Type.POISON).set(AttackStrenght.WEAK);
        advantages.get(Type.POISON).get(Type.GROUND).set(AttackStrenght.WEAK);
        advantages.get(Type.POISON).get(Type.ROCK).set(AttackStrenght.WEAK);
        advantages.get(Type.POISON).get(Type.GHOST).set(AttackStrenght.WEAK);
        advantages.get(Type.POISON).get(Type.STEEL).set(AttackStrenght.WEAK);

        //psychic 
            //strenghts
        advantages.get(Type.PSYCHIC).get(Type.FIGHTING).set(AttackStrenght.STRONG);
        advantages.get(Type.PSYCHIC).get(Type.POISON).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.PSYCHIC).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.PSYCHIC).get(Type.PSYCHIC).set(AttackStrenght.WEAK);
        advantages.get(Type.PSYCHIC).get(Type.DARK).set(AttackStrenght.WEAK);

        //rock 
            //strenghts
        advantages.get(Type.ROCK).get(Type.FLYING).set(AttackStrenght.STRONG);
        advantages.get(Type.ROCK).get(Type.BUG).set(AttackStrenght.STRONG);
        advantages.get(Type.ROCK).get(Type.FIRE).set(AttackStrenght.STRONG);
        advantages.get(Type.ROCK).get(Type.ICE).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.ROCK).get(Type.FIGHTING).set(AttackStrenght.WEAK);
        advantages.get(Type.ROCK).get(Type.GROUND).set(AttackStrenght.WEAK);
        advantages.get(Type.ROCK).get(Type.STEEL).set(AttackStrenght.WEAK);

        //steel 
            //strenghts
        advantages.get(Type.STEEL).get(Type.ROCK).set(AttackStrenght.STRONG);
        advantages.get(Type.STEEL).get(Type.ICE).set(AttackStrenght.STRONG);
        advantages.get(Type.STEEL).get(Type.FAIRY).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.STEEL).get(Type.STEEL).set(AttackStrenght.WEAK);
        advantages.get(Type.STEEL).get(Type.FIRE).set(AttackStrenght.WEAK);
        advantages.get(Type.STEEL).get(Type.WATER).set(AttackStrenght.WEAK);
        advantages.get(Type.STEEL).get(Type.ELECTRIC).set(AttackStrenght.WEAK);

        //water 
            //strenghts
        advantages.get(Type.WATER).get(Type.GROUND).set(AttackStrenght.STRONG);
        advantages.get(Type.WATER).get(Type.ROCK).set(AttackStrenght.STRONG);
        advantages.get(Type.WATER).get(Type.FIRE).set(AttackStrenght.STRONG);
            //weaknesses
        advantages.get(Type.WATER).get(Type.WATER).set(AttackStrenght.WEAK);
        advantages.get(Type.WATER).get(Type.GRASS).set(AttackStrenght.WEAK);
        advantages.get(Type.WATER).get(Type.DRAGON).set(AttackStrenght.WEAK);
    }

    public void putAllRandomly(double strongChance, double weakChance) {
        double normalchance = 1-strongChance-weakChance;
        for (Type a : Type.allTypes()) {
            for (Type d : Type.allTypes()) {
                double r = random.nextDouble();
                if (0<=r && r<strongChance) {
                    setAttacks(a, d, AttackStrenght.STRONG);
                } else if (strongChance<=r && r<strongChance+normalchance) {
                    setAttacks(a, d, AttackStrenght.NORMAL);
                } else {
                    setAttacks(a, d, AttackStrenght.WEAK);
                }
            }
        }
    }

    public boolean saveToFile(String filename) {
        try {
            File newFile = new File("typeConfigurations/" + filename + ".txt");
            newFile.createNewFile();
            FileWriter writeFile = new FileWriter(newFile);
            for (Type a: Type.allTypes()) {
                for (Type d: Type.allTypes()) {
                    switch (this.attacks(a, d)) {
                        case WEAK:
                            writeFile.write('-');
                            break;
                        case STRONG:
                            writeFile.write('+');
                            break;
                        default:
                            writeFile.write('n');
                            break;
                    }
                }
                writeFile.write('\n');
            }
            writeFile.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean readFromFile(String filename) {
        try {
            File readFile = new File("typeConfigurations/" + filename);
            Scanner myReader = new Scanner(readFile);
            int a = 0;
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                for (int d=0; d<Math.min(line.length(), Type.allTypes().length); d++) {
                    switch(line.charAt(d)) {
                        case '+':
                            this.setAttacks(Type.allTypes()[a], Type.allTypes()[d], AttackStrenght.STRONG);
                            break;
                        case '-':
                            this.setAttacks(Type.allTypes()[a], Type.allTypes()[d], AttackStrenght.WEAK);
                            break;
                        default:
                            this.setAttacks(Type.allTypes()[a], Type.allTypes()[d], AttackStrenght.NORMAL);
                            break;
                    }
                }
                a++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public double calculateAttackMultiplier(Type a, Creature d, double adv) {
        return calculateAttackMultiplier(a, d.getDefenseMap(), adv);
    }

    public double calculateAttackMultiplier(Type a, Map<Type, Integer> d, double adv) {
        double damage = 1d;
        for (Type t: d.keySet()) {
            damage *= Math.pow(a.attacks(t, adv), d.get(t));
        }
        return damage;
    }

    //getters
    public AttackStrenght attacks(Type a, Type d) {
        return advantages.get(a).get(d).get();
    } 

    public File getConfigFolder() {
        return homefolder;
    }



    //setters
    public void setAttacks(Type a, Type d, AttackStrenght newValue) {
        advantages.get(a).get(d).set(newValue);
    }

    //properties
    public ObjectProperty<AttackStrenght> attackProperty(Type a, Type d) {
        return advantages.get(a).get(d);
    }
}
