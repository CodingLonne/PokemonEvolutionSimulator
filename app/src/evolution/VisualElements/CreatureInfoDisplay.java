package evolution.VisualElements;

import evolution.Creature;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
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

public class CreatureInfoDisplay extends HBox{
    final private double nameFraction = 0.4;
    final private double barsFraction = 0.3;
    final private double lifesFraction = 0.1;
    final private double ageFraction = 0.2;

    private Creature selectedCreature;

    private VBox nameBox;
    private VBox barsBox;
    private VBox lifesBox;
    private VBox ageBox;

    private Label nameLabel;
    private MyStatsBar healthBar;
    private MyStatsBar energyBar;
    private Label killsLabel;
    private Label childrenLabel;
    private Label ageLabel;

    private SimpleStringProperty name;
    private SimpleObjectProperty<Color> type;
    private SimpleDoubleProperty health;
    private SimpleDoubleProperty energy;
    private SimpleIntegerProperty kills;
    private SimpleIntegerProperty children;
    private SimpleIntegerProperty age;
    public CreatureInfoDisplay(Creature creature) {
        selectedCreature = creature;

        name = new SimpleStringProperty("-");
        type = new SimpleObjectProperty<Color>(Color.BLACK);
        health = new SimpleDoubleProperty(0);
        energy = new SimpleDoubleProperty(0);
        kills = new SimpleIntegerProperty(0);
        children = new SimpleIntegerProperty(0);
        age = new SimpleIntegerProperty(0);

        //formatting
        this.setBackground(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(5), new Insets(0))));
        this.setBorder(new Border(new BorderStroke(MyColors.earthYellow, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(1), new Insets(0))));
        this.setSpacing(15);
        nameLabel = new Label();
        nameLabel.textProperty().bind(name);
        nameLabel.textFillProperty().bind(type);
        nameLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 28));
        healthBar = new MyStatsBar(1);
        healthBar.setColorScheme(MyColors.cherryBlossomPink, MyColors.folly);
        healthBar.setPrefWidth(200);
        healthBar.setPrefHeight(30);
        healthBar.progressProperty().bind(health);
        energyBar = new MyStatsBar(1);
        energyBar.setColorScheme(MyColors.lightGreen2, MyColors.SGBUSGreen);
        energyBar.setPrefWidth(200);
        energyBar.setPrefHeight(30);
        energyBar.progressProperty().bind(energy);
        killsLabel = new Label();
        killsLabel.textProperty().bind(kills.asString().concat("☠"));
        killsLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 23));
        childrenLabel = new Label();
        childrenLabel.textProperty().bind(children.asString().concat("♥"));
        childrenLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 23));
        ageLabel = new Label();
        ageLabel.textProperty().bind(age.asString().concat(" days"));
        ageLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));

        nameBox = new VBox(nameLabel);
        nameBox.prefHeightProperty().bind(this.heightProperty());
        nameBox.prefWidthProperty().bind(this.widthProperty().multiply(nameFraction));
        nameBox.setAlignment(Pos.CENTER_LEFT);
        barsBox = new VBox(healthBar, energyBar);
        barsBox.prefHeightProperty().bind(this.heightProperty());
        barsBox.prefWidthProperty().bind(this.widthProperty().multiply(barsFraction));
        barsBox.setAlignment(Pos.CENTER_LEFT);
        lifesBox = new VBox(killsLabel, childrenLabel);
        lifesBox.prefHeightProperty().bind(this.heightProperty());
        lifesBox.prefWidthProperty().bind(this.widthProperty().multiply(lifesFraction));
        lifesBox.setAlignment(Pos.CENTER_LEFT);
        ageBox = new VBox(ageLabel);
        ageBox.prefHeightProperty().bind(this.heightProperty());
        ageBox.prefWidthProperty().bind(this.widthProperty().multiply(ageFraction));
        ageBox.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().addAll(nameBox, barsBox, lifesBox, ageBox);
        connect(creature);

        this.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                selectedCreature.gotClickedOn();
            }
            
        });
    }
    public void connect(Creature c) {
        name.bind(c.nameProperty());
        type.set(c.mostProminentType().getColor());
        health.bind(c.healthProperty().divide(c.maxHealthProperty()));
        energy.bind(c.energyProperty().divide(c.maxEnergyProperty()));
        kills.bind(c.killProperty());
        children.bind(c.childrenAmountProperty());
        age.bind(c.ageProperty());
    }
    public void disconnect() {
        name.unbind();
        type.unbind();
        health.unbind();
        energy.unbind();
        kills.unbind();
        children.unbind();
        age.unbind();
    }
    public void setCreature(Creature c) {
        disconnect();
        selectedCreature = c;
        connect(c);
    }
}
