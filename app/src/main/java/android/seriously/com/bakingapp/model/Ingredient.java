package android.seriously.com.bakingapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Ingredient implements Serializable {

    private final String quantity;
    private final String measure;
    private final String ingredient;

    public Ingredient(String quantity, @NonNull String measure, @NonNull String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingredient that = (Ingredient) o;

        if (!quantity.equals(that.quantity)) return false;
        if (!measure.equals(that.measure)) return false;
        return ingredient.equals(that.ingredient);
    }

    @Override
    public int hashCode() {
        int result = quantity.hashCode();
        result = 31 * result + measure.hashCode();
        result = 31 * result + ingredient.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}
