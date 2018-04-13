package android.seriously.com.bakingapp.adapter;

import android.content.Context;
import android.seriously.com.bakingapp.databinding.IngredientItemBinding;
import android.seriously.com.bakingapp.model.Ingredient;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private Context context;
    private final List<Ingredient> ingredients = new ArrayList<>();

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        this.context = context;
        this.ingredients.addAll(ingredients);
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        IngredientItemBinding binding = IngredientItemBinding.inflate(inflater, parent, false);
        return new IngredientViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.bind(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        private IngredientItemBinding binding;

        IngredientViewHolder(IngredientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final Ingredient ingredient) {
            binding.ingredientName.setText(ingredient.getIngredient());
            binding.ingredientQuantity.setText(ingredient.getQuantity());
            binding.ingredientMeasure.setText(ingredient.getMeasure());
        }
    }
}