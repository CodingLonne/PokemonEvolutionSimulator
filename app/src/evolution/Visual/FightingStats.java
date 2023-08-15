package evolution.Visual;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import evolution.AdvantageSettings;
import evolution.Creature;
import evolution.FightingSettings;
import evolution.MyBindings;
import evolution.Type;
import evolution.AdvantageSettings.AttackStrenght;
import evolution.Visual.VisualElements.CreatureAddLabel;
import evolution.Visual.VisualElements.CreatureLabel;
import evolution.Visual.VisualElements.MultiFacedDisplay;
import evolution.Visual.VisualElements.MyChoiceBoxSkin;
import evolution.Visual.VisualElements.MyColors;
import evolution.Visual.VisualElements.SwordShape;
import evolution.Visual.VisualElements.TypeChoiceBox;
import evolution.World.CreatureListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.converter.DefaultStringConverter;

public class FightingStats extends VBox implements CreatureListener{
    private FightingSettings settings;
    private List<Creature> creatures;
    //type overview section
    private ObjectProperty<Type> highlightedOffenseType;
    private ObjectProperty<Type> highlightedDefenseType;
    //multiply slider
    private DoubleProperty damageMultiplier;
    //damage testing section
    private ObjectProperty<Type> selectedAttackType;
    private ObjectProperty<Type> selectedDefenseType;
    private ObjectProperty<Creature> selectedDefenseCreature;
    private ObjectProperty<DefenseKind> defenseKind;
    private DoubleProperty attackOutcome;
    private StringProperty searchString;
    private int suggestionsShown;

    //visual variables
    private final double headerHeight = 60;
    private final double gridScale = 30;
    private final double gridEdge = 120;
    private final Color strenghtColor = Color.rgb(59, 206, 79);
    private final Color weaknessColor = Color.rgb(254, 136, 72);
    private final double circleSize = 0.95;
    private final double symbolSize = 0.75;
    private final double symbolwidth = 0.25;
    private final double symbolCornerSize = 0.1;
    private final Color selectedColor = MyColors.wheat;
    private final Color deselectedColor = MyColors.dutchWhite;
    private final double arrowSize = 40;
    private final double damageTestingFieldsWidth = 110;
    private final int maxSearchSuggestions = 10;

    //boxes
    private HBox headerSection;
        private Label titleLabel;
    private VBox advantageSettingsSection;
        private GridPane typeOverviewSection;
            private ScrollPane defenseTypesScrollPane;
                private HBox defenseTypesBox;
            private ScrollPane offenseTypesScrollPane;
                private VBox offenseTypesBox;
            private ScrollPane overviewScrollPane;
                private GridPane overviewBox;
        private VBox randomnessSection;
            private Label randomnessTitle;
            private HBox randomnessBox;
                private VBox randomnessSliders;
                    private HBox strenghtSliderHBox;
                        private Label strenghtSliderLabel;
                        private Slider strenghtSlider;
                        private Label strenghtSliderPercentage;
                    private HBox weaknessSliderHBox;
                        private Label weaknessSliderLabel;
                        private Slider weaknessSlider;
                        private Label weaknessSliderPercentage;
                private Button randomnessButton;
        private VBox fileSection;
            private HBox fileSaveBox;
                private TextField fileNameTextField;
                private Button saveButton;
            private HBox fileLoadBox;
                private ChoiceBox<String> fileNameChoiceBox;
                private Button loadButton;
        private VBox multiplySliderSection;
            private Label multiplySliderTitle;
            private Text multiplySliderDescription;
            private Slider multiplySlider;
            private Label multiplySliderOutcome;
        private VBox typeCheckSection;
            private Label typeCheckTitle;
            private GridPane typeCheckGridPane;
                private HBox attackBox;
                    private ChoiceBox<Type> attackChoiceBox;
                private SwordShape swords;
                private HBox defenseBox;
                    private ChoiceBox<Type> defenseChoiceBox;
                    private VBox defenseCreatureBox;
                        private CreatureLabel defenseCreature;
                private Shape arrow;
                private Label outcomeLabel;
            private VBox searchBox;
                private TextField creatureSearchField;
                private VBox creatureSuggestions;
                    private List<CreatureAddLabel> suggestionLabels;

