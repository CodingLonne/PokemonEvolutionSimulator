package evolution;

import java.util.HashSet;
import java.util.Set;

public class Relationship {
    public enum Relation{
        LOVE, HATE, NEUTRAL;
    };
    static int treshhold = 50;
    public static Relation evaluate(Creature c1, Creature c2, int day, int loveGenerations, int hateGenerations, double lovePercentage, double hatePercentage) {
        String dna1 = c1.getDna().getLeftChromosome().getDna()+c1.getDna().getRightChromosome().getDna();
        String dna2 = c2.getDna().getLeftChromosome().getDna()+c2.getDna().getRightChromosome().getDna();
        int loveTreshhold = Double.valueOf(Math.min(Double.valueOf(dna1.length()), Double.valueOf(dna2.length()))*lovePercentage).intValue();
        int hateTreshhold = Double.valueOf(Math.min(Double.valueOf(dna1.length()), Double.valueOf(dna2.length()))*hatePercentage).intValue();
        if (dna1.charAt(day%dna1.length()) == dna2.charAt(day%dna2.length())) { //they have positive/neutral relation if they're the same and negative/neutral if not
            if (compareDna(dna1, dna2) < loveTreshhold) { //their difference has to be small for large emotions
                if (isRelated(c1, c2, loveGenerations)) {
                    return Relation.NEUTRAL;
                } else {
                    return Relation.LOVE;
                }
            } else {
                return Relation.NEUTRAL;
            }
        } else {
            if (compareDna(dna1, dna2) > Math.min(dna1.length(), dna2.length())-hateTreshhold) { //their difference has to be small for large emotions
                if (isRelated(c1, c2, hateGenerations)) {
                    return Relation.NEUTRAL;
                } else {
                    return Relation.HATE;
                }
            } else {
                return Relation.NEUTRAL;
            }
        }
    }

    private static boolean isRelated(Creature c1, Creature c2, int generations) {
        Set<Creature> c1Relatives = getRelatives(c1, generations);
        Set<Creature> c2Relatives = getRelatives(c2, generations);
        Set<Creature> commonRelatives = new HashSet<>(c1Relatives);
        commonRelatives.retainAll(c2Relatives);
        return !commonRelatives.isEmpty();
    }

    private static Set<Creature> getRelatives(Creature c, int generations) {
        if (generations <= 0) {
            return new HashSet<Creature>();
        } else {
            Set<Creature> motherRelatives = c.getMother()==null ? new HashSet<>() : getRelatives(c.getMother(), generations-1);
            Set<Creature> fatherRelatives = c.getFather()==null ? new HashSet<>() : getRelatives(c.getFather(), generations-1);
            Set<Creature> allRelatives = new HashSet<>();
            allRelatives.addAll(motherRelatives);
            allRelatives.addAll(fatherRelatives);
            allRelatives.add(c);
            return allRelatives;
        }
    }

    private static int compareDna(String dna1, String dna2) {
        int differences = 0;
        for (int i = 0; i<Math.min(dna1.length(), dna2.length()); i++) {
            if (dna1.charAt(i) != dna2.charAt(i)) {
                differences++;
            }
        }
        return differences;
    }
    
    public static Creature breed(World world, double x, double y, Creature c1, Creature c2, int mutations, double crossingOverProbability, proteinEncodingManager encoder) {
        Creature child = new Creature(world, x, y, Dna.pair(c1.getDna(), c2.getDna(), mutations, crossingOverProbability, encoder), encoder);
        child.setParents(c1, c2);
        c1.addChild(child);
        c2.addChild(child);
        return child;
    }
}
