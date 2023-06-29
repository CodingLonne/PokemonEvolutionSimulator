package evolution;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import evolution.Interfaces.CreaturePlaceField;
import evolution.VisualElements.MyCheckboxSkin;
import evolution.VisualElements.MyChoiceBoxSkin;
import evolution.VisualElements.MyColors;
import evolution.VisualElements.TypeChoiceBox;
import evolution.World.CreatureListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class CreaturesDisplay extends VBox implements CreatureListener{
    //basic
    private LinkedList<Creature> creatures;
    private List<CreaturePlaceField> creatureBiomes;
    private SimpleIntegerProperty creaturesAmount;
    private SimpleIntegerProperty numAwakeCreatures;
    private HashMap<Creature, SimpleBooleanProperty> awakeStatusses;
    private SimpleStringProperty averageAgeCreatures;
    private SimpleIntegerProperty ageSumCreatures;
    private HashMap<Creature, SimpleIntegerProperty> ageStatusses;

    //creature adding variables
    private SimpleIntegerProperty creatureAddAmount;
    private SimpleStringProperty creatureAddWhere;
    private SimpleBooleanProperty creatureAddGeneModification;
    private SimpleObjectProperty<Type> modAddGene;
    private SimpleObjectProperty<Type> modRemoveGene;
    private SimpleStringProperty modAddToWhich;
    private SimpleStringProperty modRemoveFromWhich;
    private int maxGenes;
    private SimpleIntegerProperty geneAmount;
    private HashMap<Type, SimpleIntegerProperty> typeDefenseData;
    private HashMap<Type, SimpleIntegerProperty> typeOffenseData;
    private Comparator<Type> byDefense;
    private Comparator<Type> byOffense;
    private SimpleBooleanProperty geneRandomness;

    //gene amounts
    private HashMap<Type, SimpleIntegerProperty> typeDefenseAmount;
    private HashMap<Type, SimpleIntegerProperty> typeOffenseAmount;

    //visual variables

    //boxes
    private HBox headerSection;
    private VBox basicInfoSection;
    private VBox creatureAddSection;
        private VBox engineeringBox;
            private HBox addGenesBox;
            private HBox removeGenesBox;
            private HBox geneInfoBox;
                private TypePieChart defensePieChart;
                private VBox defenseGeneList;
                private TypePieChart offensePieChart;
                private VBox offenseGeneList;
            private HBox suppressRandomnessBox;
        private VBox spawnButtonBox;
    private HBox pieChartSection;
        private TypePieChart defenseAmountPieChart;
        private TypePieChart offenseAmountPieChart;

    //components
    private Label titleLabel;
    private Label awakeInfo;
    private Label ageInfo;
    private TextField amountField;
    private ChoiceBox<String> whereChoiceBox;
    private CheckBox geneticEngineeringBox;
    private ChoiceBox<Type> addTypeChoiceBox;
    private ChoiceBox<String> addDefOffChoiceBox;
    private ChoiceBox<Type> removeTypeChoiceBox;
    private ChoiceBox<String> removeDefOffChoiceBox;
    private HashMap<Type, Node> typeDefenseNodes;
    private HashMap<Type, Node> typeOffenseNodes;
    private CheckBox suppressRandomnessCheck;
    private Button spawnButton;

    public CreaturesDisplay() {
        this.setStyle("-fx-background-color: #EAD2AC;");
        this.setPadding(new Insets(5, 30, 5, 20));
        this.setSpacing(10);
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
        creatureBiomes = new LinkedList<>();
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

        //creature add section
        //enemies and lovers
        creatureAddSection = new VBox();
        creatureAddSection.setBackground(new Background(new BackgroundFill(MyColors.lightGreen, new CornerRadii(5), new Insets(0))));
        creatureAddSection.setBorder(new Border(new BorderStroke(MyColors.darkPastelGreen, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1), new Insets(0))));
        creatureAddSection.setAlignment(Pos.TOP_LEFT);
        creatureAddSection.prefWidthProperty().bind(this.widthProperty().multiply(0.9));
            //header
            Label creatureAddHeaderLabel = new Label("Creature adding");
            creatureAddHeaderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            creatureAddHeaderLabel.setAlignment(Pos.BOTTOM_CENTER);
            VBox creatureAddHeader = new VBox(creatureAddHeaderLabel);
            creatureAddHeader.setAlignment(Pos.TOP_CENTER);
            creatureAddHeader.setBackground(new Background(new BackgroundFill(MyColors.malachite, new CornerRadii(5), new Insets(-2))));
            creatureAddHeader.setBorder(new Border(new BorderStroke(MyColors.officeGreen, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2), new Insets(-2))));
            creatureAddHeader.prefWidthProperty().bind(creatureAddSection.widthProperty());
            creatureAddSection.getChildren().add(creatureAddHeader);
        //amount selection
        Label amountLabel = new Label("Amount: ");
        amountLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        amountLabel.setAlignment(Pos.CENTER_LEFT);
        amountField = new TextField("1");
        creatureAddAmount = new SimpleIntegerProperty(0);
        creatureAddAmount.bind(Bindings.createIntegerBinding(() -> {
            for (char c : amountField.getText().toCharArray()) {
                if (!Character.isDigit(c)) {
                    return 0;
                }
            }
            if (amountField.getText().equals("")) {
                return 0;
            }
            return Integer.parseInt(amountField.getText());
        }, amountField.textProperty()));
        amountField.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(12), amountField.getInsets())));
        amountField.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        amountField.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        amountField.setPrefColumnCount(3);
        HBox amountBox = new HBox(amountLabel, amountField);
        amountBox.setPadding(new Insets(5, 10, 5, 10));
        amountBox.setAlignment(Pos.CENTER_LEFT);
        creatureAddSection.getChildren().add(amountBox);
        //where selection
        Label whereLabel = new Label("Where: ");
        whereLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        whereLabel.setAlignment(Pos.CENTER_LEFT);
        whereChoiceBox = new ChoiceBox<>();
        whereChoiceBox.getItems().addAll("along the edge", "in the middle");
        whereChoiceBox.setValue("in the middle");
        MyChoiceBoxSkin whereChoiceBoxSkin = new MyChoiceBoxSkin(whereChoiceBox);
        whereChoiceBoxSkin.setBackgroundColorField(MyColors.iceBlue);
        whereChoiceBoxSkin.setBorderColorField(MyColors.verdrigis);
        whereChoiceBoxSkin.setCellSize(35);
        whereChoiceBox.setSkin(whereChoiceBoxSkin);
        whereChoiceBox.setPrefWidth(150);
        whereChoiceBox.setMinHeight(USE_PREF_SIZE);
        whereChoiceBox.setMaxHeight(USE_PREF_SIZE);
        whereChoiceBox.prefHeightProperty().bind(Bindings.when(whereChoiceBox.showingProperty()).then(72).otherwise(40));
        creatureAddWhere = new SimpleStringProperty();
        creatureAddWhere.bind(whereChoiceBox.valueProperty());
        HBox whereBox = new HBox(whereLabel, whereChoiceBox);
        whereBox.setPadding(new Insets(5, 10, 5, 10));
        whereBox.setAlignment(Pos.CENTER_LEFT);
        creatureAddSection.getChildren().add(whereBox);
        //gene engineering?
        typeDefenseData = setUpTypePiechartData();
        typeOffenseData = setUpTypePiechartData();
        Label engineerLabel = new Label("Genetic engineering? ");
        engineerLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
        engineerLabel.setAlignment(Pos.CENTER_LEFT);
        geneticEngineeringBox = new CheckBox();
        geneticEngineeringBox.prefHeightProperty().bind(engineerLabel.heightProperty());
        MyCheckboxSkin skin = new MyCheckboxSkin(geneticEngineeringBox, MyColors.africanViolet, MyColors.iceBlue, MyColors.plum, MyColors.verdrigis, false);
        skin.prefSizeProperty().bind(engineerLabel.heightProperty().multiply(0.75));
        geneticEngineeringBox.setSkin(skin);
        creatureAddGeneModification = new SimpleBooleanProperty(false);
        creatureAddGeneModification.bind(geneticEngineeringBox.selectedProperty());
        creatureAddGeneModification.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                updateCreatureEngineerBox(newValue);
            }
            
        });
            //header
            HBox engineeringSelectBox = new HBox(engineerLabel, geneticEngineeringBox);
            engineeringSelectBox.setPadding(new Insets(5, 10, 5, 10));
            engineeringSelectBox.setAlignment(Pos.BOTTOM_LEFT);
            engineeringSelectBox.backgroundProperty().bind(Bindings.when(creatureAddGeneModification)
                .then(new Background(new BackgroundFill(MyColors.pinkLavender, new CornerRadii(6), new Insets(-2))))
                .otherwise(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(5), new Insets(0)))));
            engineeringSelectBox.borderProperty().bind(Bindings.when(creatureAddGeneModification)
                .then(new Border(new BorderStroke(MyColors.plum, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3), new Insets(-2))))
                .otherwise(new Border(new BorderStroke(null, null, null, null))));
        engineeringBox = new VBox(engineeringSelectBox);
            //add genes
            addGenesBox = new HBox();
            addGenesBox.setSpacing(10);
            addGenesBox.setPadding(new Insets(5, 10, 5, 10));
            addGenesBox.setAlignment(Pos.CENTER_LEFT);
            Button addButton = new Button("Add");
            addButton.setBackground(new Background(new BackgroundFill(MyColors.malachite, new CornerRadii(10), new Insets(0))));
            addButton.setBorder(new Border(new BorderStroke(MyColors.officeGreen, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(0))));
            addButton.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 17));
            addButton.setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    addGene(modAddGene.get(), modAddToWhich.get());
                }
                
            });
            addTypeChoiceBox = new ChoiceBox<>();
            addTypeChoiceBox.getItems().addAll(Type.allTypes());
            modAddGene = new SimpleObjectProperty<Type>();
            modAddGene.bind(addTypeChoiceBox.valueProperty());
            TypeChoiceBox addTypeChoiceSkin = new TypeChoiceBox(addTypeChoiceBox);
            addTypeChoiceBox.setSkin(addTypeChoiceSkin);
            addTypeChoiceBox.minHeightProperty().bind(Bindings.when(addTypeChoiceBox.showingProperty()).then(100).otherwise(USE_COMPUTED_SIZE));
            Label geneLabelAddGenes = new Label("gene to");
            geneLabelAddGenes.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 23));
            addDefOffChoiceBox = new ChoiceBox<>();
            addDefOffChoiceBox.getItems().addAll("defense", "offense");
            MyChoiceBoxSkin addDefOffChoiceBoxSkin = new MyChoiceBoxSkin(addDefOffChoiceBox);
            addDefOffChoiceBoxSkin.setBackgroundColorField(MyColors.amaranthPink);
            addDefOffChoiceBoxSkin.setBorderColorField(MyColors.magentaPantone);
            addDefOffChoiceBoxSkin.setCellSize(35);
            addDefOffChoiceBox.setMinHeight(USE_PREF_SIZE);
            addDefOffChoiceBox.setMaxHeight(USE_PREF_SIZE);
            addDefOffChoiceBox.prefHeightProperty().bind(Bindings.when(addDefOffChoiceBox.showingProperty()).then(72).otherwise(40));
            addDefOffChoiceBox.setMaxWidth(90);
            modAddToWhich = new SimpleStringProperty("-");
            modAddToWhich.bind(addDefOffChoiceBox.valueProperty());
            addDefOffChoiceBox.setSkin(addDefOffChoiceBoxSkin);
            addGenesBox.getChildren().addAll(addButton, addTypeChoiceBox, geneLabelAddGenes, addDefOffChoiceBox);

            //remove genes
            removeGenesBox = new HBox();
            removeGenesBox.setSpacing(10);
            removeGenesBox.setPadding(new Insets(5, 10, 5, 10));
            removeGenesBox.setAlignment(Pos.CENTER_LEFT);
            Button removeButton = new Button("Remove");
            removeButton.setBackground(new Background(new BackgroundFill(MyColors.bittersweet, new CornerRadii(10), new Insets(0))));
            removeButton.setBorder(new Border(new BorderStroke(MyColors.chiliRed, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(0))));
            removeButton.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 17));
            removeButton.setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    removeGene(modRemoveGene.get(), modRemoveFromWhich.get());
                }
                
            });
            removeTypeChoiceBox = new ChoiceBox<>();
            removeTypeChoiceBox.getItems().addAll(Type.allTypes());
            modRemoveGene = new SimpleObjectProperty<Type>();
            modRemoveGene.bind(removeTypeChoiceBox.valueProperty());
            TypeChoiceBox removeTypechoiceSkin = new TypeChoiceBox(removeTypeChoiceBox);
            removeTypeChoiceBox.setSkin(removeTypechoiceSkin);
            removeTypeChoiceBox.minHeightProperty().bind(Bindings.when(removeTypeChoiceBox.showingProperty()).then(100).otherwise(USE_COMPUTED_SIZE));
            Label geneLabelRemoveGenes = new Label("gene from");  
            geneLabelRemoveGenes.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 23));
            removeDefOffChoiceBox = new ChoiceBox<>();
            removeDefOffChoiceBox.getItems().addAll("defense", "offense");
            MyChoiceBoxSkin removeDefOffChoiceBoxSkin = new MyChoiceBoxSkin(removeDefOffChoiceBox);
            removeDefOffChoiceBoxSkin.setBackgroundColorField(MyColors.amaranthPink);
            removeDefOffChoiceBoxSkin.setBorderColorField(MyColors.magentaPantone);
            removeDefOffChoiceBoxSkin.setCellSize(35);
            removeDefOffChoiceBox.setMinHeight(USE_PREF_SIZE);
            removeDefOffChoiceBox.setMaxHeight(USE_PREF_SIZE);
            removeDefOffChoiceBox.prefHeightProperty().bind(Bindings.when(removeDefOffChoiceBox.showingProperty()).then(72).otherwise(40));
            removeDefOffChoiceBox.setMaxWidth(90);
            modRemoveFromWhich = new SimpleStringProperty("-");
            modRemoveFromWhich.bind(removeDefOffChoiceBox.valueProperty());
            removeDefOffChoiceBox.setSkin(removeDefOffChoiceBoxSkin);
            removeGenesBox.getChildren().addAll(removeButton, removeTypeChoiceBox, geneLabelRemoveGenes, removeDefOffChoiceBox);

            //gene overview
            maxGenes = Chromosome.maxLenght*2/(5+Math.max(Chromosome.getDefenseInit().length(), Chromosome.getOffenseInit().length()));
            geneAmount = new SimpleIntegerProperty(0);
            byDefense = (Type d1, Type d2)->Integer.valueOf(typeDefenseData.get(d2).get()).compareTo(Integer.valueOf(typeDefenseData.get(d1).get()));
            byOffense = (Type o1, Type o2)->Integer.valueOf(typeOffenseData.get(o2).get()).compareTo(Integer.valueOf(typeOffenseData.get(o1).get()));
            
            //defense pie chart
            defensePieChart = new TypePieChart(typeDefenseData, engineeringBox.widthProperty().multiply(0.25), "defense", "no genes\nselected");
            defensePieChart.setErrorFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 16));
            //defense gene list
            defenseGeneList = new VBox();
            defenseGeneList.prefWidthProperty().bind(engineeringBox.widthProperty().multiply(0.25));
            typeDefenseNodes = new HashMap<>();
            for (Type t: Type.allTypes()) {
                typeDefenseNodes.put(t, typeRepresentationAmount(t, typeDefenseData.get(t), defenseGeneList.widthProperty().multiply(0.25)));
            }
            //offense pie chart
            offensePieChart = new TypePieChart(typeOffenseData, engineeringBox.widthProperty().multiply(0.25), "offense", "no genes\nselected");
            offensePieChart.setErrorFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 16));
            //offense gene list
            offenseGeneList = new VBox();
            offenseGeneList.prefWidthProperty().bind(engineeringBox.widthProperty().multiply(0.25));
            typeOffenseNodes = new HashMap<>();
            for (Type t: Type.allTypes()) {
                typeOffenseNodes.put(t, typeRepresentationAmount(t, typeOffenseData.get(t), offenseGeneList.widthProperty().multiply(0.25)));
            }
            geneInfoBox = new HBox(defensePieChart, defenseGeneList, offensePieChart, offenseGeneList);
            geneInfoBox.setPadding(new Insets(5, 10, 0, 10));

            //suppress randomness option
            suppressRandomnessCheck = new CheckBox();
            MyCheckboxSkin suppressRandomnessCheckSkin = new MyCheckboxSkin(suppressRandomnessCheck, MyColors.amaranthPink, MyColors.blush, MyColors.thulianPink, MyColors.teleMagenta, true);
            suppressRandomnessCheck.setSkin(suppressRandomnessCheckSkin);
            suppressRandomnessCheckSkin.setPrefSize(40);;
            geneRandomness = new SimpleBooleanProperty(true);
            geneRandomness.bind(suppressRandomnessCheck.selectedProperty());
            Label suppressRandomnessLabel = new Label();
            suppressRandomnessLabel.textProperty().bind(Bindings.when(geneRandomness).then("gene randomness allowed").otherwise("gene randomness suppressed"));
            suppressRandomnessLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            suppressRandomnessBox = new HBox(suppressRandomnessCheck, suppressRandomnessLabel);
            suppressRandomnessBox.setSpacing(10);
            suppressRandomnessBox.setPrefHeight(60);
            suppressRandomnessBox.prefWidthProperty().bind(engineeringBox.widthProperty());
            suppressRandomnessBox.setPadding(new Insets(2, 10, 10, 10));
            
        engineeringBox.setSpacing(10);
        engineeringBox.maxWidthProperty().bind(Bindings.when(creatureAddGeneModification)
            .then(creatureAddSection.widthProperty().multiply(0.9))
            .otherwise(creatureAddSection.widthProperty().multiply(1)));
        engineeringBox.backgroundProperty().bind(Bindings.when(creatureAddGeneModification)
            .then(new Background(new BackgroundFill(MyColors.thistle, new CornerRadii(5), new Insets(0))))
            .otherwise(new Background(new BackgroundFill(Color.TRANSPARENT, null, getInsets()))));
        engineeringBox.borderProperty().bind(Bindings.when(creatureAddGeneModification)
            .then(new Border(new BorderStroke(MyColors.skyMagenta, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))))
            .otherwise(new Border(new BorderStroke(null, null, null, null))));
        VBox lineUpBox = new VBox(engineeringBox);
        lineUpBox.setAlignment(Pos.CENTER);
        lineUpBox.backgroundProperty().bind(Bindings.when(creatureAddGeneModification)
            .then(new Background(new BackgroundFill(null, null, new Insets(0, 10, 5, 10))))
            .otherwise(new Background(new BackgroundFill(null, null, new Insets(0)))));
        creatureAddSection.getChildren().add(lineUpBox);
        //the spawn button
        spawnButtonBox = new VBox();
        spawnButton = new Button("Spawn");
        spawnButton.setBackground(new Background(new BackgroundFill(Color.rgb(255, 184, 112), new CornerRadii(10), new Insets(0))));
        spawnButton.setBorder(new Border(new BorderStroke(Color.rgb(255, 143, 31), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(0))));
        spawnButton.prefWidthProperty().bind(spawnButtonBox.widthProperty());
        spawnButton.setMinHeight(50);
        spawnButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
        spawnButton.textProperty().bind(Bindings.createStringBinding(() -> {
            String result = "";
            result += "Spawn ";
            result += creatureAddAmount.get();
            if (creatureAddGeneModification.get()) {
                if (geneRandomness.get()) {
                    result += " genetically modified";
                } else {
                    result += " genetically engineered";
                }
            }
            result += creatureAddAmount.get()==1? " creature ":" creatures ";
            result += creatureAddWhere.get()==null? "-" : creatureAddWhere.get();
            return result;
        }, creatureAddAmount, creatureAddWhere, creatureAddGeneModification, geneRandomness));
        spawnButton.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event arg0) {
                SpawnCreatures(0, 0);
            }
            
        });
        spawnButtonBox.getChildren().add(spawnButton);
        spawnButtonBox.prefWidthProperty().bind(creatureAddSection.widthProperty());
        spawnButtonBox.setPadding(new Insets(5, 10, 5, 10));
        creatureAddSection.getChildren().add(spawnButtonBox);
        this.getChildren().add(creatureAddSection);

        //piecharts
        typeDefenseAmount = setUpTypePiechartData();
        typeOffenseAmount = setUpTypePiechartData();
        pieChartSection = new HBox();
        defenseAmountPieChart = new TypePieChart(typeDefenseAmount, pieChartSection.widthProperty().multiply(0.5), "defense", "No data\nfound");
        offenseAmountPieChart = new TypePieChart(typeOffenseAmount, pieChartSection.widthProperty().multiply(0.5), "defense", "No data\nfound");
        pieChartSection.getChildren().addAll(defenseAmountPieChart, offenseAmountPieChart);
        this.getChildren().add(pieChartSection);
    }

    private void SpawnCreatures(double xCenter, double yCenter) {
        Random random = new Random();
        double fullCircle = Math.PI*2;
        double step = fullCircle/creatureAddAmount.get();
        double offset = random.nextDouble()*fullCircle;
        double radius;
        for (int a=0;a<creatureAddAmount.get(); a++) {
            HashMap<Type, Integer> defense = typeDefenseData.entrySet().stream().collect(() -> new HashMap<>(), (c, e) -> c.put(e.getKey(), e.getValue().get()), (c1, c2) -> c1.putAll(c2));
            HashMap<Type, Integer> offense = typeOffenseData.entrySet().stream().collect(() -> new HashMap<>(), (c, e) -> c.put(e.getKey(), e.getValue().get()), (c1, c2) -> c1.putAll(c2));
            if (creatureAddGeneModification.get()) {
                for (CreaturePlaceField f: creatureBiomes) {
                    if (creatureAddWhere.get().equals("along the edge")) {
                        radius = f.getWorldSize();
                    } else {
                        radius = 1;
                    }
                    f.addCreature(xCenter+radius*Math.cos(offset+step*a), yCenter+radius*Math.sin(offset+step*a), Dna.artificialDna(defense, offense, geneRandomness.get(), f.getEncoder()));
                }
            } else {
                for (CreaturePlaceField f: creatureBiomes) {
                    if (creatureAddWhere.get().equals("along the edge")) {
                        radius = f.getWorldSize();
                    } else {
                        radius = 1;
                    }
                    f.addCreature(xCenter+radius*Math.cos(offset+step*a), yCenter+radius*Math.sin(offset+step*a));
                }
            }
        }
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
        //genes
        for (Type t: Type.allTypes()) {
            typeDefenseAmount.get(t).set(typeDefenseAmount.get(t).get()+c.getDefenseMap().get(t));
            typeOffenseAmount.get(t).set(typeOffenseAmount.get(t).get()+c.getOffenseMap().get(t));
        }
    }

    private void updateCreatureEngineerBox(boolean visible) {
        if (visible) {
            engineeringBox.getChildren().addAll(addGenesBox, removeGenesBox, geneInfoBox, suppressRandomnessBox);
            updateTypeRepresentation();
        } else {
            engineeringBox.getChildren().removeAll(addGenesBox, removeGenesBox, geneInfoBox, suppressRandomnessBox);
            for (Type t: Type.allTypes()) {
                typeDefenseData.get(t).set(0);
                typeOffenseData.get(t).set(0);
            }
        }
    }

    private Node typeRepresentationAmount(Type t, SimpleIntegerProperty n, DoubleBinding width) {
        Label l = new Label();
        l.textProperty().bind(Bindings.concat(t.toString(), " ", n.asString()));
        l.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
        l.setTextFill(t.getColor());
        HBox background = new HBox(l);
        background.setPadding(new Insets(1, 5, 1, 5));
        background.setBackground(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(12), new Insets(0))));
        background.setBorder(new Border(new BorderStroke(MyColors.earthYellow, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        background.prefWidthProperty().bind(width);
        return background;
    }
    
    public void updateTypeRepresentation() {
        LinkedList<Type> ranklist = new LinkedList<Type>(Arrays.asList(Type.allTypes()));
        Type t;
        //defense empty
        for (Type tDefense: Type.allTypes()) {
            defenseGeneList.getChildren().remove(typeDefenseNodes.get(tDefense));
        }
        //defense add
        ranklist.sort(byDefense);
        for (int i=0; i<ranklist.size(); i++) {
            t = ranklist.get(i);
            if (typeDefenseData.get(t).get() > 0) {
                defenseGeneList.getChildren().add(typeDefenseNodes.get(t));
            }
        }
        //offense empty
        for (Type tOffense: Type.allTypes()) {
            offenseGeneList.getChildren().remove(typeOffenseNodes.get(tOffense));
        }
        //offense add
        ranklist.sort(byOffense);
        for (int i=0; i<ranklist.size(); i++) {
            t = ranklist.get(i);
            if (typeOffenseData.get(t).get() > 0) {
                offenseGeneList.getChildren().add(typeOffenseNodes.get(t));
            }
        }
    }

    public HashMap<Type, SimpleIntegerProperty> setUpTypePiechartData() {
        HashMap<Type, SimpleIntegerProperty> newHashMap = new HashMap<Type, SimpleIntegerProperty>();
        for (Type t: Type.allTypes()) {
            newHashMap.put(t, new SimpleIntegerProperty(0));
        }
        return newHashMap;
    }

    private void addGene(Type gene, String which) {
        if (gene == null) {
            return;
        }
        if (which == "defense" && geneAmount.get()<maxGenes) {
            typeDefenseData.get(gene).set(typeDefenseData.get(gene).get() + 1);
            geneAmount.set(geneAmount.get()+1);
        } else if (which == "offense" && geneAmount.get()<maxGenes) {
            typeOffenseData.get(gene).set(typeOffenseData.get(gene).get() + 1);
            geneAmount.set(geneAmount.get()+1);
        }
        updateTypeRepresentation();
    }
    private void removeGene(Type gene, String which) {
        if (gene == null) {
            return;
        }
        if (which == "defense" && typeDefenseData.get(gene).get() > 0) {
            typeDefenseData.get(gene).set(typeDefenseData.get(gene).get() - 1);
            geneAmount.set(geneAmount.get()-1);
        } else if (which == "offense" && typeOffenseData.get(gene).get() > 0) {
            typeOffenseData.get(gene).set(typeOffenseData.get(gene).get() - 1);
            geneAmount.set(geneAmount.get()-1);
        }
        updateTypeRepresentation();
    }

    @Override
    public void onCreatureDelete(Creature c) {
        creaturesAmount.set(creaturesAmount.get()-1);
        awakeStatusses.get(c).unbind();
        awakeStatusses.remove(c);
        ageStatusses.get(c).unbind();
        ageStatusses.remove(c);
        creatures.remove(c);
        //genes
        for (Type t: Type.allTypes()) {
            typeDefenseAmount.get(t).set(typeDefenseAmount.get(t).get()-c.getDefenseMap().get(t));
            typeOffenseAmount.get(t).set(typeOffenseAmount.get(t).get()-c.getOffenseMap().get(t));
        }
    }
    @Override
    public void onCreatureUpdate(Creature c) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'onCreatureUpdate'");
    }

    public void addCreatureBiome(CreaturePlaceField p) {
        creatureBiomes.add(p);
    }
}
