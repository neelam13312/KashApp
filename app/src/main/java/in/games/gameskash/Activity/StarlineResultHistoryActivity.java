package in.games.gameskash.Activity;

import static in.games.gameskash.Config.BaseUrls.URL_STARLINE_RESULT;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import in.games.gameskash.Adapter.ResultHistoryAdapter;
import in.games.gameskash.Config.Module;
import in.games.gameskash.Model.StarlineResultModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.LoadingBar;
import in.games.gameskash.Util.SessionMangement;

public class StarlineResultHistoryActivity extends AppCompatActivity {
ImageView img_back;
SwipeRefreshLayout swipe;
RecyclerView rec_history;
LoadingBar loadingBar;
Activity activity = StarlineResultHistoryActivity.this;
ArrayList<StarlineResultModel> list;
Module module;
ResultHistoryAdapter resultHistoryAdapter;
SessionMangement sessionMangement;
TextView tv_date;
    String filterDate="";
    DatePickerDialog.OnDateSetListener setListener;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starline_result_history);
        initView();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView() {

        sessionMangement = new SessionMangement(activity);
        module = new Module(activity);
        list = new ArrayList<>();
        loadingBar = new LoadingBar(activity);
        img_back = findViewById(R.id.img_back);
        tv_date = findViewById(R.id.tv_date);
        swipe = findViewById(R.id.swipe);
        rec_history = findViewById(R.id.rec_history);
        rec_history.setLayoutManager(new LinearLayoutManager(activity));
        module.getCurrentDate(tv_date);
        String date = parseDateToddMMyyyy(tv_date.getText().toString().substring(0,10));
        tv_date.setText(date);
        img_back.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        } );
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
//         filterDate = tv_date.getText().toString();
        getStarlineResultHistory(tv_date.getText().toString());
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (swipe.isRefreshing())
                {
                    getStarlineResultHistory(tv_date.getText().toString());
                    swipe.setRefreshing(false);
                }
            }
        });
    }
    private void getStarlineResultHistory(String date) {
        loadingBar.show();
        list.clear();
         HashMap<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        Log.e("ResultHistory",  date);
        module.postRequest(URL_STARLINE_RESULT, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("URL_STARLINE_RESULT", response.toString());
                loadingBar.dismiss();
                try {
                    JSONObject object = new JSONObject(response.toString());
                    JSONArray array = object.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        StarlineResultModel model = new StarlineResultModel();

                        model.setTime(obj.getString("s_game_time"));
                        model.setStarting_num(obj.getString("starting_num"));
                        model.setResult_num(obj.getString("result_num"));
                        list.add(model);


                    }
                    Log.e("resultListSize", String.valueOf(list.size()));

                    if (list.size() > 0) {
                        resultHistoryAdapter = new ResultHistoryAdapter(list,activity);
                        rec_history.setAdapter(resultHistoryAdapter);
                        resultHistoryAdapter.notifyDataSetChanged();

                    } else {
                        module.No_historyDailoge();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                module.showVolleyError(error);
                loadingBar.dismiss();
            }
        });
    }
    private void datePicker() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, android.R.style.Theme_Material_Light_DarkActionBar, setListener, year, month, day);
        datePickerDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);


        setListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                String outputPattern = "dd/MM/yyyy";
//                String outputPattern = "yyyy-MM-dd";
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                Date date1 = null;
                try {
                    date1 = outputFormat.parse(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String d = outputFormat.format(date1);
                parseDateToddMMyyyy(d);
                tv_date.setText(parseDateToddMMyyyy(d));
                getStarlineResultHistory(tv_date.getText().toString());
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        String outputPattern = "YYYY-MM-dd";

        SimpleDateFormat outputFormat = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            outputFormat = new SimpleDateFormat(outputPattern);
        }

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
            Log.e("Sdfrgth",str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}