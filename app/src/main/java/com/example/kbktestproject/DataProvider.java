package com.example.kbktestproject;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.MutableLiveData;

import com.example.kbktestproject.model.UserTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataProvider {
    public static DataProvider instance;
    public ArrayList<UserTask> taskList = new ArrayList<>();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static OnUserActionListener onUserActionListener;

    public static DataProvider getInstance( Context context){
        if(instance == null){
            instance = new DataProvider();
        }
        onUserActionListener = (OnUserActionListener) context;
        return instance;
    }

    public MutableLiveData<ArrayList<UserTask>> fetchUserTasks(){
        loadUserData();
        MutableLiveData<ArrayList<UserTask>> userData = new MutableLiveData<>();
        userData.setValue(taskList);
        return userData;
    }
   // data fetcher from Firebase
    private void loadUserData() {
        CollectionReference tasks = db.collection("tasks");
        tasks.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size() != 0){
                    taskList.clear();
                }
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String userTask = documentSnapshot.getString("task");
                    String id = documentSnapshot.getId();
                    taskList.add(new UserTask(userTask, id));
                }
                onUserActionListener.dataUpdated();
            }
        });
    }
    // adds task to database
    public MutableLiveData<ArrayList<UserTask>> addTask(String task){
        addUserTask(task);
        MutableLiveData<ArrayList<UserTask>> userData = new MutableLiveData<>();
        userData.setValue(taskList);
        return userData;
    }
    private void addUserTask(String task){
        final String userTask = task;
        Map<String, Object> data = new HashMap<>();
        data.put("task", task);
        db.collection("tasks")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        taskList.add(new UserTask(userTask,documentReference.getId()));
                        onUserActionListener.taskAdded();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    //marks as a task as done and remove from database
    public MutableLiveData<ArrayList<UserTask>> markAdDone(String taskId){
        removeUserTask(taskId);
        MutableLiveData<ArrayList<UserTask>> userData = new MutableLiveData<>();
        userData.setValue(taskList);
        return userData;
    }
    private void removeUserTask(String documentId){
        final String taskId = documentId;
        db.collection("tasks").document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //remove the task with the same Id
                        Iterator<UserTask> iterator = taskList.iterator();
                        while(iterator.hasNext()){
                            UserTask task = iterator.next();
                            if(task.id.equals(taskId)){
                                iterator.remove();
                                break;
                            }
                        }

                        onUserActionListener.taskRemoved();
                    }
                });
    }

}
