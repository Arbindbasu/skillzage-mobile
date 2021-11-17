package com.abt.skillzage.ui.test_yourself;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.OrderByViewAdapter;
import com.abt.skillzage.ui.test_yourself.util.ItemOrderByCallback;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.abt.skillzage.widget.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TestYourself extends Fragment implements StartDragListener{

    androidx.constraintlayout.widget.ConstraintLayout mainconstraintlayout;
    LinearLayout linearalllayoutincluded;
    ItemTouchHelper touchHelper;
    com.google.android.material.button.MaterialButton  btnViewComplete , btnViewQuizResult;

    public TestYourself() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_test_yourself, container, false);
        mainconstraintlayout = rootView.findViewById(R.id.mainconstraintlayout);
        linearalllayoutincluded = rootView.findViewById(R.id.linearalllayoutincluded);
        btnViewComplete  = rootView.findViewById(R.id.btnViewComplete);
        btnViewQuizResult  = rootView.findViewById(R.id.btnViewQuizResult);
        btnViewQuizResult.setVisibility(View.GONE);

        int quizid =  ((MainActivity)getActivity()).getQuizid();
        getQuestionsForQuestionset(quizid);
        ((MainActivity)getActivity()).setQuizid(0);
        btnViewComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Confirmation")
                        .setMessage(" Test Complete ? ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            //    Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                Log.d("orderdatachk", finallistQuestions+"  #######  ");
                                for(int t=0; t < finallistQuestions.size();t++){
                                    if(finallistQuestions.get(t).isOrderBy()){
                                        calculateOrderByQuestionScore(finallistQuestions.get(t));
                                    }
                                }
                                btnViewQuizResult.setVisibility(View.VISIBLE);
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();

                            }}).show();
            }
        });

        btnViewQuizResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalwrong < 0){
                    totalwrong = Math.abs(totalwrong);
                }

                String result = " Congratulations. \n you have successfully completed your test. Here is your test result.\n" +
                        "Total Score : "+totalscore+"\n"+
                        "Total Correct Answer : "+totalcorrect+"\n"+
                        "Total Wrong Answer : "+totalwrong+"\n";

                new AlertDialog.Builder(getActivity())
                        .setTitle(" Test Result ")
                        .setMessage(result)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                               // Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                if(((MainActivity)getActivity()).isIsnavigatefromhome()){
                                  //  ((MainActivity)getActivity()).setIsnavigatefromhome(false);
                                    Navigation.findNavController(rootView).navigate(R.id.action_navigation_test_ur_self_to_home);
                                }else if(((MainActivity)getActivity()).isIsnavigatefromcourse_knowurself()){
                                   // ((MainActivity)getActivity()).setIsnavigatefromcourse_knowurself(false);
                                    Navigation.findNavController(rootView).navigate(R.id.action_navigation_test_ur_self_to_navigation_course_detail);
                                }else if(((MainActivity)getActivity()).isIsnavigatefromsession_test_quiz()){
                                    //((MainActivity)getActivity()).setIsnavigatefromsession_test_quiz(false);
                                    Navigation.findNavController(rootView).navigate(R.id.action_navigation_test_ur_self_to_navigation_course_detail);
                                }else if(((MainActivity)getActivity()).isIsnavigatefromproject()){
                                    //((MainActivity)getActivity()).setIsnavigatefromsession_test_quiz(false);
                                    Navigation.findNavController(rootView).navigate(R.id.action_navigation_test_ur_self_to_navigation_projet_detail);
                                }
                                dialog.dismiss();
                            }})
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();

                            }}).show();
            }
        });

        return rootView;
    }
    int y_chk = 0;
    androidx.appcompat.widget.AppCompatTextView question ;
    AppCompatCheckBox multichoiceoptions;
    LinearLayout multiplechoicelinear;

    public void calculateScoreMultipleChoiceQuestion(QuestionModel questionmodel , String selected){
        for(int ch = 0; ch < questionmodel.getLisOfAns().size();ch++){
            if(questionmodel.getLisOfAns().get(ch).getAnswer_option().equalsIgnoreCase(selected)){
                if(questionmodel.getLisOfAns().get(ch).isCorrect()){
                    if(listOfCorrectQuestion.contains(String.valueOf(questionmodel.getId()))){

                    }else{
                        totalscore = totalscore + questionmodel.getLisOfAns().get(ch).getScore();
                        totalcorrect = totalcorrect + 1;
                        if(totalwrong > 0){
                            totalwrong = totalwrong - 1;
                        }

                        listOfCorrectQuestion.add(String.valueOf(questionmodel.getId()));
                    }
                }else{
                    if(listOfCorrectQuestion.contains(String.valueOf(questionmodel.getId()))){
                        listOfCorrectQuestion.remove(String.valueOf(questionmodel.getId()));
                        totalscore = totalscore - questionmodel.getLisOfAns().get(ch).getScore();
                        totalwrong = totalwrong + 1;
                        if(totalcorrect > 0){
                            totalcorrect = totalcorrect - 1;
                        }
                    }else{

                    }
                }
            }
        }
    }

    public void createMultipleChoiceQuestion(QuestionModel questionmodel){

        question = new AppCompatTextView(getActivity());
        multiplechoicelinear = new LinearLayout(getActivity());
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        multiplechoicelinear.setOrientation(LinearLayout.VERTICAL);
        multiplechoicelinear.setLayoutParams(lparams);
        question.setLayoutParams(lparams);

        if(questionmodel.getQuestion() != null){
            question.setText(questionmodel.getQuestion());
            multiplechoicelinear.addView(question);
        }


        String ans_given = "";
        for(y_chk = 0; y_chk < questionmodel.getLisOfAns().size();y_chk++){
            multichoiceoptions = new AppCompatCheckBox(getActivity());
            multichoiceoptions.setText(questionmodel.getLisOfAns().get(y_chk).getAnswer_option());
            multichoiceoptions.setLayoutParams(lparams);
            multiplechoicelinear.addView(multichoiceoptions);

            multichoiceoptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                      //  System.out.println(questionmodel.getLisOfAns().get(y_chk).getAnswer_option()+"  checkbox event id  :::::   "+y_chk+" ****************    "+buttonView.getText());
                        calculateScoreMultipleChoiceQuestion(questionmodel , buttonView.getText().toString() );
                    }
                }
            });
        }


    }

    androidx.appcompat.widget.AppCompatTextView question_single_choice ;
    AppCompatRadioButton singlechoiceoptions;
    RadioGroup singlechoiceoptions_radiogroup;
    LinearLayout singleplechoicelinear;
    int totalcorrect = 0;
    int totalwrong = 0;
    int totalscore = 0;
    List<String> listOfCorrectQuestion = new ArrayList<>();

    public void createSingleChoiceQuestion(QuestionModel questionmodel){

        singlechoiceoptions_radiogroup = new RadioGroup(getActivity());
        question_single_choice = new AppCompatTextView(getActivity());
        singleplechoicelinear = new LinearLayout(getActivity());
        LinearLayout.LayoutParams l_single_choice_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        singleplechoicelinear.setOrientation(LinearLayout.VERTICAL);
        question_single_choice.setLayoutParams(l_single_choice_params);
        RadioGroup.LayoutParams l_radio_group_params = new RadioGroup.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        singlechoiceoptions_radiogroup.setLayoutParams(l_radio_group_params);
        singleplechoicelinear.addView(question_single_choice);

        if(questionmodel.getQuestion() != null){
            question_single_choice.setText(questionmodel.getQuestion());
        }
        for(int y=0; y < questionmodel.getLisOfAns().size();y++){
            singlechoiceoptions = new AppCompatRadioButton(getActivity());
            singlechoiceoptions.setText(questionmodel.getLisOfAns().get(y).getAnswer_option());
            singlechoiceoptions.setLayoutParams(l_single_choice_params);
            singlechoiceoptions_radiogroup.addView(singlechoiceoptions);
        }
        singleplechoicelinear.addView(singlechoiceoptions_radiogroup);

        singlechoiceoptions_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int a = 0; a < group.getChildCount(); a++) {
                    RadioButton btn = (RadioButton) group.getChildAt(a);
                    if (btn.getId() == checkedId) {
                        String text = btn.getText().toString();
                        System.out.println(questionmodel.getLisOfAns().get(a).getAnswer_option()+"  checkbox event id  :::::   "+a);
                        if(questionmodel.getLisOfAns().get(a).isCorrect()){
                            if(listOfCorrectQuestion.contains(String.valueOf(questionmodel.getId()))){

                            }else{
                                totalscore = totalscore + questionmodel.getLisOfAns().get(a).getScore();
                                totalcorrect = totalcorrect + 1;
                                totalwrong = totalwrong - 1;
                                listOfCorrectQuestion.add(String.valueOf(questionmodel.getId()));
                            }
                        }else{
                            if(listOfCorrectQuestion.contains(String.valueOf(questionmodel.getId()))){
                                listOfCorrectQuestion.remove(String.valueOf(questionmodel.getId()));
                                totalscore = totalscore - questionmodel.getLisOfAns().get(a).getScore();
                                totalwrong = totalwrong + 1;
                                totalcorrect = totalcorrect - 1;
                            }else{

                            }
                        }
                    }
                }
            }
        });

