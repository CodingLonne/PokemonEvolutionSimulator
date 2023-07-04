package evolution.Visual.VisualElements;

import evolution.Visual.TypeStatsDisplay;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TypeCodeDisplay extends HBox{
    public class BinarySquare extends Rectangle{
        SimpleBooleanProperty myBit;
        SimpleBooleanProperty valid;
        BinarySquare(SimpleBooleanProperty bit, SimpleBooleanProperty isValidGene, double squareSize) {
            super(squareSize, squareSize);
            myBit = new SimpleBooleanProperty();
            myBit.bindBidirectional(bit);
            valid = new SimpleBooleanProperty();
            valid.bind(isValidGene);
            this.fillProperty().bind(Bindings.when(valid).
                                    then(Bindings.when(myBit).then(Color.WHITE).otherwise(Color.BLACK)).
                                    otherwise(Bindings.when(myBit).
                                        then(Color.rgb(255, 220, 220)).
                                        otherwise(Color.rgb(128, 0, 32))));
        }
        public SimpleBooleanProperty getBit() {
            return myBit;
        }
        public void switchBit() {
            if (myBit.getValue()) {
                myBit.set(false);
            } else {
                myBit.set(true);
            }
        }
    }
    BinarySquare[] bitCode;
    SimpleBooleanProperty valid;
    public TypeCodeDisplay(SimpleBooleanProperty[] codes, SimpleBooleanProperty isValidGene, double squareSize, TypeStatsDisplay listener) {
        super();
        this.setPadding(new Insets(4, 5, 4, 5));
        //D7A85B=dirty yellow, 26CCCF=light blue, E75A0D=orange
        this.setStyle("-fx-background-color: #D7A85B;");
        valid = new SimpleBooleanProperty();
        valid.bind(isValidGene);
        bitCode = new BinarySquare[codes.length];
        for (int i=0; i<codes.length; i++) {
            bitCode[i] = new BinarySquare(codes[i], valid, squareSize);
            bitCode[i].setOnMouseClicked(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    ((BinarySquare) event.getTarget()).switchBit();
                    listener.adjustGeneShortcutText();
                }
            });
            this.getChildren().add(bitCode[i]);
        }
    }
}
