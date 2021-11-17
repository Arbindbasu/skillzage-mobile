package com.abt.skillzage.ui.chat.firestore;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.ui.chat.model.ChatMsg;
import com.abt.skillzage.ui.chat.util.ObservableChatMsg;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FireStoreService {
    static List<ChatMsg> list = new ArrayList<>();

    public static void addMsgDocument(Context con , FirebaseFirestore firestoreDB , Map<String, Object> chatmsg){

        firestoreDB.collection("blog_messages")
                .add(chatmsg)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("MSG", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(con, "DocumentSnapshot added with ID: " + documentReference.getId(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MSG", "Error : " );
                        Toast.makeText(con, "ERROR" +e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public static void addUserDocument(Context con ,FirebaseFirestore firestoreDB , Map<String, Object> newuser){


        CollectionReference yourCollRef = firestoreDB.collection("users");
        Query query = yourCollRef.whereEqualTo("email", newuser.get("email").toString());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean sta = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("UESRTAG", document.getId() + " => " + document.getData());
                        if(document.getId() != null){
                            sta = true;
                        }
                    }
                    if(sta){
                        Log.d("UESRTAG", " User Document exists!  "+newuser.get("email").toString());
                    }else{
                        Log.d("UESRTAG", "User Document Not exists!   "+newuser.get("email").toString());

                        firestoreDB.collection("users")
                                .add(newuser)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("MSG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        Toast.makeText(con, "DocumentSnapshot added with ID: " + documentReference.getId(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("MSG", " Error : ");
                                        Toast.makeText(con, "ERROR" +e.toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }
                } else {
                    Log.d("UESRTAG", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    public static void addTopicDocument(Context con ,FirebaseFirestore firestoreDB , Map<String, Object> newtopic){

        CollectionReference yourCollRef = firestoreDB.collection("topic");
        Query query = yourCollRef.whereEqualTo("topicId", newtopic.get("topicId").toString());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean sta = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TOPICTAG", document.getId() + " => " + document.getData());
                        if(document.getId() != null){
                            sta = true;
                        }
                    }
                    if(sta){
                        Log.d("TOPICTAG", " TOPICTAG Document exists!  "+newtopic.get("topicId").toString());
                    }else{
                        Log.d("TOPICTAG", "TOPICTAG Document Not exists!   "+newtopic.get("topicId").toString());

                        firestoreDB.collection("topic")
                                .add(newtopic)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("TOPICTAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                        Toast.makeText(con, "DocumentSnapshot added with ID: " + documentReference.getId(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("MSG", "    Error: ");
                                        Toast.makeText(con, "ERROR" +e.toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                         }
                } else {
                    Log.d("TOPICTAG", "Error getting documents: ", task.getException());
                }
            }
        });





    }



    public static void getRealtimeChatUpdate(Context con ,FirebaseFirestore firestoreDB , ObservableChatMsg obsrChatMsgModel ,String chatgroup) {
        firestoreDB.collection("blog_messages").orderBy("createdDateTime", Query.Direction.ASCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("FIRESTORE", document.getData().toString()+"  **********   ");
                        ChatMsg msg = new ChatMsg();
                        msg.setMessage(document.get("message").toString());
                        msg.setEmailId(document.get("emailId").toString());
                        msg.setTopicId(document.get("topicId").toString());
                        msg.setUserName(document.get("userName").toString());
                        msg.setDatetime(document.get("createdDate").toString() +" "+document.get("createdTime").toString());
                        if(document.get("emailId").toString().equalsIgnoreCase(SharedPrefUtil.with(con).getString("skillid",""))){
                            msg.setMSGTYPE("0");  // Own Messages
                        }else{
                            msg.setMSGTYPE("1");  // Others messages
                        }
                        list.add(msg);
                    }
                    obsrChatMsgModel.getCurrentChatMsgs().setValue(list);
                } else {
                    Log.d("FIRESTORE", "Error getting documents: ", task.getException());
                }
            }
        });
    }

}
