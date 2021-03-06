package android.seriously.com.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.adapter.RecipeStepsAdapter;
import android.seriously.com.bakingapp.fragment.RecipeDetailsFragment;
import android.seriously.com.bakingapp.fragment.RecipeStepDetailsFragment;
import android.seriously.com.bakingapp.fragment.dialog.IngredientsDialog;
import android.seriously.com.bakingapp.model.Ingredient;
import android.seriously.com.bakingapp.model.Recipe;
import android.seriously.com.bakingapp.model.RecipeStep;
import android.seriously.com.bakingapp.utils.ViewUtils;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_INGREDIENTS;
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

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean isTablet = ViewUtils.isTablet(this);
        int contentViewId = isTablet ? R.layout.recipe_details_tablet_activity :
                R.layout.recipe_details_mobile_activity;

        setContentView(contentViewId);

        if (savedInstanceState == null) {
            Recipe recipe = (Recipe) getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE);
            openRecipeDetailsFragment(recipe);

            if (isTablet) {
                openRecipeStepDetailsFragment(recipe.getSteps().get(0));
            }
        }
    }

    @Override
    public void onIngredientsSelected(List<Ingredient> ingredients) {
        openIngredientsDialog(ingredients);
    }

    @Override
    public void onRecipeStepSelected(RecipeStep recipeStep) {
        openRecipeStepDetailsFragment(recipeStep);
    }

    @Override
    public void onNavigationButtonPressed(int recipeStepToBeOpenedId) {
        Recipe recipe = (Recipe) getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE);
        List<RecipeStep> recipeSteps = recipe.getSteps();

        for (RecipeStep recipeStep : recipeSteps) {
            if (recipeStep.getId() == recipeStepToBeOpenedId) {
                openRecipeStepDetailsFragment(recipeStep);
            }
        }
    }

    @Override
    public void onBackToListButtonPressed() {
        if (ViewUtils.isTablet(this)) {
            finish();
        } else {
            //noinspection StatementWithEmptyBody
            while (getSupportFragmentManager().popBackStackImmediate()) ;
            openRecipeDetailsFragment((Recipe) getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    private void openIngredientsDialog(List<Ingredient> ingredients) {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY_INGREDIENTS, new ArrayList<>(ingredients));
        IngredientsDialog dialog = (IngredientsDialog) Fragment.instantiate(this,
                IngredientsDialog.class.getCanonicalName(), args);
        dialog.show(getSupportFragmentManager(), IngredientsDialog.TAG);
    }

    private void openRecipeDetailsFragment(Recipe recipe) {
        Bundle args = prepareBasicArgs(BUNDLE_KEY_RECIPE, recipe);
        RecipeDetailsFragment fragment = (RecipeDetailsFragment) Fragment.instantiate(this,
                RecipeDetailsFragment.class.getCanonicalName(), args);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    private void openRecipeStepDetailsFragment(RecipeStep recipeStep) {
        Bundle args = prepareBasicArgs(BUNDLE_KEY_RECIPE_STEP, recipeStep);
        addNavigationArgs(args, recipeStep);
        RecipeStepDetailsFragment fragment = (RecipeStepDetailsFragment) Fragment.instantiate(this,
                RecipeStepDetailsFragment.class.getCanonicalName(), args);

        boolean isTablet = ViewUtils.isTablet(this);
        int fragmentConteinerId = isTablet ? R.id.fragment_details_container :
                R.id.fragment_container;

        getSupportFragmentManager().beginTransaction().replace(fragmentConteinerId, fragment)
                .addToBackStack(RecipeStepDetailsFragment.TAG).commit();
    }

    private Bundle prepareBasicArgs(String key, Serializable value) {
        Bundle args = new Bundle();
        args.putSerializable(key, value);
        return args;
    }

    private void addNavigationArgs(Bundle args, RecipeStep currentRecipeStep) {
        Recipe recipe = (Recipe) getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE);
        List<RecipeStep> recipeSteps = recipe.getSteps();

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
