package android.seriously.com.bakingapp.activity;

import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.adapter.RecipeCardsAdapter;
import android.seriously.com.bakingapp.fragment.RecipeSelectionFragment;
import android.seriously.com.bakingapp.model.Recipe;
import android.support.v7.app.AppCompatActivity;

public class RecipeSelectionActivity extends AppCompatActivity implements RecipeCardsAdapter.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_selection_activity);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
                new RecipeSelectionFragment()).commit();
    }

    @Override
    public void onRecipeCardSelected(Recipe recipe) {
        startActivity(RecipeDetailsActivity.getStartingIntent(this, recipe));
    }
}
