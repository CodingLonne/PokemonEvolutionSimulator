package evolution;

import java.util.HashMap;
import java.util.Random;

public class Chromosome {

    private final static Random random = new Random();

    public static int maxLenght = 50; 

    private final String dna;

    private static String defenseInit = "10";
    private static String offenseInit = "01";
    //private String defenseInit = "";
    //private String offenseInit = "";

    private HashMap<Type, Integer> proteinDefense;
    private HashMap<Type, Integer> proteinOffense;

    private proteinEncodingManager encoder;

    public Chromosome(String dna, proteinEncodingManager encoder) {
        this.dna = dna.substring(0, Math.min(maxLenght, dna.length()));
        this.encoder = encoder;
        encode();
    }

    private void encode() {
        this.proteinDefense = new HashMap<Type, Integer>();
        this.proteinOffense = new HashMap<Type, Integer>();
        for (Type type : Type.allTypes()) {
            this.proteinDefense.put(type, 0);
            this.proteinOffense.put(type, 0);
        }
        String code;
        Type type;
        for (int i = 0; i<=dna.length()-Math.max(offenseInit.length(), defenseInit.length())-proteinEncodingManager.proteinLenght; i++) {
            if (this.dna.substring(i, i+offenseInit.length()).equals(offenseInit)) {
                code = dna.substring(i+offenseInit.length(), i+offenseInit.length()+proteinEncodingManager.proteinLenght);
                if (encoder.isEncoding(code)) {
                    type = encoder.translate(code);
                    this.proteinOffense.put(type, this.proteinOffense.get(type)+1);
                }
                i += proteinEncodingManager.proteinLenght+offenseInit.length()-1;
            } else if (this.dna.substring(i, i+defenseInit.length()).equals(defenseInit)) {
                code = dna.substring(i+defenseInit.length(), i+defenseInit.length()+proteinEncodingManager.proteinLenght);
                if (encoder.isEncoding(code)) {
                    type = encoder.translate(code);
                    this.proteinDefense.put(type, this.proteinDefense.get(type)+1);
                }
                i += proteinEncodingManager.proteinLenght+defenseInit.length()-1;
            }
        }
    }

    public void update() {
        encode();
    }
    
    public static Chromosome getRandom(proteinEncodingManager encoder) {
        StringBuilder newDna = new StringBuilder();
        for (int i=0; i<maxLenght; i++) {
            newDna.append(random.nextBoolean() ? '1' : '0');
        }
        return new Chromosome(newDna.toString(), encoder);
    }


    public static Chromosome mutate(Chromosome c) {
        int r = random.nextInt(c.getDna().length());
        StringBuilder dnaBuilder = new StringBuilder(c.getDna());
        dnaBuilder.setCharAt(r, dnaBuilder.charAt(r) == '1' ? '0' : '1');
        return new Chromosome(dnaBuilder.toString(), c.getEncoder());
    }

    public static Chromosome crossingOver(Chromosome dna1, Chromosome dna2, proteinEncodingManager encoder) {
        int split = random.nextInt(Chromosome.maxLenght);
        String newDna;
        if (random.nextBoolean()) {
            newDna = dna1.getDna().substring(0, split) + dna2.getDna().substring(dna2.getDna().length()-Chromosome.maxLenght+split, dna2.getDna().length());
        } else {
            newDna = dna2.getDna().substring(0, split) + dna1.getDna().substring(dna1.getDna().length()-Chromosome.maxLenght+split, dna1.getDna().length());
        }
        return new Chromosome(newDna, encoder);
    }

    public String getDna() {
        return dna;
    }
    public HashMap<Type, Integer> getOffenseMap() {
        return proteinOffense;
    }
    public HashMap<Type, Integer> getDefenseMap() {
        return proteinDefense;
    }

    @Override
    public String toString() {
        String result = "";
        result += dna;
        result += "\n";
        for (Type type : Type.allTypes()) {
            result += type.toString() + ": " + proteinDefense.get(type) + " " + proteinOffense.get(type) + "\n";
        }
        return result;
    }

    public static String getDefenseInit() {
        return Chromosome.defenseInit;
    }

    public static String getOffenseInit() {
        return Chromosome.offenseInit;
    }

    public proteinEncodingManager getEncoder() {
        return encoder;
    }
}
