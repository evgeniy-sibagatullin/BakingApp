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
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_BEFORE;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_NEXT;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeStepsAdapter.Listener,
        RecipeDetailsFragment.Listener,
        RecipeStepDetailsFragment.Listener {

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

    @Override
    public void onNavigationButtonPressed(int recipeStepToBeOpenedId) {
        Recipe recipe = (Recipe) getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE);
        List<RecipeStep> recipeSteps = recipe.getRecipeSteps();

        for (RecipeStep recipeStep : recipeSteps) {
            if (recipeStep.getId() == recipeStepToBeOpenedId) {
                openRecipeStepDetailsFragment(recipeStep);
            }
        }
    }

    @Override
    public void onBackToListButtonPressed() {
        //noinspection StatementWithEmptyBody
        while (getSupportFragmentManager().popBackStackImmediate()) ;

        openRecipeDetailsFragment();
    }

    private void openRecipeDetailsFragment() {
        Bundle args = prepareBasicArgs(BUNDLE_KEY_RECIPE, getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE));
        RecipeDetailsFragment fragment = (RecipeDetailsFragment) Fragment.instantiate(this,
                RecipeDetailsFragment.class.getCanonicalName(), args);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    private void openRecipeStepDetailsFragment(RecipeStep recipeStep) {
        Bundle args = prepareBasicArgs(BUNDLE_KEY_RECIPE_STEP, recipeStep);
        addNavigationArgs(args, recipeStep);
        RecipeStepDetailsFragment fragment = (RecipeStepDetailsFragment) Fragment.instantiate(this,
                RecipeStepDetailsFragment.class.getCanonicalName(), args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                .addToBackStack(RecipeStepDetailsFragment.TAG).commit();
    }

    private Bundle prepareBasicArgs(String key, Serializable value) {
        Bundle args = new Bundle();
        args.putSerializable(key, value);
        return args;
    }

    private void addNavigationArgs(Bundle args, RecipeStep currentRecipeStep) {
        Recipe recipe = (Recipe) getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE);
        List<RecipeStep> recipeSteps = recipe.getRecipeSteps();

        int idBefore = 0; //Does not matter, will be redefined before usage anyway
        boolean isFirstStepPassed = false;
        boolean isIdNextToBeDefined = false;

        for (RecipeStep recipeStep : recipeSteps) {
            if (isIdNextToBeDefined) {
                args.putInt(BUNDLE_KEY_RECIPE_STEP_ID_NEXT, recipeStep.getId());
                break;
            }

            if (currentRecipeStep.equals(recipeStep)) {
                if (isFirstStepPassed) {
                    args.putInt(BUNDLE_KEY_RECIPE_STEP_ID_BEFORE, idBefore);
                }

                isIdNextToBeDefined = true;
            } else {
                idBefore = recipeStep.getId();
            }

            isFirstStepPassed = true;
        }
    }
}
