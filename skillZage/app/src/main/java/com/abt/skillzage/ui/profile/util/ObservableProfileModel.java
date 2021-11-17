package com.abt.skillzage.ui.profile.util;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.abt.skillzage.ui.chat.model.ChatMsg;
import java.util.List;

public class ObservableProfileModel extends ViewModel {
    // Create a LiveData with a Model
    private MutableLiveData<String> profileImg;

    public MutableLiveData<String> getCurrentProfileImg() {
        if (profileImg == null) {
            profileImg = new MutableLiveData<String>();
        }
        return profileImg;
    }
}
