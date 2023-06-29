package evolution;

public class Relationship {
    enum Relation{
        LOVE, HATE, NEUTRAL;
    };
    static int treshhold = 32;
    public static Relation evaluate(Creature c1, Creature c2, int day) {
        String dna1 = c1.getDna().getLeftChromosome().getDna()+c1.getDna().getRightChromosome().getDna();
        String dna2 = c2.getDna().getLeftChromosome().getDna()+c2.getDna().getRightChromosome().getDna();
        if (dna1.charAt(day%dna1.length()) == dna2.charAt(day%dna2.length())) { //they have positive/neutral relation if they're the same and negative/neutral if not
            if (compareDna(dna1, dna2) < treshhold) { //their difference has to be small for large emotions
                return Relation.LOVE;
            } else {
                return Relation.NEUTRAL;
            }
        } else {
            if (compareDna(dna1, dna2) < treshhold) { //their difference has to be small for large emotions
                return Relation.HATE;
            } else {
                return Relation.NEUTRAL;
            }
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
