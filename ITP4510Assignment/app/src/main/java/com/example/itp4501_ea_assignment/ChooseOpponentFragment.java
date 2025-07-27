package com.example.itp4501_ea_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;

public class ChooseOpponentFragment extends Fragment {

    private RecyclerView rvOpponentList;
    private MyAdapter myAdapter;
    private TextView tvChosenOpponent;
    private View btnCreateOpponent;

    private View pageChooseOpponent;
    private View pageCreateOpponent;
    private EditText inputNewOpponent;
    private Button btnSaveOpponent;

    private String[] dataArray;
    private List<String> opponentList;
    public static final String EXTRA_OPPONENT_NAME = "OPPONENT_NAME";

    public ChooseOpponentFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choose_opponent, container, false);

        // Page 1
        rvOpponentList = view.findViewById(R.id.rvOpponentList);
        tvChosenOpponent = view.findViewById(R.id.tvChosenOpponent);
        btnCreateOpponent = view.findViewById(R.id.btnCreateOpponent);

        // switch to Page 2
        pageChooseOpponent = view.findViewById(R.id.pageChooseOpponent);
        pageCreateOpponent = view.findViewById(R.id.pageCreateOpponent);
        inputNewOpponent = view.findViewById(R.id.inputNewOpponent);
        btnSaveOpponent = view.findViewById(R.id.btnSaveOpponent);

        // Setup data
        dataArray = getResources().getStringArray(R.array.Opponent_array);
        opponentList = new ArrayList<>(Arrays.asList(dataArray));

        myAdapter = new MyAdapter(getContext(), opponentList);
        rvOpponentList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOpponentList.setAdapter(myAdapter);

        myAdapter.setItemClickListener(v1 -> {
            int position = rvOpponentList.getChildAdapterPosition(v1);
            String selectedOpponent = opponentList.get(position);
            tvChosenOpponent.setText("Fight with " + selectedOpponent);
            tvChosenOpponent.setOnClickListener(v -> {
                // Create an Intent to go to GameActivity
                Intent intent = new Intent(getActivity(), GameActivity.class);

                // Add the opponent's name to the Intent as an "extra"
                intent.putExtra(EXTRA_OPPONENT_NAME, selectedOpponent);

                // Start the GameActivity
                startActivity(intent);
            });
        });

        // Go to Page 2
        btnCreateOpponent.setOnClickListener(v2 -> {
            pageChooseOpponent.setVisibility(View.GONE);
            pageCreateOpponent.setVisibility(View.VISIBLE);
        });

        // Save new opponent and return to list
        btnSaveOpponent.setOnClickListener(v3 -> {
            String newOpponent = inputNewOpponent.getText().toString().trim();
            if (!newOpponent.isEmpty()) {
                opponentList.add(newOpponent);
                myAdapter.notifyItemInserted(opponentList.size() - 1);
                inputNewOpponent.setText("");

                // Switch back to page 1
                pageCreateOpponent.setVisibility(View.GONE);
                pageChooseOpponent.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
