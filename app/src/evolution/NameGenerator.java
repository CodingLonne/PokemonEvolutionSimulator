package evolution;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

import evolution.World.CreatureListener;

public class NameGenerator implements CreatureListener{
    File file;
    boolean giveNames;
    LinkedList<String> allNames;
    LinkedList<String> availableNames;
    int amountOfNamesGiven;
    
    @SuppressWarnings("unchecked")
    NameGenerator() {
        giveNames = true;
        allNames = new LinkedList<>();
        amountOfNamesGiven = 0;
        try{
            file = new File("names.txt");
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                allNames.add(data);
            }
            myReader.close();
            availableNames = (LinkedList<String>) allNames.clone();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int determineIndex(int bottom, int top, String dnaString, int dnaIndex) {
        if (top-bottom<=1) {
            return bottom;
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
                int index = determineIndex(0, availableNames.size(), c.getDna().getDna(), 0);
                String name = availableNames.get(index);
                availableNames.remove(index);
                c.giveName(name);
            } else {
                c.giveName("Creature" + Integer.toString(amountOfNamesGiven++));
            }
        } else {
            c.giveName("Creature" + Integer.toString(amountOfNamesGiven++));
        }
    }
    @Override
    public void onCreatureDelete(Creature c) {
        String name = c.getName();
        if (allNames.contains(name)) {
            double around = ((double) allNames.indexOf(name))/allNames.size();
            availableNames.add((int) around*availableNames.size(), name);
        }
    }
    @Override
    public void onCreatureUpdate(Creature c) {
        return;
    }
}
