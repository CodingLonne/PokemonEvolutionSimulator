package evolution.Visual.VisualElements;

import evolution.Type;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SkinBase;
import javafx.scene.control.cell.ChoiceBoxListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;

public class TypeChoiceBox extends SkinBase<ChoiceBox<Type>>{
    private Label label;
    private VBox background;
    private ListView<Type> selectTable;
    private SimpleBooleanProperty isSelecting;
    //colors
    private Color backgroundColorField = MyColors.dutchWhite;
    private Color borderColorField = MyColors.earthYellow;

    public TypeChoiceBox(ChoiceBox<Type> control) {
        super(control);
        control.setBackground(null);
        isSelecting = new SimpleBooleanProperty(false);
        isSelecting.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    showList();
                } else {
                    hideList();
                }
            }
            
        });
        isSelecting.bind(control.showingProperty());
        label = new Label();
        label.setAlignment(Pos.CENTER);
        label.textProperty().bind(Bindings.createStringBinding(() -> {
            if (control.getValue() == null) {
                return "-";
            }
            return control.getValue().name();
        }, control.valueProperty()));
        label.textFillProperty().bind(Bindings.createObjectBinding(() -> {
            if (control.getValue() == null) {
                return Color.BLACK;
            }
            return control.getValue().getColor();
        }, control.valueProperty()));
        label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        background = new VBox(label);
        background.setAlignment(Pos.CENTER);
        background.prefWidthProperty().bind(Bindings.max(label.widthProperty().add(20), 80));
        background.setBackground(new Background(new BackgroundFill(backgroundColorField, new CornerRadii(12), new Insets(2))));
        background.setBorder(new Border(new BorderStroke(borderColorField, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        background.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                control.show();
            }
            
        });
        this.getChildren().add(background);
        selectTable = new ListView<>(control.getItems());
        control.valueProperty().bind(selectTable.getSelectionModel().selectedItemProperty());
        selectTable.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                control.hide();
            }
            
        });
        selectTable.setCellFactory(x -> {
            ChoiceBoxListCell<Type> choiceBoxListCell = new ChoiceBoxListCell<Type>(new TypeStringConverter(), x.getItems());
            choiceBoxListCell.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
            choiceBoxListCell.setBackground(new Background(new BackgroundFill(backgroundColorField, new CornerRadii(12), new Insets(0))));
            choiceBoxListCell.setBorder(new Border(new BorderStroke(borderColorField, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
            choiceBoxListCell.textFillProperty().bind(Bindings.createObjectBinding(() -> {
                if (choiceBoxListCell.getItem() == null) {
                    return Color.BLACK;
                } else {
                    return choiceBoxListCell.getItem().getColor();
                }
            }, choiceBoxListCell.itemProperty()));
            return choiceBoxListCell;
        });
        selectTable.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, new CornerRadii(12), new Insets(0))));
        selectTable.setPrefWidth(120);
    }
    private void hideList() {
        this.getChildren().remove(selectTable);
        background.setVisible(true);
    }
    private void showList() {
        if (!this.getChildren().contains(selectTable)) {
            this.getChildren().add(selectTable);
            background.setVisible(false);
        }
    }

    public Color getBackgroundColorField() {
        return backgroundColorField;
    }

    public void setBackgroundColorField(Color c) {
        backgroundColorField = c;
    }

    public Color getBorderColorField() {
        return backgroundColorField;
    }

    public void setBorderColorField(Color c) {
        borderColorField = c;
    }
}

class TypeStringConverter extends StringConverter<Type> {

    @Override
    public Type fromString(String arg0) {
        return Type.valueOf(arg0);
    }

    @Override
    public String toString(Type arg0) {
        return arg0.toString();
    }

}
