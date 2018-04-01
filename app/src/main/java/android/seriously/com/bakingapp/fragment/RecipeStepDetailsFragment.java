package android.seriously.com.bakingapp.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.databinding.RecipeStepDetailsFragmentBinding;
import android.seriously.com.bakingapp.model.RecipeStep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP;

@SuppressWarnings("ConstantConditions")
public class RecipeStepDetailsFragment extends Fragment {

    public static final String TAG = RecipeStepDetailsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecipeStepDetailsFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_step_details_fragment, container, false);

        RecipeStep recipeStep = (RecipeStep) getArguments().get(BUNDLE_KEY_RECIPE_STEP);
        setupInstruction(binding.recipeStepInstruction, recipeStep.getFullDesc());

        return binding.getRoot();
    }

    private void setupInstruction(TextView recipeStepInstruction, String fullDesc) {
        recipeStepInstruction.setText(fullDesc);
    }
}