//        TextView tv=new TextView(this);
//        tv.setLayoutParams(lparams);
//        tv.setText("test");
        //  this.mainconstraintlayout.addView(tv);

    }


    androidx.appcompat.widget.AppCompatTextView question_video_chooice ;
    com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView videochoice_youtube_player;
    LinearLayout svideochoicelinear;
    public void createVideoChoiceQuestion(QuestionModel questionmodel){

        Log.d("video  url **  ", questionmodel.getVideoUrl().toString());

        question_video_chooice = new AppCompatTextView(getActivity());
        videochoice_youtube_player = new com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView(getActivity());
        svideochoicelinear = new LinearLayout(getActivity());
        LinearLayout.LayoutParams l_video_choice_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        svideochoicelinear.setOrientation(LinearLayout.VERTICAL);
        question_video_chooice.setLayoutParams(l_video_choice_params);
        videochoice_youtube_player.setLayoutParams(l_video_choice_params);
        svideochoicelinear.setLayoutParams(l_video_choice_params);

        if(questionmodel.getQuestion() != null){
            question_video_chooice.setText(questionmodel.getQuestion());
        }

        if(questionmodel.getVideoUrl().contains("youtube")){
            String videoid = Util.extractYTId(questionmodel.getVideoUrl());
            videochoice_youtube_player.setVisibility(View.VISIBLE);
            getLifecycle().addObserver(videochoice_youtube_player);
            videochoice_youtube_player.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    String videoId = videoid;
                    youTubePlayer.loadVideo(videoId, 0);
                    youTubePlayer.pause();
                }
            });
        }
        svideochoicelinear.addView(question_video_chooice);
        svideochoicelinear.addView(videochoice_youtube_player);

