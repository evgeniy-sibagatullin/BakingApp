package android.seriously.com.bakingapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    private final int id;
    private final String name;
    private final String image;
    private final List<Ingredient> ingredients;
    private final List<RecipeStep> steps;

    public Recipe(int id, @NonNull String name, @NonNull String image,
                  @NonNull List<Ingredient> ingredients, @NonNull List<RecipeStep> steps) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (id != recipe.id) return false;
        if (!name.equals(recipe.name)) return false;
        if (!image.equals(recipe.image)) return false;
        if (!ingredients.equals(recipe.ingredients)) return false;
        return steps.equals(recipe.steps);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + image.hashCode();
        result = 31 * result + ingredients.hashCode();
        result = 31 * result + steps.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", ingredients=" + ingredients +
                ", steps=" + steps +
                '}';
    }
}
