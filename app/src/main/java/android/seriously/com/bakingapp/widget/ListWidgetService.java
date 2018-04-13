package android.seriously.com.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.model.Ingredient;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.seriously.com.bakingapp.utils.Constants.PREFERENCES_KEY_INGREDIENTS;
import static android.seriously.com.bakingapp.utils.Constants.PREFERENCES_NAME;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final String LOG_TAG = ListRemoteViewsFactory.class.getSimpleName();

    private Context context;
    private final List<Ingredient> ingredients = new ArrayList<>();

    ListRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        String json = context.getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE)
                .getString(PREFERENCES_KEY_INGREDIENTS, "");
        fillIngredients(json);
    }

    private void fillIngredients(String json) {
        if (!json.isEmpty()) {
            try {
                ingredients.clear();
                Gson gson = new Gson();
                JSONArray array = new JSONArray(json);

                for (int index = 0; index < array.length(); index++) {
                    Ingredient ingredient = gson.fromJson(array.getString(index), Ingredient.class);
                    ingredients.add(ingredient);
                }
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing ingredients JSON ", e);
            }
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredients.get(position);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_item);
        views.setTextViewText(R.id.ingredient_name, ingredient.getIngredient());
        views.setTextViewText(R.id.ingredient_measure, ingredient.getMeasure());
        views.setTextViewText(R.id.ingredient_quantity, ingredient.getQuantity());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
