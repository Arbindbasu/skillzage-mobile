package com.abt.skillzage.widget;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CourseViewModel extends ViewModel {

    public MutableLiveData<String> getCourseLiveData() {
        return courseLiveData;
    }

    public void setCourseLiveData(String data) {
        this.courseLiveData.setValue(data);
    }

    private final MutableLiveData<String> courseLiveData = new MutableLiveData<>();

    public CourseViewModel() {
        // trigger user load.
    }


}
