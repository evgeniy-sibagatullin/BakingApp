package android.seriously.com.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.adapter.RecipeStepsAdapter;
import android.seriously.com.bakingapp.fragment.RecipeDetailsFragment;
import android.seriously.com.bakingapp.fragment.RecipeStepDetailsFragment;
import android.seriously.com.bakingapp.model.Ingredient;
import android.seriously.com.bakingapp.model.Recipe;
import android.seriously.com.bakingapp.model.RecipeStep;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsFragment.Listener, RecipeStepsAdapter.Listener {

    public static Intent getStartingIntent(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeDetailsActivity.class);
        intent.putExtra(BUNDLE_KEY_RECIPE, recipe);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recipe_details_activity);

        if (savedInstanceState == null) {
            openRecipeDetailsFragment();
        }
    }

    @Override
    public void onIngredientsSelected(List<Ingredient> ingredients) {
        Toast.makeText(this, ingredients.get(0).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRecipeStepSelected(RecipeStep recipeStep) {
        openRecipeStepDetailsFragment(recipeStep);
    }

    private void openRecipeDetailsFragment() {
        Bundle args = prepareArgs(BUNDLE_KEY_RECIPE, getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE));
        RecipeDetailsFragment fragment = (RecipeDetailsFragment) Fragment.instantiate(this,
                RecipeDetailsFragment.class.getCanonicalName(), args);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    private void openRecipeStepDetailsFragment(RecipeStep recipeStep) {
        Bundle args = prepareArgs(BUNDLE_KEY_RECIPE_STEP, recipeStep);
        RecipeStepDetailsFragment fragment = (RecipeStepDetailsFragment) Fragment.instantiate(this,
                RecipeStepDetailsFragment.class.getCanonicalName(), args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(RecipeStepDetailsFragment.TAG).commit();
    }

    private Bundle prepareArgs(String key, Serializable value) {
        Bundle args = new Bundle();
        args.putSerializable(key, value);
        return args;
    }
}
