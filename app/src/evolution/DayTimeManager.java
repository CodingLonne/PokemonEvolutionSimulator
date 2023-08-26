package evolution;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DayTimeManager {
    private static final int defaultDayLenght = 5000;
    private static final int defaultSunrise = 0;
    private static final int defaultSunSet = 4000;

    private IntegerProperty daylenght;
    private IntegerProperty sunrise;
    private IntegerProperty sunset;
    private IntegerProperty currentTime;
    private BooleanProperty sunShining;
    private BooleanProperty sunIsRising;
    private BooleanProperty sunIsSetting;

    public DayTimeManager(int daylenght, int sunrise, int sunset) {
        this.daylenght = new SimpleIntegerProperty(daylenght);
        this.sunrise = new SimpleIntegerProperty(sunrise);
        this.sunset = new SimpleIntegerProperty(sunset);
        this.currentTime = new SimpleIntegerProperty(0);
        this.sunShining = new SimpleBooleanProperty(0<sunrise || sunset>0); //sunset hasn't happened yet or sunrise has happened
        this.sunIsRising = new SimpleBooleanProperty(sunrise == 0);
        this.sunIsSetting = new SimpleBooleanProperty(sunset == 0);
    } 

    public DayTimeManager() {
        this(defaultDayLenght, defaultSunrise, defaultSunSet);
    }

    public void step() {
        currentTime.set((currentTime.get()+1)%daylenght.get());
        if (currentTime.get() == sunrise.get()) {
            sunShining.set(true);
            sunIsRising.set(true);
        } else sunIsRising.set(false);
        if (currentTime.get() == sunset.get()) {
            sunShining.set(false);
            sunIsSetting.set(true);
        } else sunIsSetting.set(false);
    }

    //getters
    public int getDayLenght() {
        return daylenght.get();
    }

    public int getSunrise() {
        return sunrise.get();
    }

    public int getSunset() {
        return sunset.get();
    }

    public int getCurrentTime() {
        return currentTime.get();
    }

    public boolean isSunShining() {
        return sunShining.get();
    }

    public boolean isSunRising() {
        return sunIsRising.get();
    }

    public boolean isSunSetting() {
        return sunIsSetting.get();
    }

    //setters
    public void setDayLength(int day) {
        daylenght.set(day);
    }

    public void setSunrise(int day) {
        sunrise.set(day);
    }

    public void setSunset(int day) {
        sunset.set(day);
    }

    public void setCurrentTime(int day) {
        currentTime.set(day);
    }

    //properties
    public ReadOnlyIntegerProperty daylenghtProperty() {
        return daylenght;
    }

    public ReadOnlyIntegerProperty sunriseProperty() {
        return sunrise;
    }

    public ReadOnlyIntegerProperty sunsetProperty() {
        return sunset;
    }

    public ReadOnlyIntegerProperty currentTimeProperty() {
        return currentTime;
    }

    public ReadOnlyBooleanProperty sunShiningProperty() {
        return sunShining;
    }

    public ReadOnlyBooleanProperty sunRisingProperty() {
        return sunIsRising;
    }

    public ReadOnlyBooleanProperty sunSettingProperty() {
        return sunIsSetting;
    }
}
