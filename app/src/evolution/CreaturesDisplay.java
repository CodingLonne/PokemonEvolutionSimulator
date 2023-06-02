package evolution;

import java.util.HashMap;
import java.util.LinkedList;

import evolution.World.CreatureListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CreaturesDisplay extends VBox implements CreatureListener{
    //basic
    private LinkedList<Creature> creatures;
    private SimpleIntegerProperty creaturesAmount;
    private SimpleIntegerProperty numAwakeCreatures;
    private HashMap<Creature, SimpleBooleanProperty> awakeStatusses;
    private SimpleStringProperty averageAgeCreatures;
    private SimpleIntegerProperty ageSumCreatures;
    private HashMap<Creature, SimpleIntegerProperty> ageStatusses;

    //visual variables

    //boxes
    private HBox headerSection;
    private VBox basicInfoSection;

    //components
    private Label titleLabel;
    private Label awakeInfo;
    private Label ageInfo;

    public CreaturesDisplay() {
        this.setStyle("-fx-background-color: #EAD2AC;");
        this.setPadding(new Insets(5, 30, 5, 20));
        creatures = new LinkedList<>();
        creaturesAmount = new SimpleIntegerProperty(0);
        numAwakeCreatures = new SimpleIntegerProperty(0);
        awakeStatusses = new HashMap<>();
        averageAgeCreatures = new SimpleStringProperty();
        ageSumCreatures = new SimpleIntegerProperty(0);
        ageStatusses = new HashMap<>();
        averageAgeCreatures.bind(Bindings.createStringBinding(() -> {
            return creaturesAmount.get()==0 ? "-" : Double.toString(Math.round((((double) ageSumCreatures.get())/((double) creaturesAmount.get())*100)/100));
        }, ageSumCreatures, creaturesAmount));

        //header
        headerSection = new HBox();
        titleLabel = new Label("Creature overview");
        titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 40));
        headerSection.getChildren().add(titleLabel);
        headerSection.setPrefHeight(60);
        this.getChildren().add(headerSection);

        //basic info section
        basicInfoSection = new VBox();
        basicInfoSection.setPadding(new Insets(0, 10, 0, 10));
        awakeInfo = new Label("Creatures awake: -/-");
        awakeInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        awakeInfo.textProperty().bind(Bindings.concat("Creatures awake: ", numAwakeCreatures.asString(), "/", creaturesAmount.asString()));
        ageInfo = new Label("Average age: -");
        ageInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        ageInfo.textProperty().bind(Bindings.concat("Average age: ", averageAgeCreatures));
        basicInfoSection.getChildren().addAll(awakeInfo, ageInfo);
        this.getChildren().add(basicInfoSection);
    }
    @Override
    public void onCreatureCreate(Creature c) {
        creaturesAmount.set(creaturesAmount.get()+1);
        creatures.add(c);
        //awake
        SimpleBooleanProperty awake = new SimpleBooleanProperty(false);
        awake.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!oldValue && newValue) {
                    numAwakeCreatures.set(numAwakeCreatures.get() + 1);
                } else if (oldValue && !newValue) {
                    numAwakeCreatures.set(numAwakeCreatures.get() - 1);
                }
            }
        });
        awake.bind(c.sleepingProperty().not());
        awakeStatusses.put(c, awake);
        //age
        SimpleIntegerProperty age = new SimpleIntegerProperty(0);
        age.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                ageSumCreatures.set(ageSumCreatures.get()+(newValue.intValue()-oldValue.intValue()));
            }
        });
        age.bind(c.ageProperty());
        ageStatusses.put(c, age);
    }
    @Override
    public void onCreatureDelete(Creature c) {
        creaturesAmount.set(creaturesAmount.get()-1);
        awakeStatusses.get(c).unbind();
        awakeStatusses.remove(c);
        ageStatusses.get(c).unbind();
        ageStatusses.remove(c);
        creatures.remove(c);
    }
    @Override
    public void onCreatureUpdate(Creature c) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'onCreatureUpdate'");
    }
}
