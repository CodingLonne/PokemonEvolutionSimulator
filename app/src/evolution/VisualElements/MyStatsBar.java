package evolution.VisualElements;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MyStatsBar extends Pane {
    private SimpleDoubleProperty progress;
    private Rectangle progressBar;
    public MyStatsBar(double progress){
        this.progress = new SimpleDoubleProperty(progress);
        progressBar = new Rectangle(0, 0, this.getWidth(), this.getHeight());
        progressBar.heightProperty().bind(this.heightProperty());
        progressBar.widthProperty().bind(this.widthProperty().multiply(this.progress));
        this.getChildren().add(progressBar);
    }
    public MyStatsBar(DoubleBinding progress){
        this.progress = new SimpleDoubleProperty();
        this.progress.bind(progress);
        progressBar = new Rectangle(0, 0, this.getWidth(), this.getHeight());
        progressBar.heightProperty().bind(this.heightProperty());
        progressBar.widthProperty().bind(this.widthProperty().multiply(this.progress));
        this.getChildren().add(progressBar);
    }
    public Rectangle getProgressBar() {
        return progressBar;
    }
    public double getProgress() {
        return progress.get();
    }
    public void setProgress(double p) {
        progress.set(p);
    }
    public SimpleDoubleProperty progressProperty() {
        return progress;
    }
    public void setColorScheme(Color background, Color bar) {
        this.setBackground(new Background(new BackgroundFill(background, new CornerRadii(10), getInsets())));
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(1))));
        progressBar.setFill(bar);
        progressBar.setArcWidth(10);
        progressBar.setArcHeight(10);
        progressBar.setStroke(Color.BLACK);
    }
}
