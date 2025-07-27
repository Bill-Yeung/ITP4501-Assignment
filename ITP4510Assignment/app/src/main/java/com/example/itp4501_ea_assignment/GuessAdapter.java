package com.example.itp4501_ea_assignment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GuessAdapter extends RecyclerView.Adapter<GuessAdapter.GuessViewHolder> {
    private final List<Integer> guessOptions;
    private int selectedPosition = -1;
    public GuessAdapter(List<Integer> guessOptions) {
        this.guessOptions = guessOptions;
    }
    // Reset the selected position
    @NonNull
    @Override
    public GuessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_guess, parent, false);
        return new GuessViewHolder(view);
    }

    // Bind the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull GuessViewHolder holder, int position) {
        holder.bind(guessOptions.get(position), position);
    }
    // Return the total number of items in the list
    @Override
    public int getItemCount() {
        return guessOptions.size();
    }
    // Get the currently selected value, or null if no selection
    public Integer getSelectedValue() {
        if (selectedPosition == -1) {
            return null;
        }
        return guessOptions.get(selectedPosition);
    }
    // ViewHolder class to hold the views for each guess option
    class GuessViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbOption;

        public GuessViewHolder(@NonNull View itemView) {
            super(itemView);
            rbOption = itemView.findViewById(R.id.rbOption);

            itemView.setOnClickListener(v -> {
                // Deselect the old item and select the new one
                int previousPosition = selectedPosition;
                selectedPosition = getAdapterPosition();
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
            });
        }

        void bind(int number, int position) {
            rbOption.setText(String.valueOf(number));
            rbOption.setChecked(position == selectedPosition);
        }
    }
}