package evolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Dna {
    private final static Random random = new Random();
    
    private Chromosome leftChromosome;
    private Chromosome rightChromosome;

    private HashMap<Type, Integer> proteinDefense;
    private HashMap<Type, Integer> proteinOffense;

    private proteinEncodingManager encoder;

    public Dna(String left, String right, proteinEncodingManager encoder) {
        leftChromosome = new Chromosome(left, encoder);
        rightChromosome = new Chromosome(right, encoder);
        this.encoder = encoder;
        encode();
    }

    public Dna(Chromosome left, Chromosome right, proteinEncodingManager encoder) {
        leftChromosome = left;
        rightChromosome = right;
        this.encoder = encoder;
        encode();
    }


    private void encode() {
        this.proteinDefense = new HashMap<Type, Integer>();
        this.proteinOffense = new HashMap<Type, Integer>();
        for (Type t : Type.allTypes()) {
            proteinDefense.put(t, leftChromosome.getDefenseMap().get(t)+rightChromosome.getDefenseMap().get(t));
            proteinOffense.put(t, leftChromosome.getOffenseMap().get(t)+rightChromosome.getOffenseMap().get(t));
        }
    }

    public void update() {
        leftChromosome.update();
        rightChromosome.update();
    }

    //static functions

    public static Dna randomDna(proteinEncodingManager encoder) {
        return new Dna(Chromosome.getRandom(encoder), Chromosome.getRandom(encoder), encoder);
    }

    public static Dna artificialDna(HashMap<Type, Integer> defense, HashMap<Type, Integer> offense, boolean allowRandomness, proteinEncodingManager encoder) {
        LinkedList<Type> defenseTypes = new LinkedList<>();
        LinkedList<Type> offenseTypes = new LinkedList<>();
        for (Type t: Type.allTypes()) {
            //defense genes
            for (int i=0; i<defense.get(t); i++) {
                defenseTypes.add(t);
            }
            //offense genes
            for (int i=0; i<offense.get(t); i++) {
                offenseTypes.add(t);
            }
        }
        //construct the chromosomes
        StringBuilder leftChromosome = new StringBuilder("");
        StringBuilder rightChromosome = new StringBuilder("");
        int geneLength = proteinEncodingManager.proteinLenght-Math.max(Chromosome.getDefenseInit().length(), Chromosome.getOffenseInit().length());
        Random random = new Random();
        int totalGenes = defenseTypes.size()+offenseTypes.size();
        int r;
        Type type;
        boolean isDefense;
        for (int i=totalGenes; i>0; i--) {
            r = random.nextInt(i);//random.nextInt(i);
            if (r<defenseTypes.size()) {
                type = defenseTypes.get(r);
                defenseTypes.remove(r);
                isDefense = true;
            } else {
                type = offenseTypes.get(r-defenseTypes.size());
                offenseTypes.remove(r-defenseTypes.size());
                isDefense = false;
            }
            if (leftChromosome.length()>Chromosome.maxLenght-geneLength) { //left chromosome is full, thus add to right chromosome
                rightChromosome.append((isDefense? Chromosome.getDefenseInit() : Chromosome.getOffenseInit())+encoder.getGene(type));
            } else if (rightChromosome.length()>Chromosome.maxLenght-geneLength) { //right chromosome is full, thus add to left chromosome
                leftChromosome.append((isDefense? Chromosome.getDefenseInit() : Chromosome.getOffenseInit())+encoder.getGene(type));
            } else { //determine randomly
                if (random.nextBoolean()) { //random.nextBoolean()
                    rightChromosome.append((isDefense? Chromosome.getDefenseInit() : Chromosome.getOffenseInit())+encoder.getGene(type));
                } else {
                    leftChromosome.append((isDefense? Chromosome.getDefenseInit() : Chromosome.getOffenseInit())+encoder.getGene(type));
                }
            }
            
        }
        if (allowRandomness) {
            for (int i=0; i<Chromosome.maxLenght-leftChromosome.length(); i++) {
                leftChromosome.append(random.nextBoolean()? '1' : '0');
            }
            for (int i=0; i<Chromosome.maxLenght-rightChromosome.length(); i++) {
                rightChromosome.append(random.nextBoolean()? '1' : '0');
            }
        }
        return new Dna(leftChromosome.toString(), rightChromosome.toString(), encoder);
    }

    private static Dna mutate(Dna dna) {
        if (random.nextBoolean()) {
            return new Dna(Chromosome.mutate(dna.getLeftChromosome()), dna.getRightChromosome(), dna.getEncoder());
        } else {
            return new Dna(dna.getLeftChromosome(), Chromosome.mutate(dna.getRightChromosome()), dna.getEncoder());
        }
    }

    public static Dna pair(Dna dna1, Dna dna2, int mutations, double crossingOverProbability, proteinEncodingManager encoder) {
        Chromosome chro1;
        Chromosome chro2;
        if (random.nextDouble()<crossingOverProbability) { //crossing over
            if (random.nextBoolean()) {
                chro1 = Chromosome.crossingOver(dna1.getLeftChromosome(), dna1.getRightChromosome(), encoder);
                chro2 = dna2.getRandomChromosome();
            } else {
                chro1 = dna1.getRandomChromosome();
                chro2 = Chromosome.crossingOver(dna2.getLeftChromosome(), dna2.getRightChromosome(), encoder);
            }
        } else { //no crossing over
            chro1 = dna1.getRandomChromosome();
            chro2 = dna2.getRandomChromosome();
        } 
        //decide sides
        Chromosome left;
        Chromosome right;
        if (random.nextBoolean()) {
            left = chro1;
            right = chro2;
        } else {
            left = chro2;
            right = chro1;
        }
        Dna newDna = new Dna(left, right, encoder);
        for (int i = 0; i<mutations; i++) {
            newDna = Dna.mutate(newDna);
        }
        return newDna;
    }

    //getters

    public Chromosome getLeftChromosome() {
        return leftChromosome;
    }

    public Chromosome getRightChromosome() {
        return rightChromosome;
    }

    public Chromosome getRandomChromosome() {
        if (random.nextBoolean()) {
            return getLeftChromosome();
        } else {
            return getRightChromosome();
        }
    }

    public proteinEncodingManager getEncoder() {
        return encoder;
    }

    public HashMap<Type, Integer> getOffenseMap() {
        return proteinOffense;
    }
    public HashMap<Type, Integer> getDefenseMap() {
        return proteinDefense;
    }

    //setters

    public void setEncoder(proteinEncodingManager p) {
        encoder = p;
    }
}
