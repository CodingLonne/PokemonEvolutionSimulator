package evolution;

import java.util.Arrays;
import java.util.Comparator;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CreatureStats extends VBox implements screenManagerOwned, proteinChangeListener, CreatureClickListener{
    private SimpleStringProperty title;
    private SimpleBooleanProperty creatureSelected;
    private Creature selectedCreature;
    private HashMap<Type, SimpleIntegerProperty> typeDefenseData;
    private HashMap<Type, SimpleIntegerProperty> typeOffenseData;
    private Comparator<Type> byDefense;
    private Comparator<Type> byOffense;

    //visual variables
    private double headerHeight = 60;
    private int maxRelationshipList = 5;
    private double familyTreeLabelHeight = 50;
    private int symbolSize = 45;
    
    //boxes
    private HBox headerSection;
    private VBox basicInfoSection;
    private GridPane relationshipSection;
    private HBox pieChartSection;
    private GridPane typeOverviewSection;
    private GridPane familyTreeSection;
    private GridPane childrenListSection;

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
    private Label defenseHeaderLabel;
    private Label offenseHeaderLabel;
    private HashMap<Type, Label> defenseTypeLabels;
    private HashMap<Type, Label> offenseTypeLabels;
    private HashMap<Type, Label> defenseTypeValues;
    private HashMap<Type, Label> offenseTypeValues;
    private CreatureLabel selfLabel;
    private CreatureLabel motherLabel;
    private CreatureLabel fatherLabel;
    private CreatureLabel GrandMotherMothersSideLabel;
    private CreatureLabel GrandFatherMothersSideLabel;
    private CreatureLabel GrandMotherFathersSideLabel;
    private CreatureLabel GrandFatherFathersSideLabel;
    private LinkedList<CreatureLabel> mothersLabels;
    private LinkedList<HeartShape> loveLabels;
    private LinkedList<CreatureLabel> fatherLabels;
    private LinkedList<Shape> resultLabels;
    private LinkedList<CreatureLabel> childLabels;
    CreatureStats(proteinEncodingManager encodingManager) {
        super();
        encodingManager.addListener(this);
        title = new SimpleStringProperty("individual stats");
        creatureSelected = new SimpleBooleanProperty(false);

        //graphics
        this.setPadding(new Insets(4, 5, 4, 5));
        this.setSpacing(30);
        this.setStyle("-fx-background-color: #ff0000;");
        this.setAlignment(Pos.TOP_CENTER);
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
        //relationshipSection.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        relationshipSection.prefWidthProperty().bind(this.widthProperty().multiply(0.9));
            //header
            Label relationShipLabel = new Label("relationShips");
            relationShipLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            relationShipLabel.setAlignment(Pos.BOTTOM_CENTER);
            VBox relationShipHeader = new VBox(relationShipLabel);
            relationShipHeader.setBackground(new Background(new BackgroundFill(Color.rgb(249, 111, 93), new CornerRadii(0), new Insets(0))));
            relationShipHeader.setBorder(new Border(new BorderStroke(Color.rgb(224, 56, 34), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
            relationShipHeader.prefWidthProperty().bind(relationshipSection.widthProperty());
            relationshipSection.add(relationShipHeader, 0, 0);
            GridPane.setColumnSpan(relationShipHeader, 4);
            GridPane.setFillWidth(relationShipHeader, true);

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
        relationshipSection.add(loversHeader, 0, 1);
        relationshipSection.add(enemiesHeader, 2, 1);
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
            relationshipSection.add(loverNames[i], 0, i+2);
            relationshipSection.add(loverDistances[i], 1, i+2);
            relationshipSection.add(enemyNames[i], 2, i+2);
            relationshipSection.add(enemyDistances[i], 3, i+2);
        }
        this.getChildren().add(relationshipSection);
        //type pie chart
        typeDefenseData = setUpTypePiechartData();
        typeOffenseData = setUpTypePiechartData();
        piechartDefense = new TypePieChart(typeDefenseData, this.widthProperty().divide(2), "Defense", "No Data\nfound");
        piechartOffense = new TypePieChart(typeOffenseData, this.widthProperty().divide(2), "Offense", "No Data\nfound");
        pieChartSection = new HBox(piechartDefense, piechartOffense);
        this.getChildren().add(pieChartSection);
        //type overview
        typeOverviewSection = new GridPane();
        typeOverviewSection.setStyle("-fx-background-color: #EFDCBD;");
        typeOverviewSection.setAlignment(Pos.TOP_CENTER);
        typeOverviewSection.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        typeOverviewSection.setHgap(10);
        Label typeHeaderLabel = new Label("overview of all genes");
        typeHeaderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
        VBox typeDistributionHeader = new VBox(typeHeaderLabel);
        typeDistributionHeader.setBackground(new Background(new BackgroundFill(Color.rgb(169, 76, 169), new CornerRadii(0), new Insets(0))));
        typeDistributionHeader.setBorder(new Border(new BorderStroke(Color.rgb(113, 51, 113), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        typeDistributionHeader.prefWidthProperty().bind(typeOverviewSection.widthProperty());
        GridPane.setColumnSpan(typeDistributionHeader, 4);
        typeOverviewSection.add(typeDistributionHeader, 0, 0);
        defenseHeaderLabel = new Label("Defense genes");
        offenseHeaderLabel = new Label("Offense genes");
        defenseHeaderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
        offenseHeaderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
        typeOverviewSection.add(defenseHeaderLabel, 0, 1);
        typeOverviewSection.add(offenseHeaderLabel, 2, 1);
        defenseTypeLabels = new HashMap<Type, Label>();
        offenseTypeLabels = new HashMap<Type, Label>();
        defenseTypeValues = new HashMap<Type, Label>();
        offenseTypeValues = new HashMap<Type, Label>();
        byDefense = (Type d1, Type d2)->Integer.valueOf(typeDefenseData.get(d2).get()).compareTo(Integer.valueOf(typeDefenseData.get(d1).get()));
        byOffense = (Type o1, Type o2)->Integer.valueOf(typeOffenseData.get(o2).get()).compareTo(Integer.valueOf(typeOffenseData.get(o1).get()));
        for (Type t: Type.allTypes()) {
            defenseTypeLabels.put(t, new Label(t.toString()));
            offenseTypeLabels.put(t, new Label(t.toString()));
            defenseTypeValues.put(t, new Label());
            offenseTypeValues.put(t, new Label());
            defenseTypeLabels.get(t).setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            offenseTypeLabels.get(t).setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            defenseTypeValues.get(t).setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            offenseTypeValues.get(t).setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            defenseTypeValues.get(t).textProperty().bind(typeDefenseData.get(t).asString());
            offenseTypeValues.get(t).textProperty().bind(typeOffenseData.get(t).asString());
            defenseTypeValues.get(t).setAlignment(Pos.CENTER_RIGHT);
            offenseTypeValues.get(t).setAlignment(Pos.CENTER_RIGHT);
            defenseTypeLabels.get(t).setTextFill(t.getColor());
            offenseTypeLabels.get(t).setTextFill(t.getColor());
        }
        this.getChildren().add(typeOverviewSection);
        //predecessor overview
        familyTreeSection = new GridPane();
        familyTreeSection.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        familyTreeSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.setStyle("-fx-background-color: #EFDCBD;");

        Label familyTreeHeaderLabel = new Label("Family Tree");
        familyTreeHeaderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        familyTreeHeaderLabel.setAlignment(Pos.BOTTOM_CENTER);
        VBox familyTreeHeader = new VBox(familyTreeHeaderLabel);
        familyTreeHeader.setBackground(new Background(new BackgroundFill(Color.rgb(79, 207, 85), new CornerRadii(0), new Insets(0))));
        familyTreeHeader.setBorder(new Border(new BorderStroke(Color.rgb(35, 128, 40), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        familyTreeSection.add(familyTreeHeader, 0, 0);
        GridPane.setColumnSpan(familyTreeHeader, 4);

        GrandMotherMothersSideLabel = new CreatureLabel(null);
        GrandMotherMothersSideLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        GrandMotherMothersSideLabel.setAlignment(Pos.BOTTOM_CENTER);
        GrandMotherMothersSideLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GrandMotherMothersSideLabel.getCreature() != null) {
                    setCreature(GrandMotherMothersSideLabel.getCreature());
                }
            }
            
        });
        VBox GrandMotherMothersSideSection = new VBox(GrandMotherMothersSideLabel);
        GrandMotherMothersSideSection.setBackground(new Background(new BackgroundFill(Color.rgb(239, 220, 189), new CornerRadii(0), new Insets(0))));
        GrandMotherMothersSideSection.setBorder(new Border(new BorderStroke(Color.rgb(231, 203, 156), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        GrandMotherMothersSideSection.prefWidthProperty().bind(familyTreeSection.widthProperty().divide(4));
        GrandMotherMothersSideSection.setPrefHeight(familyTreeLabelHeight);
        GrandMotherMothersSideSection.setPadding(new Insets(0, 5, 0, 5));
        GrandMotherMothersSideSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.add(GrandMotherMothersSideSection, 0, 1);

        GrandFatherMothersSideLabel = new CreatureLabel(null);
        GrandFatherMothersSideLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        GrandFatherMothersSideLabel.setAlignment(Pos.BOTTOM_CENTER);
        GrandFatherMothersSideLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GrandFatherMothersSideLabel.getCreature() != null) {
                    setCreature(GrandFatherMothersSideLabel.getCreature());
                }
            }
            
        });
        VBox GrandFatherMothersSideSection = new VBox(GrandFatherMothersSideLabel);
        GrandFatherMothersSideSection.setBackground(new Background(new BackgroundFill(Color.rgb(239, 220, 189), new CornerRadii(0), new Insets(0))));
        GrandFatherMothersSideSection.setBorder(new Border(new BorderStroke(Color.rgb(231, 203, 156), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        GrandFatherMothersSideSection.prefWidthProperty().bind(familyTreeSection.widthProperty().divide(4));
        GrandFatherMothersSideSection.setPrefHeight(familyTreeLabelHeight);
        GrandFatherMothersSideSection.setPadding(new Insets(0, 5, 0, 5));
        GrandFatherMothersSideSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.add(GrandFatherMothersSideSection, 1, 1);

        GrandMotherFathersSideLabel = new CreatureLabel(null);
        GrandMotherFathersSideLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        GrandMotherFathersSideLabel.setAlignment(Pos.BOTTOM_CENTER);
        GrandMotherFathersSideLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GrandMotherFathersSideLabel.getCreature() != null) {
                    setCreature(GrandMotherFathersSideLabel.getCreature());
                }
            }
            
        });
        VBox GrandMotherFathersSideSection = new VBox(GrandMotherFathersSideLabel);
        GrandMotherFathersSideSection.setBackground(new Background(new BackgroundFill(Color.rgb(239, 220, 189), new CornerRadii(0), new Insets(0))));
        GrandMotherFathersSideSection.setBorder(new Border(new BorderStroke(Color.rgb(231, 203, 156), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        GrandMotherFathersSideSection.prefWidthProperty().bind(familyTreeSection.widthProperty().divide(4));
        GrandMotherFathersSideSection.setPrefHeight(familyTreeLabelHeight);
        GrandMotherFathersSideSection.setPadding(new Insets(0, 5, 0, 5));
        GrandMotherFathersSideSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.add(GrandMotherFathersSideSection, 2, 1);

        GrandFatherFathersSideLabel = new CreatureLabel(null);
        GrandFatherFathersSideLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        GrandFatherFathersSideLabel.setAlignment(Pos.BOTTOM_CENTER);
        GrandFatherFathersSideLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (GrandFatherFathersSideLabel.getCreature() != null) {
                    setCreature(GrandFatherFathersSideLabel.getCreature());
                }
            }
            
        });
        VBox GrandFatherFathersSideSection = new VBox(GrandFatherFathersSideLabel);
        GrandFatherFathersSideSection.setBackground(new Background(new BackgroundFill(Color.rgb(239, 220, 189), new CornerRadii(0), new Insets(0))));
        GrandFatherFathersSideSection.setBorder(new Border(new BorderStroke(Color.rgb(231, 203, 156), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        GrandFatherFathersSideSection.prefWidthProperty().bind(familyTreeSection.widthProperty().divide(4));
        GrandFatherFathersSideSection.setPrefHeight(familyTreeLabelHeight);
        GrandFatherFathersSideSection.setPadding(new Insets(0, 5, 0, 5));
        GrandFatherFathersSideSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.add(GrandFatherFathersSideSection, 3, 1);

        motherLabel = new CreatureLabel(null);
        motherLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        motherLabel.setAlignment(Pos.BOTTOM_CENTER);
        motherLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (motherLabel.getCreature() != null) {
                    setCreature(motherLabel.getCreature());
                }
            }
            
        });
        VBox motherSection = new VBox(motherLabel);
        motherSection.setBackground(new Background(new BackgroundFill(Color.rgb(239, 220, 189), new CornerRadii(0), new Insets(0))));
        motherSection.setBorder(new Border(new BorderStroke(Color.rgb(231, 203, 156), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        motherSection.prefWidthProperty().bind(familyTreeSection.widthProperty().divide(2));
        motherSection.setPrefHeight(familyTreeLabelHeight);
        motherSection.setPadding(new Insets(0, 5, 0, 5));
        motherSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.add(motherSection, 0, 2);
        GridPane.setColumnSpan(motherSection, 2);

        fatherLabel = new CreatureLabel(null);
        fatherLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        fatherLabel.setAlignment(Pos.BOTTOM_CENTER);
        fatherLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (fatherLabel.getCreature() != null) {
                    setCreature(fatherLabel.getCreature());
                }
            }
            
        });
        VBox fatherSection = new VBox(fatherLabel);
        fatherSection.setBackground(new Background(new BackgroundFill(Color.rgb(239, 220, 189), new CornerRadii(0), new Insets(0))));
        fatherSection.setBorder(new Border(new BorderStroke(Color.rgb(231, 203, 156), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        fatherSection.prefWidthProperty().bind(familyTreeSection.widthProperty().divide(2));
        fatherSection.setPrefHeight(familyTreeLabelHeight);
        fatherSection.setPadding(new Insets(0, 5, 0, 5));
        fatherSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.add(fatherSection, 2, 2);
        GridPane.setColumnSpan(fatherSection, 2);

        selfLabel = new CreatureLabel(null);
        selfLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 40));
        selfLabel.setAlignment(Pos.BOTTOM_CENTER);
        VBox selfSection = new VBox(selfLabel);
        selfSection.setBackground(new Background(new BackgroundFill(Color.rgb(239, 220, 189), new CornerRadii(0), new Insets(0))));
        selfSection.setBorder(new Border(new BorderStroke(Color.rgb(231, 203, 156), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))));
        selfSection.prefWidthProperty().bind(familyTreeSection.widthProperty());
        selfSection.setPrefHeight(familyTreeLabelHeight);
        selfSection.setPadding(new Insets(0, 5, 0, 5));
        selfSection.setAlignment(Pos.TOP_CENTER);
        familyTreeSection.add(selfSection, 0, 3);
        GridPane.setColumnSpan(selfSection, 4);
        familyTreeSection.prefHeightProperty().bind(selfSection.heightProperty().add(Bindings.max(fatherSection.heightProperty(), motherSection.heightProperty()).add(Bindings.max(Bindings.max(GrandMotherMothersSideSection.heightProperty(), GrandFatherMothersSideSection.heightProperty()), Bindings.max(GrandMotherFathersSideSection.heightProperty(), GrandFatherFathersSideSection.heightProperty())))).add(familyTreeHeader.heightProperty()));
        this.getChildren().add(familyTreeSection);

        //children
        childrenListSection = new GridPane();
        childrenListSection.setStyle("-fx-background-color: #EFDCBD;");
        childrenListSection.setAlignment(Pos.TOP_CENTER);
        childrenListSection.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        childrenListSection.setHgap(10);
        Label motherLabel = new Label("mother");
        Label fatherLabel = new Label("father");
        Label childLabel = new Label("child");
        motherLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        fatherLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        childLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        childrenListSection.add(motherLabel, 0, 0);
        childrenListSection.add(fatherLabel, 2, 0);
        childrenListSection.add(childLabel, 4, 0);
        childLabels = new LinkedList<>();
        mothersLabels = new LinkedList<>();
        fatherLabels = new LinkedList<>();
        loveLabels = new LinkedList<>();
        resultLabels = new LinkedList<>();
        this.getChildren().add(childrenListSection);
        //height
        this.minHeightProperty().bind(headerSection.heightProperty().add(basicInfoSection.heightProperty().add(relationshipSection.heightProperty().add(pieChartSection.heightProperty().add(typeOverviewSection.heightProperty().add(familyTreeSection.heightProperty().add(childrenListSection.heightProperty())))))).multiply(1.1).add(this.getSpacing()*7));
    }

    public void updateChildrenList(Creature c) {
        int maxInfra = childLabels.size();
        //int maxInfra = Math.min(Math.min(Math.min(mothersLabels.size(), fatherLabels.size()), childLabels.size()), Math.min(loveLabels.size(), resultLabels.size()));
        if (c != null) {
            LinkedList<Creature> children = c.getChildren();
            children.sort((x, y) -> y.getAge()-x.getAge());
            for (int i=0; i<Math.max(children.size(), maxInfra); i++) {
                if (i<children.size() && i<maxInfra) {
                    //child
                    childLabels.get(i).setCreature(children.get(i));
                    childrenListSection.getChildren().remove(childLabels.get(i));
                    childrenListSection.add(childLabels.get(i), 4, i+1);
                    //mother
                    mothersLabels.get(i).setCreature(children.get(i).getMother());
                    childrenListSection.getChildren().remove(mothersLabels.get(i));
                    childrenListSection.add(mothersLabels.get(i), 0, i+1);
                    //father
                    fatherLabels.get(i).setCreature(children.get(i).getFather());
                    childrenListSection.getChildren().remove(fatherLabels.get(i));
                    childrenListSection.add(fatherLabels.get(i), 2, i+1);
                    //lovesymbol
                    childrenListSection.getChildren().remove(loveLabels.get(i).getShapeToShow());
                    childrenListSection.add(loveLabels.get(i).getShapeToShow(), 1, i+1);
                    //resultsymbol
                    childrenListSection.getChildren().remove(resultLabels.get(i));
                    childrenListSection.add(resultLabels.get(i), 3, i+1);
                } else if(i>=children.size()) {
                    childrenListSection.getChildren().remove(childLabels.get(i));
                    childrenListSection.getChildren().remove(mothersLabels.get(i));
                    childrenListSection.getChildren().remove(fatherLabels.get(i));
                    childrenListSection.getChildren().remove(loveLabels.get(i).getShapeToShow());
                    childrenListSection.getChildren().remove(resultLabels.get(i));
                } else if (i>=maxInfra){
                    //child
                    CreatureLabel newChildLabel = new CreatureLabel(children.get(i));
                    newChildLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
                    newChildLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (newChildLabel.getCreature() != null) {
                                setCreature(newChildLabel.getCreature());
                            }
                        }
                        
                    });
                    childLabels.add(newChildLabel);
                    childrenListSection.add(newChildLabel, 4, i+1);
                    //mother
                    CreatureLabel newMotherLabel = new CreatureLabel(children.get(i).getMother());
                    newMotherLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
                    newMotherLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (newMotherLabel.getCreature() != null) {
                                setCreature(newMotherLabel.getCreature());
                            }
                        }
                        
                    });
                    mothersLabels.add(newMotherLabel);
                    childrenListSection.add(newMotherLabel, 0, i+1);
                    //father
                    CreatureLabel newFatherLabel = new CreatureLabel(children.get(i).getFather());
                    newFatherLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
                    newFatherLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            if (newFatherLabel.getCreature() != null) {
                                setCreature(newFatherLabel.getCreature());
                            }
                        }
                        
                    });
                    fatherLabels.add(newFatherLabel);
                    childrenListSection.add(newFatherLabel, 2, i+1);
                    //lovesymbol
                    HeartShape heart = new HeartShape(symbolSize, Color.RED);
                    //edge Color.rgb(249, 87, 56)
                    //fill Color.rgb(248, 112, 96)
                    heart.getShapeToShow().setFill(Color.rgb(248, 112, 96));
                    heart.getShapeToShow().setStroke(Color.rgb(249, 87, 56));
                    heart.getShapeToShow().setStrokeWidth(4);
                    loveLabels.add(heart);
                    childrenListSection.add(heart.getShapeToShow(), 1, i+1);
                    //resultSymbol
                    Rectangle base = new Rectangle(-symbolSize, -symbolSize*0.3, symbolSize*0.6, symbolSize*0.6);
                    Polygon point = new Polygon(-symbolSize*0.4, -symbolSize*0.5, -symbolSize*0.4, symbolSize*0.5, 0, 0);
                    Shape arrow = Shape.union(base, point);
                    arrow.setFill(Color.rgb(85, 221, 224));
                    arrow.setStroke(Color.rgb(31, 211, 214));
                    arrow.setStrokeWidth(4);
                    resultLabels.add(arrow);
                    childrenListSection.add(arrow, 3, i+1);
                }
            }
        }
    }

    public void putTypeValues() {
        LinkedList<Type> ranklist = new LinkedList<Type>(Arrays.asList(Type.allTypes()));
        Type t;
        //defense
        ranklist.sort(byDefense);
        for (int i=0; i<ranklist.size(); i++) {
            t = ranklist.get(i);
            if (typeDefenseData.get(t).get() > 0) {
                typeOverviewSection.add(defenseTypeLabels.get(t), 0, i+2);
                typeOverviewSection.add(defenseTypeValues.get(t), 1, i+2);
            }
        }
        //offense
        ranklist.sort(byOffense);
        for (int i=0; i<ranklist.size(); i++) {
            t = ranklist.get(i);
            if (typeOffenseData.get(t).get() > 0) {
                typeOverviewSection.add(offenseTypeLabels.get(t), 2, i+2);
                typeOverviewSection.add(offenseTypeValues.get(t), 3, i+2);
            }
        }
    }
    public void removeTypeValues() {
        for (Type t: Type.allTypes()) {
            typeOverviewSection.getChildren().remove(defenseTypeLabels.get(t));
            typeOverviewSection.getChildren().remove(offenseTypeLabels.get(t));
            typeOverviewSection.getChildren().remove(defenseTypeValues.get(t));
            typeOverviewSection.getChildren().remove(offenseTypeValues.get(t));
        }
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
        //basic info
        healthInfo.textProperty().unbind();
        healthInfo.setText("Health: -");
        energyInfo.textProperty().unbind();
        energyInfo.setText("Energy: -");
        ageInfo.textProperty().unbind();
        ageInfo.setText("Age: -");
        //family tree
        selfLabel.setCreature(null);
        motherLabel.setCreature(null);
        fatherLabel.setCreature(null);
        GrandMotherMothersSideLabel.setCreature(null);
        GrandFatherMothersSideLabel.setCreature(null);
        GrandMotherFathersSideLabel.setCreature(null);
        GrandFatherFathersSideLabel.setCreature(null);
        //type data map
        for (Type t: Type.allTypes()) {
            typeDefenseData.get(t).set(0);
            typeOffenseData.get(t).set(0);
        }
        //visual type data map
        removeTypeValues();
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
        //basic info
        healthInfo.textProperty().bind(Bindings.concat("Health: ", c.healthProperty().asString()));
        energyInfo.textProperty().bind(Bindings.concat("Energy: ", c.energyProperty().asString()));
        ageInfo.textProperty().bind(Bindings.concat("Age: ", c.ageProperty().asString()));
        //type map
        connectTypeMapTo(c);
        //visual type map
        putTypeValues();
        //family
        selfLabel.setCreature(c);
        if (c != null) {
            motherLabel.setCreature(c.getMother());
            fatherLabel.setCreature(c.getFather());
            if (c.getMother() != null) {
                GrandMotherMothersSideLabel.setCreature(c.getMother().getMother());
                GrandFatherMothersSideLabel.setCreature(c.getMother().getFather());
            }
            if (c.getFather() != null) {
                GrandMotherFathersSideLabel.setCreature(c.getFather().getMother());
                GrandFatherFathersSideLabel.setCreature(c.getFather().getFather());
            }
        }
        //children
        updateChildrenList(c);
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
        //attempted fix at gridheight not updating on creature change.
        this.setHeight((headerSection.getHeight()+
                       basicInfoSection.getHeight()+
                        relationshipSection.getHeight()+
                        pieChartSection.getHeight()+
                        typeOverviewSection.getHeight()+
                        familyTreeSection.getHeight()+
                        childrenListSection.getHeight())*1.2);
    }
}

class CreatureLabel extends Label{
    Creature creature;
    CreatureLabel(Creature c) {
        super();
        creature = c;
        setCreature(c);
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