    public FightingStats(FightingSettings fightingSettings) {
        super();
        this.setBackground(new Background(new BackgroundFill(MyColors.wheat, null, null)));
        this.setPadding(new Insets(5, 30, 5, 20));
        this.setSpacing(15);
        //variables
        this.settings = fightingSettings;
        creatures = new LinkedList<>();
        //build appearance
        makeHeader();
        makeAdvantagesSettings();
    }
    
    private void makeHeader() {
        headerSection = new HBox();
        headerSection.setPadding(new Insets(0, 10, 0, 10));
        titleLabel = new Label("Fighting");
        titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 40));
        headerSection.getChildren().add(titleLabel);
        headerSection.setPrefHeight(headerHeight);
        this.getChildren().add(headerSection);
    }

    private void makeAdvantagesSettings() {
        advantageSettingsSection = new VBox();
        advantageSettingsSection.setBackground(new Background(new BackgroundFill(Color.rgb(102, 0, 20), new CornerRadii(10), new Insets(0))));
        advantageSettingsSection.setBorder(new Border(new BorderStroke(Color.rgb(61, 0, 12), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        advantageSettingsSection.setPadding(new Insets(15, 15, 5, 15));
        advantageSettingsSection.setSpacing(15);
        makeTypeOverview();
        makeRandomSection();
        makeFileSection();
        makeMultiplySliderSection();
        makeTypeCheckSection();
        this.getChildren().add(advantageSettingsSection);
    }

    private void makeTypeOverview() {
        //variables 
        highlightedOffenseType = new SimpleObjectProperty<>();
        highlightedDefenseType = new SimpleObjectProperty<>();
        //general
        typeOverviewSection = new GridPane();
        //left offense type list
        offenseTypesBox = new VBox();
        for (Type t: Type.allTypes()) {
                VBox offenseTypeLabelBox = new VBox();
                offenseTypeLabelBox.setPrefSize(gridEdge, gridScale);
                offenseTypeLabelBox.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
                offenseTypeLabelBox.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
                offenseTypeLabelBox.setOnMouseClicked(e -> highlightedOffenseType.set(t));
                offenseTypeLabelBox.backgroundProperty().bind(Bindings.when(highlightedOffenseType.isEqualTo(t))
                    .then(new Background(new BackgroundFill(selectedColor, new CornerRadii(0), new Insets(0))))
                    .otherwise(new Background(new BackgroundFill(deselectedColor, new CornerRadii(0), new Insets(0)))));
                offenseTypeLabelBox.setBorder(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
                    Text offenseTypeLabel = new Text(t.toString());
                    offenseTypeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 18));
                    offenseTypeLabel.setFill(t.getColor());
                offenseTypeLabelBox.getChildren().add(offenseTypeLabel);
            offenseTypesBox.getChildren().add(offenseTypeLabelBox);
        }
        offenseTypesScrollPane = new ScrollPane(offenseTypesBox);
        offenseTypesScrollPane.setPrefWidth(gridEdge);
        offenseTypesScrollPane.setMinWidth(USE_PREF_SIZE);
        offenseTypesScrollPane.setMaxWidth(USE_PREF_SIZE);
        offenseTypesScrollPane.setPannable(false);
        offenseTypesScrollPane.setHmax(0);
        offenseTypesScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        offenseTypesScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        typeOverviewSection.add(offenseTypesScrollPane, 0, 1);
        //top defense type list
        defenseTypesBox = new HBox();
        for (Type t: Type.allTypes()) {
                HBox defenseTypeLabelBox = new HBox();
                defenseTypeLabelBox.setPrefSize(gridScale, gridEdge);
                defenseTypeLabelBox.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
                defenseTypeLabelBox.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
                defenseTypeLabelBox.setOnMouseClicked(e -> highlightedDefenseType.set(t));
                defenseTypeLabelBox.backgroundProperty().bind(Bindings.when(highlightedDefenseType.isEqualTo(t))
                    .then(new Background(new BackgroundFill(selectedColor, new CornerRadii(0), new Insets(0))))
                    .otherwise(new Background(new BackgroundFill(deselectedColor, new CornerRadii(0), new Insets(0)))));
                defenseTypeLabelBox.setBorder(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
                    Text defenseTypeLabel = new Text(t.toString());
                    defenseTypeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 18));
                    defenseTypeLabel.setFill(t.getColor());
                    defenseTypeLabel.getTransforms().add(new Rotate(90));
                defenseTypeLabelBox.getChildren().add(defenseTypeLabel);
            defenseTypesBox.getChildren().add(defenseTypeLabelBox);
        }
        defenseTypesScrollPane = new ScrollPane(defenseTypesBox);
        defenseTypesScrollPane.setPrefHeight(gridEdge);
        defenseTypesScrollPane.setMinHeight(USE_PREF_SIZE);
        defenseTypesScrollPane.setMaxHeight(USE_PREF_SIZE);
        defenseTypesScrollPane.setPannable(false);
        defenseTypesScrollPane.setVmax(0);
        defenseTypesScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        defenseTypesScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        typeOverviewSection.add(defenseTypesScrollPane, 1, 0);
        //top left display
        Pane axisNamesBox = new Pane();
        axisNamesBox.setBackground(new Background(new BackgroundFill(deselectedColor, new CornerRadii(0), new Insets(0))));
        axisNamesBox.setPrefSize(gridEdge, gridEdge);
        axisNamesBox.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        axisNamesBox.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
            Label defenseArrow = new Label("Defense ▶");
            defenseArrow.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            defenseArrow.setLayoutX(15);
            defenseArrow.setLayoutY(0);
            Text offenseArrow = new Text("Attack ▶");
            offenseArrow.setLayoutX(-3);
            offenseArrow.setLayoutY(10);
            offenseArrow.getTransforms().add(new Rotate(90, -3, 10));
            offenseArrow.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        axisNamesBox.getChildren().addAll(defenseArrow, offenseArrow);
        typeOverviewSection.add(axisNamesBox, 0, 0);
        //grid
        overviewBox = new GridPane();
        for (int o = 0; o<Type.allTypes().length; o++) {
            for (int d = 0; d<Type.allTypes().length; d++) {
                Type offenseType = Type.allTypes()[o];
                Type defenseType = Type.allTypes()[d];
                Map<AttackStrenght, Node> faces = new HashMap<>();
                //https://coolors.co/dbd3ad-40b54f-f47e3e
                //normal
                Pane normal = new Pane();
                normal.backgroundProperty().bind(Bindings.when(highlightedDefenseType.isEqualTo(defenseType).or(highlightedOffenseType.isEqualTo(offenseType)))
                    .then(new Background(new BackgroundFill(selectedColor, new CornerRadii(0), new Insets(0))))
                    .otherwise(new Background(new BackgroundFill(deselectedColor, new CornerRadii(0), new Insets(0)))));
                normal.setBorder(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
                normal.setPrefSize(gridScale, gridScale);
                normal.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
                normal.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
                //normal.setStroke(MyColors.sunset);
                faces.put(AttackStrenght.NORMAL, normal);
                //strong
                    Circle strongCircle = new Circle(gridScale*0.5, gridScale*0.5, gridScale*0.5*circleSize, strenghtColor);
                    Rectangle strongPlusHor = new Rectangle((1-symbolSize)*0.5*gridScale, gridScale*0.5*(1-symbolwidth), gridScale*symbolSize, gridScale*symbolwidth);
                    strongPlusHor.setFill(Color.WHITE);
                    strongPlusHor.setArcHeight(gridScale*symbolCornerSize);
                    strongPlusHor.setArcWidth(gridScale*symbolCornerSize);
                    Rectangle strongPlusVer = new Rectangle(gridScale*0.5*(1-symbolwidth), (1-symbolSize)*0.5*gridScale, gridScale*symbolwidth, gridScale*symbolSize);
                    strongPlusVer.setFill(Color.WHITE);
                    strongPlusVer.setArcHeight(gridScale*symbolCornerSize);
                    strongPlusVer.setArcWidth(gridScale*symbolCornerSize);
                Pane strong = new Pane(strongCircle, strongPlusHor, strongPlusVer);
                strong.backgroundProperty().bind(Bindings.when(highlightedDefenseType.isEqualTo(defenseType).or(highlightedOffenseType.isEqualTo(offenseType)))
                    .then(new Background(new BackgroundFill(selectedColor, new CornerRadii(0), new Insets(0))))
                    .otherwise(new Background(new BackgroundFill(deselectedColor, new CornerRadii(0), new Insets(0)))));
                strong.setBorder(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
                strong.setPrefSize(gridScale, gridScale);
                strong.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
                strong.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
                faces.put(AttackStrenght.STRONG, strong);
                //weak
                    Circle weakCircle = new Circle(gridScale*0.5, gridScale*0.5, gridScale*0.5*circleSize, weaknessColor);
                    Rectangle weakMinHor = new Rectangle((1-symbolSize)*0.5*gridScale, gridScale*0.5*(1-symbolwidth), gridScale*symbolSize, gridScale*symbolwidth);
                    weakMinHor.setFill(Color.WHITE);
                    weakMinHor.setArcHeight(gridScale*symbolCornerSize);
                    weakMinHor.setArcWidth(gridScale*symbolCornerSize);
                Pane weak = new Pane(weakCircle, weakMinHor);
                weak.backgroundProperty().bind(Bindings.when(highlightedDefenseType.isEqualTo(defenseType).or(highlightedOffenseType.isEqualTo(offenseType)))
                    .then(new Background(new BackgroundFill(selectedColor, new CornerRadii(0), new Insets(0))))
                    .otherwise(new Background(new BackgroundFill(deselectedColor, new CornerRadii(0), new Insets(0)))));
                weak.setBorder(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
                weak.setPrefSize(gridScale, gridScale);
                weak.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
                weak.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
                faces.put(AttackStrenght.WEAK, weak);
                //make display
                MultiFacedDisplay<AttackStrenght> strenghtSquare = new MultiFacedDisplay<AttackStrenght>(faces, normal, AttackStrenght.NORMAL);
                strenghtSquare.valueProperty().bind(AdvantageSettings.settings.attackProperty(offenseType, defenseType));
                strenghtSquare.setOnMouseClicked(new EventHandler<Event>() {

                    @Override
                    public void handle(Event event) {
                        switch (AdvantageSettings.settings.attacks(offenseType, defenseType)) {
                            case WEAK:
                                AdvantageSettings.settings.setAttacks(offenseType, defenseType, AttackStrenght.NORMAL);
                                break;
                            case NORMAL:
                                AdvantageSettings.settings.setAttacks(offenseType, defenseType, AttackStrenght.STRONG);
                                break;
                            case STRONG:
                                AdvantageSettings.settings.setAttacks(offenseType, defenseType, AttackStrenght.WEAK);
                                break;
                        }
                    }
                    
                });
                overviewBox.add(strenghtSquare, d, o);
            }
        }
        overviewScrollPane = new ScrollPane(overviewBox);
        overviewScrollPane.setPannable(false);
        overviewScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        overviewScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        typeOverviewSection.add(overviewScrollPane, 1, 1);
        //general close
        advantageSettingsSection.getChildren().add(typeOverviewSection);
        overviewScrollPane.vvalueProperty().bindBidirectional(offenseTypesScrollPane.vvalueProperty());
        overviewScrollPane.hvalueProperty().bindBidirectional(defenseTypesScrollPane.hvalueProperty());
        //heights
        offenseTypesScrollPane.prefViewportHeightProperty().bind(defenseTypesScrollPane.widthProperty());
        offenseTypesScrollPane.setMinViewportHeight(USE_PREF_SIZE);
        offenseTypesScrollPane.setMaxHeight(USE_PREF_SIZE);
        overviewScrollPane.prefViewportHeightProperty().bind(defenseTypesScrollPane.widthProperty());
        overviewScrollPane.setMinViewportHeight(USE_PREF_SIZE);
        overviewScrollPane.setMaxHeight(USE_PREF_SIZE);
        typeOverviewSection.minHeightProperty().bind(typeOverviewSection.widthProperty());
    }

    private void makeRandomSection() {
        randomnessSection = new VBox();
        randomnessSection.setSpacing(5);
            randomnessTitle = new Label("Randomize");
            randomnessTitle.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            randomnessTitle.setTextFill(Color.WHITE);
            randomnessBox = new HBox();
            randomnessBox.setSpacing(10);
                randomnessSliders = new VBox();
                randomnessSliders.setSpacing(5);
                    strenghtSliderHBox = new HBox();
                    strenghtSliderHBox.setSpacing(10);
                        strenghtSliderLabel = new Label("Strenth:");
                        strenghtSliderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        strenghtSliderLabel.setTextFill(strenghtColor);
                        strenghtSlider = new Slider(0, 50, 16);
                        strenghtSlider.setBlockIncrement(1);
                        strenghtSlider.setMajorTickUnit(10);
                        strenghtSlider.setMinorTickCount(9);
                        strenghtSlider.setShowTickLabels(true);
                        strenghtSlider.setSnapToTicks(true);
                        strenghtSliderPercentage = new Label();
                        strenghtSliderPercentage.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        strenghtSliderPercentage.setTextFill(strenghtColor);
                        strenghtSliderPercentage.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString((int) strenghtSlider.getValue()) + "%", strenghtSlider.valueProperty()));
                    strenghtSliderHBox.getChildren().addAll(strenghtSliderLabel, strenghtSlider, strenghtSliderPercentage);
                    weaknessSliderHBox = new HBox();
                    weaknessSliderHBox.setSpacing(10);
                        weaknessSliderLabel = new Label("Weakness:");
                        weaknessSliderLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        weaknessSliderLabel.setTextFill(weaknessColor);
                        weaknessSlider = new Slider(0, 50, 16);
                        weaknessSlider.setBlockIncrement(1);
                        weaknessSlider.setMajorTickUnit(10);
                        weaknessSlider.setMinorTickCount(9);
                        weaknessSlider.setShowTickLabels(true);
                        weaknessSlider.setSnapToTicks(true);
                        weaknessSliderPercentage = new Label();
                        weaknessSliderPercentage.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        weaknessSliderPercentage.setTextFill(weaknessColor);
                        weaknessSliderPercentage.textProperty().bind(Bindings.createStringBinding(() -> Integer.toString((int) weaknessSlider.getValue()) + "%", weaknessSlider.valueProperty()));
                    weaknessSliderHBox.getChildren().addAll(weaknessSliderLabel, weaknessSlider, weaknessSliderPercentage);
                randomnessSliders.getChildren().addAll(strenghtSliderHBox, weaknessSliderHBox);
                randomnessSliders.setPrefWidth(300);
                randomnessButton = new Button("randomize\nconfiguration"); //f39c6b
                randomnessButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 18));
                randomnessButton.setBackground(new Background(new BackgroundFill(Color.rgb(248, 244, 166), new CornerRadii(12), randomnessButton.getInsets())));
                randomnessButton.setBorder(new Border(new BorderStroke(Color.rgb(224, 142, 69), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3)))); 
                randomnessButton.prefHeightProperty().bind(randomnessSliders.heightProperty());
                randomnessButton.setOnAction(e -> AdvantageSettings.settings.putAllRandomly(strenghtSlider.getValue()*0.01, weaknessSlider.getValue()*0.01));
                randomnessButton.setPrefWidth(150);
            randomnessBox.getChildren().addAll(randomnessSliders, randomnessButton);
        randomnessSection.getChildren().addAll(randomnessTitle, randomnessBox);
        advantageSettingsSection.getChildren().add(randomnessSection);
    }

    private void makeFileSection() {
        fileSection = new VBox();
        fileSection.setSpacing(5);
            Label titleLabel = new Label("File saving and loading");
            titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            titleLabel.setTextFill(Color.rgb(255, 240, 31));
            fileSaveBox = new HBox();
                fileNameTextField = new TextField();
                fileNameTextField.setPromptText("filename");
                fileNameTextField.setBackground(new Background(new BackgroundFill(MyColors.celadon, new CornerRadii(12), fileNameTextField.getInsets())));
                fileNameTextField.setBorder(new Border(new BorderStroke(MyColors.pigmentGreen, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                fileNameTextField.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                fileNameTextField.setPrefWidth(200);
                saveButton = new Button("Save configuration to file");
                saveButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                saveButton.setBackground(new Background(new BackgroundFill(MyColors.robinEggBlue, new CornerRadii(12), saveButton.getInsets())));
                saveButton.setBorder(new Border(new BorderStroke(MyColors.ylnMnBlue, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));  
                saveButton.setOnAction(e -> {AdvantageSettings.settings.saveToFile(fileNameTextField.getText());
                                             updateFileList();});
            fileSaveBox.getChildren().addAll(fileNameTextField, saveButton);
            fileLoadBox = new HBox();
                fileNameChoiceBox = new ChoiceBox<>();
                updateFileList();
                MyChoiceBoxSkin<String> fileNameChoiceBoxSkin = new MyChoiceBoxSkin<>(fileNameChoiceBox, new DefaultStringConverter());
                fileNameChoiceBoxSkin.setBackgroundColorField(MyColors.celadon);
                fileNameChoiceBoxSkin.setBorderColorField(MyColors.pigmentGreen);
                fileNameChoiceBoxSkin.setCellSize(35);
                fileNameChoiceBox.setPrefWidth(200);
                fileNameChoiceBox.setSkin(fileNameChoiceBoxSkin);
                loadButton = new Button("load configuration from file");
                loadButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                loadButton.setBackground(new Background(new BackgroundFill(MyColors.robinEggBlue, new CornerRadii(9), saveButton.getInsets())));
                loadButton.setBorder(new Border(new BorderStroke(MyColors.ylnMnBlue, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));  
                loadButton.setOnAction(e -> {AdvantageSettings.settings.readFromFile(fileNameChoiceBox.getValue());
                                             updateFileList();});
            fileLoadBox.getChildren().addAll(fileNameChoiceBox, loadButton);
        fileSection.getChildren().addAll(titleLabel, fileSaveBox, fileLoadBox);
        advantageSettingsSection.getChildren().add(fileSection);
    }

    private void makeMultiplySliderSection() {
        damageMultiplier = new SimpleDoubleProperty(settings.getAdvantageMultiplier());
        settings.advantageMultiplierProperty().bind(MyBindings.roundOnDecimals(damageMultiplier, 2));

        multiplySliderSection = new VBox();
        multiplySliderSection.setSpacing(5);
            multiplySliderTitle = new Label("Multiply slider");
            multiplySliderTitle.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            multiplySliderTitle.setTextFill(Color.rgb(31, 255, 113));
            multiplySliderDescription = new Text("The factor by which an attack is stronger, when it is 'strong' against\na certain defense.\n(When an attack is weaker the inverse of this factor gets used).");
            multiplySliderDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            multiplySliderDescription.setFill(Color.rgb(179, 255, 207));
            multiplySlider = new Slider(1, 2, settings.getAdvantageMultiplier());
            multiplySlider.setBlockIncrement(0.01);
            multiplySlider.setMajorTickUnit(0.2);
            multiplySlider.setMinorTickCount(9);
            multiplySlider.setShowTickLabels(true);
            multiplySlider.setSnapToTicks(true);
            damageMultiplier.bind(multiplySlider.valueProperty());
            multiplySliderOutcome = new Label();
            multiplySliderOutcome.setTextFill(Color.rgb(31, 255, 113));
            multiplySliderOutcome.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            multiplySliderOutcome.textProperty().bind(Bindings.concat("A strong attack is ", settings.advantageMultiplierProperty().asString(), "x stronger."));
        multiplySliderSection.getChildren().addAll(multiplySliderTitle, multiplySliderDescription, multiplySlider, multiplySliderOutcome);
        advantageSettingsSection.getChildren().add(multiplySliderSection);
    }

    private void makeTypeCheckSection() {
        //initiate variables
        selectedAttackType = new SimpleObjectProperty<Type>();
        selectedDefenseType = new SimpleObjectProperty<Type>();
        selectedDefenseCreature = new SimpleObjectProperty<Creature>();
        defenseKind = new SimpleObjectProperty<DefenseKind>(DefenseKind.TYPE);
        attackOutcome = new SimpleDoubleProperty(1d);
        searchString = new SimpleStringProperty("");

        selectedAttackType.addListener((p, ov, nv) -> updateOutcome());
        selectedDefenseType.addListener((p, ov, nv) -> updateOutcome());
        selectedDefenseCreature.addListener((p, ov, nv) -> updateOutcome());
        defenseKind.addListener((p, ov, nv) -> updateOutcome());
        defenseKind.addListener((p, ov, nv) -> updateDefenseDisplay());
        searchString.addListener((p, ov, nv) -> updateSearchSuggestions());
        settings.advantageMultiplierProperty().addListener((p, ov, nv) -> updateOutcome());
        //appearance
        typeCheckSection = new VBox();
        typeCheckSection.setSpacing(5);
            typeCheckTitle = new Label("Damage testing");
            typeCheckTitle.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            typeCheckTitle.setTextFill(Color.rgb(31, 162, 255));
            typeCheckGridPane = new GridPane();
            typeCheckGridPane.setPadding(new Insets(7));
            typeCheckGridPane.setBackground(new Background(new BackgroundFill(Color.rgb(155, 59, 78), new CornerRadii(10), new Insets(0))));
            typeCheckGridPane.setHgap(10);
            typeCheckGridPane.setVgap(7);
                attackBox = new HBox();
                attackBox.setPrefWidth(damageTestingFieldsWidth);
                    attackChoiceBox = new ChoiceBox<Type>();
                    attackChoiceBox.getItems().addAll(Type.allTypes());
                    TypeChoiceBox attackChoiceBoxSkin = new TypeChoiceBox(attackChoiceBox);
                    attackChoiceBox.setSkin(attackChoiceBoxSkin);
                    attackChoiceBox.setPrefWidth(damageTestingFieldsWidth);
                    selectedAttackType.bind(attackChoiceBox.valueProperty());
                attackBox.getChildren().add(attackChoiceBox);
            typeCheckGridPane.add(attackBox, 0, 0);
                swords = new SwordShape(40); 
            typeCheckGridPane.add(swords, 1, 0);
                defenseBox = new HBox();
                defenseBox.setPrefWidth(damageTestingFieldsWidth);
                defenseBox.setOnMouseClicked(e -> defenseKind.set(DefenseKind.TYPE));
                    defenseChoiceBox = new ChoiceBox<Type>();
                    defenseChoiceBox.getItems().addAll(Type.allTypes());
                    TypeChoiceBox defenseChoiceBoxSkin = new TypeChoiceBox(defenseChoiceBox);
                    defenseChoiceBox.setSkin(defenseChoiceBoxSkin);
                    defenseChoiceBox.setPrefWidth(damageTestingFieldsWidth);
                    selectedDefenseType.bind(defenseChoiceBox.valueProperty());
                    defenseCreatureBox = new VBox();
                    defenseCreatureBox.setBackground(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(10), new Insets(1))));
                    defenseCreatureBox.setBorder(new Border(new BorderStroke(MyColors.earthYellow, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                    defenseCreatureBox.setAlignment(Pos.CENTER);
                    defenseCreatureBox.setPrefWidth(damageTestingFieldsWidth);
                        defenseCreature = new CreatureLabel(null);
                        defenseCreature.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
                        defenseCreature.creatureProperty().bind(selectedDefenseCreature);
                    defenseCreatureBox.getChildren().add(defenseCreature);
                defenseBox.getChildren().add(defenseChoiceBox);
            typeCheckGridPane.add(defenseBox, 2, 0);
                    Rectangle base = new Rectangle(-arrowSize, -arrowSize*0.3, arrowSize*0.6, arrowSize*0.6);
                    Polygon point = new Polygon(-arrowSize*0.4, -arrowSize*0.5, -arrowSize*0.4, arrowSize*0.5, 0, 0);
                arrow = Shape.union(base, point);
                arrow.setFill(MyColors.robinEggBlue);
                arrow.setStroke(MyColors.robinEggBlue2);
                arrow.setStrokeWidth(4);
            typeCheckGridPane.add(arrow, 3, 0);
                outcomeLabel = new Label();
                outcomeLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
                outcomeLabel.textFillProperty().bind(Bindings.when(attackOutcome.lessThan(1.0)).then(weaknessColor).otherwise(Bindings.when(attackOutcome.greaterThan(1.0)).then(strenghtColor).otherwise(Color.WHITE)));
                outcomeLabel.textProperty().bind(attackOutcome.asString().concat("x"));
            typeCheckGridPane.add(outcomeLabel, 4, 0);
            searchBox = new VBox();
                creatureSearchField = new TextField();
                creatureSearchField.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 20));
                creatureSearchField.setPromptText("search creature");
                creatureSearchField.setBackground(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(10), new Insets(1))));
                creatureSearchField.setBorder(new Border(new BorderStroke(MyColors.earthYellow, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                searchString.bind(creatureSearchField.textProperty());
            searchBox.getChildren().add(creatureSearchField);
                creatureSuggestions = new VBox();
                    suggestionLabels = new LinkedList<>();
                    suggestionsShown = 0;
                    for (int i=0; i<maxSearchSuggestions; i++) {
                        CreatureAddLabel newLabel = new CreatureAddLabel(null);
                        newLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
                        newLabel.setInfoEvent(e -> newLabel.getCreature().gotClickedOn());
                        newLabel.setAddEvent(e -> {selectedDefenseCreature.set(newLabel.getCreature());
                                                   defenseKind.set(DefenseKind.CREATURE);
                                                   creatureSearchField.setText("");});     
                        suggestionLabels.add(newLabel);
                    }
            searchBox.getChildren().add(creatureSuggestions);
        typeCheckSection.getChildren().addAll(typeCheckTitle, typeCheckGridPane, searchBox);
        advantageSettingsSection.getChildren().add(typeCheckSection);
    }

    //help functions
    private void updateFileList() {
        List<String> fileList = Arrays.asList(AdvantageSettings.settings.getConfigFolder().listFiles()).stream().map(x -> x.getName()).filter(x -> x.charAt(0)!='.').toList();
        fileNameChoiceBox.getItems().setAll(fileList);
        fileNameChoiceBox.prefHeightProperty().bind(Bindings.when(fileNameChoiceBox.showingProperty()).then(Math.min(fileList.size()*35, 100)).otherwise(40));
    }

    private void updateDefenseDisplay() {
        if (defenseKind.get() == DefenseKind.TYPE) {
            defenseBox.getChildren().remove(defenseCreatureBox);
            defenseBox.getChildren().add(defenseChoiceBox);
        } else {
            defenseBox.getChildren().remove(defenseChoiceBox);
            defenseBox.getChildren().add(defenseCreatureBox);
        }
    }

    private void updateOutcome() {
        if (selectedAttackType.get() == null) {
            attackOutcome.set(0);
            return;
        }
        if (defenseKind.get() == DefenseKind.TYPE) {
            double outcome = selectedAttackType.get().attacks(selectedDefenseType.get(), settings.getAdvantageMultiplier());
            attackOutcome.set((double) Math.round(outcome*100)/100);
            return;
        } else if (defenseKind.get() == DefenseKind.CREATURE) {
            if (selectedDefenseCreature.get() == null) {
                attackOutcome.set(0);
                return;
            }
            double outcome = AdvantageSettings.settings.calculateAttackMultiplier(selectedAttackType.get(), selectedDefenseCreature.get(), settings.getAdvantageMultiplier());
            attackOutcome.set((double) Math.round(outcome*100)/100);
            return;
        }
    }

    private void updateSearchSuggestions() { 
        List<Creature> foundCreatures = creatures.stream().filter(c -> c.getName().toLowerCase().substring(0, Math.min(c.getName().length(), searchString.get().length())).equals(searchString.get().toLowerCase())).sorted((Creature c1, Creature c2) -> (c1.getName().toLowerCase().compareTo(c2.getName().toLowerCase()))).toList();
        if (foundCreatures.size()>maxSearchSuggestions || foundCreatures.size()==0 || searchString.get().equals("")) {
            for (CreatureAddLabel l: suggestionLabels) {
                creatureSuggestions.getChildren().remove(l);
                suggestionsShown = 0;
            }
        } else {
            for (int i=0; i<maxSearchSuggestions; i++) {
                if (i<foundCreatures.size()) { //is supposed to be there
                    if (i<suggestionsShown) { //is there
                        suggestionLabels.get(i).setCreature(foundCreatures.get(i));
                    } else { //is not there
                        creatureSuggestions.getChildren().add(suggestionLabels.get(i));
                        suggestionLabels.get(i).setCreature(foundCreatures.get(i));
                    }
                } else { //is not supposed to be there
                    creatureSuggestions.getChildren().remove(suggestionLabels.get(i));
                }
            }
            suggestionsShown = foundCreatures.size();
        }
    }

    //listening commands

    @Override
    public void onCreatureCreate(Creature c) {
        creatures.add(c);
    }

    @Override
    public void onCreatureDelete(Creature c) {
        creatures.remove(c);
    }

    @Override
    public void onCreatureUpdate(Creature c) {}
}

enum DefenseKind {
    TYPE, CREATURE;
}