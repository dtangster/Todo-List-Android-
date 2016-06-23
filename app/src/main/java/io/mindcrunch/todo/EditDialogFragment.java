package io.mindcrunch.todo;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class EditDialogFragment extends DialogFragment {
    static EditDialogFragment newInstance(int position, String text) {
        EditDialogFragment fragment = new EditDialogFragment();
        Bundle arguments = new Bundle();

        arguments.putInt("position", position);
        arguments.putString("text", text);
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

        editText.setText(getArguments().getString("text"));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int position = getArguments().getInt("position");
                String newText = editText.getText().toString();
                ((MainActivity) getActivity()).setItem(position, newText);
                dismiss();
            }
        });

        return view;
    }
}
