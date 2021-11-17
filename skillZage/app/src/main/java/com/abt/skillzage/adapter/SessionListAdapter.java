package com.abt.skillzage.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.course_detail.model.CourseSessionModel;
import com.abt.skillzage.widget.Util;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.SessionViewHolder> {

    public List<CourseSessionModel> listCourseSessionModel = new ArrayList<>();
    public Context context;
    public Lifecycle lifecycle;
    public View rootView;

    @NonNull
    @Override
    public SessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_session_item, parent, false);
        return new SessionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionViewHolder holder, int position) {
        //holder.constraint_hidedata_animation.setVisibility(View.VISIBLE);
        if(listCourseSessionModel.get(position).getShow_collapse_status().equalsIgnoreCase("0")){
            holder.constraint_hidedata_animation.setVisibility(View.GONE);
        }else{
            holder.constraint_hidedata_animation.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(listCourseSessionModel.get(holder.getAdapterPosition()).getSession_logo()).into(holder.sessionimg);
        holder.sessionLabel.setText(listCourseSessionModel.get(holder.getAdapterPosition()).getSession_name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.sessiondescription.setText(Html.fromHtml(listCourseSessionModel.get(holder.getAdapterPosition()).getSession_description(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.sessiondescription.setText(Html.fromHtml(listCourseSessionModel.get(holder.getAdapterPosition()).getSession_description()));
        }

        holder.sessionLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listCourseSessionModel.get(holder.getAdapterPosition()).getShow_collapse_status().equalsIgnoreCase("0")){
                    listCourseSessionModel.get(holder.getAdapterPosition()).setShow_collapse_status("1");
                    holder.constraint_hidedata_animation.setVisibility(View.VISIBLE);
                }else{
                    listCourseSessionModel.get(holder.getAdapterPosition()).setShow_collapse_status("0");
                    holder.constraint_hidedata_animation.setVisibility(View.GONE);
                }
            }
        });




        if(listCourseSessionModel != null){

            if(listCourseSessionModel.get(holder.getAdapterPosition()).getSession_video_url() != null && !listCourseSessionModel.get(holder.getAdapterPosition()).getSession_video_url().equalsIgnoreCase("null")){
                if(listCourseSessionModel.get(holder.getAdapterPosition()).getSession_video_url().contains("youtube")){
                    String videoid = Util.extractYTId(listCourseSessionModel.get(holder.getAdapterPosition()).getSession_video_url());
                    holder.videourl_youtube.setVisibility(View.VISIBLE);
//                    holder.videourl_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(context , listCourseSessionModel.get(holder.getAdapterPosition()).getSession_video_url());
//                        }
//                    });

                    lifecycle.addObserver(holder.videourl_youtube);
                    holder.videourl_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });

                }else {

                }
            }

            if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1() != null && !listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().equalsIgnoreCase("null")) {
                if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().contains("youtube")){
                    String videoid = Util.extractYTId(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1());
                    holder.url1_youtube.setVisibility(View.VISIBLE);
                    lifecycle.addObserver(holder.url1_youtube);
                    holder.url1_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });
//                    holder.url1_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(context , videoid);
//                        }
//                    });
                }else if(!listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().contains("youtube") &&
                        !listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().contains(".mp4") &&
                        listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().contains("http://") &&
                        listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().contains("https://")){

                    holder.url1_webview.setVisibility(View.VISIBLE);
                    holder.url1_webview.loadData(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1(), "text/html", "utf-8");
                }else if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().contains("doc")){
                    holder.ur1_docx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/msword";
                            intent.setDataAndType(Uri.fromFile(new File(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1())), type);
                            context.startActivity(intent);
                        }
                    });
                }else if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1().contains("pdf")){
                    holder.ur1_docx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/pdf";
                            intent.setDataAndType(Uri.fromFile(new File(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_1())), type);
                            context.startActivity(intent);
                        }
                    });
                }
            }

            if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2() != null && !listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().equalsIgnoreCase("null")) {
                if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().contains("youtube")){
                    String videoid = Util.extractYTId(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2());
                    holder.url2_youtube.setVisibility(View.VISIBLE);
                    lifecycle.addObserver(holder.url2_youtube);
                    holder.url2_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });
