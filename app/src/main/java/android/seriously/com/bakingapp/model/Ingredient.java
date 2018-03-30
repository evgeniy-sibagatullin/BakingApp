package android.seriously.com.bakingapp.model;

import android.support.annotation.NonNull;

public class Ingredient {

    private final int quantity;
    private final String measure;
    private final String name;

    public Ingredient(int quantity, @NonNull String measure, @NonNull String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        if (quantity != that.quantity) return false;
        if (!measure.equals(that.measure)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = quantity;
        result = 31 * result + measure.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
