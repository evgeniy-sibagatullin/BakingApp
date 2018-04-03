package android.seriously.com.bakingapp.fragment;

import android.databinding.DataBindingUtil;
import android.net.Uri;
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

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_BEFORE;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_NEXT;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@SuppressWarnings("ConstantConditions")
public class RecipeStepDetailsFragment extends Fragment {

    public static final String TAG = RecipeStepDetailsFragment.class.getSimpleName();

    private ExoPlayer exoPlayer;
    private MediaSource mediaSource;
    private RecipeStepDetailsFragmentBinding binding;

    public interface Listener {
        void onNavigationButtonPressed(int recipeStepToBeOpenedId);

        void onBackToListButtonPressed();
    }

    private RecipeStep recipeStep;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.recipe_step_details_fragment,
                container, false);
        recipeStep = (RecipeStep) getArguments().get(BUNDLE_KEY_RECIPE_STEP);

        setupInstruction();
        setupNavigationButtons();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSource != null) {
            mediaSource = null;
        }
    }

    private void initializePlayer() {
        String videoUrl = recipeStep.getVideoUrl();

        if (videoUrl == null || videoUrl.isEmpty()) return;

        binding.videoPlayer.setVisibility(VISIBLE);

        if (exoPlayer == null) {
            DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)));
            mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(videoUrl));

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);

            binding.videoPlayer.requestFocus();
            binding.videoPlayer.setPlayer(exoPlayer);
        }
    }

    private void setupInstruction() {
        binding.recipeStepInstruction.setText(recipeStep.getFullDesc());
    }

    private void setupNavigationButtons() {
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
