package evolution.Visual;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import evolution.Chromosome;
import evolution.Creature;
import evolution.Dna;
import evolution.Type;
import evolution.proteinEncodingManager;
import evolution.Interfaces.CreaturePlaceField;
import evolution.Visual.VisualElements.CreatureInfoDisplay;
import evolution.Visual.VisualElements.MyCheckboxSkin;
import evolution.Visual.VisualElements.MyChoiceBoxSkin;
import evolution.Visual.VisualElements.MyColors;
import evolution.Visual.VisualElements.TwoFacedCheckBox;
import evolution.Visual.VisualElements.TypeChoiceBox;
import evolution.Visual.VisualElements.TypePieChart;
import evolution.World.CreatureListener;
import evolution.proteinEncodingManager.proteinChangeListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class CreaturesDisplay extends VBox implements CreatureListener, proteinChangeListener{
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
    private Comparator<Type> byDefenseSpawn;
    private Comparator<Type> byOffenseSpawn;
    private SimpleBooleanProperty geneRandomness;

    //gene amounts
    private HashMap<Type, SimpleIntegerProperty> typeDefenseAmount;
    private HashMap<Type, SimpleIntegerProperty> typeOffenseAmount;
    private HashMap<Type, Node> typeDefenseLabel;
    private HashMap<Type, Node> typeDefenseValue;
    private HashMap<Type, Node> typeOffenseLabel;
    private HashMap<Type, Node> typeOffenseValue;
    private Comparator<Type> byDefense;
    private Comparator<Type> byOffense;

    //creature list
    private SimpleObjectProperty<Atributes> sortingKind;
    private SimpleBooleanProperty inverseSort;
    private SimpleObjectProperty<Atributes> filteringKind;
    private LinkedList<CreatureInfoDisplay> creatureDisplays;
    private SimpleIntegerProperty creatureDisplaysShown;
    private List<Creature> creatureList;
    private SimpleStringProperty searchText;
    private SimpleObjectProperty<FilterComparators> filterComparator;
    private SimpleDoubleProperty filterAmount;

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
    private VBox geneAmountSection;
        private VBox geneAmountHeader;
        private GridPane geneAmountDisplay;
    private VBox creatureViewSection;
        private VBox creatureViewHeader;
        private HBox creatureListPropertiesBox;
            private VBox creatureListSortingBox;
                private HBox creatureChoiceSortingBox;
                private HBox creatureSortButtonBox;
            private VBox creatureListFilteringBox;
                private HBox creatureChoiceFilteringBox;
                private HBox creatureListFilterSettingBox;
                private HBox creatureFilterButtonBox;
        private ScrollPane creatureListScrollPane;
            private VBox creatureListBox;

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
    private ChoiceBox<Atributes> sortingChoiceBox;
    private Button sortButton;
    private CheckBox sortOrderCheckBox;
    private ChoiceBox<Atributes> filteringChoiceBox;
    private Label filterLabel;
    private TextField nameSearchField;
    private ChoiceBox<FilterComparators> comparatorChoiceBox;
    private TextField compareToField;
    private Button filterButton;
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
        MyChoiceBoxSkin<String> whereChoiceBoxSkin = new MyChoiceBoxSkin<>(whereChoiceBox, new DefaultStringConverter());
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
            MyChoiceBoxSkin<String> addDefOffChoiceBoxSkin = new MyChoiceBoxSkin<>(addDefOffChoiceBox, new DefaultStringConverter());
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
            MyChoiceBoxSkin<String> removeDefOffChoiceBoxSkin = new MyChoiceBoxSkin<>(removeDefOffChoiceBox, new DefaultStringConverter());
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
            maxGenes = Chromosome.maxLenght*2/(proteinEncodingManager.proteinLenght+Math.max(Chromosome.getDefenseInit().length(), Chromosome.getOffenseInit().length()));
            geneAmount = new SimpleIntegerProperty(0);
            byDefenseSpawn = (Type d1, Type d2)->Integer.valueOf(typeDefenseData.get(d2).get()).compareTo(Integer.valueOf(typeDefenseData.get(d1).get()));
            byOffenseSpawn = (Type o1, Type o2)->Integer.valueOf(typeOffenseData.get(o2).get()).compareTo(Integer.valueOf(typeOffenseData.get(o1).get()));
            
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
        offenseAmountPieChart = new TypePieChart(typeOffenseAmount, pieChartSection.widthProperty().multiply(0.5), "offense", "No data\nfound");
        pieChartSection.getChildren().addAll(defenseAmountPieChart, offenseAmountPieChart);
        this.getChildren().add(pieChartSection);

        //gene display
        byDefense = (Type d1, Type d2)->Integer.valueOf(typeDefenseAmount.get(d2).get()).compareTo(Integer.valueOf(typeDefenseAmount.get(d1).get()));
        byOffense = (Type o1, Type o2)->Integer.valueOf(typeOffenseAmount.get(o2).get()).compareTo(Integer.valueOf(typeOffenseAmount.get(o1).get()));
        typeDefenseLabel = new HashMap<>();
        typeOffenseLabel = new HashMap<>();
        typeDefenseValue = new HashMap<>();
        typeOffenseValue = new HashMap<>();
        for (Type t: Type.allTypes()) {
            Label dl = new Label();
            Label ol = new Label();
            Label dv = new Label();
            Label ov = new Label();
            dl.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            ol.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            dv.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            ov.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
            dl.setText(t.toString());
            ol.setText(t.toString());
            dv.textProperty().bind(typeDefenseAmount.get(t).asString());
            ov.textProperty().bind(typeOffenseAmount.get(t).asString());
            dl.setTextFill(t.getColor());
            ol.setTextFill(t.getColor());
            dv.setTextFill(t.getColor());
            ov.setTextFill(t.getColor());
            typeDefenseLabel.put(t, dl);
            typeOffenseLabel.put(t, ol);
            typeDefenseValue.put(t, dv);
            typeOffenseValue.put(t, ov);
        }
        geneAmountSection = new VBox();
        geneAmountSection.setBackground(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(5), new Insets(0))));
        geneAmountSection.setBorder(new Border(new BorderStroke(MyColors.earthYellow, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1), new Insets(0))));
        geneAmountSection.setAlignment(Pos.TOP_CENTER);
            Label geneAmountLabel = new Label("Gene leaderboard");
            geneAmountLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            geneAmountLabel.setAlignment(Pos.BOTTOM_CENTER);
            geneAmountHeader = new VBox(geneAmountLabel);
            geneAmountHeader.setBackground(new Background(new BackgroundFill(MyColors.bittersweet, new CornerRadii(5), new Insets(-2))));
            geneAmountHeader.setBorder(new Border(new BorderStroke(MyColors.chiliRed, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2), new Insets(-2))));
            geneAmountHeader.prefWidthProperty().bind(geneAmountSection.widthProperty());
            geneAmountHeader.setPrefHeight(50);
            geneAmountHeader.setPadding(new Insets(2, 10, 2, 10));
        geneAmountSection.getChildren().add(geneAmountHeader);
            geneAmountDisplay = new GridPane();
            geneAmountDisplay.prefWidthProperty().bind(geneAmountSection.widthProperty());
            geneAmountDisplay.setAlignment(Pos.TOP_CENTER);
            geneAmountDisplay.setHgap(25);
            updateTypeLeaderBoard();
        geneAmountSection.getChildren().add(geneAmountDisplay);
        geneAmountSection.prefHeightProperty().bind(geneAmountHeader.heightProperty().add(geneAmountDisplay.heightProperty()));
        this.getChildren().add(geneAmountSection);

        //creature list
        creatureList = new LinkedList<>();
        creatureDisplays = new LinkedList<>();
        creatureDisplaysShown = new SimpleIntegerProperty(0);
        creatureViewSection = new VBox();
        creatureViewSection.setBackground(new Background(new BackgroundFill(Color.rgb(198, 179, 230), new CornerRadii(5), new Insets(0))));
        creatureViewSection.setBorder(new Border(new BorderStroke(Color.rgb(163, 133, 214), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1), new Insets(0))));
        creatureViewSection.setAlignment(Pos.TOP_CENTER);
            Label creatureViewLabel = new Label("Creature list");
            creatureViewLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            creatureViewLabel.setAlignment(Pos.BOTTOM_CENTER);
            creatureViewHeader = new VBox(creatureViewLabel);
            creatureViewHeader.setBackground(new Background(new BackgroundFill(Color.rgb(130, 87, 199), new CornerRadii(5), new Insets(-2))));
            creatureViewHeader.setBorder(new Border(new BorderStroke(Color.rgb(57, 32, 97), BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2), new Insets(-2))));
            creatureViewHeader.prefWidthProperty().bind(creatureViewSection.widthProperty());
            creatureViewHeader.setPrefHeight(50);
            creatureViewHeader.setPadding(new Insets(2, 10, 2, 10));
        creatureViewSection.getChildren().add(creatureViewHeader);
            creatureListPropertiesBox = new HBox();
            creatureListPropertiesBox.prefWidthProperty().bind(creatureViewSection.widthProperty());
            creatureListPropertiesBox.setPadding(new Insets(5, 10, 5, 10));
                        Label sortingLabel = new Label("Sort by:");
                        sortingLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
                        sortingChoiceBox = new ChoiceBox<>();
                        sortingChoiceBox.getItems().addAll(Atributes.values());
                        sortingChoiceBox.setValue(Atributes.Name);
                        MyChoiceBoxSkin<Atributes> sortingChoiceBoxSkin = new MyChoiceBoxSkin<Atributes>(sortingChoiceBox, new AtributesConverter());
                        sortingChoiceBoxSkin.setBackgroundColorField(MyColors.iceBlue);
                        sortingChoiceBoxSkin.setBorderColorField(MyColors.verdrigis);
                        sortingChoiceBoxSkin.setCellSize(30);
                        sortingChoiceBoxSkin.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 14));
                        sortingChoiceBox.setSkin(sortingChoiceBoxSkin);
                        sortingChoiceBox.setMinHeight(USE_PREF_SIZE);
                        sortingChoiceBox.setMaxHeight(USE_PREF_SIZE);
                        sortingChoiceBox.prefHeightProperty().bind(Bindings.when(sortingChoiceBox.showingProperty()).then(100).otherwise(35));
                        sortingKind = new SimpleObjectProperty<>();
                        sortingKind.bind(sortingChoiceBox.valueProperty());
                    creatureChoiceSortingBox = new HBox(sortingLabel, sortingChoiceBox);
                    creatureChoiceSortingBox.setSpacing(15);
                        sortButton = new Button("Sort");
                        sortButton.setPrefHeight(30);
                        sortButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        sortButton.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(12), sortButton.getInsets())));
                        sortButton.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                        sortButton.setOnMouseClicked(e -> updateCreatureList());
                        sortOrderCheckBox = new CheckBox();
                        Polygon normalOrderArrow = new Polygon(5, 12.5, 15, 22.5, 25, 12.5);
                        normalOrderArrow.setFill(MyColors.verdrigis);
                        Pane normalOrderPane = new Pane(normalOrderArrow);
                        normalOrderPane.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(12), normalOrderPane.getInsets())));
                        normalOrderPane.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                        normalOrderPane.setMinHeight(30);
                        normalOrderPane.setMinWidth(30);
                        Polygon reverseOrderArrow = new Polygon(5, 17.5, 15, 7.5, 25, 17.5);
                        reverseOrderArrow.setFill(MyColors.verdrigis);
                        Pane reversOrderPane = new Pane(reverseOrderArrow);
                        reversOrderPane.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(12), reversOrderPane.getInsets())));
                        reversOrderPane.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                        reversOrderPane.setMinHeight(30);
                        reversOrderPane.setMinWidth(30);
                        sortOrderCheckBox.setSkin(new TwoFacedCheckBox(sortOrderCheckBox, normalOrderPane, reversOrderPane, false));
                        inverseSort = new SimpleBooleanProperty();
                        inverseSort.bind(sortOrderCheckBox.selectedProperty());
                        inverseSort.addListener((p, ov, nv) -> updateCreatureList());
                    creatureSortButtonBox = new HBox(sortButton, sortOrderCheckBox);
                    creatureSortButtonBox.setSpacing(10);
                creatureListSortingBox = new VBox(creatureChoiceSortingBox, creatureSortButtonBox);
                creatureListSortingBox.prefWidthProperty().bind(creatureListPropertiesBox.widthProperty().multiply(0.5));
                        Label filteringLabel = new Label("Filter on:");
                        filteringLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
                        filteringChoiceBox = new ChoiceBox<>();
                        filteringChoiceBox.getItems().addAll(Atributes.values());
                        filteringChoiceBox.setValue(Atributes.Name);
                        MyChoiceBoxSkin<Atributes> filteringChoiceBoxSkin = new MyChoiceBoxSkin<Atributes>(filteringChoiceBox, new AtributesConverter());
                        filteringChoiceBoxSkin.setBackgroundColorField(MyColors.iceBlue);
                        filteringChoiceBoxSkin.setBorderColorField(MyColors.verdrigis);
                        filteringChoiceBoxSkin.setCellSize(30);
                        filteringChoiceBoxSkin.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 14));
                        filteringChoiceBox.setSkin(filteringChoiceBoxSkin);
                        filteringChoiceBox.setMinHeight(USE_PREF_SIZE);
                        filteringChoiceBox.setMaxHeight(USE_PREF_SIZE);
                        filteringChoiceBox.prefHeightProperty().bind(Bindings.when(filteringChoiceBox.showingProperty()).then(100).otherwise(35));
                        filteringKind = new SimpleObjectProperty<>();
                        filteringKind.bind(filteringChoiceBox.valueProperty());
                        filteringKind.addListener((p, ov, nv) -> updateFilterSettingDisplay(nv));
                    creatureChoiceFilteringBox = new HBox(filteringLabel, filteringChoiceBox);
                    creatureChoiceFilteringBox.setSpacing(10);
                        filterLabel = new Label();
                        filterLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 17));
                        filterLabel.textProperty().bind(Bindings.createStringBinding(() -> {
                            if (filteringKind.get() == null) {
                                return "";
                            } else if (filteringKind.get() == Atributes.Name) {
                                return "Name contains";
                            } else {
                                return filteringKind.get().toString();
                            }
                        }, filteringKind));
                        nameSearchField = new TextField("");
                        nameSearchField.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(9), reversOrderPane.getInsets())));
                        nameSearchField.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                        nameSearchField.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        nameSearchField.setMaxWidth(90);
                        searchText = new SimpleStringProperty("");
                        searchText.bind(nameSearchField.textProperty());
                        comparatorChoiceBox = new ChoiceBox<>();
                        comparatorChoiceBox.getItems().addAll(FilterComparators.values());
                        MyChoiceBoxSkin<FilterComparators> comparatorChoiceBoxSkin = new MyChoiceBoxSkin<FilterComparators>(comparatorChoiceBox, new FilterComparatorsConverter());
                        comparatorChoiceBoxSkin.setBackgroundColorField(MyColors.iceBlue);
                        comparatorChoiceBoxSkin.setBorderColorField(MyColors.verdrigis);
                        comparatorChoiceBoxSkin.setCellSize(30);
                        comparatorChoiceBox.setSkin(comparatorChoiceBoxSkin);
                        comparatorChoiceBox.setPrefWidth(70);
                        comparatorChoiceBox.setMinHeight(USE_PREF_SIZE);
                        comparatorChoiceBox.setMaxHeight(USE_PREF_SIZE);
                        comparatorChoiceBox.prefHeightProperty().bind(Bindings.when(comparatorChoiceBox.showingProperty()).then(95).otherwise(35));
                        filterComparator = new SimpleObjectProperty<>();
                        filterComparator.bind(comparatorChoiceBox.valueProperty());
                        compareToField = new TextField("0");
                        compareToField.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(9), reversOrderPane.getInsets())));
                        compareToField.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                        compareToField.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        compareToField.setMaxWidth(60);
                        compareToField.setPrefHeight(35);
                        filterAmount = new SimpleDoubleProperty(0);
                        filterAmount.bind(Bindings.createDoubleBinding(() -> {
                            if (compareToField.getText().equals("")) {
                                return 0d;
                            } else {
                                try {
                                    return Double.parseDouble(compareToField.getText());
                                } catch (NullPointerException | NumberFormatException e) {
                                    return null;
                                }
                            }
                        }, compareToField.textProperty()));
                    creatureListFilterSettingBox = new HBox();
                    creatureListFilterSettingBox.setSpacing(10);
                        filterButton = new Button("Filter");
                        filterButton.setPrefHeight(30);
                        filterButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        filterButton.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(9), sortButton.getInsets())));
                        filterButton.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                        filterButton.setOnMouseClicked(e -> updateCreatureList());
                    creatureFilterButtonBox = new HBox(filterButton);
                creatureListFilteringBox = new VBox(creatureChoiceFilteringBox, creatureListFilterSettingBox, creatureFilterButtonBox);
                creatureListFilteringBox.prefWidthProperty().bind(creatureListPropertiesBox.widthProperty().multiply(0.5));
            creatureListPropertiesBox.getChildren().addAll(creatureListSortingBox, creatureListFilteringBox);
        creatureViewSection.getChildren().add(creatureListPropertiesBox);
            creatureListBox = new VBox();
            creatureListScrollPane = new ScrollPane(creatureListBox);
            creatureListBox.setBackground(new Background(new BackgroundFill(Color.rgb(198, 179, 230), new CornerRadii(0), new Insets(0))));
            creatureListBox.minHeightProperty().bind(creatureListScrollPane.heightProperty());
            creatureListScrollPane.setFitToWidth(true);
            creatureListScrollPane.prefWidthProperty().bind(creatureViewSection.widthProperty());
            creatureListScrollPane.setMinHeight(USE_PREF_SIZE);
            creatureListScrollPane.minHeightProperty().bind(Bindings.min(creaturesAmount.multiply(50), 400));
            creatureListScrollPane.setBackground(new Background(new BackgroundFill(Color.rgb(198, 179, 230), new CornerRadii(0), new Insets(0))));
        creatureViewSection.getChildren().add(creatureListScrollPane);
        this.getChildren().add(creatureViewSection);
    }
    private void updateCreatureList() {
        this.creatureList = creatures.stream().filter(c -> filteringKind.get() == null || filteringKind.get().compare(c, filterComparator.get(), filterAmount.get())).filter(c -> filteringKind.get() == null || filteringKind.get().contains(c, searchText.get())).sorted(sortingKind.get().getCreatureComparator(inverseSort.get())).collect(Collectors.toList());
        for (int i=0; i<Math.max(creatureList.size(), creatureDisplays.size()); i++) {
            if (i<creatureList.size() && i<creatureDisplaysShown.get()) {
                creatureDisplays.get(i).setCreature(creatureList.get(i));
            } else if (i<creatureList.size() && i<creatureDisplays.size()) {
                creatureDisplays.get(i).setCreature(creatureList.get(i));
                creatureListBox.getChildren().add(creatureDisplays.get(i));
            } else if (i<creatureList.size()) {
                CreatureInfoDisplay display = new CreatureInfoDisplay(creatureList.get(i));
                display.setPrefHeight(50);
                creatureDisplays.add(display);
                creatureListBox.getChildren().add(display);
            } else {
                creatureListBox.getChildren().remove(creatureDisplays.get(i));
            }
        }
        creatureDisplaysShown.set(creatureList.size());
    }

    private void updateFilterSettingDisplay(Atributes a) {
        creatureListFilterSettingBox.getChildren().removeAll(filterLabel, nameSearchField, comparatorChoiceBox, compareToField);
        if (a == null || a == Atributes.Nothing) {
            return;
        }
        if (a == Atributes.Name) {
            creatureListFilterSettingBox.getChildren().addAll(filterLabel, nameSearchField);
        } else {
            creatureListFilterSettingBox.getChildren().addAll(filterLabel, comparatorChoiceBox, compareToField);
        }
    }

    private void updateTypeLeaderBoard() {
        List<Type> types = Arrays.asList(Type.allTypes());
        Type t;
        //defense
        for (Type r: Type.allTypes()) {
            geneAmountDisplay.getChildren().remove(typeDefenseLabel.get(r));
            geneAmountDisplay.getChildren().remove(typeDefenseValue.get(r));
        }
        types.sort(byDefense);
        for (int i=0; i<types.size(); i++) {
            t = types.get(i);

            geneAmountDisplay.add(typeDefenseValue.get(t), 0, i);
            geneAmountDisplay.add(typeDefenseLabel.get(t), 1, i);
        }
        //offense
        for (Type r: Type.allTypes()) {
            geneAmountDisplay.getChildren().remove(typeOffenseLabel.get(r));
            geneAmountDisplay.getChildren().remove(typeOffenseValue.get(r));
        }
        types.sort(byOffense);
        for (int i=0; i<types.size(); i++) {
            t = types.get(i);
            geneAmountDisplay.add(typeOffenseValue.get(t), 2, i);
            geneAmountDisplay.add(typeOffenseLabel.get(t), 3, i);
        }

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
                    if (creatureAddWhere.get() != null && creatureAddWhere.get().equals("along the edge")) {
                        radius = f.getWorldSize();
                    } else {
                        radius = 15;
                    }
                    f.addCreature(xCenter+radius*Math.cos(offset+step*a), yCenter+radius*Math.sin(offset+step*a), Dna.artificialDna(defense, offense, geneRandomness.get(), f.getEncoder()));
                }
            } else {
                for (CreaturePlaceField f: creatureBiomes) {
                    if (creatureAddWhere.get() != null && creatureAddWhere.get().equals("along the edge")) {
                        radius = f.getWorldSize();
                    } else {
                        radius = 15;
                    }
                    f.addCreature(xCenter+radius*Math.cos(offset+step*a), yCenter+radius*Math.sin(offset+step*a));
                }
            }
        }
    }

    private void addCreatureToList(Creature c) {
        if (creatureDisplaysShown.get()>=creatureDisplays.size()) {
            CreatureInfoDisplay display = new CreatureInfoDisplay(c);
            creatureDisplays.add(display);
            display.setPrefHeight(50);
            creatureListBox.getChildren().add(display);
        } else {
            creatureDisplays.get(creatureDisplaysShown.get()).setCreature(c);
            creatureListBox.getChildren().add(creatureDisplays.get(creatureDisplaysShown.get()));
        }
        creatureDisplaysShown.set(creatureDisplaysShown.get()+1);
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
        updateTypeLeaderBoard();
        //creature list
        addCreatureToList(c);
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
                geneAmount.set(0);
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
        ranklist.sort(byDefenseSpawn);
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
        ranklist.sort(byOffenseSpawn);
        for (int i=0; i<ranklist.size(); i++) {
            t = ranklist.get(i);
            if (typeOffenseData.get(t).get() > 0) {
                offenseGeneList.getChildren().add(typeOffenseNodes.get(t));
            }
        }
    }

    private HashMap<Type, SimpleIntegerProperty> setUpTypePiechartData() {
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
        updateTypeLeaderBoard();
    }
    @Override
    public void onCreatureUpdate(Creature c) {
        return;
    }

    public void addCreatureBiome(CreaturePlaceField p) {
        creatureBiomes.add(p);
    }

    @Override
    public void onProteinChange() {
        return;
    }

    @Override
    public void onProteinChangeFinished() {
        for (Type t: Type.allTypes()) {
            typeDefenseAmount.get(t).set(0);
            typeOffenseAmount.get(t).set(0);
        }
        for (Creature c: creatures) {
            for (Type t: Type.allTypes()) {
                typeDefenseAmount.get(t).set(typeDefenseAmount.get(t).getValue()+c.getDefenseMap().get(t));
                typeOffenseAmount.get(t).set(typeOffenseAmount.get(t).getValue()+c.getOffenseMap().get(t));
            }
        }
        updateTypeLeaderBoard();
    }
}

