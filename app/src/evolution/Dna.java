package evolution;

import java.util.HashMap;
import java.util.Random;

public class Dna {

    private final static Random random = new Random();

    public static int DnaLenght = 128; 

    private final String dna;

    private String defenseInit = "10";
    private String offenseInit = "01";
    //private String defenseInit = "01";
    //private String offenseInit = "10";

    private HashMap<Type, Integer> proteinDefense;
    private HashMap<Type, Integer> proteinOffense;

    private proteinEncodingManager encoder;

    Dna(String dna, proteinEncodingManager encoder) {
        this.dna = dna;
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
        for (int i = 0; i<dna.length()-Math.max(offenseInit.length(), defenseInit.length())-proteinEncodingManager.proteinLenght; i++) {
            if (this.dna.substring(i, i+offenseInit.length()).equals(offenseInit)) {
                code = dna.substring(i+offenseInit.length(), i+offenseInit.length()+proteinEncodingManager.proteinLenght);
                if (encoder.isEncoding(code)) {
                    type = encoder.translate(code);
                    this.proteinOffense.put(type, this.proteinOffense.get(type)+1);
                    i += proteinEncodingManager.proteinLenght+offenseInit.length()-1;
                }
            } else if (this.dna.substring(i, i+defenseInit.length()).equals(defenseInit)) {
                code = dna.substring(i+defenseInit.length(), i+defenseInit.length()+proteinEncodingManager.proteinLenght);
                if (encoder.isEncoding(code)) {
                    type = encoder.translate(code);
                    this.proteinDefense.put(type, this.proteinDefense.get(type)+1);
                    i += proteinEncodingManager.proteinLenght+defenseInit.length()-1;
                }
            }
        }
    }

    /* 
    private void encode() {
        this.proteinDefense = new HashMap<Type, Integer>();
        this.proteinOffense = new HashMap<Type, Integer>();
        for (Type type : Type.allTypes()) {
            this.proteinDefense.put(type, 0);
            this.proteinOffense.put(type, 0);
        }
        String code;
        Type type;
        for (int i = 0; i<dna.length()-2-proteinEncodingManager.proteinLenght; i++) {
            code = dna.substring(i+2, i+2+proteinEncodingManager.proteinLenght);
            if (encoder.isEncoding(code)) {
                type = encoder.translate(code);
                if (this.dna.charAt(i) == '0' && this.dna.charAt(i+1) == '1') {
                    this.proteinOffense.put(type, this.proteinOffense.get(type)+1);
                    i += proteinEncodingManager.proteinLenght+1;
                } else if (this.dna.charAt(i) == '1' && this.dna.charAt(i+1) == '0'){
                    this.proteinDefense.put(type, this.proteinDefense.get(type)+1);
                    i += proteinEncodingManager.proteinLenght+1;
                }
            }
        }
    }
    */

    public void update() {
        encode();
    }
    
    public static Dna randomDna(proteinEncodingManager encoder) {
        StringBuilder newDna = new StringBuilder();
        for (int i=0; i<DnaLenght; i++) {
            newDna.append(random.nextBoolean() ? '1' : '0');
        }
        return new Dna(newDna.toString(), encoder);
    }

    public static Dna pair(Dna dna1, Dna dna2, int mutations, proteinEncodingManager encoder) {
        String dnaString1 = dna1.getDna();
        String dnaString2 = dna2.getDna();
        String newDnaString = crossingOver(dnaString1, dnaString2);
        for (int i = 0; i<mutations; i++) {
            newDnaString = mutate(newDnaString.toString());
        }
        return new Dna(newDnaString.toString(), encoder);
    }

    private static String crossingOver(String dna1, String dna2) {
        int split = random.nextInt(DnaLenght);
        String newDna = dna1.substring(0, split) + dna2.substring(split, dna2.length());
        return newDna;
    }

    private static String mutate(String dna) {
        int r = random.nextInt(DnaLenght);
        StringBuilder dnaBuilder = new StringBuilder(dna);
        dnaBuilder.setCharAt(r, dnaBuilder.charAt(r) == '1' ? '0' : '1');
        return dnaBuilder.toString();
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
}
