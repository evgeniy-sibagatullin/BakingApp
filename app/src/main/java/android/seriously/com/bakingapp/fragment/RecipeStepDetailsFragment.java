package android.seriously.com.bakingapp.fragment;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.databinding.RecipeStepDetailsFragmentBinding;
import android.seriously.com.bakingapp.model.RecipeStep;
import android.seriously.com.bakingapp.utils.ViewUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_BEFORE;
import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_RECIPE_STEP_ID_NEXT;
import static android.seriously.com.bakingapp.utils.Constants.PLAYER_KEY_RESUME_POSITION;
import static android.seriously.com.bakingapp.utils.Constants.PLAYER_KEY_RESUME_WINDOW;
import static android.seriously.com.bakingapp.utils.Constants.PLAYER_KEY_SHOULD_AUTO_PLAY;
import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

@SuppressWarnings("ConstantConditions")
public class RecipeStepDetailsFragment extends Fragment {

    public static final String TAG = RecipeStepDetailsFragment.class.getSimpleName();
    private static final int ANIMATION_DURATION = 1024;

    private Dialog fullScreenDialog;
    private ExoPlayer exoPlayer;
    private MediaSource mediaSource;
    private RecipeStepDetailsFragmentBinding binding;

    private long resumePosition;
    private int resumeWindow;
    private boolean shouldAutoPlay;

    public interface Listener {
        void onNavigationButtonPressed(int recipeStepToBeOpenedId);

        void onBackToListButtonPressed();
    }

    private RecipeStep recipeStep;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            resumePosition = C.TIME_UNSET;
            resumeWindow = C.INDEX_UNSET;
            shouldAutoPlay = true;
        } else {
            resumePosition = savedInstanceState.getLong(PLAYER_KEY_RESUME_POSITION);
            resumeWindow = savedInstanceState.getInt(PLAYER_KEY_RESUME_WINDOW);
            shouldAutoPlay = savedInstanceState.getBoolean(PLAYER_KEY_SHOULD_AUTO_PLAY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.recipe_step_details_fragment,
                container, false);
        recipeStep = (RecipeStep) getArguments().get(BUNDLE_KEY_RECIPE_STEP);

        setupThumbnail();
        setupInstruction();
        setupNavigationButtons();

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong(PLAYER_KEY_RESUME_POSITION, resumePosition);
        outState.putInt(PLAYER_KEY_RESUME_WINDOW, resumeWindow);
        outState.putBoolean(PLAYER_KEY_SHOULD_AUTO_PLAY, shouldAutoPlay);
    }

    private void releasePlayer() {
        if (fullScreenDialog != null) {
            fullScreenDialog.dismiss();
            fullScreenDialog = null;
        }

        if (exoPlayer != null) {
            resumePosition = Math.max(0, exoPlayer.getContentPosition());
            resumeWindow = exoPlayer.getCurrentWindowIndex();
            shouldAutoPlay = exoPlayer.getPlayWhenReady();

            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

        if (mediaSource != null) {
            mediaSource = null;
        }
    }

    private void initializePlayer() {
        String videoUrl = recipeStep.getVideoURL();

        if (TextUtils.isEmpty(videoUrl)) return;

        if (exoPlayer == null) {
            setupPlayer(videoUrl);

            if (ViewUtils.isOrientationLandscape(getResources())
                    && !ViewUtils.isTablet(getContext())) {
                setupFullScreenDialog();
            }
        }
    }

    private void setupPlayer(String videoUrl) {
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), getString(R.string.app_name)));
        mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(videoUrl));

        exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());
        boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            exoPlayer.seekTo(resumeWindow, resumePosition);
        }

        exoPlayer.prepare(mediaSource, !haveResumePosition, false);
        exoPlayer.setPlayWhenReady(shouldAutoPlay);
        exoPlayer.addListener(getPlayerEventListener());

        binding.videoPlayer.requestFocus();
        binding.videoPlayer.setPlayer(exoPlayer);

        binding.videoLoadingProgress.startAnimation(prepareAnimationForProgressImage());
        binding.videoLoadingProgress.setVisibility(VISIBLE);
    }

    private void setupFullScreenDialog() {
        fullScreenDialog = new Dialog(getContext(),
                android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            @Override
            public void onBackPressed() {
                this.dismiss();
                super.onBackPressed();
            }
        };

        binding.videoLoadingProgress.setVisibility(GONE);
        ((ViewGroup) binding.videoPlayer.getParent()).removeView(binding.videoPlayer);
        binding.videoPlayer.setVisibility(VISIBLE);

        fullScreenDialog.addContentView(binding.videoPlayer,
                new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        fullScreenDialog.show();
    }

    private void setupThumbnail() {
        if (!TextUtils.isEmpty(recipeStep.getThumbnailURL())) {
            Picasso.with(getContext()).load(recipeStep.getThumbnailURL())
                    .placeholder(R.drawable.loading_progress)
                    .error(R.drawable.ic_error_outline_black_64dp)
                    .into(binding.recipeStepThumbnail);
            binding.recipeStepThumbnail.setVisibility(VISIBLE);
        }
    }

    private void setupInstruction() {
        binding.recipeStepInstruction.setText(recipeStep.getDescription());
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

    private Player.EventListener getPlayerEventListener() {
        return new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_READY) {
                    binding.videoLoadingProgress.setVisibility(GONE);
                    binding.videoPlayer.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffeModeEnabled) {
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
            }

            @Override
            public void onPositionDiscontinuity(int reason) {
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            }

            @Override
            public void onSeekProcessed() {
            }
        };
    }

    private RotateAnimation prepareAnimationForProgressImage() {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(ANIMATION_DURATION);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        return rotateAnimation;
    }
}
