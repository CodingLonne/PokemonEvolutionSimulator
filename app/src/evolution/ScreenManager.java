package evolution;

import evolution.VisualElements.MyColors;
import evolution.World.CreatureClickListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;


public class ScreenManager implements CreatureClickListener{
    private ScrollPane background;
    private Pane switchPane;
    private int options;
    private Button[] buttons;
    private Pane[] optionList;
    private SimpleStringProperty[] names;
    private SimpleIntegerProperty currentScreen;

    private class switchButton extends Button {
        private int num;
        switchButton(SimpleStringProperty name, int i) {
            super(name.getValue());
            this.textProperty().bind(name);
            this.num = i;
        }
        public int getNum() {
            return num;
        }
    }
    interface screenManagerOwned {
        void bindHeaderValue(SimpleStringProperty name);
    }

    ScreenManager(ScrollPane background, Pane switchPane, int options, Pane[] switchOptions, String[] names) {
        this.background = background;
        this.switchPane = switchPane;
        this.options = options;
        if (switchOptions.length < options) {
            throw new IllegalArgumentException("Too few panes were given! Only " + Integer.toString(switchOptions.length) + " of the " + Integer.toString(options) + " panes were given.");
        }
        this.optionList = switchOptions;
        if (names.length < options) {
            throw new IllegalArgumentException("Too few names were given! Only " + Integer.toString(names.length) + " of the " + Integer.toString(options) + " names were given.");
        }
        this.names = new SimpleStringProperty[options];
        for (int i=0; i<options; i++) {
            this.names[i] = new SimpleStringProperty(names[i]);
            if (optionList[i] instanceof screenManagerOwned) {
                ((screenManagerOwned) optionList[i]).bindHeaderValue(this.names[i]);
            }
        }
        this.currentScreen = new SimpleIntegerProperty(0);
        this.currentScreen.addListener(
            new ChangeListener<Number>() {

                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    switchPage(newValue.intValue());
                }
            
        });
        buttons = new Button[options];
        for (int i=0; i<options; i++) {
            buttons[i] = new switchButton(this.names[i], i);
            //size
            buttons[i].prefHeightProperty().bind(switchPane.heightProperty().divide(options));
            buttons[i].prefWidthProperty().bind(switchPane.widthProperty().divide(options));
            //appearance
            buttons[i].setBackground(new Background(new BackgroundFill(MyColors.robinEggBlue, new CornerRadii(12), buttons[i].getInsets())));
            buttons[i].setBorder(new Border(new BorderStroke(MyColors.ylnMnBlue, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
            //action
            buttons[i].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    buttonPress(((switchButton) event.getTarget()).getNum());
                }
            });
        }
        setButtonHighlight(currentScreen.getValue());
        this.switchPane.getChildren().addAll(buttons);
    }
    public void buttonPress(int i) {
        this.currentScreen.setValue(i);
    }
    public void switchPage(int page) {
        if (this.currentScreen.getValue() != page) {
            this.currentScreen.setValue(page);
        }
        this.background.setContent(optionList[page]);
        setButtonHighlight(page);
    }
    private void setButtonHighlight(int page) {
        for (int i = 0; i<options; i++) {
            if (page==i) {
                buttons[i].setBorder(new Border(new BorderStroke(MyColors.richBlack, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
            } else {
                buttons[i].setBorder(new Border(new BorderStroke(MyColors.ylnMnBlue, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(4))));
            }
        }
    }
    @Override
    public void OnCreatureClick(Creature c) {
        switchPage(2);
    }
}
