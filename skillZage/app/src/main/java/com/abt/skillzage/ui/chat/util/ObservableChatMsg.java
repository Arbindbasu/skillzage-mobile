package com.abt.skillzage.ui.chat.util;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.abt.skillzage.ui.chat.model.ChatMsg;
import java.util.List;

public class ObservableChatMsg extends ViewModel {

    // Create a LiveData with a Model
    private MutableLiveData<List<ChatMsg>> currentchatmsg;

    public MutableLiveData<List<ChatMsg>> getCurrentChatMsgs() {
        if (currentchatmsg == null) {
            currentchatmsg = new MutableLiveData<List<ChatMsg>>();
        }
        return currentchatmsg;
    }

}
