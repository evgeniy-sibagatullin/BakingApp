package android.seriously.com.bakingapp.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.databinding.RecipeDetailsFragmentBinding;
import android.seriously.com.bakingapp.model.Recipe;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE;

public class RecipeDetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecipeDetailsFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_details_fragment, container, false);

        Recipe recipe = (Recipe) getArguments().get(BUNDLE_KEY_RECIPE);

        binding.recipeName.setText(recipe.getName());

        return binding.getRoot();
    }
}
