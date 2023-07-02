package evolution.VisualElements;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private SimpleObjectProperty<Color> backgroundColor;
    private SimpleObjectProperty<Color> barColor;
    public MyStatsBar(double progress){
        this.progress = new SimpleDoubleProperty(progress);
        progressBar = new Rectangle(0, 0, this.getWidth(), this.getHeight());
        progressBar.heightProperty().bind(this.heightProperty());
        progressBar.widthProperty().bind(this.widthProperty().multiply(this.progress));
        this.getChildren().add(progressBar);
        backgroundColor = new SimpleObjectProperty<Color>(Color.WHITE);
        barColor = new SimpleObjectProperty<Color>(Color.BLACK);
        setColorScheme(backgroundColor.get(), barColor.get());
        backgroundColor.addListener(new ChangeListener<Color>() {

            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                setColorScheme(backgroundColor.get(), barColor.get());
            }
            
        });
        barColor.addListener(new ChangeListener<Color>() {

            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                setColorScheme(backgroundColor.get(), barColor.get());
            }
            
        });
    }
    public MyStatsBar(DoubleBinding progress){
        this.progress = new SimpleDoubleProperty();
        this.progress.bind(progress);
        progressBar = new Rectangle(0, 0, this.getWidth(), this.getHeight());
        progressBar.heightProperty().bind(this.heightProperty());
        progressBar.widthProperty().bind(this.widthProperty().multiply(this.progress));
        this.getChildren().add(progressBar);
        backgroundColor = new SimpleObjectProperty<Color>(Color.WHITE);
        barColor = new SimpleObjectProperty<Color>(Color.BLACK);
        setColorScheme(backgroundColor.get(), barColor.get());
        backgroundColor.addListener(new ChangeListener<Color>() {

            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                setColorScheme(backgroundColor.get(), barColor.get());
            }
            
        });
        barColor.addListener(new ChangeListener<Color>() {

            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                setColorScheme(backgroundColor.get(), barColor.get());
            }
            
        });
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

    public Color getBackgroundColor() {
        return backgroundColor.get();
    }

    public Color getBarColor() {
        return barColor.get();
    }

    public void setBackgroundColor(Color c) {
        backgroundColor.set(c);
    }

    public void setBarColor(Color c) {
        barColor.set(c);
    }

    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    public ObjectProperty<Color> barColorProperty() {
        return barColor;
    }
}
