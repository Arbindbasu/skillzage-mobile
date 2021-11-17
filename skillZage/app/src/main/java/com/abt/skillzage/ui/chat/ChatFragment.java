package com.abt.skillzage.ui.chat;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.chat.firestore.FireStoreService;
import com.abt.skillzage.ui.chat.model.ChatMsg;
import com.abt.skillzage.ui.chat.util.ObservableChatMsg;
import com.abt.skillzage.ui.chat_group.ChatViewModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<ChatMsg> chats;
    private ChatViewModel appModel;
    private RecyclerView rvChats;
    private ChatsAdapter adapter;
    AppCompatEditText inputmsg;
    AppCompatImageButton fabsendmsg;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    FirebaseFirestore db;
    private static ObservableChatMsg obsrChatMsgModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        db = FirebaseFirestore.getInstance();
        chats = new ArrayList<ChatMsg>();
        rvChats = view.findViewById(R.id.rvChats);
        inputmsg = view.findViewById(R.id.message_text);
        fabsendmsg = view.findViewById(R.id.button_send);

        adapter = new ChatsAdapter(getActivity(), chats);
        rvChats.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvChats.setAdapter(adapter);

        String chatgroup = ((MainActivity)getActivity()).getChatgroupname();

        obsrChatMsgModel = new ViewModelProvider(getActivity()).get(ObservableChatMsg.class);
        FireStoreService.getRealtimeChatUpdate(getActivity() , db ,obsrChatMsgModel , chatgroup);

        // Create the observer which updates the UI.
        final Observer<List<ChatMsg>> chatmsgObserver = new Observer<List<ChatMsg>>() {
            @Override
            public void onChanged(@Nullable final List<ChatMsg> listchat) {
                // Update the UI, in this case,
                chats = listchat;
                System.out.println(" All blog messages ::::    "+listchat.toString());
                adapter.chats = chats;
                adapter.notifyDataSetChanged();
            }
        };

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        obsrChatMsgModel.getCurrentChatMsgs().observe(getActivity(), chatmsgObserver);

        Map<String , Object> userdoc = new HashMap<>();
        userdoc.put("email",  SharedPrefUtil.with(getActivity()).getString("userid",""));
        userdoc.put("name",  SharedPrefUtil.with(getActivity()).getString("name",""));
        userdoc.put("profile_img",  "");
        userdoc.put("role",  SharedPrefUtil.with(getActivity()).getString("role",""));
        userdoc.put("skillzag_id",  SharedPrefUtil.with(getActivity()).getString("skillzagid",""));
        userdoc.put("status",  "ACTIVE");
        FireStoreService.addUserDocument(getActivity() , db , userdoc);


        Map<String , Object> topicdoc = new HashMap<>();
        topicdoc.put("topicId",  chatgroup);
        topicdoc.put("topicName",  chatgroup);
        FireStoreService.addTopicDocument(getActivity() , db , topicdoc);


        fabsendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.getDefault());
                String formattedDate = df.format(c);
                System.out.println("Current time => " + formattedDate);
                String dt = formattedDate.split(" ")[0];
                String time = formattedDate.split(" ")[1];

                Map<String , Object> chatmsgdoc = new HashMap<>();
                chatmsgdoc.put("emailId",  SharedPrefUtil.with(getActivity()).getString("userid",""));
                chatmsgdoc.put("message",  inputmsg.getText().toString());
                chatmsgdoc.put("profilePic",  "");
                chatmsgdoc.put("topicId",  chatgroup);
                chatmsgdoc.put("userName",  SharedPrefUtil.with(getActivity()).getString("name",""));
                chatmsgdoc.put("createdDate",  dt);
                chatmsgdoc.put("createdDateTime",  Calendar.getInstance().getTime().getTime());
                chatmsgdoc.put("createdTime",  time);
                FireStoreService.addMsgDocument(getActivity() , db , chatmsgdoc);


                ChatMsg chtmsg = new ChatMsg();
                chtmsg.setDatetime(formattedDate);
                chtmsg.setMessage(inputmsg.getText().toString());
                chtmsg.setTopicId(chatgroup);
                chtmsg.setEmailId(SharedPrefUtil.with(getActivity()).getString("userid",""));
                chtmsg.setUserName(SharedPrefUtil.with(getActivity()).getString("name",""));
                chtmsg.setMSGTYPE("0");
                chats.add(chtmsg);
                adapter.chats = chats;
                adapter.notifyDataSetChanged();

                inputmsg.setText("");
              //  FireStoreService.getRealtimeChatUpdate(getActivity() , db ,obsrChatMsgModel);
            }
        });

        return view;
    }




}