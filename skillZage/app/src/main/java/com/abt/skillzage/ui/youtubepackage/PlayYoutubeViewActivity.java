//package com.abt.skillzage.ui.youtubepackage;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.ActivityNotFoundException;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.abt.skillzage.widget.Util;
//import com.google.android.youtube.player.YouTubeBaseActivity;
//import com.abt.skillzage.R;
//import com.google.android.youtube.player.YouTubeInitializationResult;
//import com.google.android.youtube.player.YouTubePlayer;
//
//public class PlayYoutubeViewActivity extends AppCompatActivity {
//
//
//    String youtubeurl = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_play_youtube_view);
//
//        youtubeurl = "https://www.youtube.com/watch?v=ukzFI9rgwfU";
//
//
//        String videoid = Util.extractYTId(youtubeurl);
//        watchYoutubeVideo(PlayYoutubeViewActivity.this , videoid);
//    }
//    public static void watchYoutubeVideo(Context context, String id){
//        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
//        Intent webIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://www.youtube.com/watch?v=" + id));
//        try {
//            context.startActivity(appIntent);
//        } catch (ActivityNotFoundException ex) {
//            context.startActivity(webIntent);
//        }
//    }
//
//}