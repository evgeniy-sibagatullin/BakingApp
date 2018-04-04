package android.seriously.com.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.seriously.com.bakingapp.model.Ingredient;
import android.seriously.com.bakingapp.model.Recipe;
import android.seriously.com.bakingapp.model.RecipeStep;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private static final String RECIPE_LISTING_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    /* Request parameters */
    private static final int READ_TIMEOUT_MILLIS = 10000;
    private static final int CONNECTION_TIMEOUT_MILLIS = 15000;
    private static final String CONNECTION_METHOD = "GET";

    /* Json keys*/
    private static final String JSON_RECIPE_KEY_ID = "id";
    private static final String JSON_RECIPE_KEY_NAME = "name";
    private static final String JSON_RECIPE_KEY_INGREDIENTS = "ingredients";
    private static final String JSON_RECIPE_KEY_STEPS = "steps";

    private static final String JSON_INGREDIENT_KEY_QUANTITY = "quantity";
    private static final String JSON_INGREDIENT_KEY_MEASUERE = "measure";
    private static final String JSON_INGREDIENT_KEY_INGREDIENT = "ingredient";

    private static final String JSON_RECIPE_STEP_KEY_ID = "id";
    private static final String JSON_RECIPE_STEP_KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_RECIPE_STEP_KEY_DESCRIPTION = "description";
    private static final String JSON_RECIPE_STEP_KEY_VIDEO_URL = "videoURL";
    private static final String JSON_RECIPE_STEP_KEY_THUMBNAIL_URL = "thumbnailURL";

    private NetworkUtils() {
    }

    public static boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Nullable
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
    }

    public static List<Recipe> fetchRecipesData() {
        URL url = createUrl(RECIPE_LISTING_URL);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractRecipes(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT_MILLIS);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT_MILLIS);
            urlConnection.setRequestMethod(CONNECTION_METHOD);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Recipe> extractRecipes(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();

        if (jsonResponse == null || jsonResponse.trim().isEmpty()) return recipes;

        try {
            JSONArray recipesJSONarray = new JSONArray(jsonResponse);

            for (int index = 0; index < recipesJSONarray.length(); index++) {
                JSONObject recipeJSON = recipesJSONarray.getJSONObject(index);

                int recipeId = recipeJSON.getInt(JSON_RECIPE_KEY_ID);
                String recipeName = recipeJSON.getString(JSON_RECIPE_KEY_NAME);
                List<Ingredient> recipeIngredients = extractRecipeIngredients(
                        recipeJSON.getJSONArray(JSON_RECIPE_KEY_INGREDIENTS));
                List<RecipeStep> recipeSteps = extractRecipeSteps(
                        recipeJSON.getJSONArray(JSON_RECIPE_KEY_STEPS));

                recipes.add(new Recipe(recipeId, recipeName, recipeIngredients, recipeSteps));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem extracting the recipe JSON data", e);
        }

        return recipes;
    }

    private static List<Ingredient> extractRecipeIngredients(JSONArray ingredientsJSONarray) {
        List<Ingredient> recipeIngredients = new ArrayList<>();

        try {
            for (int index = 0; index < ingredientsJSONarray.length(); index++) {
                JSONObject ingredientJSON = ingredientsJSONarray.getJSONObject(index);

                int quantity = ingredientJSON.getInt(JSON_INGREDIENT_KEY_QUANTITY);
                String measure = ingredientJSON.getString(JSON_INGREDIENT_KEY_MEASUERE);
                String name = ingredientJSON.getString(JSON_INGREDIENT_KEY_INGREDIENT);

                recipeIngredients.add(new Ingredient(quantity, measure, name));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem extracting the recipe ingredients JSON data", e);
        }

        return recipeIngredients;
    }

    private static List<RecipeStep> extractRecipeSteps(JSONArray recipeStepsJSONarray) {
        List<RecipeStep> recipeSteps = new ArrayList<>();

        try {
            for (int index = 0; index < recipeStepsJSONarray.length(); index++) {
                JSONObject recipeStepJSON = recipeStepsJSONarray.getJSONObject(index);

                int id = recipeStepJSON.getInt(JSON_RECIPE_STEP_KEY_ID);
                String shortDesc = recipeStepJSON.getString(JSON_RECIPE_STEP_KEY_SHORT_DESCRIPTION);
                String fullDesc = recipeStepJSON.getString(JSON_RECIPE_STEP_KEY_DESCRIPTION);
                String videoURL = recipeStepJSON.getString(JSON_RECIPE_STEP_KEY_VIDEO_URL);

                if (videoURL == null || videoURL.trim().isEmpty()) {
                    videoURL = recipeStepJSON.getString(JSON_RECIPE_STEP_KEY_THUMBNAIL_URL);
                }

                recipeSteps.add(new RecipeStep(id, shortDesc, fullDesc, videoURL));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem extracting the recipe steps JSON data", e);
        }

        return recipeSteps;
    }
}