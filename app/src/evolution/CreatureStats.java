package evolution;

import java.util.HashMap;
import java.util.LinkedList;

import evolution.ScreenManager.screenManagerOwned;
import evolution.World.CreatureClickListener;
import evolution.proteinEncodingManager.proteinChangeListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CreatureStats extends VBox implements screenManagerOwned, proteinChangeListener, CreatureClickListener{
    private SimpleStringProperty title;
    private SimpleBooleanProperty creatureSelected;
    private Creature selectedCreature;
    private HashMap<Type, SimpleIntegerProperty> typeDefenseData;
    private HashMap<Type, SimpleIntegerProperty> typeOffenseData;

    //visual variables
    private double headerHeight = 60;
    private int maxRelationshipList = 5;
    
    //boxes
    private HBox headerSection;
    private VBox basicInfoSection;
    private GridPane relationshipSection;
    private HBox pieChartSection;
    private GridPane typeOverviewSection;

    //components
    private Label titleLabel;
    private Label healthInfo;
    private Label energyInfo;
    private Label ageInfo;
    private CreatureLabel[] loverNames;
    private Label[] loverDistances;
    private CreatureLabel[] enemyNames;
    private Label[] enemyDistances;
    private TypePieChart piechartDefense;
    private TypePieChart piechartOffense;
    private Label defenseHeader;
    private Label offenseHeader;
    CreatureStats(proteinEncodingManager encodingManager) {
        super();
        encodingManager.addListener(this);
        title = new SimpleStringProperty("individual stats");
        creatureSelected = new SimpleBooleanProperty(false);

        //graphics
        this.setPadding(new Insets(4, 5, 4, 5));
        this.setStyle("-fx-background-color: #ff0000;");
        //header
        headerSection = new HBox();
        headerSection.setPadding(new Insets(0, 10, 5, 10));
        titleLabel = new Label();
        titleLabel.textProperty().bind(Bindings.when(creatureSelected).then(title).otherwise("-"));
        titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 40));
        if (selectedCreature != null) {
            titleLabel.setTextFill(selectedCreature.mostProminentType().getColor());
        }
        headerSection.getChildren().add(titleLabel);
        headerSection.setPrefHeight(headerHeight);
        this.getChildren().add(headerSection);
        //basic info
        basicInfoSection = new VBox();
        healthInfo = new Label("Health: -");
        energyInfo = new Label("Energy: -");
        ageInfo = new Label("Age: -");
        healthInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        energyInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        ageInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        basicInfoSection.getChildren().addAll(healthInfo, energyInfo, ageInfo);
        this.getChildren().add(basicInfoSection);
        //enemies and lovers
        relationshipSection = new GridPane();
        relationshipSection.setStyle("-fx-background-color: #EFDCBD;");
        relationshipSection.setAlignment(Pos.TOP_CENTER);
        relationshipSection.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        loverNames = new CreatureLabel[maxRelationshipList];
        loverDistances = new Label[maxRelationshipList];
        enemyNames = new CreatureLabel[maxRelationshipList];
        enemyDistances = new Label[maxRelationshipList];
        Label loversHeader = new Label("Lovers");
        Label enemiesHeader = new Label("Enemies");
        loversHeader.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        enemiesHeader.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        loversHeader.setAlignment(Pos.BOTTOM_CENTER);
        enemiesHeader.setAlignment(Pos.BOTTOM_CENTER);
        relationshipSection.add(loversHeader, 0, 0);
        relationshipSection.add(enemiesHeader, 2, 0);
        for (int i = 0; i<maxRelationshipList; i++) {
            loverNames[i] = new CreatureLabel("-----", null);
            int index = i;
            loverNames[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (loverNames[index].getCreature() != null) {
                        setCreature(loverNames[index].getCreature());
                    }
                }
                
            });
            loverDistances[i] = new Label("-");
            enemyNames[i] = new CreatureLabel("-----", null);
            enemyDistances[i] = new Label("-");
            loverNames[i].setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            loverDistances[i].setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            enemyNames[i].setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            enemyDistances[i].setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            relationshipSection.add(loverNames[i], 0, i+1);
            relationshipSection.add(loverDistances[i], 1, i+1);
            relationshipSection.add(enemyNames[i], 2, i+1);
            relationshipSection.add(enemyDistances[i], 3, i+1);
        }
        this.getChildren().add(relationshipSection);
        //type pie chart
        typeDefenseData = setUpTypePiechartData();
        typeOffenseData = setUpTypePiechartData();
        piechartDefense = new TypePieChart(typeDefenseData, this.widthProperty().divide(2), "Defense", "No creature\nselected");
        piechartOffense = new TypePieChart(typeOffenseData, this.widthProperty().divide(2), "Offense", "No creature\nselected");
        pieChartSection = new HBox(piechartDefense, piechartOffense);
        this.getChildren().add(pieChartSection);
        //type overview
        typeOverviewSection = new GridPane();
        typeOverviewSection.setStyle("-fx-background-color: #EFDCBD;");
        typeOverviewSection.setAlignment(Pos.TOP_CENTER);
        typeOverviewSection.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        defenseHeader = new Label("Defense genes");
        defenseHeader.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
        typeOverviewSection.add(defenseHeader, 0, 0);
        offenseHeader = new Label("Offense genes");
        offenseHeader.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
        typeOverviewSection.add(offenseHeader, 2, 0);
        this.getChildren().add(typeOverviewSection);

        this.minHeightProperty().bind(headerSection.heightProperty().add(basicInfoSection.heightProperty().add(relationshipSection.heightProperty().add(pieChartSection.heightProperty()))).multiply(1.1));
    }

    public HashMap<Type, SimpleIntegerProperty> setUpTypePiechartData() {
        HashMap<Type, SimpleIntegerProperty> newHashMap = new HashMap<Type, SimpleIntegerProperty>();
        for (Type t: Type.allTypes()) {
            newHashMap.put(t, new SimpleIntegerProperty(0));
        }
        return newHashMap;
    }

    @Override
    public void bindHeaderValue(SimpleStringProperty name) {
        name.bind(title);
    }

    @Override
    public void onProteinChange() {
        // onProteinChange in CreatureStats
    }

    @Override
    public void onProteinChangeFinished() {
        // onProteinChangeFinished in CreatureStats
    }

    public void updateRelationShips() {
        LinkedList<Creature> loves = this.selectedCreature.getLoves();
        LinkedList<Creature> enemies = this.selectedCreature.getEnemies();
        for (int i=0; i<maxRelationshipList; i++) {
            if (i<loves.size()) {
                loverNames[i].setCreature(loves.get(i));
                loverDistances[i].setText(Integer.toString((int) this.selectedCreature.getDistanceTo(loves.get(i))));
            } else {
                loverNames[i].setCreature(null);
                loverDistances[i].setText("-");
            }
            if (i<enemies.size()) {
                enemyNames[i].setCreature(enemies.get(i));
                enemyDistances[i].setText(Integer.toString((int) this.selectedCreature.getDistanceTo(enemies.get(i))));
            } else {
                enemyNames[i].setCreature(null);
                enemyDistances[i].setText("-");
            }
        }
    }

    public void disconnect(Creature c) {
        title.unbind();
        titleLabel.setTextFill(Color.BLACK);
        healthInfo.textProperty().unbind();
        healthInfo.setText("Health: -");
        energyInfo.textProperty().unbind();
        energyInfo.setText("Energy: -");
        ageInfo.textProperty().unbind();
        ageInfo.setText("Age: -");
        for (Type t: Type.allTypes()) {
            typeDefenseData.get(t).set(0);
            typeOffenseData.get(t).set(0);
        }
    }

    public void connectTypeMapTo(Creature c) {
        for (Type t: Type.allTypes()) {
            typeDefenseData.get(t).set(c.getDefenseMap().get(t));
            typeOffenseData.get(t).set(c.getOffenseMap().get(t));
        }
    }
    public void connect(Creature c) {
        title.bind(c.nameProperty());
        if (c != null) {
            titleLabel.setTextFill(c.mostProminentType().getColor());
        }
        healthInfo.textProperty().bind(Bindings.concat("Health: ", c.healthProperty().asString()));
        energyInfo.textProperty().bind(Bindings.concat("Energy: ", c.energyProperty().asString()));
        ageInfo.textProperty().bind(Bindings.concat("Age: ", c.ageProperty().asString()));
        connectTypeMapTo(c);
    }
    
    public void setCreature(Creature c) {
        disconnect(selectedCreature);
        connect(c);
        selectedCreature = c;
        if (this.selectedCreature != null) {
            updateRelationShips();
        }
    }

    @Override
    public void OnCreatureClick(Creature c) {
        setCreature(c);
        creatureSelected.set(true);
    }
}

class CreatureLabel extends Label{
    Creature creature;
    CreatureLabel(Creature c) {
        super();
        creature = c;
        if (c != null) {
            this.textProperty().bind(c.nameProperty());
        } else {
            this.setText("-");
        }
    }
    CreatureLabel(String s, Creature c) {
        super(s);
        setCreature(c);
    }
    public Creature getCreature() {
        return creature;
    }
    public void setCreature(Creature c) {
        this.textProperty().unbind();
        creature = c;
        if (c != null) {
            this.textProperty().bind(c.nameProperty());
            this.setTextFill(c.mostProminentType().getColor());
        } else {
            this.setText("-");
            this.setTextFill(Color.BLACK);
        }
    }
}
