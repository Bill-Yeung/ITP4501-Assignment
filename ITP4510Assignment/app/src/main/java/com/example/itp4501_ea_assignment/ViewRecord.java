package com.example.itp4501_ea_assignment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewRecord extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView lvRecords;
    private Cursor cursor;
    // This activity displays the game records stored in the database
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout .activity_view_records);

        dbHelper = new DatabaseHelper(this);
        lvRecords = findViewById(R.id.lvRecords);

        displayRecords();
    }
    //  This method retrieves all game logs from the database and displays them in a ListView
    private void displayRecords() {
        cursor = dbHelper.getAllGameLogs();

        String[] fromColumns = {
                DatabaseHelper.COLUMN_OPPONENT_NAME,
                DatabaseHelper.COLUMN_GAME_DATE // We will combine columns, so we just need placeholders
        };

        // Define the IDs of the TextViews in your list item layout
        int[] toViews = {
                R.id.tvRecordMain,
                R.id.tvRecordSub
        };

        // Create the adapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_record,
                cursor,
                fromColumns,
                toViews,
                0
        );

        // Set a custom ViewBinder to format the text in the list items
        adapter.setViewBinder((view, cursor, columnIndex) -> {
            // Main text view
            if (view.getId() == R.id.tvRecordMain) {
                String opponent = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_OPPONENT_NAME));
                String result = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_RESULT));
                ((TextView) view).setText("Opponent: " + opponent + " - Result: " + result);
                return true; // We handled this view
            }
            // Sub text view
            if (view.getId() == R.id.tvRecordSub) {
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GAME_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_GAME_TIME));
                int rounds = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROUNDS));
                ((TextView) view).setText(date + " " + time + " | Rounds: " + rounds);
                return true; // We handled this view
            }
            return false;
        });

        // Set the adapter on the ListView
        lvRecords.setAdapter(adapter);
    }
    // This method is called when the activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the cursor when the activity is destroyed to prevent memory leaks
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}