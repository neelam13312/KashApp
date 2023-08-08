package in.games.GamesSattaBets.Activity;

//import static com.onesignal.OSUtils.SchemaType.HTTP;
//import static in.games.GamesSattaBet.Config.BaseUrls.All_MSG;
//import static in.games.GamesSattaBet.Config.BaseUrls.SEND_MSG;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_GET_SUPPORT_MSG_QUERY;
import static in.games.GamesSattaBets.Config.BaseUrls.URL_SEND_SUPPORT_MSG_QUERY;
import static in.games.GamesSattaBets.Config.Constants.KEY_ID;

        import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

        import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
        import android.os.SystemClock;
import android.text.Editable;
        import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
        import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
        import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.reflect.TypeToken;
import com.vanniktech.emoji.EmojiPopup;

        import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
        import java.util.HashMap;
import java.util.List;

        import in.games.GamesSattaBets.Adapter.SupportChatAdapter;
import in.games.GamesSattaBets.Config.CommonMethods;
import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Config.VideoChooser;
import in.games.GamesSattaBets.Interfaces.GetAppSettingData;
import in.games.GamesSattaBets.Model.IndexResponse;
import in.games.GamesSattaBets.Model.SupportModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;
import in.games.GamesSattaBets.Util.SessionMangement;

public class SupportActivity extends AppCompatActivity {
    RecyclerView rec_chat;
    TextView tv_title;

    //SupportAdapter messageAdpter;
    ImageView img_back, iv_mic_send,iv_emoji,img_call;
    ArrayList<SupportModel> mList = new ArrayList<>();
    //new-- use this adapter for set message
    SupportChatAdapter supportChatAdapter;
    private NestedScrollView nestedSV;
    private int SELECT_VIDEOS = 101;
    private static final int SELECT_PDF = 204;
    Module common;
    private ArrayList<Uri> pdfUriList = new ArrayList<>();
    LoadingBar loadingBar;
    SessionMangement session_management;
    EditText et_message;
    LinearLayout lin_mesg_layout;
    View lin_record_layout;
    Chronometer tv_record_timer;
    ImageView  img_upload_image;
    CardView img_send;
    String coverImgString = "", message = "", audio_msg = "";
    String s_call="";
    int page = 0;
    int totalPage=1;
    LinearLayoutManager manager;
    private ArrayList<String> videoPathList = new ArrayList<>();
    private ArrayList<Uri> videoUriList = new ArrayList<>();

