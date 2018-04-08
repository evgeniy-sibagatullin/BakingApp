package android.seriously.com.bakingapp.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.adapter.RecipeCardsAdapter;
import android.seriously.com.bakingapp.fragment.RecipeSelectionFragment;
import android.seriously.com.bakingapp.model.Ingredient;
import android.seriously.com.bakingapp.model.Recipe;
import android.seriously.com.bakingapp.widget.BakingAppWidgetProvider;

import com.google.gson.Gson;

import java.util.List;

import static android.seriously.com.bakingapp.utils.Constants.PREFERENCES_KEY_INGREDIENTS;
import static android.seriously.com.bakingapp.utils.Constants.PREFERENCES_NAME;

public class RecipeSelectionActivity extends BasicActivity implements RecipeCardsAdapter.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_selection_activity);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                new RecipeSelectionFragment()).commit();
    }

    @Override
    public void onRecipeCardSelected(Recipe recipe) {
        setupWidgets(recipe.getIngredients());
        startActivity(RecipeDetailsActivity.getStartingIntent(this, recipe));
    }

    private void setupWidgets(List<Ingredient> ingredients) {
        setupIngredientsPreferences(ingredients);
        notifyWidgets();
    }

    private void setupIngredientsPreferences(List<Ingredient> ingredients) {
        String json = new Gson().toJson(ingredients);
        getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE).edit()
                .putString(PREFERENCES_KEY_INGREDIENTS, json).apply();
    }

    private void notifyWidgets() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(this,
                BakingAppWidgetProvider.class));
        widgetManager.notifyAppWidgetViewDataChanged(widgetIds, R.id.widget_list_view);
    }
}
