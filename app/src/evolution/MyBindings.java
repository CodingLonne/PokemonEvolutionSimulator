package evolution;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableDoubleValue;

public class MyBindings {
    public static DoubleBinding roundOnDecimals(ObservableDoubleValue p, int decimals) {
        return Bindings.createDoubleBinding(() -> ((double) Math.round(p.get()*Math.pow(10, decimals)))/Math.pow(10, decimals), p);
    }
}
