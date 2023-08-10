package evolution.Visual.VisualElements;

import evolution.Creature;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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

public class CreatureAddLabel extends HBox{
    private Creature selectedCreature;
    private VBox creatureSection;
        private CreatureLabel creatureLabel; 
    private Button infoButton;
    private Button addButton;
    public CreatureAddLabel(Creature c) {
        super();
        selectedCreature = c;
        //creatureNameSection
            creatureLabel = new CreatureLabel("-", selectedCreature);
            creatureLabel.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
        creatureSection = new VBox(creatureLabel);
        creatureSection.setAlignment(Pos.CENTER_LEFT);
        creatureSection.setPadding(new Insets(0, 3, 0, 3));
        creatureSection.backgroundProperty().bind(Bindings.when(creatureLabel.aliveProperty())
            .then(new Background(new BackgroundFill(MyColors.dutchWhite, new CornerRadii(10), new Insets(0))))
            .otherwise(new Background(new BackgroundFill(MyColors.dutchWhite.grayscale(), new CornerRadii(10), new Insets(0)))));
        creatureSection.borderProperty().bind(Bindings.when(creatureLabel.aliveProperty())
            .then(new Border(new BorderStroke(MyColors.sunset, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))))
            .otherwise(new Border(new BorderStroke(MyColors.sunset.grayscale(), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2)))));
        //info button
        infoButton = new Button("ℹ");
        infoButton.setBackground(new Background(new BackgroundFill(MyColors.iceBlue, new CornerRadii(12), new Insets(0))));
        infoButton.setBorder(new Border(new BorderStroke(MyColors.verdrigis, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        infoButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
        //addButton
        addButton = new Button("+");
        addButton.setBackground(new Background(new BackgroundFill(Color.rgb(87, 199, 135), new CornerRadii(12), new Insets(0))));
        addButton.setBorder(new Border(new BorderStroke(Color.rgb(50, 150, 93), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        addButton.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
        //sizes
        creatureSection.prefHeightProperty().bind(this.prefHeightProperty());
        infoButton.prefHeightProperty().bind(this.prefHeightProperty());
        addButton.prefHeightProperty().bind(this.prefHeightProperty());
        infoButton.prefWidthProperty().bind(this.prefHeightProperty());
        addButton.prefWidthProperty().bind(this.prefHeightProperty());
        creatureSection.prefWidthProperty().bind(this.widthProperty().subtract(infoButton.widthProperty()).subtract(addButton.widthProperty()));
        //round up
        this.getChildren().addAll(creatureSection, infoButton, addButton);
    }
    //getters
    public Creature getCreature() {
        return selectedCreature;
    }

    //setters
    public void setCreature(Creature c) {
        creatureLabel.setCreature(c);
        selectedCreature = c;
    }

    public void setInfoEvent(EventHandler<ActionEvent> event) {
        infoButton.setOnAction(event);
    }

    public void setAddEvent(EventHandler<ActionEvent> event) {
        addButton.setOnAction(event);
    }

    public void setFont(Font f) {
        creatureLabel.setFont(f);
        infoButton.setFont(f);
        addButton.setFont(f);
    }
}