//        TextView tv=new TextView(this);
//        tv.setLayoutParams(lparams);
//        tv.setText("test");
        //  this.mainconstraintlayout.addView(tv);

    }

    androidx.appcompat.widget.AppCompatTextView question_img_chooice ;
    AppCompatImageView imagechoice_view;
    LinearLayout simgchoicelinear;
    public void createImgChoiceQuestion(QuestionModel questionmodel){

        question_img_chooice = new AppCompatTextView(getActivity());
        simgchoicelinear = new LinearLayout(getActivity());
        LinearLayout.LayoutParams l_img_choice_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        simgchoicelinear.setOrientation(LinearLayout.VERTICAL);
        question_img_chooice.setLayoutParams(l_img_choice_params);
        simgchoicelinear.setLayoutParams(l_img_choice_params);
        LinearLayout.LayoutParams l_img_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 500);
        imagechoice_view = new AppCompatImageView(getActivity());
        imagechoice_view.setLayoutParams(l_img_params);
        if(questionmodel.getQuestion() != null){
            question_img_chooice.setText(questionmodel.getQuestion());
        }

        if( questionmodel.getImageUrl() != null &&
                !questionmodel.getImageUrl().equalsIgnoreCase("null") &&
                !questionmodel.getImageUrl().isEmpty()){
            Picasso.get().load(questionmodel.getImageUrl())
                    .into(imagechoice_view);
        }

        simgchoicelinear.addView(question_img_chooice);
        simgchoicelinear.addView(imagechoice_view);




