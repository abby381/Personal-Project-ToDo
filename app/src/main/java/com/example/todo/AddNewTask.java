package com.example.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.todo.Model.ToDoModel;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTasktext;
    private Button newTaskSaveButton;
    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return  new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_task, container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTasktext = getView().findViewById(R.id.newTasktext);
        newTaskSaveButton = getView().findViewById(R.id.newtaskbutton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if (bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            newTasktext.setText(task);
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        newTasktext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String text = newTasktext.getText().toString();
                ToDoModel model = new ToDoModel();
                model.setStatus(0);
                model.setTask(text);
                db.insertTask(model);
//                if (finalIsUpdate)
//                {
//                    db.updateTask(bundle.getInt("id"), text); // mahirap ano hahaha oo
//                } else
//                    {
//                    ToDoModel task = new ToDoModel();
//                    task.setTask(text);
//                    task.setStatus(0); //meron na :) yehegaling ye tuloy mo na hahahh thank youuuuu <3 bukas na uli haha sigii byee hahah good night <3
//                }
                dismiss();
            }
        });
    }
        @Override
        public void onDismiss(DialogInterface dialog){
            Activity activity = getActivity();
            if (activity instanceof DialogCloseListener){
                ((DialogCloseListener)activity).handleDialogClose(dialog);
            }
        }



}
