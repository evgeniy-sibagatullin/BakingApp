package android.seriously.com.bakingapp.adapter;

import android.content.Context;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.databinding.RecipeCardItemBinding;
import android.seriously.com.bakingapp.model.Recipe;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeCardsAdapter extends RecyclerView.Adapter<RecipeCardsAdapter.RecipeViewHolder> {

    public interface Listener {
        void onRecipeCardSelected(Recipe recipe);
    }

    private Context context;
    private final List<Recipe> recipes = new ArrayList<>();

    public RecipeCardsAdapter(Context context) {
        this.context = context;
    }

    public void addRecipes(List<Recipe> recipes) {
        if (this.recipes.isEmpty()) {
            this.recipes.addAll(recipes);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecipeCardItemBinding binding = RecipeCardItemBinding.inflate(inflater, parent, false);
        return new RecipeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        private RecipeCardItemBinding binding;

        RecipeViewHolder(RecipeCardItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Recipe recipe) {
            binding.cardRecipeName.setText(recipe.getName());
            binding.cardRecipeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Listener) context).onRecipeCardSelected(recipe);
                }
            });

            if (!TextUtils.isEmpty(recipe.getImageUrl())) {
                Picasso.with(context).load(recipe.getImageUrl())
                        .placeholder(R.drawable.loading_progress)
                        .into(binding.cardRecipeIcon);
            }
        }
    }
}