//        for(int y=0; y < questionmodel.getLisOfAns().size();y++){
//            singlechoiceoptions = new AppCompatRadioButton(getActivity());
//            singlechoiceoptions.setText(questionmodel.getLisOfAns().get(y).getAnswer_option());
//            singleplechoicelinear.addView(singlechoiceoptions);
//        }


//        TextView tv=new TextView(this);
//        tv.setLayoutParams(lparams);
//        tv.setText("test");
        //  this.mainconstraintlayout.addView(tv);

    }


    OrderByViewAdapter mAdapter;
    ArrayList<String> original_ordered_orderbyArrayList = new ArrayList<>();
    RecyclerView recycler_orderby_View;

    private void populateQuestion(QuestionModel questionmodel) {
        ArrayList<String> orderbyArrayList = new ArrayList<>();
        for(int t=0;t < questionmodel.getLisOfAns().size();t++){
            orderbyArrayList.add(questionmodel.getLisOfAns().get(t).getAnswer_option());
        }
        original_ordered_orderbyArrayList = orderbyArrayList;
        Collections.shuffle(orderbyArrayList);
        mAdapter = new OrderByViewAdapter(orderbyArrayList,this,getActivity());
        ItemTouchHelper.Callback callback =
                new ItemOrderByCallback(mAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recycler_orderby_View);
        recycler_orderby_View.setAdapter(mAdapter);
        sorderbychoicelinear.addView(recycler_orderby_View);

    }



    androidx.appcompat.widget.AppCompatTextView question_orderby_chooice ;
    LinearLayout sorderbychoicelinear;
    public void createOrderByQuestion(QuestionModel questionmodel){

        recycler_orderby_View = new RecyclerView(getActivity());
        question_orderby_chooice = new AppCompatTextView(getActivity());
        sorderbychoicelinear = new LinearLayout(getActivity());
        RecyclerView.LayoutParams re_orderby_choice_params = new RecyclerView.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 800);
        LinearLayout.LayoutParams l_orderby_choice_params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        sorderbychoicelinear.setOrientation(LinearLayout.VERTICAL);
        recycler_orderby_View.setNestedScrollingEnabled(false);
        sorderbychoicelinear.setLayoutParams(l_orderby_choice_params);
        question_orderby_chooice.setLayoutParams(l_orderby_choice_params);
        recycler_orderby_View.setLayoutParams(re_orderby_choice_params);
        recycler_orderby_View.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(questionmodel.getQuestion() != null){
            question_orderby_chooice.setText(questionmodel.getQuestion());
        }
        sorderbychoicelinear.addView(question_orderby_chooice);
        populateQuestion(questionmodel);

