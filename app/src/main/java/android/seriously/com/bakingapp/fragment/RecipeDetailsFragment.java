package android.seriously.com.bakingapp.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.adapter.RecipeStepsAdapter;
import android.seriously.com.bakingapp.databinding.RecipeDetailsFragmentBinding;
import android.seriously.com.bakingapp.model.Ingredient;
import android.seriously.com.bakingapp.model.Recipe;
import android.seriously.com.bakingapp.model.RecipeStep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE;

@SuppressWarnings("ConstantConditions")
public class RecipeDetailsFragment extends Fragment {

    public interface Listener {
        void onIngredientsSelected(List<Ingredient> ingredients);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecipeDetailsFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_details_fragment, container, false);

        Recipe recipe = (Recipe) getArguments().get(BUNDLE_KEY_RECIPE);

        setTitle(recipe.getName());
        setupIngredients(binding.recipeIngredients, recipe.getIngredients());
        setupRecyclerView(binding.recyclerView, recipe.getSteps());

        return binding.getRoot();
    }

    private void setTitle(String title) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
    }

    private void setupIngredients(View ingredientsView, final List<Ingredient> ingredients) {
        ingredientsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Listener) getContext()).onIngredientsSelected(ingredients);
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<RecipeStep> recipeSteps) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RecipeStepsAdapter(getContext(), recipeSteps));
    }
}
