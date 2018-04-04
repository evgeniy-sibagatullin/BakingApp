package android.seriously.com.bakingapp.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.adapter.RecipeCardsAdapter;
import android.seriously.com.bakingapp.databinding.RecipeSelectionFragmentBinding;
import android.seriously.com.bakingapp.loader.RecipeLoader;
import android.seriously.com.bakingapp.model.Recipe;
import android.seriously.com.bakingapp.utils.NetworkUtils;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class RecipeSelectionFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final int GRID_WIDTH = 2;
    private static final int LOADER_ID = 777;

    private RecipeCardsAdapter recipeCardsAdapter;
    private View noConnection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        RecipeSelectionFragmentBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_selection_fragment, container, false);
        setupRecyclerView(binding.recyclerView);
        setupNoConnectionView(binding.noConnection);
        return binding.getRoot();
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), GRID_WIDTH));
        recipeCardsAdapter = new RecipeCardsAdapter(getContext());
        recyclerView.setAdapter(recipeCardsAdapter);
    }

    private void setupNoConnectionView(ImageView noConnection) {
        this.noConnection = noConnection;
        handleConnection();

        noConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleConnection();
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, @Nullable Bundle args) {
        return new RecipeLoader(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> data) {
        if (!data.isEmpty()) {
            recipeCardsAdapter.addRecipes(data);
            noConnection.setVisibility(GONE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {
    }

    private void handleConnection() {
        if (NetworkUtils.isConnected(getContext())) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            noConnection.setVisibility(VISIBLE);
        }
    }
}
