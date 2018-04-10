package android.seriously.com.bakingapp.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {

    private final int id;
    private final String name;
    private final String imageUrl;
    private final List<Ingredient> ingredients;
    private final List<RecipeStep> recipeSteps;

    public Recipe(int id, @NonNull String name, @NonNull String imageUrl,
                  @NonNull List<Ingredient> ingredients, @NonNull List<RecipeStep> recipeSteps) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.recipeSteps = recipeSteps;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<RecipeStep> getRecipeSteps() {
        return recipeSteps;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;

        if (id != recipe.id) return false;
        if (!name.equals(recipe.name)) return false;
        if (!imageUrl.equals(recipe.imageUrl)) return false;
        if (!ingredients.equals(recipe.ingredients)) return false;
        return recipeSteps.equals(recipe.recipeSteps);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + imageUrl.hashCode();
        result = 31 * result + ingredients.hashCode();
        result = 31 * result + recipeSteps.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ingredients=" + ingredients +
                ", recipeSteps=" + recipeSteps +
                '}';
    }
}
