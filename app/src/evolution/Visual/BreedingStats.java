package evolution.Visual;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import evolution.BreedingSettings;
import evolution.Creature;
import evolution.FightingSettings;
import evolution.NameGenerator;
import evolution.Relationship;
import evolution.Relationship.Relation;
import evolution.Visual.VisualElements.CreatureAddLabel;
import evolution.Visual.VisualElements.CreatureLabel;
import evolution.Visual.VisualElements.HeartShape;
import evolution.Visual.VisualElements.MultiFacedDisplay;
import evolution.Visual.VisualElements.MyChoiceBoxSkin;
import evolution.Visual.VisualElements.MyColors;
import evolution.Visual.VisualElements.SwordShape;
import evolution.World.CreatureListener;
import evolution.World.WorldListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.converter.DefaultStringConverter;

public class BreedingStats extends VBox implements CreatureListener, WorldListener {
    private BreedingSettings settings;
    private DoubleProperty averageMutations;
    private DoubleProperty crossingOverProbability;
    private DoubleProperty attraction;
    private IntegerProperty incestPrevention;
    private DoubleProperty breedingProximity;
    private NameGenerator nameGenerator;
    private FightingSettings fightingSettings;
    private File homeFolder;
    private List<Creature> creatures;
    private ObjectProperty<Creature> selectedGirlfriend;
    private ObjectProperty<Creature> selectedBoyfriend;
    private IntegerProperty currentDay;
    
    //Visual variables
    private final int headerHeight = 60;
    private final int maxOptions = 10;
    private final double creatureLabelHeight = 20;
    private final int maxPredictions = 10;
    private final double predictionSquareHeight = 27;
    private final double predictionSquareWidth = 37;

    //Boxes
    private HBox headerSection;
    private VBox slidersSection;
        private VBox sliderBoxHeader;
        private VBox slidersBox;
    private VBox namePickerSection;
        private VBox namePickerHeader;
        private VBox namePickerContents;
            private HBox fileSelectBox;
    private VBox compatibilitySection;
        private VBox compatibilityHeader;
        private VBox compatibilityContent;
            private GridPane compatibilitySelect;
                private VBox girlfriendSuggestionBox;
                private VBox boyfriendSuggestionBox;
            private GridPane compatibilityDisplay;

