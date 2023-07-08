package evolution.Visual;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import evolution.BreedingSettings;
import evolution.Creature;
import evolution.NameGenerator;
import evolution.Visual.VisualElements.MyChoiceBoxSkin;
import evolution.Visual.VisualElements.MyColors;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
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
import javafx.util.converter.DefaultStringConverter;

public class BreedingStats extends VBox {
    private BreedingSettings affectedSettings;
    private DoubleProperty averageMutations;
    private DoubleProperty crossingOverProbability;
    private DoubleProperty attraction;
    private DoubleProperty breedingProximity;
    private NameGenerator nameGenerator;
    private File homeFolder;
    
    //Visual variables
    private int headerHeight;
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

    //Components
    private Label titleLabel;
    private Slider mutationsSlider;
    private Slider crossingOverSlider;
    private Slider attractionSlider;
    private Slider breedingProximitySlider;
    private ChoiceBox<String> fileSelectionChoiceBox;
    private Button fileSelectionButton;
    private Label fileSelectionErrorMessage;
    private Label fileSelectionStatusMessage;
    public BreedingStats(BreedingSettings breedingSettings, NameGenerator nameGenerator) {
        super();
        this.setBackground(new Background(new BackgroundFill(MyColors.wheat, null, null)));
        this.setPadding(new Insets(5, 30, 5, 20));
        this.setSpacing(15);
        //initiate variables
        affectedSettings = breedingSettings;
        this.nameGenerator = nameGenerator;
        averageMutations = new SimpleDoubleProperty(breedingSettings.getAverageMutations());
        crossingOverProbability = new SimpleDoubleProperty(breedingSettings.getCrossingOverProbability());
        attraction = new SimpleDoubleProperty(breedingSettings.getAttraction());
        breedingProximity = new SimpleDoubleProperty(breedingSettings.getBreedingProximity());
        //bind
        breedingSettings.averageMutationsProperty().bind(averageMutations);
        breedingSettings.CrossingOverProbabilityProperty().bind(crossingOverProbability);
        breedingSettings.AttractionProperty().bind(attraction);
        breedingSettings.BreedingProximityProperty().bind(breedingProximity);
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
            sliderBoxHeader.setBackground(new Background(new BackgroundFill(Color.rgb(244, 102, 147), new CornerRadii(10), new Insets(-2))));
            sliderBoxHeader.setBorder(new Border(new BorderStroke(Color.rgb(239, 26, 94), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(-2))));
            //mutations
            Label mutationLabel = new Label("Mutations");
            mutationLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            Label mutationDescription = new Label("The average amount of mutations that occur when a new child gets \nconceived.");
            mutationDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            mutationsSlider = new Slider(0, 5, affectedSettings.getAverageMutations());
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
            Label crossingOverDescription = new Label("The probability that a crossing over occurs when a new child gets\nconceived.");
            crossingOverDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            crossingOverSlider = new Slider(0, 1, affectedSettings.getCrossingOverProbability());
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
            Label attractionDescription = new Label("The probability 2 random creatures find each other attractive.");
            attractionDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            attractionSlider = new Slider(0, 1, affectedSettings.getAttraction());
            attractionSlider.setBlockIncrement(0.01);
            attractionSlider.setMajorTickUnit(0.1);
            attractionSlider.setMinorTickCount(4);
            attractionSlider.setShowTickLabels(true);
            attractionSlider.setSnapToTicks(true);
            attraction.bind(attractionSlider.valueProperty());
            Label attractionValue = new Label();
            attractionValue.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            attractionValue.textProperty().bind(attraction.asString().concat(" chance of attraction"));
            //breeding proximity
            Label breedingProximityLabel = new Label("Breeding proximity");
            breedingProximityLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            Label breedingProximityDescription = new Label("The distance at which 2 creatures can succesfully breed.");
            breedingProximityDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            breedingProximitySlider = new Slider(Creature.defaultSize, 50, affectedSettings.getBreedingProximity());
            breedingProximitySlider.setBlockIncrement(1);
            breedingProximitySlider.setMajorTickUnit(10d);
            breedingProximitySlider.setMinorTickCount(4);
            breedingProximitySlider.setShowTickLabels(true);
            breedingProximitySlider.setSnapToTicks(true);
            breedingProximity.bind(breedingProximitySlider.valueProperty());
            Label breedingProximityValue = new Label();
            breedingProximityValue.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
            breedingProximityValue.textProperty().bind(attraction.asString().concat(" breeding distance"));
        //box
        slidersBox = new VBox(mutationLabel, mutationDescription, mutationsSlider, mutationValue,
                              crossingOverLabel, crossingOverDescription, crossingOverSlider, crossingOVerValue,
                              attractionLabel, attractionDescription, attractionSlider, attractionValue,
                              breedingProximityLabel, breedingProximityDescription, breedingProximitySlider, breedingProximityValue);
        slidersBox.setPadding(new Insets(0, 10, 5, 10));
        slidersSection = new VBox(sliderBoxHeader, slidersBox);
        slidersSection.setBackground(new Background(new BackgroundFill(Color.rgb(249, 159, 188), new CornerRadii(10), null)));
        slidersSection.setBorder(new Border(new BorderStroke(Color.rgb(246, 121, 161), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
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
            namePickerHeader.setBackground(new Background(new BackgroundFill(Color.rgb(80, 134, 185), new CornerRadii(10), new Insets(-2))));
            namePickerHeader.setBorder(new Border(new BorderStroke(Color.rgb(52, 93, 131), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2), new Insets(-2))));
                Label namePickerDescription = new Label("File to pick the names from:");
                namePickerDescription.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
                    fileSelectionChoiceBox = new ChoiceBox<>();
                    fileSelectionChoiceBox.getItems().addAll(Arrays.asList(homeFolder.listFiles()).stream().map(x -> x.getName()).filter(x -> x.charAt(0)!='.').toList());
                    MyChoiceBoxSkin<String> fileSelectionChoiceBoxSkin = new MyChoiceBoxSkin<>(fileSelectionChoiceBox, new DefaultStringConverter());
                    fileSelectionChoiceBoxSkin.setBackgroundColorField(Color.rgb(143, 224, 147));
                    fileSelectionChoiceBoxSkin.setBorderColorField(Color.rgb(48, 176, 55));
                    fileSelectionChoiceBox.setSkin(fileSelectionChoiceBoxSkin);
                    fileSelectionButton = new Button("change file");
                    fileSelectionButton.setBackground(new Background(new BackgroundFill(Color.rgb(254, 192, 134), new CornerRadii(10), new Insets(0))));
                    fileSelectionButton.setBorder(new Border(new BorderStroke(Color.rgb(253, 150, 53), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3), new Insets(0))));
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
                fileSelectionStatusMessage.textProperty().bind(Bindings.concat("Currently in use file: ", nameGenerator.nameFileProperty()));
            namePickerContents = new VBox(namePickerDescription, fileSelectBox, fileSelectionErrorMessage, fileSelectionStatusMessage);
            namePickerContents.setPadding(new Insets(0, 10, 5, 10));
            namePickerContents.setSpacing(5);
        namePickerSection = new VBox(namePickerHeader, namePickerContents);
        namePickerSection.setBackground(new Background(new BackgroundFill(Color.rgb(182, 205, 226), new CornerRadii(10), null)));
        namePickerSection.setBorder(new Border(new BorderStroke(Color.rgb(109, 154, 197), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        this.getChildren().add(namePickerSection);
    }

}
