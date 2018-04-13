package android.seriously.com.bakingapp.adapter;

import android.content.Context;
import android.seriously.com.bakingapp.databinding.RecipeStepItemBinding;
import android.seriously.com.bakingapp.model.RecipeStep;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepViewHolder> {

    public interface Listener {
        void onRecipeStepSelected(RecipeStep recipeStep);
    }

    private Context context;
    private final List<RecipeStep> recipeSteps = new ArrayList<>();

    public RecipeStepsAdapter(Context context, List<RecipeStep> recipeSteps) {
        this.context = context;
        this.recipeSteps.addAll(recipeSteps);
    }

    @NonNull
    @Override
    public RecipeStepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        RecipeStepItemBinding binding = RecipeStepItemBinding.inflate(inflater, parent, false);
        return new RecipeStepViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepViewHolder holder, int position) {
        holder.bind(recipeSteps.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeSteps.size();
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder {

        private RecipeStepItemBinding binding;

        RecipeStepViewHolder(RecipeStepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final RecipeStep recipeStep) {
            binding.recipeStep.setText(recipeStep.getShortDescription());
            binding.recipeStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Listener) context).onRecipeStepSelected(recipeStep);
                }
            });
        }
    }
}