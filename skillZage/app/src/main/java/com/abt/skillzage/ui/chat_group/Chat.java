//package com.abt.skillzage.ui.chat_group;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.net.http.SslError;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.JavascriptInterface;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.widget.AppCompatTextView;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.abt.skillzage.EmailAuthenticationScreen;
//import com.abt.skillzage.MainActivity;
//import com.abt.skillzage.R;
//import com.abt.skillzage.adapter.ChatGrpAdapter;
//import com.abt.skillzage.model.CourseModel;
//import com.abt.skillzage.ui.payment.ProceedToPay;
//import com.abt.skillzage.widget.SharedPrefUtil;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.TextHttpResponseHandler;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import cz.msebera.android.httpclient.Header;
//
//public class Chat extends Fragment {
//
//    private ChatViewModel mViewModel;
//
//    public Chat() {
//        // Required empty public constructor
//    }
//
//
//    public static Chat newInstance() {
//        return new Chat();
//    }
//
//    private View rootView;
//    private WebView chatweb;
//    private Handler mHandler = new Handler();
//    String chatgroupname , name , email;
//    private final String base_url = getResources().getString(R.string.basechaturl)+"mob-chat?topic=BCA&name=Arbbind&email=basuarbind@gmail.com";
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.chat_fragment, container, false);
//        chatweb = rootView.findViewById(R.id.chatweb);
//
//        chatgroupname =  ((MainActivity)getActivity()).getChatgroupname();
//        name =   SharedPrefUtil.with(getActivity()).getString("name" , "");
//        email =   SharedPrefUtil.with(getActivity()).getString("userid" , "");
//
//        chatweb.setWebViewClient(new Chat.MyWebViewClient());
//        chatweb.getSettings().setBuiltInZoomControls(true);
//        chatweb.getSettings().setUseWideViewPort(false);
//        chatweb.getSettings().setLoadWithOverviewMode(false);
//        chatweb.getSettings().setDomStorageEnabled(true);
//        chatweb.setVisibility(View.VISIBLE);
//        chatweb.clearCache(true);
//        chatweb.addJavascriptInterface(new Chat.PayUJavaScriptInterface(getActivity()),
//                "SkillChat");
//        chatweb.getSettings().setJavaScriptEnabled(true);
//        Map<String, String> mapParams = new HashMap<String, String>();
//        mapParams.put("name" , SharedPrefUtil.with(getActivity()).getString("name",""));
//        mapParams.put("email" , SharedPrefUtil.with(getActivity()).getString("userid",""));
//        mapParams.put("topic" ,  ((MainActivity)getActivity()).getChatgroupname());
//
//        webviewClientPost(chatweb, base_url, mapParams.entrySet());
//        return rootView;
//    }
//
//
//
//    public class PayUJavaScriptInterface {
//        Context mContext;
//        /** Instantiate the interface and set the context */
//        PayUJavaScriptInterface(Context c) {
//            mContext = c;
//        }
//
//        public String toString() { return "SkillChat"; }
//
//
//        @JavascriptInterface
//        public void success(final String result,final String transactionId,final String orderNo,final String transactionStatus,
//                            final String shoppingAmount, final String currency,final String paymentDate, final String  approvalCode,
//                            final String paymentChannel) {
//            mHandler.post(new Runnable() {
//
//                public void run() {
//
//                    mHandler = null;
//                    Toast.makeText(getActivity(), "Transaction Successful",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//
//        @JavascriptInterface
//        public void fail(final String result,final String transactionId,final String orderNo,final String transactionStatus,
//                         final String shoppingAmount, final String currency,final String paymentDate, final String  approvalCode, final String paymentChannel,
//                         final String errorCode) {
//            mHandler.post(new Runnable() {
//
//                public void run() {
//
//                    mHandler = null;
//                    Toast.makeText(getActivity(), "Transaction Failed",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//
//
//        @JavascriptInterface
//        public void cancel(final String result,final String transactionId,final String orderNo,final String transactionStatus,
//                           final String shoppingAmount, final String currency,final String paymentDate, final String  approvalCode, final String paymentChannel,
//                           final String errorCode) {
//            mHandler.post(new Runnable() {
//
//                public void run() {
//
//                    mHandler = null;
//                    Toast.makeText(getActivity(), "Transaction Canceled",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }
//
//
//
//    public void webviewClientPost(WebView webView, String url,
//                                   Collection<Map.Entry<String, String>> postData) {
//        StringBuilder sb = new StringBuilder();
//        // Toast.makeText(this, "  Callling to payment gt", Toast.LENGTH_LONG).show();
//        sb.append("<html><head></head>");
//        sb.append("<body onload='form1.submit()'>");
//        sb.append(String.format("<form id='form1' action='%s' method='%s'>",
//                url, "post"));
//        for (Map.Entry<String, String> item : postData) {
//            sb.append(String.format(
//                    "<input name='%s' type='hidden' value='%s' />",
//                    item.getKey(), item.getValue()));
//        }
//        sb.append("</form></body></html>");
//        Log.d("payment", "webview_ClientPost called");
//        webView.loadData(sb.toString(), "text/html", "utf-8");
//    }
//
//    public boolean empty(String s) {
//        return s == null || s.trim().equals("");
//    }
//
//    public String hashCal(String type, String str) {
//        byte[] hashseq = str.getBytes();
//        StringBuffer hexString = new StringBuffer();
//        try {
//            MessageDigest algorithm = MessageDigest.getInstance(type);
//            algorithm.reset();
//            algorithm.update(hashseq);
//            byte[] messageDigest = algorithm.digest();
//
//            for (int i = 0; i < messageDigest.length; i++) {
//                String hex = Integer.toHexString(0xFF & messageDigest[i]);
//                if (hex.length() == 1)
//                    hexString.append("0");
//                hexString.append(hex);
//            }
//        } catch (NoSuchAlgorithmException nsae) {
//        }
//        return hexString.toString();
//
//    }
//
//    private ProgressBar progress;
//    private class MyWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//
//            super.onPageFinished(view, url);
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//        }
//
//        @Override
//        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//            handler.proceed();
//
//            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage("SSL Exception");
//            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    handler.proceed();
//                }
//            });
//            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    handler.cancel();
//                }
//            });
//            final AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//    }
//
//
//}