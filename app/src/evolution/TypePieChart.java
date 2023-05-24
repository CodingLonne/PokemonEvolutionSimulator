package evolution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;


public class TypePieChart extends Pane{
    HashMap<Type, SimpleIntegerProperty> data;
    LinkedList<Arc> piePieces;
    Label label;
    Circle noDataCicle;
    String noDataErrorMessage;
    Label noDataLabel;
    SimpleStringProperty name;
    SimpleIntegerProperty total;
    private HashMap<Type, SimpleDoubleProperty> fractions;
    TypePieChart(HashMap<Type, SimpleIntegerProperty> data, DoubleBinding width, String name, String noDataErrorMessage) {
        this.prefWidthProperty().bind(width);
        this.minHeightProperty().bind(width.add(30));
        this.data = data;
        this.fractions = new HashMap<Type, SimpleDoubleProperty>();
        this.total = new SimpleIntegerProperty();
        total.bind(addI(Type.allTypes(), data));

        for (Type t : Type.allTypes()) {
            fractions.put(t, new SimpleDoubleProperty());
            fractions.get(t).bind(Bindings.createDoubleBinding(() -> {
                int typeAmount = data.get(t).getValue();
                double totalAmount = total.get();
                return totalAmount==0.0 ? 0.0 : typeAmount/totalAmount;
            }, data.get(t), total));
        }
        this.piePieces = new LinkedList<Arc>();
        for (Type t: Type.allTypes()) {
            piePieces.add(makeArc(t));
        }
        this.getChildren().addAll(piePieces);
        this.noDataCicle = new Circle();
        this.noDataLabel = new Label();
        this.noDataErrorMessage = noDataErrorMessage;
        installNoDataNotif(noDataCicle, noDataLabel);
        this.getChildren().addAll(noDataCicle, noDataLabel);
        this.label = new Label(name);
        styleTitle(label);
        this.getChildren().add(label);
        this.name = new SimpleStringProperty(name);
    }
    void addFraction(HashMap<Type, SimpleDoubleProperty> map, Type t) {
        fractions.put(t, new SimpleDoubleProperty());
        fractions.get(t).bind(Bindings.createDoubleBinding(() -> {
            int typeAmount = data.get(t).getValue();
            double totalAmount = total.get();
            return totalAmount==0.0 ? 0.0 : typeAmount/totalAmount;
        }, data.get(t), addI(Type.allTypes(), data)));
    }
    public Arc makeArc(Type t) {
        Arc newArc = new Arc();
        newArc.centerXProperty().bind(this.widthProperty().divide(2));
        newArc.centerYProperty().bind(this.widthProperty().divide(2));
        newArc.radiusXProperty().bind(this.widthProperty().multiply(0.45));
        newArc.radiusYProperty().bind(this.widthProperty().multiply(0.45));
        newArc.lengthProperty().bind(fractions.get(t).multiply(360));
        newArc.startAngleProperty().bind(addD(Arrays.copyOfRange(Type.allTypes(), 0, Arrays.asList(Type.allTypes()).indexOf(t)), fractions).multiply(360));

        newArc.setFill(t.getColor());
        newArc.setType(ArcType.ROUND);
        return newArc;
    }
    public NumberBinding addI(Type[] Types, HashMap<Type, SimpleIntegerProperty> data) {
        SimpleIntegerProperty zero = new SimpleIntegerProperty(0);
        NumberBinding result = zero.add(0);
        for (Type t: Types) {
            result = result.add(data.get(t));
        }
        return result;
    }

    public NumberBinding addD(Type[] Types, HashMap<Type, SimpleDoubleProperty> data) {
        SimpleIntegerProperty zero = new SimpleIntegerProperty(0);
        NumberBinding result = zero.add(0);
        for (Type t: Types) {
            result = result.add(data.get(t));
        }
        return result;
    }

    public void installNoDataNotif(Circle circle, Label label) {
        circle.centerXProperty().bind(this.widthProperty().divide(2));
        circle.centerYProperty().bind(this.widthProperty().divide(2));
        circle.radiusProperty().bind(this.widthProperty().multiply(0.45));
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.RED);
        circle.setStrokeWidth(10);
        label.translateXProperty().bind(this.widthProperty().divide(2).subtract(label.widthProperty().divide(2)));
        label.translateYProperty().bind(this.widthProperty().divide(2).subtract(label.heightProperty().divide(2)));
        label.setText(noDataErrorMessage);
        label.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 28));
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        //visible
        circle.visibleProperty().bind(Bindings.equal(0, addI(Type.allTypes(), data)));
        label.visibleProperty().bind(Bindings.equal(0, addI(Type.allTypes(), data)));
    }
    public void styleTitle(Label title) {
        title.translateXProperty().bind(this.widthProperty().divide(2).subtract(title.widthProperty().divide(2)));
        title.translateYProperty().bind(this.widthProperty().subtract(title.heightProperty().divide(2)));
        title.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.REGULAR, 20));
    }
    public SimpleDoubleProperty getFraction(Type t) {
        return fractions.get(t);
    }
}
