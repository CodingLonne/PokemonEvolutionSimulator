package evolution;

import java.util.Arrays;
import java.util.HashMap;
import evolution.ScreenManager.screenManagerOwned;
import evolution.VisualElements.MyColors;
import evolution.VisualElements.TypeCodeDisplay;
import evolution.VisualElements.TypePieChart;
import evolution.World.CreatureListener;
import evolution.proteinEncodingManager.proteinChangeListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;


public class TypeStatsDisplay extends VBox implements screenManagerOwned, CreatureListener, proteinChangeListener{
    private World world;
    private proteinEncodingManager encodingManager;
    private SimpleStringProperty title;
    private Label shortcutLabel;
    private HBox pieChartSection;
    private HBox TypeTypeCodeSection;
    private HBox geneModifyingSection;
    private HBox geneSafeSection;
    private HBox sliderInfoBox;
    private HBox typeAdvantageStrength;
    private GridPane geneTypeChart;
    private TypePieChart piechartDefense;
    private TypePieChart piechartOffense;
    private HashMap<Type, SimpleIntegerProperty> typeDefenseData;
    private HashMap<Type, SimpleIntegerProperty> typeOffenseData;
    private SimpleBooleanProperty newSettingsSaved;
    private SimpleBooleanProperty newSettingsSavable;
    private SimpleBooleanProperty[][] geneEncodingData;
    private SimpleStringProperty[] geneEncodingStrings;
    private SimpleBooleanProperty[] validGenes;
    private TextField shortcutTextArea;
    private Button geneSafeButton;
    private Label typeAdvantageFactorInfo;
    private Slider typeAdvantagetrengthSlider;
    TypeStatsDisplay(proteinEncodingManager encodingManager, World world){
        super();
        this.world = world;
        this.encodingManager = encodingManager;
        encodingManager.addListener(this);
        title = new SimpleStringProperty("genes");
        //piechart section
        typeDefenseData = setUpTypePiechartData();
        typeOffenseData = setUpTypePiechartData();
        piechartDefense = new TypePieChart(typeDefenseData, this.widthProperty().divide(2), "Defense", "No creatures\nalive");
        piechartOffense = new TypePieChart(typeOffenseData, this.widthProperty().divide(2), "Offense", "No creatures\nalive");
        pieChartSection = new HBox(piechartDefense, piechartOffense);
        this.getChildren().add(pieChartSection);
        //type the shortcut code section
        shortcutLabel = new Label("Shortcut gene settings:");
        shortcutLabel.setTextFill(Color.BLACK);
        shortcutLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        shortcutTextArea = new TextField(encodingManager.getSetting());
        shortcutTextArea.setBackground(new Background(new BackgroundFill(MyColors.celadon, new CornerRadii(12), shortcutTextArea.getInsets())));
        shortcutTextArea.setBorder(new Border(new BorderStroke(MyColors.pigmentGreen, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        shortcutTextArea.setFont(Font.font("Comic Sans MS", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        shortcutTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                adjustGenesDisplay();
            }
        });
        TypeTypeCodeSection = new HBox(shortcutLabel, shortcutTextArea);
        TypeTypeCodeSection.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().add(TypeTypeCodeSection);
        //Gene Type Chart
        geneModifyingSection = new HBox();
        geneTypeChart = new GridPane();
        geneTypeChart.setStyle("-fx-background-color: #EFDCBD;");
        geneTypeChart.setAlignment(Pos.CENTER);
            //genes
            Label GeneColumnName = new Label("Genes");
            GeneColumnName.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
            GeneColumnName.setTextFill(Color.BLACK);
            GeneColumnName.setPadding(new Insets(0, 5, 0, 5));
            geneTypeChart.add(GeneColumnName, 0, 0);
            //type
            Label TypeColumnName = new Label("Type");
            TypeColumnName.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
            TypeColumnName.setTextFill(Color.BLACK);
            TypeColumnName.setPadding(new Insets(0, 5, 0, 5));
            geneTypeChart.add(TypeColumnName, 1, 0);
            //Defense
            Label DefenseColumnName = new Label("Defense(%)");
            DefenseColumnName.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
            DefenseColumnName.setTextFill(Color.BLACK);
            DefenseColumnName.setPadding(new Insets(0, 5, 0, 5));
            geneTypeChart.add(DefenseColumnName, 2, 0);
            //Offense
            Label OffenseColumnName = new Label("Offense(%)");
            OffenseColumnName.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
            OffenseColumnName.setTextFill(Color.BLACK);
            OffenseColumnName.setPadding(new Insets(0, 5, 0, 5));
            geneTypeChart.add(OffenseColumnName, 3, 0);
        geneEncodingData = new SimpleBooleanProperty[Type.allTypes().length][proteinEncodingManager.proteinLenght];
        geneEncodingStrings = new SimpleStringProperty[Type.allTypes().length];
        newSettingsSaved = new SimpleBooleanProperty(true);
        validGenes = new SimpleBooleanProperty[Type.allTypes().length];
        setupGeneDisplay(geneTypeChart, geneEncodingData, geneEncodingStrings, validGenes, encodingManager);
        newSettingsSavable = new SimpleBooleanProperty(true);
        newSettingsSavable.bind(existsInvalid(validGenes).not());
        geneModifyingSection.getChildren().add(geneTypeChart);
        this.getChildren().add(geneModifyingSection);
        //gene safe
        geneSafeSection = new HBox();
        geneSafeSection.setPadding(new Insets(5, 0, 5, 0));
        geneSafeButton = new Button("Safe");
        geneSafeButton.textProperty().bind(
            Bindings.when(newSettingsSavable).
                then(Bindings.when(newSettingsSaved).
                        then("Saved").
                        otherwise("Save")).
                otherwise("invalid"));
        //size
        geneSafeButton.setPrefHeight(30);
        //appearance
        geneSafeButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
        geneSafeButton.setBackground(new Background(new BackgroundFill(MyColors.robinEggBlue, new CornerRadii(12), geneSafeButton.getInsets())));
        geneSafeButton.setBorder(new Border(new BorderStroke(MyColors.ylnMnBlue, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        //action
        geneSafeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (newSettingsSavable.get()) {
                    encodingManager.setSetting(shortcutTextArea.textProperty().get());
                    newSettingsSaved.set(true);
                }
            }
        });
        geneSafeSection.getChildren().add(geneSafeButton);
        this.getChildren().add(geneSafeSection);
        //TypeAdvantagetrength info and display
        typeAdvantageFactorInfo = new Label("The multiplication factor for type advantages:");
        typeAdvantageFactorInfo.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 18));
        typeAdvantageFactorInfo.textProperty().bind(new SimpleStringProperty("The multiplication factor for type advantages: ").concat(encodingManager.getTypeAdvantageStrengthProperty().asString()));
        sliderInfoBox = new HBox(typeAdvantageFactorInfo);
        this.getChildren().add(sliderInfoBox);
        //TypeAdvantagetrength slider
        typeAdvantageStrength = new HBox();
        typeAdvantagetrengthSlider = new Slider(1d, 2d, encodingManager.getTypeAdvantageStrength());
        typeAdvantagetrengthSlider.setMajorTickUnit(0.1d);
        typeAdvantagetrengthSlider.setMinorTickCount(5);
        typeAdvantagetrengthSlider.setSnapToTicks(true);
        typeAdvantagetrengthSlider.setBlockIncrement(0.1);
        typeAdvantagetrengthSlider.setShowTickMarks(true);
        typeAdvantagetrengthSlider.prefWidthProperty().bind(typeAdvantageStrength.widthProperty());
        
        encodingManager.getTypeAdvantageStrengthProperty().bind(Bindings.createDoubleBinding(() -> {
            return ((double) Math.round(typeAdvantagetrengthSlider.valueProperty().get()*100))/100;
        }, typeAdvantagetrengthSlider.valueProperty()));
        typeAdvantageStrength.getChildren().add(typeAdvantagetrengthSlider);
        this.getChildren().add(typeAdvantageStrength);
        //height of the whole screen
        this.minHeightProperty().bind(pieChartSection.heightProperty().add(TypeTypeCodeSection.heightProperty().add(geneModifyingSection.heightProperty().add(geneSafeSection.heightProperty()))).multiply(1.1));
    }

