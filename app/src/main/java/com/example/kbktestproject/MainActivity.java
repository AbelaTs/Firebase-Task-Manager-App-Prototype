package com.example.kbktestproject;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.kbktestproject.model.UserTask;
import com.example.kbktestproject.utilities.OnUserActionListener;
import com.example.kbktestproject.view_model.UserTaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnUserActionListener {
    public static FirebaseFirestore db;
    public static ArrayList<UserTask> taskList = new ArrayList<>();
    private static RecyclerView recyclerView;
    private static  TaskViewAdaptor myAdapter;
    private UserTaskViewModel userTaskViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        //emptyTextView = findViewById(R.id.emptyText);
        db = FirebaseFirestore.getInstance();
        //Initializing our view model
        ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();
        userTaskViewModel =  new ViewModelProvider(this, factory).get(UserTaskViewModel.class);
        userTaskViewModel.init(this);
        // setting up recycler view
        recyclerView = (RecyclerView) findViewById(R.id.taskLists);
        initRecyclerView();
        //top app bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // task adder dialog
        final EditText input = new EditText(this);
        AlertDialog.Builder addTaskDialog = new AlertDialog.Builder(this);
        final AlertDialog.Builder successTaskDialog = new AlertDialog.Builder(this);
        // add task dialog
        addTaskDialog.setView(input)
                .setTitle("Add your task")
                // Add action buttons
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (input.getText().toString().length() != 0) {
                            String text = input.getText().toString();
                            userTaskViewModel.addUserTask(text);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        input.setText("");
                    }
                });
        final AlertDialog addDialog = addTaskDialog.create();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog.show();
            }
        });
    }

 // Populating our recycler view with our live data
    private void initRecyclerView(){
        if(myAdapter == null){
            myAdapter = new TaskViewAdaptor(this,userTaskViewModel.getUserTasks().getValue(),userTaskViewModel);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }


    @Override
    public void taskAdded() {
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void taskRemoved() {
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void dataUpdated() {
        myAdapter.notifyDataSetChanged();
    }
}