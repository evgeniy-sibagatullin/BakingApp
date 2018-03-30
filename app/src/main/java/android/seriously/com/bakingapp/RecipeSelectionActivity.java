package android.seriously.com.bakingapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.seriously.com.bakingapp.utils.QueryUtils;
import android.support.v7.app.AppCompatActivity;

public class RecipeSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_selection);
        new TestFetchRecipesData().execute();
    }

    private static class TestFetchRecipesData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            QueryUtils.fetchRecipesData();
            return null;
        }
    }
}