    public void setupGeneDisplay(GridPane grid, SimpleBooleanProperty[][] genes, SimpleStringProperty[] geneStrings, SimpleBooleanProperty[] validGenes, proteinEncodingManager informant) {
        Type[] typeList = Type.allTypes();
        SimpleStringProperty empty = new SimpleStringProperty("");
        for (int g = 0; g<typeList.length; g++) {
            genes[g] = new SimpleBooleanProperty[proteinEncodingManager.proteinLenght];
            for (int b = 0; b<proteinEncodingManager.proteinLenght; b++) {
                genes[g][b] = new SimpleBooleanProperty(informant.getGene(typeList[g]).charAt(b) == '1');
            }
        }
        StringExpression geneString;
        for (int g = 0; g<typeList.length; g++) {
            geneString = StringExpression.stringExpression(empty);
            for (int b = 0; b<proteinEncodingManager.proteinLenght; b++) {
                geneString = geneString.concat(Bindings.when(genes[g][b]).then("1").otherwise("0"));
            }
            geneStrings[g] = new SimpleStringProperty();
            geneStrings[g].bind(geneString);
        }
        for (int g = 0; g<typeList.length; g++) {
            validGenes[g] = new SimpleBooleanProperty(true);
            validGenes[g].bind(checkForDoubles(Type.allTypes(), Type.allTypes()[g], geneStrings).not());
        }
        Label l;
        for (int y = 0; y<typeList.length; y++) {
            grid.add(new TypeCodeDisplay(genes[y], validGenes[y], 30, this), 0, y+1);
            l = new Label(typeList[y].toString());
            l.setTextFill(typeList[y].getColor());
            l.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            l.setAlignment(Pos.CENTER);
            l.setPadding(new Insets(0, 5, 0, 5));
            l.setUnderline(true);
            grid.add(l, 1, y+1);
        }
        Label d1;
        Label o1;
        for (int y = 0; y<typeList.length; y++) {
            //make defense label
            Type currentType = typeList[y];
            d1 = new Label(currentType.toString());
            d1.setTextFill(currentType.getColor());
            d1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            d1.setAlignment(Pos.CENTER);
            d1.setPadding(new Insets(0, 5, 0, 5));
            //make offense label
            o1 = new Label(currentType.toString());
            o1.setTextFill(currentType.getColor());
            o1.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
            o1.setAlignment(Pos.CENTER);
            o1.setPadding(new Insets(0, 5, 0, 5));
            //link up
            d1.textProperty().bind(
                Bindings.when(piechartDefense.fractionProperty(currentType).isEqualTo(0)).
                then("-").
                otherwise(Bindings.createDoubleBinding(
                            () -> (double) Math.round(piechartDefense.fractionProperty(currentType).doubleValue()*1000)/10, 
                            piechartDefense.fractionProperty(currentType))
                        .asString().concat("%")));
            o1.textProperty().bind(
                Bindings.when(piechartOffense.fractionProperty(currentType).isEqualTo(0)).
                then("-").
                otherwise(Bindings.createDoubleBinding(
                            () -> (double) Math.round(piechartOffense.fractionProperty(currentType).doubleValue()*1000)/10, 
                            piechartOffense.fractionProperty(currentType))
                        .asString().concat("%")));
            //add to grid
            grid.add(d1, 2, y+1);
            grid.add(o1, 3, y+1);
        }

    }

