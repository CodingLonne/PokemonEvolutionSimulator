package evolution.VisualElements;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class MyChoiceBoxSkin<T> extends SkinBase<ChoiceBox<T>>{
    private Label label;
    private VBox background;
    private ListView<T> selectTable;
    private SimpleBooleanProperty isSelecting;

    private Color borderColorField = Color.BLACK;
    private Color backgroundColorField = Color.WHITE;
    private SimpleDoubleProperty cellSize;
    private StringConverter<T> stringConverter;
    private ObjectProperty<Font> font;
    public MyChoiceBoxSkin(ChoiceBox<T> control, StringConverter<T> converter) {
        super(control);
        stringConverter = converter;
        font = new SimpleObjectProperty<Font>(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 15));
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
            return stringConverter.toString(control.getValue());
        }, control.valueProperty()));
        label.setTextFill(Color.BLACK);
        label.fontProperty().bind(font);
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        background = new VBox(label);
        background.setAlignment(Pos.CENTER);
        background.prefWidthProperty().bind(Bindings.max(label.widthProperty().add(10), 80));
        colorBackground();
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
        cellSize = new SimpleDoubleProperty(35);
        selectTable.setCellFactory(x -> {
            ChoiceBoxListCell<T> choiceBoxListCell = new ChoiceBoxListCell<T>(converter, x.getItems());
            choiceBoxListCell.fontProperty().bind(font);
            choiceBoxListCell.setBackground(new Background(new BackgroundFill(backgroundColorField, new CornerRadii(12), new Insets(0))));
            choiceBoxListCell.setBorder(new Border(new BorderStroke(borderColorField, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
            choiceBoxListCell.setTextFill(Color.BLACK);
            choiceBoxListCell.prefHeightProperty().bind(cellSize);
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

    private void colorBackground() {
        background.setBackground(new Background(new BackgroundFill(backgroundColorField, new CornerRadii(12), new Insets(2))));
        background.setBorder(new Border(new BorderStroke(borderColorField, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(3))));
        
    }
    //getters
    public Color getBackgroundColorField() {
        return backgroundColorField;
    }

    public Color getBorderColorField() {
        return borderColorField;
    }

    public double getCellSize() {
        return cellSize.get();
    }

    public Font getFont() {
        return font.get();
    }

    //setters
    public void setBackgroundColorField(Color c) {
        backgroundColorField = c;
        colorBackground();
    }

    public void setBorderColorField(Color c) {
        borderColorField = c;
        colorBackground();
    }

    public void setCellSize(double s) {
        cellSize.set(s);
    }
    
    public void setFont(Font f) {
        font.set(f);
    }

    //properties
    public DoubleProperty cellSizeProperty() {
        return cellSize;
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }
    
}