    String msg_type = "audio";
    MediaRecorder mRecorder;
    String fileName;
    File file;
    Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        initView();
        if (SplashActivity.sessionCountDownTimer!=null){
            SplashActivity.sessionCountDownTimer.cancel();
        }
        module.checkDeviceLogin();
        et_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    msg_type = "text";
                    iv_mic_send.setImageResource(R.drawable.ic_baseline_send_24);
                } else {
                    msg_type = "audio";
                    iv_mic_send.setImageResource(R.drawable.ic_baseline_mic_24);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final EmojiPopup popup = EmojiPopup.Builder
                .fromRootView(findViewById(R.id.rootView)).build(et_message);


        iv_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.toggle();
            }
        });
        setMessageAdapter();
        allChat(0);
        et_message.setOnFocusChangeListener (new View.OnFocusChangeListener ( ) {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b && supportChatAdapter.getItemCount ()>1){
                    rec_chat.getLayoutManager ().smoothScrollToPosition (rec_chat,null,supportChatAdapter.getItemCount ()-1);
                }
            }
        });

        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY==v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight()){
                    if (page>0) {
                        page--;
                        Log.e("page", "" + page);
                        allChat(page);
                    }

                }else if (scrollY==0){

                    if (page<totalPage-1)
                        page++;
                    Log.e("page",""+page);
                    // loadingPB.setVisibility(View.VISIBLE);
                    allChat(page);

                   // Log.e("page",""+page);
                }
            }
        });

    }

    private void initView() {

        lin_mesg_layout=findViewById(R.id.lin_message_layout);
        tv_record_timer=findViewById(R.id.tv_timer);
        lin_record_layout=findViewById(R.id.lin_record);
        img_call=findViewById (R.id.img_call);
        module=new Module (SupportActivity.this);
        module.getConfigData(new GetAppSettingData () {
            @Override
            public void getAppSettingData(IndexResponse model) {
                s_call=model.getSupport ();


            }
        });
        img_call.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent (Intent.ACTION_DIAL);
                intent.setData (Uri.parse ("tel:"+s_call));
                startActivity (intent);
            }
        });
        iv_mic_send = findViewById(R.id.iv_send_msg);
        iv_emoji = findViewById(R.id.iv_emoji);

        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Support");
        img_back = findViewById(R.id.img_back);
       nestedSV = findViewById(R.id.nested_scoll_msg);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SupportActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        common = new Module(SupportActivity.this);
        page = 0;
        session_management = new SessionMangement(SupportActivity.this);
        loadingBar = new LoadingBar(SupportActivity.this);
        // mList = new ArrayList<>();
        et_message = findViewById(R.id.et_message);
        img_send = findViewById(R.id.img_send);
        rec_chat = findViewById(R.id.rec_chat);
        img_upload_image = findViewById(R.id.img_upload_image);
        manager = new LinearLayoutManager(SupportActivity.this);

        LinearLayoutManager line=new LinearLayoutManager (SupportActivity.this);
        // line.setReverseLayout (true);
        line.setStackFromEnd (true);
        rec_chat.setHasFixedSize (true);
        rec_chat.setLayoutManager(line);

        allChat(page);

        Log.e("adsfgrt", String.valueOf(page));
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("sdefrgt",msg_type);
                if (msg_type.equals("text")) {
                    lin_mesg_layout.setVisibility(View.GONE);
                    lin_record_layout.setVisibility(View.VISIBLE);
                } else if (msg_type.equals("audio")) {
                    common.showToast("Hold to record, release to send");
                }
            }
        });
        img_send.setOnLongClickListener(new View.OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onLongClick(View v) {
                Log.e("sdefrgtLong",msg_type);
                //record audio message and send
                //common.showToast("Recording started");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (CommonMethods.checkRecordPermission(getApplicationContext())){
                        if (msg_type.equals("audio")) {
//                            touch=true;
                            lin_mesg_layout.setVisibility(View.GONE);
                            lin_record_layout.setVisibility(View.VISIBLE);
                            startRecording();
                        }
                    }else{
                        CommonMethods.requestRecordPermission(SupportActivity.this,222);
                    }
                }

                return false;
            }
        });
        img_send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("sdefrgtTouch",msg_type);
                switch (event.getAction()){
                    case MotionEvent.ACTION_UP:
                        if (msg_type.equals("audio")) {
                            if (mRecorder!=null){
                                stopRecording();
                                msg_type = "text";
                                et_message.setText(" ");
//                                tv_record_timer.stop();
                            }
                            //common.showToast("Recording released");
                            lin_mesg_layout.setVisibility(View.VISIBLE);
                            lin_record_layout.setVisibility(View.GONE);
                        }
                        else if (msg_type.equals("text")){
                            message=et_message.getText().toString().trim();
                            if (!TextUtils.isEmpty(message)){
                                try {
                                    sendMessage();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
//                                et_message.getText().clear();
                                et_message.setText(" ");

                            }
                        }
                        return true;
                }
                return false;
            }
        });
        img_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(view);
            }
        });
    }

    // this function will start the recrding
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() {

        if(mRecorder!=null) {
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder=null;
        }

        fileName="audio_msg_"+ System.currentTimeMillis();
        try {
//            file=File.createTempFile(fileName,".3gp");
            file=File.createTempFile(fileName,".mp3");}catch (Exception ex){
            ex.printStackTrace();
        }

        mRecorder = new MediaRecorder();

        if(mRecorder!=null)
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        if(mRecorder!=null)
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        if(mRecorder!=null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mRecorder.setOutputFile(file);
            }

        if(mRecorder!=null)
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            if(mRecorder!=null)
                mRecorder.prepare();
        } catch (IOException e) {
            Log.e("resp", "prepare() failed");
        }
        if(mRecorder!=null)
            mRecorder.start();
        tv_record_timer.setBase(SystemClock.elapsedRealtime());
        tv_record_timer.start();
    }

    private void stopRecording() {
        if (mRecorder !=null){
            try {
                mRecorder.stop();
                tv_record_timer.setBase(SystemClock.elapsedRealtime());
                tv_record_timer.stop();
                mRecorder.release();
                mRecorder=null;
                Log.e("asdfrgthy", String.valueOf(file));
                audio_msg=CommonMethods.convertToBase64String(Uri.fromFile(file),this);
                sendMessage();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void sendMessage() throws UnsupportedEncodingException {

        HashMap<String, String> params = new HashMap<>();

        params.put("message", URLEncoder.encode(message,
                "UTF-8"));
        params.put("user_id", session_management.getUserDetails().get(KEY_ID));
        params.put("image", coverImgString);
        params.put("audio", audio_msg);
        Log.e("swdefr",params.toString());

        common.postRequest(URL_SEND_SUPPORT_MSG_QUERY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("send_message", "" + response);
                try {
                    // hiding
                    JSONObject jsonObject = new JSONObject(response);
                    boolean resp = jsonObject.getBoolean("responce");
                    if (resp) {
                        common.showToast(jsonObject.getString("message"));
                        et_message.getText().clear();
                        coverImgString = "";
                        audio_msg = "";
                        message = "";
                        msg_type = "audio";
                        loadingBar.dismiss();
                        allChat(0);

                    } else {
                        common.showToast("unknown error");
                        loadingBar.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loadingBar.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                error.printStackTrace();
                common.showVolleyError(error);
            }
        });

    }

    public void allChat(int page) {
        if (page == 0) {
            mList.clear();
        }

        loadingBar.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", session_management.getUserDetails().get(KEY_ID));
        params.put("page", String.valueOf(page));

        Log.e("pagefvgbhnjmk", String.valueOf(page)+"--"+params);

        common.postRequest(URL_GET_SUPPORT_MSG_QUERY, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("get_msg", "" + response);
                //allChat(page);
                try {
                    mList.clear();

                    JSONObject jsonObject=new JSONObject(response);
                    boolean status = jsonObject.getBoolean ("responce");
                    if (status) {

                        // }

                        // {
                        totalPage=Integer.parseInt(jsonObject.getString("total_pages"));
                        JSONArray data_arr = jsonObject.getJSONArray ("data");
                        ArrayList<SupportModel> tlist = new ArrayList<>();
                        Gson gson =new Gson();
                        Type typeList=new TypeToken<List<SupportModel>> (){}.getType();
                        tlist=gson.fromJson(data_arr.toString(),typeList);

                        if(tlist.size()>0){
                            mList.addAll(tlist);
                            Log.e("msgsize",""+mList.size());
                            new SetMessageFormatTask().execute();
                        }
                    }
                    loadingBar.dismiss();

                } catch (Exception e) {
                    loadingBar.dismiss();
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingBar.dismiss();
                error.printStackTrace();
                common.showVolleyError(error);

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    private void chooseImage(View v) {
        ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(102);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            try {

                Uri imgUri = data.getData();
                // Glide.with(this).load(imgUri).into(user_imge);
                ContentResolver cr = SupportActivity.this.getContentResolver();
                InputStream is = cr.openInputStream(imgUri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                coverImgString = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                page = 0;
                msg_type = "image";
                sendMessage();
                common.showToast("image send");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == 101 && resultCode == RESULT_OK) {

            videoPathList.clear();
            videoPathList = getSelectedVideos(requestCode, data);
            Log.e("videoList ", String.valueOf(videoPathList.size()));

            common.showToast("success video");
        } else if (requestCode == 204 && resultCode == RESULT_OK) {
            pdfUriList.clear();

            if (data != null) {
                if (data.getClipData() != null) {
                    // Getting the length of data and logging up the logs using index
                    for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                        // Getting the URIs of the selected files and logging them into logcat at debug level
                        Uri uri = data.getClipData().getItemAt(index).getUri();
                        pdfUriList.add(uri);
                        //Log.d("filesUri [" + uri + "] : ", String.valueOf(uri) );
                    }
                } else {
                    // Getting the URI of the selected file and logging into logcat at debug level
                    Uri uri = data.getData();
                    pdfUriList.add(uri);
                    Log.d("fileUri: ", String.valueOf(uri));
                }
            }
            common.showToast("success pdf");
            Log.e("pdf_list ", String.valueOf(pdfUriList.size()));
        }
    }

    private ArrayList<String> getSelectedVideos(int requestCode, Intent data) {
        videoUriList.clear();
        ArrayList<String> result = new ArrayList<>();
        ClipData clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                videoUriList.add(videoURI);
                String filePath = VideoChooser.getPath(SupportActivity.this, videoURI);
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            videoUriList.add(videoURI);
            String filePath = VideoChooser.getPath(SupportActivity.this, videoURI);
            result.add(filePath);
        }

        return result;
    }

    class SetMessageFormatTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if (mList.size()>0){

                for (int i=0;i<mList.size();i++){
                    SupportModel model=mList.get(i);
                    String converted= CommonMethods.changeDateTimeFmt("yyyy-MM-dd hh:mm:ss","dd-MMM-yyyy hh:mm a",model.getCreated_at());
                    Log.e("c_date",""+converted);
                    model.setDate(converted.substring(0,11));
                    model.setTime(converted.substring(12));

                }


                // setMessageAdapter();
                // supportChatAdapter.notifyDataSetChanged();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            if (mList.size()>0) {
             supportChatAdapter.notifyDataSetChanged();

            }
            if (page==0){
                nestedSV.post(new Runnable() {
                    @Override
                    public void run() {
                        nestedSV.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }


        }
    }

    public void setMessageAdapter() {
        Log.e("ASWDEFR","wEFRG");
        Log.e("Swdefrgt", String.valueOf(mList.size()));



        supportChatAdapter=new SupportChatAdapter(this, mList, session_management.getUserDetails().get(KEY_ID),
                new SupportChatAdapter.OnChatMessageClickListener() {
                    @Override
                    public void onImageClick(int position, SupportModel model) {

                    }
                });
        rec_chat.setAdapter(supportChatAdapter);
        supportChatAdapter.notifyDataSetChanged();
        rec_chat.scrollToPosition(supportChatAdapter.getItemCount()-1);
    }

}
