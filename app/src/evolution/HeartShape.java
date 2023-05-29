package evolution;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public class HeartShape {
    private Shape heart;
    private double width;
    private double distancePoints;
    private double middleX;
    private double middleY;
    private double leftStepX;
    private double leftStepY;
    private double rightStepX;
    private double rightStepY;
    private double upStepX;
    private double upStepY;
    private double downStepX;
    private double downStepY;

    HeartShape(double leftX, double leftY, double rightX, double rightY, Paint fill) {
        distancePoints = Math.sqrt(Math.pow(leftX-rightX, 2)+Math.pow(leftY-rightY, 2));
        width = distancePoints*(1+Math.sqrt(2));
        middleX = (leftX+rightX)/2;
        middleY = (leftY+rightY)/2;
        leftStepX = leftX-middleX;
        leftStepY = leftY-middleY;
        rightStepX = rightX-middleX;
        rightStepY = rightY-middleY;
        upStepX = rightStepY;
        upStepY = -rightStepX;
        downStepX = leftStepY;
        downStepY = -leftStepX;
        Circle leftCircle = new Circle(leftX, leftY, (distancePoints/2)*Math.sqrt(2), fill);
        Circle rightCircle = new Circle(rightX, rightY, (distancePoints/2)*Math.sqrt(2), fill);
        Polygon point = new Polygon(middleX+upStepX, middleY+upStepY, 
                                    middleX+downStepX+2*rightStepX, middleY+downStepY+2*rightStepY,
                                    middleX+3*downStepX, middleY+3*downStepY,
                                    middleX+downStepX+2*leftStepX, middleY+downStepY+2*leftStepY);
        heart = Shape.union(Shape.union(leftCircle, rightCircle), point);
        heart.setFill(fill);
    }

    HeartShape(double middleX, double middleY, double width, Paint fill) {
        this(middleX-(width/(1+Math.sqrt(2)))/2, middleY, middleX+(width/(1+Math.sqrt(2)))/2, middleY, fill);
    }

    HeartShape(double width, Paint fill) {
        this(-(width/(1+Math.sqrt(2)))/2, 0, (width/(1+Math.sqrt(2)))/2, 0, fill);
    }

    HeartShape(double width) {
        this(-(width/(1+Math.sqrt(2)))/2, 0, (width/(1+Math.sqrt(2)))/2, 0, null);
    }

    public Shape getShapeToShow() {
        return heart;
    }

    public double getWidth() {
        return width;
    }

}
