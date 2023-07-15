package evolution.Visual.VisualElements;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class SwordShape extends Pane{
    private final static Color heftColor = Color.rgb(129, 50, 14);
    private final static Color heftColorBorder = Color.rgb(129, 50, 14);
    private final static Color ironColor = Color.rgb(150, 150, 156);
    private final static Color ironColorBorder = Color.rgb(119, 119, 126);

    private Rectangle leftHeft;
    private Rectangle rightHeft;
    private Rectangle leftHandle;
    private Rectangle rightHandle;
    private Polygon leftBlade;
    private Polygon rightBlade;
    private Line leftBladeAccent;
    private Line rightBladeAccent;
    public SwordShape(double size) {
        super();
        this.setPrefSize(size, size);
        this.setMinSize(size, size);
        this.setMaxSize(size, size);
        double scale = size/11;
        double diagonalScale = scale*Math.sqrt(2);
        //left heft
        leftHeft = new Rectangle(0*scale, 10*scale, 2.5*diagonalScale, 1*diagonalScale);
        leftHeft.setFill(heftColor);
        leftHeft.setStroke(heftColorBorder);
        leftHeft.getTransforms().add(new Rotate(-45, 0*scale, 10*scale));
        leftHeft.setArcHeight(diagonalScale/10);
        leftHeft.setArcWidth(diagonalScale/10);
        //right heft
        rightHeft = new Rectangle(10*scale, 11*scale, 2.5*diagonalScale, 1*diagonalScale);
        rightHeft.setFill(heftColor);
        rightHeft.setStroke(heftColorBorder);
        rightHeft.getTransforms().add(new Rotate(225, 10*scale, 11*scale));
        rightHeft.setArcHeight(diagonalScale/10);
        rightHeft.setArcWidth(diagonalScale/10);
        //left handle
        leftHandle = new Rectangle(2*scale, 6*scale, 3*diagonalScale, 1*diagonalScale);
        leftHandle.setFill(heftColor);
        leftHandle.setStroke(heftColorBorder);
        leftHandle.getTransforms().add(new Rotate(45, 2*scale, 6*scale));
        leftHandle.setArcHeight(diagonalScale/10);
        leftHandle.setArcWidth(diagonalScale/10);
        //right handle
        rightHandle = new Rectangle(9*scale, 6*scale, 1*diagonalScale, 3*diagonalScale);
        rightHandle.setFill(heftColor);
        rightHandle.setStroke(heftColorBorder);
        rightHandle.getTransforms().add(new Rotate(45, 9*scale, 6*scale));
        rightHandle.setArcHeight(diagonalScale/10);
        rightHandle.setArcWidth(diagonalScale/10);
        //left blade
        leftBlade = new Polygon(4*scale, 8.5*scale, 
                                9.5*scale, 3*scale, 
                                10.5*scale, 0.5*scale,
                                8*scale, 1.5*scale, 
                                2.5*scale, 7*scale);
        leftBlade.setFill(ironColor);
        leftBlade.setStroke(ironColorBorder);
        //right blade
        rightBlade = new Polygon(7*scale, 8*scale, 
                                1.5*scale, 3*scale, 
                                0.5*scale, 0.5*scale,
                                3*scale, 1.5*scale, 
                                8.5*scale, 7*scale);
        rightBlade.setFill(ironColor);
        rightBlade.setStroke(ironColorBorder);
        //left sword middle line
        leftBladeAccent = new Line(4*scale, 7*scale, 8.5*scale, 1.5*diagonalScale);
        leftBladeAccent.setStroke(ironColorBorder);
        //right sword middle line
        rightBladeAccent = new Line(7*scale, 7*scale, 2.5*scale, 1.5*diagonalScale);
        rightBladeAccent.setStroke(ironColorBorder);
        //add as children
        this.getChildren().addAll(rightHeft, rightBlade, rightHandle, rightBladeAccent, leftHeft, leftBlade, leftHandle, leftBladeAccent);
    }
}
