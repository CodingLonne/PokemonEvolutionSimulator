package evolution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javafx.beans.property.SimpleDoubleProperty;

class proteinEncodingManager {
    interface proteinChangeListener{
        public void onProteinChange();
        public void onProteinChangeFinished();
    }
    private double defaultTypeAdvantageStrength = 1.6;
    private SimpleDoubleProperty typeAdvantageStrength;
    private HashMap<String, Type> encodings;
    private HashMap<Type, String> encoders;
    private String defaultSetting = "3679ABCDEHIJKLMOPS";
    private String currentSetting = defaultSetting;
    public static int proteinLenght = 5;
    private LinkedList<proteinChangeListener> listeners;

    public proteinEncodingManager() {
        listeners = new LinkedList<proteinChangeListener>();
        typeAdvantageStrength = new SimpleDoubleProperty(defaultTypeAdvantageStrength);
        setSetting(defaultSetting);
    }

    public boolean isEncoding(String code) {
        if (code.length() != 5) {
            System.out.println("wrong length");
            return false;
        }
        return encodings.containsKey(code);
    }

    public Type translate(String code) {
        return encodings.get(code);
    }
    public String getGene(Type t) {
        return encoders.get(t);
    }

    public String getSetting() {
        return currentSetting;
    }
    public void setSetting(String setting) {
        Type[] allTypes = Type.allTypes();
        HashMap<String, Type> newEncodings = new HashMap<String, Type>();
        HashMap<Type, String> newEncoders = new HashMap<Type, String>();
        for (int i = 0; i<allTypes.length; i++) {
            newEncodings.put(intToBits(charToInt(setting.charAt(i))), allTypes[i]);
            newEncoders.put(allTypes[i], intToBits(charToInt(setting.charAt(i))));
        }
        this.encodings = newEncodings;
        this.encoders = newEncoders;
        this.currentSetting = setting;
        pingListeners();
    }

    public void setSetting(HashMap<String, Type> setting) {
        encodings = setting;
        HashMap<Type, String> newEncoders = new HashMap<Type, String>();
        for (Map.Entry<String, Type> e : setting.entrySet()) {
            newEncoders.put(e.getValue(), e.getKey());
        }
        StringBuilder newSettingString = new StringBuilder("");
        for (Type t: Type.allTypes()) {
            newSettingString.append(intToChar(bitsToInt(newEncoders.get(t))));
        }
        currentSetting = newSettingString.toString();
        pingListeners();
    }

    public String charToBits(char c) {
        return intToBits(charToInt(c));
    }
    public char bitsToChar(String bits) {
        return intToChar(bitsToInt(bits));
    }

    public int charToInt(char c) {
        int ascii = (int) c;
        if (48<=ascii && ascii<58) {
            return ascii-48;
        } else if (65<=ascii && ascii<87){
            return ascii-55;
        } else if (97<=ascii && ascii<119){
            return ascii-87;
        } else {
            return 0;
        }
    }
    public char intToChar(int n) {
        return "0123456789ABCDEFGHIJKLMNOPQRSTUV".charAt(n%((int) Math.pow(2, proteinLenght)));
    }
    public String intToBits(int n) {
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i<proteinLenght; i++) {
            bits.insert(0, 1 == (n/((int) Math.pow(2, i)))%2 ? '1' : '0');
        }
        return bits.toString();
    }
    public int bitsToInt(String bits) {
        int result = 0;
        for (int i = 0; i<bits.length(); i++) {
            result += (bits.charAt(bits.length()-1-i) == '1' ? 1 : 0) * ((int) Math.pow(2, i));
        }
        return result;
    }

    public void pingListeners() {
        for (proteinChangeListener listener : listeners) {
            listener.onProteinChange();
        }
        for (proteinChangeListener listener : listeners) {
            listener.onProteinChangeFinished();
        }
    }

    public void addListener(proteinChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(proteinChangeListener listener) {
        listeners.remove(listener);
    }

    public double getTypeAdvantageStrength() {
        return typeAdvantageStrength.get();
    }

    public SimpleDoubleProperty getTypeAdvantageStrengthProperty() {
        return typeAdvantageStrength;
    }

    @Override
    public String toString() {
        String result = "";
        for (Map.Entry<String, Type> entry : encodings.entrySet()) {
            result += entry.getValue().toString() + ": " + entry.getKey() + "\n";
        }
        return result;
    }
}