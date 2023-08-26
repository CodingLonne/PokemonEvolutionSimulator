package evolution;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.value.ObservableDoubleValue;

public class MyBindings {
    public static DoubleBinding roundOnDecimals(ObservableDoubleValue p, int decimals) {
        return Bindings.createDoubleBinding(() -> ((double) Math.round(p.get()*Math.pow(10, decimals)))/Math.pow(10, decimals), p);
    }
    public static IntegerBinding round(ObservableDoubleValue p) {
        return Bindings.createIntegerBinding(() -> Math.round((float) p.get()), p);
    }
}