//                    holder.url2_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(context , videoid);
//                        }
//                    });

                }
                else if(!listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().contains("youtube") &&
                        !listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().contains(".mp4") &&
                        listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().contains("http://") &&
                        listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().contains("https://")){

                    holder.url2_webview.setVisibility(View.VISIBLE);
                    holder.url2_webview.loadData(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2(), "text/html", "utf-8");
                }else if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().contains("doc")){
                    holder.url2_docx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/msword";
                            intent.setDataAndType(Uri.fromFile(new File(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2())), type);
                            context.startActivity(intent);
                        }
                    });
                }else if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2().contains("pdf")){
                    holder.url2_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/pdf";
                            intent.setDataAndType(Uri.fromFile(new File(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_2())), type);
                            context.startActivity(intent);
                        }
                    });
                }

            }

            if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3() != null && !listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().equalsIgnoreCase("null")) {
                if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().contains("youtube")){
                    String videoid = Util.extractYTId(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3());
                    holder.url3_youtube.setVisibility(View.VISIBLE);
                    lifecycle.addObserver(holder.url3_youtube);
                    holder.url3_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });
//                    holder.url3_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(context , videoid);
//                        }
//                    });

                }
                else if(!listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().contains("youtube") &&
                        !listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().contains(".mp4") &&
                        listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().contains("http://") &&
                        listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().contains("https://")){

                    holder.url3_webview.setVisibility(View.VISIBLE);
                    holder.url3_webview.loadData(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3(), "text/html", "utf-8");
                }else if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().contains("doc")){
                    holder.url3_docx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/msword";
                            intent.setDataAndType(Uri.fromFile(new File(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3())), type);
                            context.startActivity(intent);
                        }
                    });
                }else if(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3().contains("pdf")){
                    holder.url3_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/pdf";
                            intent.setDataAndType(Uri.fromFile(new File(listCourseSessionModel.get(holder.getAdapterPosition()).getUrl_3())), type);
                            context.startActivity(intent);
                        }
                    });
                }
            }


            holder.btntestyourself.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listCourseSessionModel.get(holder.getAdapterPosition()).getQuiz() == null || listCourseSessionModel.get(holder.getAdapterPosition()).getQuiz().equalsIgnoreCase("null")
                                || listCourseSessionModel.get(holder.getAdapterPosition()).getQuiz().isEmpty()){
                            return;
                        }
                        ((MainActivity)context).setIsnavigatefromsession_test_quiz(true);
                        ((MainActivity)context).setQuizid(Integer.parseInt(listCourseSessionModel.get(holder.getAdapterPosition()).getQuiz()));
                        Navigation.findNavController(rootView).navigate(R.id.action_navigation_course_detail_to_navigation_test_ur_self);
                    }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listCourseSessionModel.size();
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView sessionimg;
        public AppCompatTextView sessiondescription , sessionLabel;
        ConstraintLayout constraint_hidedata_animation;
        public android.widget.VideoView videourl_videoview , url1_videoview , url2_videoview , url3_videoview;
        public com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView videourl_youtube ,url1_youtube , url2_youtube , url3_youtube;
        public WebView url1_webview , url2_webview , url3_webview;
        public androidx.appcompat.widget.AppCompatImageView ur1_docx , ur1_pdf , url2_docx , url2_pdf , url3_docx ,
                url3_pdf ;
        public LinearLayout linear_url1_webview , linear_url2_webview , linear_url3_webview;
        com.google.android.material.button.MaterialButton btntestyourself;

        public SessionViewHolder(@NonNull View itemView) {
            super(itemView);
            sessionLabel = itemView.findViewById(R.id.sessionLabel);
            sessiondescription = itemView.findViewById(R.id.sessiondescription);
            sessionimg = itemView.findViewById(R.id.courseimg);
            constraint_hidedata_animation = itemView.findViewById(R.id.constraint_hidedata_animation);


            videourl_videoview = itemView.findViewById(R.id.videourl_videoview);
            url1_videoview = itemView.findViewById(R.id.url1_videoview);
            url2_videoview = itemView.findViewById(R.id.url2_videoview);
            url3_videoview = itemView.findViewById(R.id.url3_videoview);
            videourl_youtube = itemView.findViewById(R.id.videourl_youtube);
            url1_youtube = itemView.findViewById(R.id.url1_youtube);
            url2_youtube = itemView.findViewById(R.id.url2_youtube);
            url3_youtube = itemView.findViewById(R.id.url3_youtube);
            url1_webview = itemView.findViewById(R.id.url1_webview);
            url2_webview = itemView.findViewById(R.id.url2_webview);
            url3_webview = itemView.findViewById(R.id.url3_webview);

            ur1_docx = itemView.findViewById(R.id.ur1_docx);
            ur1_pdf = itemView.findViewById(R.id.ur1_pdf);
            url2_docx = itemView.findViewById(R.id.url2_docx);
            url2_pdf = itemView.findViewById(R.id.url2_pdf);
            url3_docx = itemView.findViewById(R.id.url3_docx);
            url3_pdf = itemView.findViewById(R.id.url3_pdf);

            btntestyourself = itemView.findViewById(R.id.btntestyourself);


        }
    }
}
