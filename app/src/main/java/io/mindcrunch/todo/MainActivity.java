package io.mindcrunch.todo;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private List<TodoItem> items;
    private TodoItemAdapter itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        itemsAdapter = new TodoItemAdapter(this, items);
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
                TodoItem itemModel = items.get(pos);
                String text = itemModel.getText();
                Priority priority = itemModel.getPriority();
                showDialog(pos, text, priority);
            }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String todoText = etNewItem.getText().toString();
        TodoItem todoItem = new TodoItem(todoText, Priority.MED);
        items.add(todoItem);

        itemsAdapter.notifyDataSetChanged();
        etNewItem.setText("");
        writeItems();
    }

    public void setItem(int position, String text, Priority priority) {
        TodoItem todoItem = items.get(position);

        todoItem.setText(text);
        todoItem.setPriority(priority);
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    private void readItems() {
        File file = new File(getFilesDir(), "todo.txt");

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String text = scanner.nextLine();
                Priority priority = Priority.valueOf(scanner.nextLine());
                TodoItem todoItem = new TodoItem(text, priority);
                this.items.add(todoItem);
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

            for (TodoItem todoItem : items) {
                writer.println(todoItem.getText());
                writer.println(todoItem.getPriority().name());
            }

            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to open " + file.getName() + " for writing!");
        }
    }

    private void showDialog(int position, String text, Priority priority) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("edit");

        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);

        DialogFragment newFragment = EditDialogFragment.newInstance(position, text, priority);
        newFragment.show(ft, "edit");
    }
}