    //Components
    private Label titleLabel;
    private Slider mutationsSlider;
    private Slider crossingOverSlider;
    private Slider attractionSlider;
    private Slider incestPreventionSlider;
    private Slider breedingProximitySlider;
    private ChoiceBox<String> fileSelectionChoiceBox;
    private Button fileSelectionButton;
    private Label fileSelectionErrorMessage;
    private Label fileSelectionStatusMessage;
    private CreatureLabel selectedGirlfriendLabel;
    private CreatureLabel selectedBoyfriendLabel;
    private TextField girlfriendSearcher;
    private TextField boyfriendSearcher;
    private LinkedList<CreatureAddLabel> girlfriendSuggestionLabels;
    private LinkedList<CreatureAddLabel> boyfriendSuggestionLabels;
    private LinkedList<MultiFacedDisplay<Relationship.Relation>> feelingDisplays;
    public BreedingStats(BreedingSettings breedingSettings, NameGenerator nameGenerator, FightingSettings fightingSettings) {
        super();
        this.setBackground(new Background(new BackgroundFill(MyColors.wheat, null, null)));
        this.setPadding(new Insets(5, 20, 5, 20));
        this.setSpacing(15);
        //initiate variables
        settings = breedingSettings;
        this.nameGenerator = nameGenerator;
        this.fightingSettings = fightingSettings;
        averageMutations = new SimpleDoubleProperty(breedingSettings.getAverageMutations());
        crossingOverProbability = new SimpleDoubleProperty(breedingSettings.getCrossingOverProbability());
        attraction = new SimpleDoubleProperty(breedingSettings.getAttraction());
        incestPrevention = new SimpleIntegerProperty(breedingSettings.getIncestPrevention());
        breedingProximity = new SimpleDoubleProperty(breedingSettings.getBreedingProximity());
        creatures = new LinkedList<>();
        currentDay = new SimpleIntegerProperty();
        currentDay.addListener((p, ov, nv) -> updateCompatibilityDisplay());
        //bind
        breedingSettings.averageMutationsProperty().bind(averageMutations);
        breedingSettings.crossingOverProbabilityProperty().bind(crossingOverProbability);
        breedingSettings.attractionProperty().bind(attraction);
        breedingSettings.incestPreventionProperty().bind(incestPrevention);
        breedingSettings.breedingProximityProperty().bind(breedingProximity);
        //visual
        //breeding settings
        headerSection = new HBox();
        headerSection.setPadding(new Insets(0, 10, 0, 10));
        titleLabel = new Label("Breeding");
        titleLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 40));
        headerSection.getChildren().add(titleLabel);
        headerSection.setPrefHeight(headerHeight);
        this.getChildren().add(headerSection);
            //header
            sliderBoxHeader = new VBox();
            sliderBoxHeader.setPadding(new Insets(0, 10, 0, 10));
                Label sliderBoxTitle = new Label("Breeding settings");
                sliderBoxTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            sliderBoxHeader.getChildren().add(sliderBoxTitle);
            sliderBoxHeader.setPrefHeight(headerHeight);
            sliderBoxHeader.setBackground(new Background(new BackgroundFill(MyColors.cyclamen, new CornerRadii(10), new Insets(-2))));
            sliderBoxHeader.setBorder(new Border(new BorderStroke(MyColors.cerise, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(-2))));
            //mutations
            Label mutationLabel = new Label("Mutations");
            mutationLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            Text mutationDescription = new Text("The average amount of mutations that occur when a new child gets \nconceived.");
            mutationDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            mutationsSlider = new Slider(0, 5, settings.getAverageMutations());
            mutationsSlider.setBlockIncrement(0.01);
            mutationsSlider.setMajorTickUnit(1d);
            mutationsSlider.setMinorTickCount(4);
            mutationsSlider.setShowTickLabels(true);
            mutationsSlider.setSnapToTicks(true);
            averageMutations.bind(mutationsSlider.valueProperty());
            Label mutationValue = new Label();
            mutationValue.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            mutationValue.textProperty().bind(averageMutations.asString().concat(" average mutations"));
            //crossing over
            Label crossingOverLabel = new Label("Crossing over");
            crossingOverLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            Text crossingOverDescription = new Text("The probability that a crossing over occurs when a new child gets\nconceived.");
            crossingOverDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            crossingOverSlider = new Slider(0, 1, settings.getCrossingOverProbability());
            crossingOverSlider.setBlockIncrement(0.01);
            crossingOverSlider.setMajorTickUnit(0.1);
            crossingOverSlider.setMinorTickCount(4);
            crossingOverSlider.setShowTickLabels(true);
            crossingOverSlider.setSnapToTicks(true);
            crossingOverProbability.bind(crossingOverSlider.valueProperty());
            Label crossingOVerValue = new Label();
            crossingOVerValue.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            crossingOVerValue.textProperty().bind(crossingOverProbability.asString().concat(" chance of crossing over"));
            //attraction
            Label attractionLabel = new Label("Attraction");
            attractionLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            Text attractionDescription = new Text("The percentage that 2 creatures need to have in common to fall\nin love.");
            attractionDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            attractionSlider = new Slider(0, 100, settings.getAttraction()*100);
            attractionSlider.setBlockIncrement(1);
            attractionSlider.setMajorTickUnit(10);
            attractionSlider.setMinorTickCount(4);
            attractionSlider.setShowTickLabels(true);
            attractionSlider.setSnapToTicks(true);
            attraction.bind(attractionSlider.valueProperty().multiply(0.01));
            Label attractionValue = new Label();
            attractionValue.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            attractionValue.textProperty().bind(attraction.asString().concat("% in common causes attraction"));
            //generation
            Label incestPreventionLabel = new Label("Incest prevention");
            incestPreventionLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            Text incestPreventionDescription = new Text("The amount of generations to check back in order to determine\nwhether 2 creatures are too closely related to fall in love.");
            incestPreventionDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            incestPreventionSlider = new Slider(0, 5, settings.getIncestPrevention());
            incestPreventionSlider.setBlockIncrement(1);
            incestPreventionSlider.setMajorTickUnit(1);
            incestPreventionSlider.setMinorTickCount(0);
            incestPreventionSlider.setShowTickLabels(true);
            incestPreventionSlider.setSnapToTicks(true);
            incestPrevention.bind(incestPreventionSlider.valueProperty());
            Label incestPreventionValue = new Label();
            incestPreventionValue.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            incestPreventionValue.textProperty().bind(incestPrevention.asString().concat(" generations back"));
            //breeding proximity
            Label breedingProximityLabel = new Label("Breeding proximity");
            breedingProximityLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            Text breedingProximityDescription = new Text("The distance at which 2 creatures can succesfully breed.");
            breedingProximityDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            breedingProximitySlider = new Slider(Creature.defaultSize*2, 200, settings.getBreedingProximity());
            breedingProximitySlider.setBlockIncrement(1);
            breedingProximitySlider.setMajorTickUnit(10d);
            breedingProximitySlider.setMinorTickCount(4);
            breedingProximitySlider.setShowTickLabels(true);
            breedingProximitySlider.setSnapToTicks(true);
            breedingProximity.bind(breedingProximitySlider.valueProperty());
            Label breedingProximityValue = new Label();
            breedingProximityValue.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            breedingProximityValue.textProperty().bind(breedingProximity.asString().concat(" breeding distance"));
            
        //box
        slidersBox = new VBox(mutationLabel, mutationDescription, mutationsSlider, mutationValue,
                              crossingOverLabel, crossingOverDescription, crossingOverSlider, crossingOVerValue,
                              attractionLabel, attractionDescription, attractionSlider, attractionValue,
                              incestPreventionLabel, incestPreventionDescription, incestPreventionSlider, incestPreventionValue,
                              breedingProximityLabel, breedingProximityDescription, breedingProximitySlider, breedingProximityValue);
        slidersBox.setPadding(new Insets(0, 10, 5, 10));
        slidersSection = new VBox(sliderBoxHeader, slidersBox);
        slidersSection.setBackground(new Background(new BackgroundFill(MyColors.amaranthPink, new CornerRadii(10), null)));
        slidersSection.setBorder(new Border(new BorderStroke(MyColors.rosePompadour, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        this.getChildren().add(slidersSection);

        //name file settings
        homeFolder = new File("names");
            //header
            namePickerHeader = new VBox();
            namePickerHeader.setPadding(new Insets(0, 10, 0, 10));
                Label namePickerTitle = new Label("Names");
                namePickerTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            namePickerHeader.getChildren().add(namePickerTitle);
            namePickerHeader.setPrefHeight(headerHeight);
            namePickerHeader.setBackground(new Background(new BackgroundFill(MyColors.steelBlue, new CornerRadii(10), new Insets(-2))));
            namePickerHeader.setBorder(new Border(new BorderStroke(MyColors.lapisLazuli, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(-2))));
                Label namePickerDescription = new Label("File to pick the names from:");
                namePickerDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                    fileSelectionChoiceBox = new ChoiceBox<>();
                    fileSelectionChoiceBox.getItems().addAll(Arrays.asList(homeFolder.listFiles()).stream().map(x -> x.getName()).filter(x -> x.charAt(0)!='.').toList());
                    MyChoiceBoxSkin<String> fileSelectionChoiceBoxSkin = new MyChoiceBoxSkin<>(fileSelectionChoiceBox, new DefaultStringConverter());
                    fileSelectionChoiceBoxSkin.setBackgroundColorField(MyColors.lightGreen);
                    fileSelectionChoiceBoxSkin.setBorderColorField(MyColors.darkPastelGreen);
                    fileSelectionChoiceBox.setSkin(fileSelectionChoiceBoxSkin);
                    fileSelectionButton = new Button("change file");
                    fileSelectionButton.setBackground(new Background(new BackgroundFill(MyColors.peach, new CornerRadii(10), new Insets(0))));
                    fileSelectionButton.setBorder(new Border(new BorderStroke(MyColors.princetonOrange, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3), new Insets(0))));
                    fileSelectionButton.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                    fileSelectionButton.setOnMouseClicked(new EventHandler<Event>() {

                        @Override
                        public void handle(Event event) {
                            if (fileSelectionChoiceBox.getValue() != null) {
                                boolean succes = nameGenerator.setNameFile(fileSelectionChoiceBox.getValue());
                                if (succes) {
                                    fileSelectionErrorMessage.setText("File changed succesfully :)");
                                    fileSelectionErrorMessage.setTextFill(Color.BLACK);
                                } else {
                                    fileSelectionErrorMessage.setText("smth went wrong :(");
                                    fileSelectionErrorMessage.setTextFill(Color.RED);
                                }
                                fileSelectionChoiceBox.getItems().setAll(Arrays.asList(homeFolder.listFiles()).stream().map(x -> x.getName()).filter(x -> x.charAt(0)!='.').toList());
                            }
                        }
                        
                    });
                fileSelectBox = new HBox(fileSelectionChoiceBox, fileSelectionButton);
                fileSelectBox.setSpacing(15);
                fileSelectionErrorMessage = new Label();
                fileSelectionErrorMessage.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                fileSelectionStatusMessage = new Label();
                fileSelectionStatusMessage.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                fileSelectionStatusMessage.textProperty().bind(Bindings.concat("Currently in use file: ", this.nameGenerator.nameFileProperty()));
            namePickerContents = new VBox(namePickerDescription, fileSelectBox, fileSelectionErrorMessage, fileSelectionStatusMessage);
            namePickerContents.setPadding(new Insets(0, 10, 5, 10));
            namePickerContents.setSpacing(5);
        namePickerSection = new VBox(namePickerHeader, namePickerContents);
        namePickerSection.setBackground(new Background(new BackgroundFill(MyColors.blueGray, new CornerRadii(10), null)));
        namePickerSection.setBorder(new Border(new BorderStroke(MyColors.columbiaBlue, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        this.getChildren().add(namePickerSection);

        //compatibility
            compatibilityHeader = new VBox();
            compatibilityHeader.setPadding(new Insets(0, 10, 0, 10));
                Label compatibilityTitle = new Label("Compatibility");
                compatibilityTitle.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 30));
            compatibilityHeader.getChildren().add(compatibilityTitle);
            compatibilityHeader.setPrefHeight(headerHeight);
            compatibilityHeader.setBackground(new Background(new BackgroundFill(MyColors.amethyst, new CornerRadii(10), new Insets(-2))));
            compatibilityHeader.setBorder(new Border(new BorderStroke(MyColors.finn2, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(-2))));
                compatibilitySelect = new GridPane();
                    //girlfirend
                    selectedGirlfriend = new SimpleObjectProperty<>(null);
                    selectedGirlfriend.addListener((p, ov, nv) -> updateCompatibilityDisplay());
                        selectedGirlfriendLabel = new CreatureLabel("-", null);
                        selectedGirlfriendLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
                        selectedGirlfriendLabel.creatureProperty().bind(selectedGirlfriend);
                    VBox selectedGirlfriendBox = new VBox(selectedGirlfriendLabel);
                    selectedGirlfriendBox.backgroundProperty().bind(Bindings.when(selectedGirlfriendLabel.aliveProperty())
                        .then(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(10), new Insets(0))))
                        .otherwise(new Background(new BackgroundFill(MyColors.dutchWhite.grayscale(), new CornerRadii(10), new Insets(0)))));
                    selectedGirlfriendBox.borderProperty().bind(Bindings.when(selectedGirlfriendLabel.aliveProperty())
                        .then(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))))
                        .otherwise(new Border(new BorderStroke(MyColors.sunset.grayscale(), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2)))));
                    selectedGirlfriendBox.prefWidthProperty().bind(compatibilitySelect.widthProperty().multiply(0.4));
                    selectedGirlfriendBox.setPrefHeight(50);
                    selectedGirlfriendBox.setAlignment(Pos.CENTER);
                compatibilitySelect.add(selectedGirlfriendBox, 0, 0);
                    //heart
                    HeartShape heart = new HeartShape(55, Color.RED);
                    heart.getShapeToShow().setFill(MyColors.bittersweet);
                    heart.getShapeToShow().setStroke(MyColors.tomato);
                    heart.getShapeToShow().setStrokeWidth(4);
                compatibilitySelect.add(heart.getShapeToShow(), 1, 0);
                    //boyfriend
                    selectedBoyfriend = new SimpleObjectProperty<>(null);
                    selectedBoyfriend.addListener((p, ov, nv) -> updateCompatibilityDisplay());
                        selectedBoyfriendLabel = new CreatureLabel("-", null);
                        selectedBoyfriendLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 25));
                        selectedBoyfriendLabel.creatureProperty().bind(selectedBoyfriend);
                    VBox selectedBoyfriendBox = new VBox(selectedBoyfriendLabel);
                    selectedBoyfriendBox.backgroundProperty().bind(Bindings.when(selectedBoyfriendLabel.aliveProperty())
                        .then(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(10), new Insets(0))))
                        .otherwise(new Background(new BackgroundFill(MyColors.dutchWhite.grayscale(), new CornerRadii(10), new Insets(0)))));
                    selectedBoyfriendBox.borderProperty().bind(Bindings.when(selectedBoyfriendLabel.aliveProperty())
                        .then(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))))
                        .otherwise(new Border(new BorderStroke(MyColors.sunset.grayscale(), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2)))));
                    selectedBoyfriendBox.prefWidthProperty().bind(compatibilitySelect.widthProperty().multiply(0.4));
                    selectedBoyfriendBox.setPrefHeight(45);
                    selectedBoyfriendBox.setAlignment(Pos.CENTER);
                compatibilitySelect.add(selectedBoyfriendBox, 2, 0);
                    //girlfriend search textfield
                    girlfriendSearcher = new TextField();
                    girlfriendSearcher.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(12), new Insets(0))));
                    girlfriendSearcher.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                    girlfriendSearcher.prefWidthProperty().bind(compatibilitySelect.widthProperty().multiply(0.4));
                    girlfriendSearcher.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 18));
                    girlfriendSearcher.textProperty().addListener(new ChangeListener<String>() {

                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            updateSearchSuggestions(newValue, girlfriendSuggestionBox, girlfriendSuggestionLabels);
                        }
                        
                    });
                compatibilitySelect.add(girlfriendSearcher, 0, 1);
                    //boyfriend search textfield
                    boyfriendSearcher = new TextField();
                    boyfriendSearcher.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(12), new Insets(0))));
                    boyfriendSearcher.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
                    boyfriendSearcher.prefWidthProperty().bind(compatibilitySelect.widthProperty().multiply(0.4));
                    boyfriendSearcher.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 18));
                    boyfriendSearcher.textProperty().addListener(new ChangeListener<String>() {

                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            updateSearchSuggestions(newValue, boyfriendSuggestionBox, boyfriendSuggestionLabels);
                        }
                        
                    });
                compatibilitySelect.add(boyfriendSearcher, 2, 1);
                    //girlfriend search suggestions
                    girlfriendSuggestionBox = new VBox();
                    girlfriendSuggestionBox.prefWidthProperty().bind(compatibilitySelect.widthProperty().multiply(0.4));
                    girlfriendSuggestionLabels = new LinkedList<>();
                    for (int i = 0; i<maxOptions; i++) {
                        CreatureAddLabel cLabel = new CreatureAddLabel(null);
                        girlfriendSuggestionLabels.add(cLabel);
                        cLabel.setPrefHeight(creatureLabelHeight);
                        cLabel.setMaxHeight(creatureLabelHeight);
                        cLabel.prefWidthProperty().bind(girlfriendSuggestionBox.widthProperty());
                        cLabel.setInfoEvent(e -> cLabel.getCreature().gotClickedOn());
                        cLabel.setAddEvent(e -> {selectedGirlfriend.set(cLabel.getCreature());
                                                 girlfriendSearcher.setText("");});
                    }
                compatibilitySelect.add(girlfriendSuggestionBox, 0, 2);
                    //girlfriend search suggestions
                    boyfriendSuggestionBox = new VBox();
                    boyfriendSuggestionBox.prefWidthProperty().bind(compatibilitySelect.widthProperty().multiply(0.4));
                    boyfriendSuggestionLabels = new LinkedList<>();
                    for (int i = 0; i<maxOptions; i++) {
                        CreatureAddLabel cLabel = new CreatureAddLabel(null);
                        boyfriendSuggestionLabels.add(cLabel);
                        cLabel.setPrefHeight(creatureLabelHeight);
                        cLabel.setMaxHeight(creatureLabelHeight);
                        cLabel.prefWidthProperty().bind(boyfriendSuggestionBox.widthProperty());
                        cLabel.setInfoEvent(e -> cLabel.getCreature().gotClickedOn());
                        cLabel.setAddEvent(e -> {selectedBoyfriend.set(cLabel.getCreature());
                                                 boyfriendSearcher.setText("");});
                    }
                compatibilitySelect.add(boyfriendSuggestionBox, 2, 2);
                compatibilitySelect.setVgap(10);
                compatibilitySelect.setHgap(10);
                compatibilityDisplay = new GridPane();
                    feelingDisplays = new LinkedList<>();
                    Label dayLabel = new Label("Day");
                    dayLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
                    settings.attractionProperty().addListener((p, nv, ov) -> updateCompatibilityDisplay());
                    settings.incestPreventionProperty().addListener((p, nv, ov) -> updateCompatibilityDisplay());
                    fightingSettings.agressivityProperty().addListener((p, nv, ov) -> updateCompatibilityDisplay());
                    fightingSettings.infightingProtectionProperty().addListener((p, nv, ov) -> updateCompatibilityDisplay());
                    compatibilityDisplay.add(dayLabel, 0, 0);
                    for (int i=0; i<maxPredictions; i++) {
                        Label smallDayLabel = new Label();
                        smallDayLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 18));
                        smallDayLabel.textProperty().bind(currentDay.add(i).asString());
                        compatibilityDisplay.add(smallDayLabel, 1+i, 0);
                        Map<Relationship.Relation, Node> emotionHashmap = new HashMap<>();
                        //love
                        HeartShape heartInLoveBox = new HeartShape(18, Color.RED);
                        heartInLoveBox.getShapeToShow().setFill(MyColors.bittersweet);
                        heartInLoveBox.getShapeToShow().setStroke(MyColors.tomato);
                        heartInLoveBox.getShapeToShow().setStrokeWidth(2);
                        HBox loveBox = new HBox(heartInLoveBox.getShapeToShow());
                        loveBox.setBackground(new Background(new BackgroundFill(MyColors.amaranthPink2, new CornerRadii(5), null)));
                        loveBox.setBorder(new Border(new BorderStroke(MyColors.rosePompadour2, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
                        loveBox.setAlignment(Pos.CENTER);
                        loveBox.setPadding(new Insets(1));
                        emotionHashmap.put(Relationship.Relation.LOVE, loveBox);
                        //neutral
                        Rectangle neutralBox = new Rectangle(predictionSquareWidth, predictionSquareHeight, MyColors.mintCream);
                        neutralBox.setStroke(MyColors.ashGrey);
                        emotionHashmap.put(Relationship.Relation.NEUTRAL, neutralBox);
                        //hate
                        SwordShape swords = new SwordShape(18);
                        HBox hateBox = new HBox(swords);
                        hateBox.setBackground(new Background(new BackgroundFill(MyColors.blackBean, new CornerRadii(5), null)));
                        hateBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1))));
                        hateBox.setAlignment(Pos.CENTER);
                        hateBox.setPadding(new Insets(1));
                        emotionHashmap.put(Relationship.Relation.HATE, hateBox);
                        //dayRelationShipDisplay
                        MultiFacedDisplay<Relationship.Relation> dayRelationShipDisplay = new MultiFacedDisplay<Relationship.Relation>(emotionHashmap, new Circle(10, Color.WHITE), Relation.NEUTRAL);
                        dayRelationShipDisplay.setPrefSize(predictionSquareWidth, predictionSquareHeight);
                        dayRelationShipDisplay.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
                        dayRelationShipDisplay.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
                        feelingDisplays.add(dayRelationShipDisplay);
                        compatibilityDisplay.add(dayRelationShipDisplay, 1+i, 1);
                    }
                compatibilityDisplay.setHgap(5);
            compatibilityContent = new VBox(compatibilitySelect, compatibilityDisplay);
            compatibilityContent.setPadding(new Insets(5, 10, 5, 10));
            compatibilityContent.setAlignment(Pos.TOP_CENTER);
        compatibilitySection = new VBox(compatibilityHeader, compatibilityContent);
        compatibilitySection.setBackground(new Background(new BackgroundFill(MyColors.wisteria, new CornerRadii(10), null)));
        compatibilitySection.setBorder(new Border(new BorderStroke(MyColors.purpureus2, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        this.getChildren().add(compatibilitySection);
    }

    private void updateCompatibilityDisplay() {
        if (selectedGirlfriend.get() == null || selectedBoyfriend.get() == null) {
            return;
        }
        int day;
        for (int i=0; i<maxOptions; i++) {
            day = currentDay.get()+i;
            feelingDisplays.get(day).setValue(Relationship.evaluate(selectedBoyfriend.get(), selectedGirlfriend.get(), day, settings.getIncestPrevention(),  fightingSettings.getInfightingProtection(), settings.getAttraction(), fightingSettings.getAgressivity()));
        }
    }

    private void updateSearchSuggestions(String searchString, VBox box, List<CreatureAddLabel> labels) {
        for (int i = 0; i<labels.size(); i++) {
            box.getChildren().remove(labels.get(i));
        }
        List<Creature> options = creatures.stream().filter(c -> c.getName().toLowerCase().substring(0, Math.min(searchString.length(), c.getName().length())).equals(searchString.toLowerCase())).toList();
        if (options.size()<=maxOptions && !searchString.equals("")) {
            for (int i = 0; i<options.size(); i++) {
                labels.get(i).setCreature(options.get(i));
                box.getChildren().add(labels.get(i));
            }
        }
    }

    @Override
    public void onCreatureCreate(Creature c) {
        creatures.add(c);
    }

    @Override
    public void onCreatureDelete(Creature c) {
        creatures.remove(c);
    }

    @Override
    public void setupNeededConnections(SimpleIntegerProperty day, SimpleDoubleProperty worldSize) {
        currentDay.bind(day);
    }
}