//        TextView tv=new TextView(this);
//        tv.setLayoutParams(lparams);
//        tv.setText("test");
        //  this.mainconstraintlayout.addView(tv);

    }

    public void calculateOrderByQuestionScore(QuestionModel questionmodel){
        ArrayList<String> data = mAdapter.getDataModel();
        Log.d("orderdata", data.toString()+"  ******   "+original_ordered_orderbyArrayList);
        Log.d("orderdatachk", original_ordered_orderbyArrayList.equals(data)+"  ******   ");

        if (original_ordered_orderbyArrayList.equals(data) == true) {
            System.out.println(" Array List are equal");
                if(listOfCorrectQuestion.contains(String.valueOf(questionmodel.getId()))){

                }else{
                    totalscore = totalscore + questionmodel.getScore();
                    totalcorrect = totalcorrect + 1;
                    if(totalwrong > 0){
                        totalwrong = totalwrong - 1;
                    }
                    listOfCorrectQuestion.add(String.valueOf(questionmodel.getId()));
                }
           }else{
            if(listOfCorrectQuestion.contains(String.valueOf(questionmodel.getId()))){
                listOfCorrectQuestion.remove(String.valueOf(questionmodel.getId()));
                totalscore = totalscore - questionmodel.getScore();
                totalwrong = totalwrong + 1;
                if(totalcorrect > 0){
                    totalcorrect = totalcorrect - 1;
                }

            }else{

            }
        }
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
    List<QuestionModel> finallistQuestions = new ArrayList<>();
    public void updateUiContent( List<QuestionModel> listQuestions ){



        for(int u=0; u < listQuestions.size();u++){
            System.out.println(listQuestions.get(u)+"   %%%%%%%%%%  "+listQuestions.get(u).getImageUrl()+"  %%%%%%%%%% :::   "+listQuestions.get(u).getSubquestionset());
        }
//        finallistQuestions.clear();

        for(int t=0; t < listQuestions.size();t++){
            System.out.println(listQuestions.get(t).getImageUrl()+"  wwwwwwwwwwwwwwww   "+listQuestions.get(t).getVideoUrl());

            System.out.println(listQuestions.get(t).isOrderBy()+"  *************  Order By  *****************   "+listQuestions.get(t));

            if(listQuestions.get(t).isTrueFalse()){
                createSingleChoiceQuestion(listQuestions.get(t));
                linearalllayoutincluded.addView(singleplechoicelinear);
            }else if(listQuestions.get(t).isMultiple()){
                createMultipleChoiceQuestion(listQuestions.get(t));
                linearalllayoutincluded.addView(multiplechoicelinear);
            }else if(listQuestions.get(t).isOrderBy()){
                createOrderByQuestion(listQuestions.get(t));
                linearalllayoutincluded.addView(sorderbychoicelinear);
            }else if(listQuestions.get(t).getImageUrl() != null &&
                    !listQuestions.get(t).getImageUrl().equalsIgnoreCase("null") &&
                    !listQuestions.get(t).getImageUrl().isEmpty() &&
                    !listQuestions.get(t).getVideoUrl().contains("youtube")){
                     System.out.println(listQuestions.get(t).getImageUrl()+"  IMGGGGGGGGGGGGGGGGGGGGGGGG   "+listQuestions.get(t).getVideoUrl());

                    createImgChoiceQuestion(listQuestions.get(t));
                    linearalllayoutincluded.addView(simgchoicelinear);

                    updateUiContent(listQuestions.get(t).getSubquestionset());

            }else if(listQuestions.get(t).getVideoUrl() != null &&
                    !listQuestions.get(t).getVideoUrl().equalsIgnoreCase("null") &&
                    !listQuestions.get(t).getVideoUrl().isEmpty() &&
                     listQuestions.get(t).getVideoUrl().contains("youtube") &&
                    !listQuestions.get(t).getImageUrl().isEmpty()){

                    System.out.println("  ***************  11111111111    ***********   ");
                    createVideoChoiceQuestion(listQuestions.get(t));
                    linearalllayoutincluded.addView(svideochoicelinear);

                    updateUiContent(listQuestions.get(t).getSubquestionset());

            }
        }


    }


    public void getAnswersForQuestion(List<QuestionModel> listQuestions ) {

        String url = getResources().getString(R.string.baseurl2)+"api/answers?page=0&size=600";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    responseString_all_answers = responseString;
                    JSONArray mainarr = new JSONArray(responseString);

                    for(int x=0; x < listQuestions.size();x++) {
                        List<AnswerModel> listAnswerModel = new ArrayList<>();
                        for (int t = 0; t < mainarr.length(); t++) {
                            if(mainarr.getJSONObject(t).getString("questionsId") != null &&
                                    !mainarr.getJSONObject(t).getString("questionsId").equalsIgnoreCase("null")){
                                if (mainarr.getJSONObject(t).getInt("questionsId") == listQuestions.get(x).getId()) {
                                    AnswerModel ansmodel = new AnswerModel();
                                    ansmodel.setAnsid(mainarr.getJSONObject(t).getInt("id"));
                                    ansmodel.setAnswer_option(mainarr.getJSONObject(t).getString("answer"));
                                    ansmodel.setCorrect(mainarr.getJSONObject(t).getBoolean("isCorrect"));
                                    ansmodel.setQuestionsId(mainarr.getJSONObject(t).getInt("questionsId"));
                                    ansmodel.setScore(mainarr.getJSONObject(t).getInt("score"));
                                    listAnswerModel.add(ansmodel);
                                }
                            }

                        }
                        listQuestions.get(x).setLisOfAns(listAnswerModel);
                    }

                 // Toast.makeText(getActivity() ,listQuestions.toString() , Toast.LENGTH_LONG ).show();

                    getQuestionsForImgVideos(listQuestions);
                    System.out.println("Question and ans model wihtout image video qus and ans :::::::     "+listQuestions.toString());
//                    if(checkimgvideotype){
//                        updateUiContent(listQuestions , true);
//                    }else{
//                        updateUiContent(listQuestions ,false);
//                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private String responseString_all_question = "";
    private String responseString_all_answers = "";
    public void getQuestionsForQuestionset(int quizid) {

        String url = getResources().getString(R.string.baseurl2)+"api/questions?page=0&size=600";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    responseString_all_question = responseString;
                    JSONArray mainarr = new JSONArray(responseString);
                    List<QuestionModel> listQuestions = new ArrayList<>();

                    for(int t=0; t < mainarr.length();t++) {
                        if(mainarr.getJSONObject(t).getString("questionSetId") != null &&
                                !mainarr.getJSONObject(t).getString("questionSetId").equalsIgnoreCase("null")){
                            if (mainarr.getJSONObject(t).getInt("questionSetId") == quizid) {
                                QuestionModel qmodel = new QuestionModel();
                                qmodel.setDiscussion(mainarr.getJSONObject(t).getBoolean("isDiscussion"));
                                qmodel.setId(mainarr.getJSONObject(t).getInt("id"));
                                if(mainarr.getJSONObject(t).getString("imageUrl") != null &&
                                        !mainarr.getJSONObject(t).getString("imageUrl").isEmpty()
                                        &&
                                        !mainarr.getJSONObject(t).getString("videoUrl").contains("youtube")){
                                    qmodel.setImageUrl(mainarr.getJSONObject(t).getString("imageUrl"));
                                    qmodel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                                    qmodel.setVideoType(false);
                                    qmodel.setImageType(true);

                                //    getQuestionsForImgVideos(listQuestions , t );

                                }else if(mainarr.getJSONObject(t).getString("videoUrl") != null &&
                                        !mainarr.getJSONObject(t).getString("videoUrl").isEmpty()
                                        && mainarr.getJSONObject(t).getString("videoUrl").contains("youtube") &&
                                        !mainarr.getJSONObject(t).getString("imageUrl").isEmpty()
                                        ) {
                                    qmodel.setImageUrl(mainarr.getJSONObject(t).getString("imageUrl"));
                                    qmodel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                                    qmodel.setVideoType(true);
                                    qmodel.setImageType(false);

                                  //  getQuestionsForImgVideos(listQuestions , t );
                                }

                                qmodel.setQuestion(mainarr.getJSONObject(t).getString("question"));
                                qmodel.setMultiple(mainarr.getJSONObject(t).getBoolean("isMultiple"));
                                qmodel.setOrderBy(mainarr.getJSONObject(t).getBoolean("isOrderBy"));
                                qmodel.setQuestionSetId(mainarr.getJSONObject(t).getInt("questionSetId"));
                                if(mainarr.getJSONObject(t).getString("score") != null){
                                    qmodel.setScore(mainarr.getJSONObject(t).getInt("score"));
                                }else{
                                    qmodel.setScore(0);
                                }

                                qmodel.setTrueFalse(mainarr.getJSONObject(t).getBoolean("isTrueFalse"));
                                listQuestions.add(qmodel);
                            }
                        }
                    }

                    getAnswersForQuestion( listQuestions);

                }catch(Exception e){
                    Toast.makeText(getActivity() ,"*** "+e.getMessage() , Toast.LENGTH_LONG ).show();

                    e.printStackTrace();
                }
            }
        });
    }



    List<QuestionModel> alllistQuestions_ = new ArrayList<>();
    public void getQuestionsForImgVideos(List<QuestionModel> alllistQuestions ) {
                try {
                    alllistQuestions_ = alllistQuestions;
                    JSONArray mainarr = new JSONArray(responseString_all_question);
                    int index_img = 0;
                    for(int x=0; x < alllistQuestions_.size();x++) {

                        List<QuestionModel> listSubQuestions = new ArrayList<>();
                        List<QuestionModel> listQuestionsNew = new ArrayList<>();
                        for (int t = 0; t < mainarr.length(); t++) {
                            if (mainarr.getJSONObject(t).get("questionSetId") != null) {
                                if (alllistQuestions_.get(x).isImageType()) {
                                    if (alllistQuestions_.get(x).getVideoUrl() != null && !alllistQuestions_.get(x).getVideoUrl().isEmpty()) {
                                        QuestionModel qmodel = new QuestionModel();
                                        if(mainarr.getJSONObject(t).getString("questionSetId") != null &&
                                                !mainarr.getJSONObject(t).getString("questionSetId").equalsIgnoreCase("null")){
                                            if (mainarr.getJSONObject(t).getInt("questionSetId") == Integer.parseInt(alllistQuestions_.get(x).getVideoUrl())) {

                                                qmodel.setDiscussion(mainarr.getJSONObject(t).getBoolean("isDiscussion"));
                                                qmodel.setId(mainarr.getJSONObject(t).getInt("id"));
                                                if (mainarr.getJSONObject(t).getString("imageUrl") != null &&
                                                        !mainarr.getJSONObject(t).getString("imageUrl").isEmpty()
                                                        &&
                                                        !mainarr.getJSONObject(t).getString("videoUrl").contains("youtube")) {
                                                    qmodel.setImageUrl(mainarr.getJSONObject(t).getString("imageUrl"));
                                                    qmodel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                                                    qmodel.setVideoType(false);
                                                    qmodel.setImageType(true);

                                                } else if (mainarr.getJSONObject(t).getString("videoUrl") != null &&
                                                        !mainarr.getJSONObject(t).getString("videoUrl").isEmpty()
                                                        && mainarr.getJSONObject(t).getString("videoUrl").contains("youtube") &&
                                                        !mainarr.getJSONObject(t).getString("imageUrl").isEmpty()
                                                ) {
                                                    qmodel.setImageUrl(mainarr.getJSONObject(t).getString("imageUrl"));
                                                    qmodel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                                                    qmodel.setVideoType(true);
                                                    qmodel.setImageType(false);
                                                }

                                            }


                                        qmodel.setQuestion(mainarr.getJSONObject(t).getString("question"));
                                        qmodel.setMultiple(mainarr.getJSONObject(t).getBoolean("isMultiple"));
                                        qmodel.setOrderBy(mainarr.getJSONObject(t).getBoolean("isOrderBy"));
                                        qmodel.setQuestionSetId(mainarr.getJSONObject(t).getInt("questionSetId"));
                                        if(mainarr.getJSONObject(t).getString("score") != null && !mainarr.getJSONObject(t).getString("score").equalsIgnoreCase("null")){
                                            qmodel.setScore(mainarr.getJSONObject(t).getInt("score"));
                                        }else{
                                            qmodel.setScore(0);
                                        }
                                        qmodel.setTrueFalse(mainarr.getJSONObject(t).getBoolean("isTrueFalse"));
                                        listSubQuestions.add(qmodel);

                                        listQuestionsNew = getAnsForImageVideoQuestions(listSubQuestions);
                                        System.out.println(listQuestionsNew.size() + " img qus and ans model list :::   " + listQuestionsNew.toString());

                                    }

                                }
                                } else if (alllistQuestions_.get(x).isVideoType()) {

                                    if (alllistQuestions_.get(x).getImageUrl() != null && !alllistQuestions_.get(x).getImageUrl().isEmpty()) {
                                        if (mainarr.getJSONObject(t).getString("questionSetId") != null &&
                                            !mainarr.getJSONObject(t).getString("questionSetId").equalsIgnoreCase("null")){
                                            if (mainarr.getJSONObject(t).getInt("questionSetId") == Integer.parseInt(alllistQuestions.get(x).getImageUrl())) {
                                                QuestionModel qmodel = new QuestionModel();
                                                qmodel.setDiscussion(mainarr.getJSONObject(t).getBoolean("isDiscussion"));
                                                qmodel.setId(mainarr.getJSONObject(t).getInt("id"));
                                                if (mainarr.getJSONObject(t).getString("imageUrl") != null &&
                                                        !mainarr.getJSONObject(t).getString("imageUrl").isEmpty()
                                                        &&
                                                        !mainarr.getJSONObject(t).getString("videoUrl").contains("youtube")) {
                                                    qmodel.setImageUrl(mainarr.getJSONObject(t).getString("imageUrl"));
                                                    qmodel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                                                    qmodel.setVideoType(false);
                                                    qmodel.setImageType(true);

                                                } else if (mainarr.getJSONObject(t).getString("videoUrl") != null &&
                                                        !mainarr.getJSONObject(t).getString("videoUrl").isEmpty()
                                                        && mainarr.getJSONObject(t).getString("videoUrl").contains("youtube") &&
                                                        !mainarr.getJSONObject(t).getString("imageUrl").isEmpty()
                                                ) {
                                                    qmodel.setImageUrl(mainarr.getJSONObject(t).getString("imageUrl"));
                                                    qmodel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                                                    qmodel.setVideoType(true);
                                                    qmodel.setImageType(false);
                                                }

                                                qmodel.setQuestion(mainarr.getJSONObject(t).getString("question"));
                                                qmodel.setMultiple(mainarr.getJSONObject(t).getBoolean("isMultiple"));
                                                qmodel.setOrderBy(mainarr.getJSONObject(t).getBoolean("isOrderBy"));
                                                qmodel.setQuestionSetId(mainarr.getJSONObject(t).getInt("questionSetId"));
                                                qmodel.setScore(mainarr.getJSONObject(t).getInt("score"));
                                                qmodel.setTrueFalse(mainarr.getJSONObject(t).getBoolean("isTrueFalse"));
                                                listSubQuestions.add(qmodel);

                                                listQuestionsNew = getAnsForImageVideoQuestions( listSubQuestions );
                                                System.out.println(listQuestionsNew.size()+" video qus and ans model list :::   "+listQuestionsNew.toString());

                                            }
                                        }
                                    }
                                }
                            }
                        }
                        alllistQuestions_.get(x).setSubquestionset(listQuestionsNew);
                    }
                    for(int u=0; u < alllistQuestions_.size();u++){
                        System.out.println(alllistQuestions_.get(u).getVideoUrl()+" *&*&*&*&*&*&"+alllistQuestions_.get(u).getImageUrl()+"*&*&*&*&*& :::   "+alllistQuestions_.get(u).getSubquestionset());
                    }

                    System.out.println("Question model with image video qus :::   "+alllistQuestions_.toString());

                    finallistQuestions = alllistQuestions_;
                    updateUiContent(alllistQuestions_);
                }catch(Exception e){
                    Toast.makeText(getActivity() ,"*** "+e.getMessage() , Toast.LENGTH_LONG ).show();

                    e.printStackTrace();
                }
            }


            public List<QuestionModel> getAnsForImageVideoQuestions(List<QuestionModel> listQuestions){
                try {
                    JSONArray mainarr = new JSONArray(responseString_all_answers);
                    for(int x=0; x < listQuestions.size();x++) {
                        List<AnswerModel> listAnswerModel = new ArrayList<>();
                        for (int t = 0; t < mainarr.length(); t++) {
                            if(mainarr.getJSONObject(t).getString("questionsId") != null &&
                                    !mainarr.getJSONObject(t).getString("questionsId").equalsIgnoreCase("null")){
                                if (mainarr.getJSONObject(t).getInt("questionsId") == listQuestions.get(x).getId()) {
                                    AnswerModel ansmodel = new AnswerModel();
                                    ansmodel.setAnsid(mainarr.getJSONObject(t).getInt("id"));
                                    ansmodel.setAnswer_option(mainarr.getJSONObject(t).getString("answer"));
                                    ansmodel.setCorrect(mainarr.getJSONObject(t).getBoolean("isCorrect"));
                                    ansmodel.setQuestionsId(mainarr.getJSONObject(t).getInt("questionsId"));
                                    listAnswerModel.add(ansmodel);
                                }
                            }

                        }
                        listQuestions.get(x).setLisOfAns(listAnswerModel);
                    }
                      System.out.println("Question and ans model wihth image video qus and ans :::::::     "+listQuestions.toString());
//                    if(checkimgvideotype){
//                        updateUiContent(listQuestions , true);
//                    }else{
//                        updateUiContent(listQuestions ,false);
//                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                return listQuestions;
            }



}