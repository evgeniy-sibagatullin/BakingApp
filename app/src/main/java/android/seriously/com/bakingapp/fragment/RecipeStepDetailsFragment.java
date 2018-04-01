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
import android.widget.ImageView;
import android.widget.TextView;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_BEFORE;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_NEXT;
import static android.view.View.INVISIBLE;

@SuppressWarnings("ConstantConditions")
public class RecipeStepDetailsFragment extends Fragment {

    public static final String TAG = RecipeStepDetailsFragment.class.getSimpleName();

    public interface Listener {
        void onNavigationButtonPressed(int recipeStepToBeOpenedId);

        void onBackToListButtonPressed();
    }

    private RecipeStep recipeStep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecipeStepDetailsFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_step_details_fragment, container, false);

        recipeStep = (RecipeStep) getArguments().get(BUNDLE_KEY_RECIPE_STEP);

        setupInstruction(binding.recipeStepInstruction);
        setupNavigationButtons(binding);

        return binding.getRoot();
    }

    private void setupInstruction(TextView recipeStepInstruction) {
        recipeStepInstruction.setText(recipeStep.getFullDesc());
    }

    private void setupNavigationButtons(RecipeStepDetailsFragmentBinding binding) {
        setupNavigationButton(binding.navigateBefore, BUNDLE_KEY_RECIPE_STEP_ID_BEFORE);
        setupNavigationButton(binding.navigateNext, BUNDLE_KEY_RECIPE_STEP_ID_NEXT);
        setupBackToListButton(binding.backToList);
    }

    private void setupNavigationButton(ImageView navigationButton, final String key) {
        if (getArguments().containsKey(key)) {
            navigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int recipeStepToBeOpenedId = getArguments().getInt(key);
                    ((Listener) getContext()).onNavigationButtonPressed(recipeStepToBeOpenedId);
                }
            });
        } else {
            navigationButton.setVisibility(INVISIBLE);
        }
    }

    private void setupBackToListButton(ImageView expandLessButton) {
        expandLessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Listener) getContext()).onBackToListButtonPressed();
            }
        });
    }
}
