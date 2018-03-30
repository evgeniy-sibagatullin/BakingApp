package android.seriously.com.bakingapp.loader;

import android.content.Context;
import android.seriously.com.bakingapp.model.Recipe;
import android.seriously.com.bakingapp.utils.QueryUtils;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private final List<Recipe> cache = new ArrayList<>();

    public RecipeLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (cache.isEmpty()) {
            forceLoad();
        }
    }

    @Override
    public List<Recipe> loadInBackground() {
        if (cache.isEmpty()) {
            cache.addAll(QueryUtils.fetchRecipesData());
        }

        return cache;
    }
}