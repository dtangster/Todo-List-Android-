package io.mindcrunch.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {
    // View lookup cache
    private static class ViewHolder {
        TextView text;
        TextView priority;
    }

    public TodoItemAdapter(Context context, List<TodoItem> todoItems) {
        super(context, R.layout.item_list, todoItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_list, parent, false);
            viewHolder.text = (TextView) convertView.findViewById(R.id.todoItem);
            viewHolder.priority = (TextView) convertView.findViewById(R.id.priority);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.text.setText(todoItem.getText());
        viewHolder.priority.setText(todoItem.getPriority().toString());
        // Return the completed view to render on screen
        return convertView;
    }
}