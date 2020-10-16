package com.example.kbktestproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kbktestproject.model.UserTask;
import com.example.kbktestproject.view_model.UserTaskViewModel;

import java.util.ArrayList;

class TaskViewAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NoteRecyclerViewAdapter";
    public UserTaskViewModel userViewModel;

    private ArrayList<UserTask> tasks = new ArrayList<>();
    private Context mContext;
    private int mSelectedNoteIndex;

    public TaskViewAdaptor(Context context, ArrayList<UserTask> userTasks, UserTaskViewModel viewModel) {
        tasks = userTasks;
        mContext = context;
        userViewModel = viewModel;
    }

    public void removeTask(String documentId){
       userViewModel.markTaskAsDone(documentId);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.my_text_view, parent, false);

        holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ViewHolder){
            final int index = position;
            ((ViewHolder)holder).title.setText(tasks.get(position).taskName);
            if(tasks.get(position).id != "") {
                ((ViewHolder) holder).markAsDone.setText("Done");
                ((ViewHolder) holder).markAsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeTask(tasks.get(index).id);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, markAsDone;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            markAsDone = itemView.findViewById(R.id.checkBox);

        }


    }
}