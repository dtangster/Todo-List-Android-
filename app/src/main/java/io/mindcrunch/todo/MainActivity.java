package io.mindcrunch.todo;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private List<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        readItems();

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
                String text = ((TextView) item).getText().toString();
                showDialog(pos, text);
            }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        items.add(etNewItem.getText().toString());
        itemsAdapter.notifyDataSetChanged();
        etNewItem.setText("");
        writeItems();
    }

    public void setItem(int position, String text) {
        items.set(position, text);
        itemsAdapter.notifyDataSetChanged();
    }

    private void readItems() {
        File file = new File(getFilesDir(), "todo.txt");

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                this.items.add(scanner.nextLine());
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("No saved todo for reading.");
        }
    }

    private void writeItems() {
        File file = new File(getFilesDir(), "todo.txt");

        try {
            PrintWriter writer = new PrintWriter(new FileWriter(file));

            for (String item : items) {
                writer.println(item);
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to open " + file.getName() + " for writing!");
        }
    }

    private void showDialog(int position, String text) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("edit");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        DialogFragment newFragment = EditDialogFragment.newInstance(position, text);
        newFragment.show(ft, "edit");
    }
}
