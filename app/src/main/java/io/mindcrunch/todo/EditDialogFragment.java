package io.mindcrunch.todo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditDialogFragment extends DialogFragment {
    static EditDialogFragment newInstance(int position, String text, Priority priority) {
        EditDialogFragment fragment = new EditDialogFragment();
        Bundle arguments = new Bundle();

        arguments.putInt("position", position);
        arguments.putString("text", text);
        arguments.putString("priority", priority.name());
        fragment.setArguments(arguments);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Holo_Light;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_dialog_fragment, container, false);
        getDialog().setTitle("Edit");
        
        Button cancelButton = (Button) view.findViewById(R.id.cancel_edit);
        Button saveButton = (Button) view.findViewById(R.id.save_edit);
        final TextView editText = (TextView) view.findViewById(R.id.editText);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        List<String> spinnerPriorities= new ArrayList<String>();

        for (Priority priority : Priority.values()) {
            spinnerPriorities.add(priority.toString());
        }

        editText.setText(getArguments().getString("text"));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerPriorities);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String priorityText = getArguments().getString("priority");
        spinner.setAdapter(spinnerAdapter);

        for (int i = 0; i < spinnerAdapter.getCount(); i++) {
            if (priorityText.equals(spinnerAdapter.getItem(i))) {
                spinner.setSelection(i);
                break;
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int position = getArguments().getInt("position");
                String newText = editText.getText().toString();
                Priority newPriority = Priority.valueOf(spinner.getSelectedItem().toString());
                MainActivity parent = (MainActivity) getActivity();
                parent.setItem(position, newText, newPriority);
                dismiss();
            }
        });

        return view;
    }
}
