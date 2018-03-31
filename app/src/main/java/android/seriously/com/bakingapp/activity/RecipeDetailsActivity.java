package android.seriously.com.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.fragment.RecipeDetailsFragment;
import android.seriously.com.bakingapp.model.Recipe;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE;

public class RecipeDetailsActivity extends AppCompatActivity {

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
            openFragment();
        }
    }

    private void openFragment() {
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_KEY_RECIPE, getIntent().getSerializableExtra(BUNDLE_KEY_RECIPE));
        RecipeDetailsFragment fragment = (RecipeDetailsFragment) Fragment.instantiate(this,
                RecipeDetailsFragment.class.getCanonicalName(), args);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }
}
