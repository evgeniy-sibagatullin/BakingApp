package android.seriously.com.bakingapp.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.seriously.com.bakingapp.R;
import android.seriously.com.bakingapp.adapter.IngredientsAdapter;
import android.seriously.com.bakingapp.databinding.IngredientsDialogBinding;
import android.seriously.com.bakingapp.model.Ingredient;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

import static android.seriously.com.bakingapp.utils.Constants.BUNDLE_KEY_INGREDIENTS;

public class IngredientsDialog extends DialogFragment {

    public static final String TAG = IngredientsDialog.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        IngredientsDialogBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.ingredients_dialog, null, false);

        //noinspection unchecked,ConstantConditions
        setupRecyclerView(binding.recyclerView, (List<Ingredient>) getArguments()
                .get(BUNDLE_KEY_INGREDIENTS));

        return new AlertDialog.Builder(getActivity())
                .setView(binding.getRoot())
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Ingredient> ingredients) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new IngredientsAdapter(getContext(), ingredients));
    }
}
