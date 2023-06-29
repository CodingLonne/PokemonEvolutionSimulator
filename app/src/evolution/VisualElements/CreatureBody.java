package evolution.VisualElements;

import evolution.Creature;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class CreatureBody extends Circle {
    private final Creature host;
    public CreatureBody(Creature host) {
        super();
        this.host = host;
    }
    public CreatureBody(Creature host, double radius) {
        super(radius);
        this.host = host;
    }
    public CreatureBody(Creature host, double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        this.host = host;
    }
    public CreatureBody(Creature host, double centerX, double centerY, double radius, Paint fill) {
        super(centerX, centerY, radius, fill);
        this.host = host;
    }
    public CreatureBody(Creature host, double radius, Paint fill) {
        super(radius, fill);
        this.host = host;
    }
    public Creature getHost() {
        return host;
    }
}