    public BooleanExpression checkForDoubles(Type[] amongTypes, Type forType, SimpleStringProperty[] values) {
        SimpleBooleanProperty notTrue = new SimpleBooleanProperty(false);
        BooleanExpression result= notTrue.or(notTrue); //start with false
        int forTypeIndex = Arrays.asList(amongTypes).indexOf(forType);
        for (int t = 0; t<amongTypes.length; t++) {
            if (t != forTypeIndex) {
                result = result.or(values[t].isEqualTo(values[forTypeIndex]));
            }
        }
        return result;
    }

    public void adjustGeneShortcutText() {
        newSettingsSaved.set(false);
        String result = "";
        for (int g=0; g<geneEncodingStrings.length; g++) {
            result += this.encodingManager.bitsToChar(geneEncodingStrings[g].getValue());
        }
        shortcutTextArea.textProperty().setValue(result);
    }

    public void adjustGenesDisplay() {
        newSettingsSaved.set(false);
        String setting = shortcutTextArea.getText();
        String thisGene;
        for (int y=0; y<Type.allTypes().length; y++) {
            if (y<setting.length()) {
                thisGene = this.encodingManager.charToBits(setting.charAt(y));
            } else {
                thisGene = "0".repeat(proteinEncodingManager.proteinLenght);
            }
            for (int x=0; x<proteinEncodingManager.proteinLenght; x++) {
                geneEncodingData[y][x].setValue(thisGene.charAt(x) == '1');
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

    public BooleanBinding existsInvalid(SimpleBooleanProperty[] bees) {
        SimpleBooleanProperty notTrue = new SimpleBooleanProperty(false);
        BooleanBinding result = notTrue.or(notTrue);
        for (SimpleBooleanProperty b: bees) {
            result = result.or(b.not());
        }
        return result;
    }

    @Override
    public void bindHeaderValue(SimpleStringProperty label) {
        label.bind(title);
    }

    @Override
    public void onCreatureCreate(Creature c) {
        HashMap<Type, Integer> defense = c.getDefenseMap();
        HashMap<Type, Integer> offense = c.getOffenseMap();
        for (Type t: Type.allTypes()) {
            typeDefenseData.get(t).set(typeDefenseData.get(t).getValue()+defense.get(t));
            typeOffenseData.get(t).set(typeOffenseData.get(t).getValue()+offense.get(t));
        }
    }

    @Override
    public void onCreatureUpdate(Creature c) {
        //technically not needed
    }

    @Override
    public void onCreatureDelete(Creature c) {
        HashMap<Type, Integer> defense = c.getDefenseMap();
        HashMap<Type, Integer> offense = c.getOffenseMap();
        for (Type t: Type.allTypes()) {
            typeDefenseData.get(t).set(typeDefenseData.get(t).getValue()-defense.get(t));
            typeOffenseData.get(t).set(typeOffenseData.get(t).getValue()-offense.get(t));
        }
    }

    @Override
    public void onProteinChange() {
        for (Type t: Type.allTypes()) {
            typeDefenseData.get(t).set(0);
            typeOffenseData.get(t).set(0);
        }
    }

    @Override
    public void onProteinChangeFinished() {
        for (Type t: Type.allTypes()) {
            typeDefenseData.get(t).set(0);
            typeOffenseData.get(t).set(0);
        }
        for (Creature c: world.getCreatures()) {
            for (Type t: Type.allTypes()) {
                typeDefenseData.get(t).set(typeDefenseData.get(t).getValue()+c.getDefenseMap().get(t));
                typeOffenseData.get(t).set(typeOffenseData.get(t).getValue()+c.getOffenseMap().get(t));
            }
        }
    }
}