enum FilterComparators {
    SMALLER, SMALLERQUALS, EQUALS, GREATEREQUALS, GREATER;
    public boolean compare(double x, double v) {
        switch(this) {
            case SMALLER:
                return x<v;
            case SMALLERQUALS:
                return x<=v;
            case EQUALS:
                return x==v; 
            case GREATEREQUALS:
                return x>=v;
            case GREATER:
                return x>v;
            default:
                return true;
        }
    }
}

enum Atributes {
    Nothing, Name, Age, Health, Energy, Kills, Children;
    public Comparator<Creature> getCreatureComparator(boolean inverse) {
        int multiplier = inverse? -1 : 1;
        switch(this) {
            case Nothing:
                return (Creature c1, Creature c2) -> 0;
            case Name:
                return (Creature c1, Creature c2) -> multiplier*(c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase()));
            case Age:
                return (Creature c1, Creature c2) -> multiplier*(Integer.valueOf(c2.getAge()).compareTo(c1.getAge()));
            case Energy:
                return (Creature c1, Creature c2) -> multiplier*(Double.valueOf(c2.getEnergy()).compareTo(c1.getEnergy()));
            case Health:
                return (Creature c1, Creature c2) -> multiplier*(Double.valueOf(c2.getHealth()).compareTo(c1.getHealth()));
            case Kills:
                return (Creature c1, Creature c2) -> multiplier*(Integer.valueOf(c2.getKills()).compareTo(c1.getKills()));
            case Children:
                return (Creature c1, Creature c2) -> multiplier*(Integer.valueOf(c2.getChildrenAmount()).compareTo(c1.getChildrenAmount()));
            default:
                return (Creature c1, Creature c2) -> multiplier*(c1.getName().compareTo(c2.getName()));
        }
    }
    public boolean compare(Creature c, FilterComparators comparator, Double v) {
        if (v == null || comparator==null) {
            return true;
        }
        switch(this) {
            case Nothing:
                return true;
            case Name:
                return true;
            case Age:
                return comparator.compare(c.getAge(), v);
            case Energy:
                return comparator.compare(c.getEnergy(), v);
            case Health:
                return comparator.compare(c.getHealth(), v);
            case Kills:
                return comparator.compare(c.getKills(), v);
            case Children:
                return comparator.compare(c.getChildrenAmount(), v);
            default:
                return true;
        }
    }
    public boolean contains(Creature c, String s) {
        switch(this) {
            case Name:
                return c.getName().toLowerCase().contains(s.toLowerCase());
            default:
                return true;
        }
    }
}

class AtributesConverter extends StringConverter<Atributes> {

    @Override
    public String toString(Atributes object) {
        return object.toString();
    }

    @Override
    public Atributes fromString(String string) {
        return Atributes.valueOf(string);
    }
    
}

class FilterComparatorsConverter extends StringConverter<FilterComparators> {

    @Override
    public String toString(FilterComparators object) {
        switch (object) {
            case SMALLER:
                return "<";
            case SMALLERQUALS:
                return "<=";
            case EQUALS:
                return "="; 
            case GREATEREQUALS:
                return ">=";
            case GREATER:
                return ">";
            default:
                return "";
        } 
    }

    @Override
    public FilterComparators fromString(String string) {
        switch (string) {
            case "<":
                return FilterComparators.SMALLER;
            case "<=":
                return FilterComparators.SMALLERQUALS;
            case "=":
                return FilterComparators.EQUALS; 
            case ">=":
                return FilterComparators.GREATEREQUALS;
            case ">":
                return FilterComparators.GREATER;
            default:
                return null;
        } 
    }
}