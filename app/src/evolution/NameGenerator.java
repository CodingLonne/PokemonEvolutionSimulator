package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import evolution.World.CreatureListener;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NameGenerator implements CreatureListener{
    private File file;
    private boolean giveNames;
    private List<String> allNames;
    private List<String> availableNames;
    private int amountOfNamesGiven;
    private StringProperty currentFile;
    private HashMap<String, Creature> assignments;
    NameGenerator() {
        giveNames = true;
        assignments = new HashMap<>();
        allNames = new LinkedList<>();
        availableNames = new LinkedList<>();
        amountOfNamesGiven = 0;
        currentFile = new SimpleStringProperty();
        boolean succes = setNameFile("names.txt");
        if (!succes) {
            currentFile.set("");
        }
    }

    public int determineIndex(int bottom, int top, String dnaString, int dnaIndex) {
        if (top-bottom<=1) {
            return bottom;
        } else if (dnaIndex<0 || dnaIndex>=dnaString.length()){
            return (bottom+top)/2;
        } else {
            if (dnaString.charAt(dnaIndex) == '1') { //look at bottom half of list
                return determineIndex(bottom+(top-bottom)/2, top, dnaString, dnaIndex+1);
            } else { //look at top half of list
                return determineIndex(bottom, top-(top-bottom)/2, dnaString, dnaIndex+1);
            }
        }
    }

    @Override
    public void onCreatureCreate(Creature c) {
        if (giveNames) {
            if (availableNames.size()>0) {
                int index = determineIndex(0, availableNames.size(), c.getDna().getRandomChromosome().getDna(), 0);
                String name = availableNames.get(index);
                availableNames.remove(index);
                c.giveName(name);
                assignments.put(name, c);
            } else {
                String name = "Creature" + Integer.toString(amountOfNamesGiven++);
                c.giveName(name);
                assignments.put(name, c);
            }
        } else {
            String name = "Creature" + Integer.toString(amountOfNamesGiven++);
            c.giveName(name);
            assignments.put(name, c);
        }
    }
    @Override
    public void onCreatureDelete(Creature c) {
        String name = c.getName();
        assignments.remove(name);
        if (allNames.contains(name)) {
            double around = ((double) allNames.indexOf(name))/allNames.size();
            availableNames.add((int) around*availableNames.size(), name);
        }
    }
    @Override
    public void onCreatureUpdate(Creature c) {
        return;
    }

    //getters
    public String getNameFile() {
        return currentFile.get();
    }

    public Creature getCreatureWithName(String name) {
        if (assignments.containsKey(name)) {
            return assignments.get(name);
        } else {
            return null;
        }
    }

    //setters
    public boolean setNameFile(String filename) {
        List<String> oldNames = new LinkedList<>(allNames);
        List<String> newNames = new LinkedList<>();
        try {
            file = new File("names/" + filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                newNames.add(data);
            }
            myReader.close();
            currentFile.set(filename);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        List<String> takenNames = oldNames.stream().filter(x -> !availableNames.contains(x)).collect(Collectors.toList());
        List<String> newAvailableList = newNames.stream().filter(x -> !takenNames.contains(x)).collect(Collectors.toList());
        this.allNames = newNames;
        this.availableNames = newAvailableList;
        return true;
    }

    //properties
    public ReadOnlyStringProperty nameFileProperty() {
        return currentFile;
    }
}
