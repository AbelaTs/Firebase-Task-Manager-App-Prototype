package com.example.kbktestproject.view_model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.kbktestproject.utilities.DataProvider;
import com.example.kbktestproject.model.UserTask;

import java.util.ArrayList;

public class UserTaskViewModel extends ViewModel {
    MutableLiveData<ArrayList<UserTask>> liveData;
    Context ctx;
    public void init(Context context){
        if(liveData != null){
            return;
        }
        ctx = context;
        liveData = DataProvider.getInstance(context).fetchUserTasks();
    }
    public LiveData<ArrayList<UserTask>> getUserTasks(){
        return liveData;
    }
    public void addUserTask(String task){
        liveData = DataProvider.getInstance(ctx).addTask(task);
    }
    public void markTaskAsDone(String documentId){
        liveData = DataProvider.getInstance(ctx).markAdDone(documentId);
    }
}
