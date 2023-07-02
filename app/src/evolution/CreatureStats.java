package evolution;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import evolution.ScreenManager.screenManagerOwned;
import evolution.VisualElements.HeartShape;
import evolution.VisualElements.MyColors;
import evolution.VisualElements.MyStatsBar;
import evolution.VisualElements.TypePieChart;
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
    private SimpleBooleanProperty alive;
    
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
    private MyStatsBar healthBar;
    private MyStatsBar energyBar;
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
        alive = new SimpleBooleanProperty(true);

        //graphics
        this.setPadding(new Insets(4, 5, 4, 5));
        this.setSpacing(30);
        this.backgroundProperty().bind(Bindings.when(alive).then(new Background(new BackgroundFill(MyColors.wheat, null, new Insets(0)))).otherwise(new Background(new BackgroundFill(MyColors.wheat.grayscale(), null, new Insets(0)))));
        this.styleProperty().bind(Bindings.when(alive).then("-fx-background-color: #EAD2AC;").otherwise(""));
        this.setAlignment(Pos.TOP_CENTER);
        //header
        headerSection = new HBox();
        headerSection.setPadding(new Insets(0, 10, 0, 10));
        titleLabel = new Label();
        titleLabel.textProperty().bind(Bindings.when(creatureSelected).then(title).otherwise("-"));
        titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 40));
        titleLabel.setUnderline(true);
        headerSection.getChildren().add(titleLabel);
        headerSection.setPrefHeight(headerHeight);
        this.getChildren().add(headerSection);
        //basic info
        basicInfoSection = new VBox();
        healthInfo = new Label("Health:");
        energyInfo = new Label("Energy:");
        ageInfo = new Label("Age:");
        healthInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        energyInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        ageInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        healthBar = new MyStatsBar(1);
        healthBar.backgroundColorProperty().bind(Bindings.when(alive).then(MyColors.cherryBlossomPink).otherwise(MyColors.cherryBlossomPink.grayscale()));
        healthBar.barColorProperty().bind(Bindings.when(alive).then(MyColors.folly).otherwise(MyColors.folly.grayscale()));
        healthBar.setPrefWidth(200);
        healthBar.setPrefHeight(30);
        energyBar = new MyStatsBar(1);
        energyBar.backgroundColorProperty().bind(Bindings.when(alive).then(MyColors.lightGreen2).otherwise(MyColors.lightGreen2.grayscale()));
        energyBar.barColorProperty().bind(Bindings.when(alive).then(MyColors.SGBUSGreen).otherwise(MyColors.SGBUSGreen.grayscale()));
        energyBar.setPrefWidth(200);
        energyBar.setPrefHeight(30);
        HBox healthBox = new HBox(healthInfo, healthBar);
        HBox energyBox = new HBox(energyInfo, energyBar);
        healthBox.setPadding(new Insets(3));
        energyBox.setPadding(new Insets(3));
        healthBox.setSpacing(10);
        energyBox.setSpacing(10);
        basicInfoSection.getChildren().addAll(healthBox, energyBox, ageInfo);
        this.getChildren().add(basicInfoSection);
        //enemies and lovers
        relationshipSection = new GridPane();
        relationshipSection.backgroundProperty().bind(Bindings.when(alive).then(new Background(new BackgroundFill(MyColors.dutchWhite, null, new Insets(0))))
                                                                          .otherwise(new Background(new BackgroundFill(MyColors.dutchWhite.grayscale(), null, new Insets(0)))));
        relationshipSection.setAlignment(Pos.TOP_CENTER);
        relationshipSection.prefWidthProperty().bind(this.widthProperty().multiply(0.9));
            //header
            Label relationShipLabel = new Label("Relationships");
            relationShipLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            relationShipLabel.setAlignment(Pos.BOTTOM_CENTER);
            VBox relationShipHeader = new VBox(relationShipLabel);
            relationShipHeader.setAlignment(Pos.TOP_CENTER);
            relationShipHeader.backgroundProperty().bind(Bindings.when(alive).then(new Background(new BackgroundFill(MyColors.bittersweet, new CornerRadii(0), new Insets(0)))).otherwise(new Background(new BackgroundFill(MyColors.bittersweet.grayscale(), new CornerRadii(0), new Insets(0)))));
            relationShipHeader.borderProperty().bind(Bindings.when(alive).then(new Border(new BorderStroke(MyColors.chiliRed, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2)))).otherwise(new Border(new BorderStroke(MyColors.chiliRed.grayscale(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2)))));
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
        piechartDefense.desaturatedProperty().bind(alive.not());
        piechartOffense.desaturatedProperty().bind(alive.not());
        pieChartSection = new HBox(piechartDefense, piechartOffense);
        this.getChildren().add(pieChartSection);
        //type overview
        typeOverviewSection = new GridPane();
        typeOverviewSection.backgroundProperty().bind(Bindings.when(alive).then(new Background(new BackgroundFill(MyColors.dutchWhite, null, new Insets(0))))
                                                                          .otherwise(new Background(new BackgroundFill(MyColors.dutchWhite.grayscale(), null, new Insets(0)))));
        typeOverviewSection.setAlignment(Pos.TOP_CENTER);
        typeOverviewSection.minWidthProperty().bind(this.widthProperty().multiply(0.9));
        typeOverviewSection.setHgap(10);
            Label typeHeaderLabel = new Label("Overview of all genes");
            typeHeaderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            VBox typeDistributionHeader = new VBox(typeHeaderLabel);
            typeDistributionHeader.setAlignment(Pos.TOP_CENTER);
            typeDistributionHeader.backgroundProperty().bind(Bindings.when(alive)
                .then(new Background(new BackgroundFill(MyColors.purpureus, new CornerRadii(0), new Insets(0))))
                .otherwise(new Background(new BackgroundFill(MyColors.purpureus.grayscale(), new CornerRadii(0), new Insets(0)))));
            typeDistributionHeader.borderProperty().bind(Bindings.when(alive)
                .then(new Border(new BorderStroke(MyColors.finn, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))))
                .otherwise(new Border(new BorderStroke(MyColors.finn.grayscale(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2)))));
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
            //defenseTypeLabels.get(t).setTextFill(t.getColor());
            defenseTypeLabels.get(t).textFillProperty().bind(Bindings.when(alive)
                .then(t.getColor())
                .otherwise(t.getColor().desaturate()));
            //offenseTypeLabels.get(t).setTextFill(t.getColor());
            offenseTypeLabels.get(t).textFillProperty().bind(Bindings.when(alive)
                .then(t.getColor())
                .otherwise(t.getColor().desaturate()));
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
        familyTreeHeader.setAlignment(Pos.TOP_CENTER);
        familyTreeHeader.backgroundProperty().bind(Bindings.when(alive)
            .then(new Background(new BackgroundFill(MyColors.limeGreen, new CornerRadii(0), new Insets(0))))
            .otherwise(new Background(new BackgroundFill(MyColors.limeGreen.grayscale(), new CornerRadii(0), new Insets(0)))));
        familyTreeHeader.borderProperty().bind(Bindings.when(alive)
            .then(new Border(new BorderStroke(MyColors.officeGreen, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))))
            .otherwise(new Border(new BorderStroke(MyColors.officeGreen.grayscale(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2)))));
        familyTreeSection.add(familyTreeHeader, 0, 0);
        GridPane.setColumnSpan(familyTreeHeader, 4);

        GrandMotherMothersSideLabel = new CreatureLabel(null);
        VBox GrandMotherMothersSideSection = new VBox(GrandMotherMothersSideLabel);
        styleFamilyTreeBlock(GrandMotherMothersSideLabel, GrandMotherMothersSideSection, 4, 20);
        familyTreeSection.add(GrandMotherMothersSideSection, 0, 1);

        GrandFatherMothersSideLabel = new CreatureLabel(null);
        VBox GrandFatherMothersSideSection = new VBox(GrandFatherMothersSideLabel);
        styleFamilyTreeBlock(GrandFatherMothersSideLabel, GrandFatherMothersSideSection, 4, 20);
        familyTreeSection.add(GrandFatherMothersSideSection, 1, 1);

        GrandMotherFathersSideLabel = new CreatureLabel(null);
        VBox GrandMotherFathersSideSection = new VBox(GrandMotherFathersSideLabel);
        styleFamilyTreeBlock(GrandMotherFathersSideLabel, GrandMotherFathersSideSection, 4, 20);
        familyTreeSection.add(GrandMotherFathersSideSection, 2, 1);

        GrandFatherFathersSideLabel = new CreatureLabel(null);
        VBox GrandFatherFathersSideSection = new VBox(GrandFatherFathersSideLabel);
        styleFamilyTreeBlock(GrandFatherFathersSideLabel, GrandFatherFathersSideSection, 4, 20);
        familyTreeSection.add(GrandFatherFathersSideSection, 3, 1);

        motherLabel = new CreatureLabel(null);
        VBox motherSection = new VBox(motherLabel);
        styleFamilyTreeBlock(motherLabel, motherSection, 2, 30);
        familyTreeSection.add(motherSection, 0, 2);
        GridPane.setColumnSpan(motherSection, 2);

        fatherLabel = new CreatureLabel(null);
        VBox fatherSection = new VBox(fatherLabel);
        styleFamilyTreeBlock(fatherLabel, fatherSection, 2, 30);
        familyTreeSection.add(fatherSection, 2, 2);
        GridPane.setColumnSpan(fatherSection, 2);

        selfLabel = new CreatureLabel(null);
        VBox selfSection = new VBox(selfLabel);
        styleFamilyTreeBlock(selfLabel, selfSection, 1, 40);
        familyTreeSection.add(selfSection, 0, 3);
        GridPane.setColumnSpan(selfSection, 4);
        familyTreeSection.prefHeightProperty().bind(selfSection.heightProperty().add(Bindings.max(fatherSection.heightProperty(), motherSection.heightProperty()).add(Bindings.max(Bindings.max(GrandMotherMothersSideSection.heightProperty(), GrandFatherMothersSideSection.heightProperty()), Bindings.max(GrandMotherFathersSideSection.heightProperty(), GrandFatherFathersSideSection.heightProperty())))).add(familyTreeHeader.heightProperty()));
        this.getChildren().add(familyTreeSection);

        //children
        childrenListSection = new GridPane();
        childrenListSection.backgroundProperty().bind(Bindings.when(alive).then(new Background(new BackgroundFill(MyColors.dutchWhite, null, new Insets(0))))
                                                                          .otherwise(new Background(new BackgroundFill(MyColors.dutchWhite.grayscale(), null, new Insets(0)))));
        
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
    public void styleFamilyTreeBlock(CreatureLabel cLabel, VBox cSection, int divideBy, int fontSize) {
        cLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, fontSize));
        cLabel.setAlignment(Pos.BOTTOM_CENTER);
        cLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (cLabel.getCreature() != null) {
                    setCreature(cLabel.getCreature());
                }
            }
            
        });
        cSection.backgroundProperty().bind(Bindings.when(cLabel.aliveProperty())
            .then(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(0), new Insets(0))))
            .otherwise(new Background(new BackgroundFill(MyColors.dutchWhite.grayscale(), new CornerRadii(0), new Insets(0)))));
        cSection.borderProperty().bind(Bindings.when(cLabel.aliveProperty())
            .then(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2))))
            .otherwise(new Border(new BorderStroke(MyColors.sunset.grayscale(), BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(2)))));
        cSection.prefWidthProperty().bind(familyTreeSection.widthProperty().divide(divideBy));
        cSection.setPrefHeight(familyTreeLabelHeight);
        cSection.setPadding(new Insets(0, 5, 0, 5));
        cSection.setAlignment(Pos.TOP_CENTER);
    }

    public void updateChildrenList(Creature c) {
        int maxInfra = Math.min(Math.min(Math.min(mothersLabels.size(), fatherLabels.size()), childLabels.size()), Math.min(loveLabels.size(), resultLabels.size()));
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
                    heart.getShapeToShow().fillProperty().bind(Bindings.when(alive)
                        .then(MyColors.bittersweet)
                        .otherwise(MyColors.bittersweet.grayscale()));
                    heart.getShapeToShow().strokeProperty().bind(Bindings.when(alive)
                        .then(MyColors.tomato)
                        .otherwise(MyColors.tomato.grayscale()));
                    heart.getShapeToShow().setStrokeWidth(4);
                    loveLabels.add(heart);
                    childrenListSection.add(heart.getShapeToShow(), 1, i+1);
                    //resultSymbol
                    Rectangle base = new Rectangle(-symbolSize, -symbolSize*0.3, symbolSize*0.6, symbolSize*0.6);
                    Polygon point = new Polygon(-symbolSize*0.4, -symbolSize*0.5, -symbolSize*0.4, symbolSize*0.5, 0, 0);
                    Shape arrow = Shape.union(base, point);
                    arrow.fillProperty().bind(Bindings.when(alive)
                        .then(MyColors.robinEggBlue)
                        .otherwise(MyColors.robinEggBlue.grayscale()));
                    arrow.strokeProperty().bind(Bindings.when(alive)
                        .then(MyColors.robinEggBlue2)
                        .otherwise(MyColors.robinEggBlue2.grayscale()));
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
        titleLabel.textFillProperty().unbind();
        titleLabel.setTextFill(Color.BLACK);
        //basic info
        healthBar.progressProperty().unbind();
        energyBar.progressProperty().unbind();
        ageInfo.textProperty().unbind();
        ageInfo.setText("Age: -");
        alive.unbind();
        alive.set(true);
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
            //titleLabel.setTextFill(c.mostProminentType().getColor());
            titleLabel.textFillProperty().bind(Bindings.when(alive)
                .then(c.mostProminentType().getColor())
                .otherwise(c.mostProminentType().getColor().desaturate()));
            //basic info
            healthBar.progressProperty().bind(c.healthProperty().divide(c.maxHealthProperty()));
            energyBar.progressProperty().bind(c.energyProperty().divide(c.maxEnergyProperty()));
            ageInfo.textProperty().bind(Bindings.concat("Age: ", c.ageProperty().asString()));
            alive.bind(c.aliveProperty());
            //type map
            connectTypeMapTo(c);
            //visual type map
            putTypeValues();
        }
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
    SimpleBooleanProperty alive;
    CreatureLabel(Creature c) {
        super();
        alive = new SimpleBooleanProperty(true);
        setCreature(c);
    }
    CreatureLabel(String s, Creature c) {
        super(s);
        alive = new SimpleBooleanProperty();
        setCreature(c);
    }
    public Creature getCreature() {
        return creature;
    }
    public void setCreature(Creature c) {
        this.textProperty().unbind();
        this.alive.unbind();
        this.textFillProperty().unbind();
        this.alive.set(false);
        creature = c;
        if (c != null) {
            this.textProperty().bind(c.nameProperty().concat(Bindings.when(alive).then("").otherwise("☠")));
            this.textFillProperty().bind(Bindings.when(alive).then(c.mostProminentType().getColor()).otherwise(c.mostProminentType().getColor().desaturate()));
            alive.bind(c.aliveProperty());
        } else {
            this.setText("-");
            this.setTextFill(Color.BLACK);
        }
    }
    public SimpleBooleanProperty aliveProperty() {
        return alive;
    }

    public boolean getAlive() {
        return alive.get();
    }
}
