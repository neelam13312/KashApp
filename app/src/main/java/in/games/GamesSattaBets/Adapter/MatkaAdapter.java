package in.games.GamesSattaBets.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Fragment.SelectGameActivity;
import in.games.GamesSattaBets.Model.MatkaModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.SessionMangement;

public class MatkaAdapter extends RecyclerView.Adapter<MatkaAdapter.ViewHolder> {
    private final String TAG=MatkaAdapter.class.getSimpleName();
    Activity context;
    ArrayList<MatkaModel> list;
    Module common;
    private int flag=0;
    Animation animation;
    Module module;
    SessionMangement sessionMangement;

    public MatkaAdapter(Activity context, ArrayList<MatkaModel> list) {
        this.context = context;
        this.list = list;
        common = new Module(context);
        module = new Module(context);
        sessionMangement=new SessionMangement (context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_matka,null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       //
        // holder.lin_num.setAnimation(animation);
       // common.swingAnimations(holder.lin_num);


        final MatkaModel model=list.get(position);
//        if(model.getColor ().equals ("0"))
//        {
//            holder.card_matka.setCardBackgroundColor (context.getResources ().getColor (R.color.yello));
//
//        }
        holder.tv_matkaName.setText(model.getName());
        String startTime="";
        String endTime="";
        String dy=new SimpleDateFormat("EEEE").format(new Date());
//        String dy="Sunday";
//        if(dy.equalsIgnoreCase("Sunday"))
//        {
//            if(getValidTime(model.getStart_time().toString(),model.getEnd_time().toString())){
//                startTime=model.getStart_time();
//                endTime=model.getEnd_time();
//            }else{
//                startTime=model.getBid_start_time();
//                endTime=model.getBid_end_time();
//            }
//        }
//        else if(dy.equalsIgnoreCase("Saturday"))
//        {
//            if(getValidTime(model.getSat_start_time().toString(),model.getSat_end_time().toString())){
//                startTime=model.getSat_start_time();
//                endTime=model.getSat_end_time();
//            }else{
//                startTime=model.getBid_start_time();
//                endTime=model.getBid_end_time();
//            }
//        }
//        else{
            startTime=model.getStart_time();
            endTime=model.getEnd_time();
//        }
        Log.e("matka_time", "onBindViewHolder: "+model.getName()+"--"+startTime+"\n "+endTime );

        if(getValidNumber(model.getEnd_num(),3).equals (""))
        {
            holder.tv_sNum.setText(getValidNumber(model.getStarting_num(),1)+"-"+getValidNumber(model.getNumber(),2)+getValidNumber(model.getEnd_num(),3));

        }
        else {
            holder.tv_sNum.setText (getValidNumber (model.getStarting_num ( ), 1) + "-" + getValidNumber (model.getNumber ( ), 2) + "-" + getValidNumber (model.getEnd_num ( ), 3));
        }
        Date date=new Date();
        SimpleDateFormat sim=new SimpleDateFormat("HH:mm:ss");
        String time1 = startTime.toString();
        String time2 = endTime.toString();

        Date cdate=new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time3=format.format(cdate);
        Date date1 = null;
        Date date2=null;
        Date date3=null;
        try {

            Log.e("alldate", "onBindViewHolder: "+date1+"empty" +date2+"empty"+date3);
//            Log.e("time", "onBindViewHolder: "+date1 );
//            Log.e("time2", "onBindViewHolder: "+date2 );
//            Log.e("time3", "onBindViewHolder: "+date3 );
            date1 = format.parse(time1);
            date2 = format.parse(time2);
            date3=format.parse(time3);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        long difference = date3.getTime() - date1.getTime();
        long as=(difference/1000)/60;

        long diff_close=date3.getTime()-date2.getTime();
        long c=(diff_close/1000)/60;

        if (model.getIs_market_open().equals("0")){
            setInactiveStatus(holder.tv_bidStatus,holder.rel_play,holder.rel_pause);
            holder.tv_openTime.setText(common.get24To12Format(model.getStart_time()));
            holder.tv_closeTime.setText(common.get24To12Format(model.getEnd_time()));
        }else{
//            if (dy.equalsIgnoreCase("Sunday")) {
//                if (getValidTime(model.getStart_time().toString(), model.getEnd_time().toString())) {
//                    getPlayButton(as, c, holder.tv_bidStatus, holder.rel_play, holder.rel_pause);
//                    holder.tv_openTime.setText(common.get24To12Format(model.getStart_time()));
//                    holder.tv_closeTime.setText(common.get24To12Format(model.getEnd_time()));
//                } else {
//                    setInactiveStatus(holder.tv_bidStatus, holder.rel_play, holder.rel_pause);
//                    holder.tv_openTime.setText(common.get24To12Format(model.getBid_start_time()));
//                    holder.tv_closeTime.setText(common.get24To12Format(model.getBid_end_time()));
////              holder.btnPlay.setVisibility(View.GONE);
//                }
//            } else if (dy.equalsIgnoreCase("Saturday")) {
//                if (getValidTime(model.getSat_start_time().toString(), model.getSat_end_time().toString())) {
//                    getPlayButton(as, c, holder.tv_bidStatus, holder.rel_play, holder.rel_pause);
//                    holder.tv_openTime.setText(common.get24To12Format(model.getSat_start_time()));
//                    holder.tv_closeTime.setText(common.get24To12Format(model.getSat_end_time()));
//                } else {
//                    setInactiveStatus(holder.tv_bidStatus, holder.rel_play, holder.rel_pause);
//                    holder.tv_openTime.setText(common.get24To12Format(model.getBid_start_time()));
//                    holder.tv_closeTime.setText(common.get24To12Format(model.getBid_end_time()));
//
////              holder.btnPlay.setVisibility(View.GONE);
//                }
//            } else {
                if (getValidTime(model.getStart_time().toString(), model.getEnd_time().toString())) {
                    getPlayButton(as, c, holder.tv_bidStatus, holder.rel_play, holder.rel_pause);
                } else {
                    setInactiveStatus(holder.tv_bidStatus, holder.rel_play, holder.rel_pause);

                }
                holder.tv_openTime.setText(common.get24To12Format(model.getStart_time()));
                holder.tv_closeTime.setText(common.get24To12Format(model.getEnd_time()));

//            }
        }

        holder.rel_matka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //remove if condition

//                if (model.getIs_market_open().equals("0")){
//                    common.marketClosed ("Bid is closed for today");
//                }else {
                    gotoGames(model);
               // }
            }
        });

        holder.rel_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove if condition
//                 if (model.getIs_market_open().equals("0")){
//                    common.marketClosed ("Bid is closed for today");
//                }else {
                     gotoGames(model);
               //  }
            }
        });
        holder.time_image.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View view) {
                Dialog dialog ;
                dialog=new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_date_timematka);
                dialog.show();
                ImageView iv_cancel=dialog.findViewById (R.id.iv_cancel);
                TextView tv_name=dialog.findViewById (R.id.tv_name);
                TextView tv_day=dialog.findViewById (R.id.tv_day);
                TextView tv_status=dialog.findViewById (R.id.tv_status);
                TextView tv_cres_time=dialog.findViewById (R.id.tv_cres_time);
                TextView tv_ores_time=dialog.findViewById (R.id.tv_ores_time);
                TextView tv_openLastTime=dialog.findViewById (R.id.tv_openLastTime);
                TextView tv_closeLastTime=dialog.findViewById (R.id.tv_closeLastTime);
                Button btn_ok=dialog.findViewById (R.id.btn_ok);

                tv_name.setText (model.getName());
                getStatus(model,tv_status);

                if (model.getIs_market_open().equals("0")){
                    tv_status.setText("BID IS CLOSED FOR TODAY");
                }else {
                        getStatus(model,tv_status);

                }
                tv_openLastTime.setText("Open Bid Last Time\n"+module.get24To12FormatJackport(model.getStart_time()));
                tv_closeLastTime.setText("Close Bid Last Time\n"+module.get24To12FormatJackport(model.getEnd_time()));
                tv_cres_time.setText("Close Result Time\n"+module.get24To12FormatJackport(model.getClose_result_time()));
                tv_ores_time.setText("Open Result Time\n"+module.get24To12FormatJackport(model.getOpen_result_time()));

                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();

                String day=(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));
                tv_day.setText (day);

                btn_ok.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss ();
                        if (model.getIs_market_open().equals("0")){
                            common.marketClosed ("Bid is closed for today");
                        }else {
                            gotoGames(model);
                        }
                    }
                });
                iv_cancel.setOnClickListener (new View.OnClickListener ( ) {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss ();
                    }
                });
                    dialog.setCanceledOnTouchOutside (false);
            }
        });

//        holder.card_matka.setBackgroundColor(Color.parseColor("#000000"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_openTime,tv_closeTime,tv_matkaName,tv_sNum,tv_mNum,tv_eNum,tv_bidStatus;
        RelativeLayout rel_pause;
        LinearLayout lin_num,lin_gameTime;
        CardView card_matka;
        RelativeLayout rel_matka;
        ImageView iv_pause,time_image;
        RelativeLayout rel_play;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lin_gameTime = itemView.findViewById(R.id.lin_gameTime);
            time_image=itemView.findViewById(R.id.time_image);
            rel_pause = itemView.findViewById(R.id.rel_pause);
            lin_num = itemView.findViewById(R.id.lin_num);
            tv_openTime = itemView.findViewById(R.id.tv_openTime);
            tv_closeTime = itemView.findViewById(R.id.tv_closeTime);
            tv_matkaName = itemView.findViewById(R.id.tv_matkaName);
            tv_sNum = itemView.findViewById(R.id.tv_sNum);
            //tv_mNum = itemView.findViewById(R.id.tv_mNum);
            tv_bidStatus = itemView.findViewById(R.id.tv_bidStatus);
           // tv_eNum = itemView.findViewById(R.id.tv_eNum);
            rel_play = itemView.findViewById(R.id.rel_play);
            card_matka = itemView.findViewById(R.id.card_matka);
            animation = AnimationUtils.loadAnimation(context, R.anim.swinging);
            //lin_num.setAnimation(animation);
            rel_matka = itemView.findViewById(R.id.rel_matka);
            iv_pause=itemView.findViewById (R.id.iv_pause);

        }
    }
    public boolean getValidTime(String sTime, String eTime){

        if(sTime.equalsIgnoreCase("00:00:00") && eTime.equalsIgnoreCase("00:00:00")){
            return false;
        }else if(sTime.equalsIgnoreCase("00:00:00.000000") && eTime.equalsIgnoreCase("00:00:00.000000")){
            return false;
        }else{
            return true;
        }
    }

    public String getValidNumber(String str, int palace){
        String validStr="";
        if(str ==null || str.isEmpty() || str.equalsIgnoreCase("null")){
            if(palace==1){
                validStr="***";
            }else if(palace==2){
                validStr="**";
            }else{
               // validStr="***";
                validStr="";
            }
        }else{
            validStr=str;
        }
        return validStr;
    }
    public void getPlayButton(long as, long c, TextView tv_status, RelativeLayout btnPlay,RelativeLayout rel_pause){
        if(as<0)
        {
            flag=2;
//       tv_status.setVisibility(View.VISIBLE);
            setActiveStatus(tv_status,btnPlay);

//        btnPlay.setVisibility(View.VISIBLE);

        }
        else if(c>0)
        {
            flag=3;
            setInactiveStatus(tv_status,btnPlay,rel_pause);
//        tv_status.setVisibility(View.VISIBLE);
//        btnPlay.setVisibility(View.GONE);
        }
        else
        {
            flag=1;
//        tv_status.setVisibility(View.VISIBLE);
            setActiveStatus(tv_status,btnPlay);
//        btnPlay.setVisibility(View.GONE);
        }
    }

    public void setActiveStatus(TextView tv, RelativeLayout btnPlay){
        tv.setVisibility(View.VISIBLE);
        tv.setText("BID Is Running For Today");
        btnPlay.setVisibility(View.VISIBLE);
        tv.setTextColor(context.getResources().getColor(R.color.green));
    }

    public void setInactiveStatus(TextView tv, RelativeLayout btnPlay,RelativeLayout rel_pause){
//        if(tv.getVisibility()== View.GONE){
//            tv.setVisibility(View.VISIBLE);
//        }
        tv.setVisibility(View.VISIBLE);
        tv.setText("BID Is Closed For Today");

        btnPlay.setVisibility(View.GONE);
        rel_pause.setVisibility (View.VISIBLE);
        tv.setTextColor(context.getResources().getColor(R.color.red));

    }

    public void gotoGames(MatkaModel model){
        String dyClick=new SimpleDateFormat("EEEE").format(new Date());
//                String dyClick="Sunday";
        Log.e("asdaee",""+dyClick);
        String stime ="";
        String etime ="";
        int err=0;
        boolean is_error=false;
        if(dyClick.equalsIgnoreCase("Sunday"))
        {
            if(getValidTime(model.getStart_time(),model.getEnd_time()))
            {err=1;
                stime=model.getStart_time().toString();
                etime=model.getEnd_time().toString();
                Log.e(TAG, "onClick: "+etime );

            }else{
                err=2;
                is_error=true;
            }


        }
        else if(dyClick.equalsIgnoreCase("Saturday"))
        {
            if(getValidTime(model.getStart_time(),model.getEnd_time()))
            {
                err=3;
                stime=model.getStart_time().toString();
                etime=model.getEnd_time().toString();
            }else{
                err=4;
                is_error=true;

            }
        }
        else
        {
            stime=model.getStart_time().toString();
            etime=model.getEnd_time().toString();
        }

        long endDiff=common.getTimeDifference(etime);
//              common.showToast(""+err);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        //Remove dialog for bid closed  and check inside games (if and else condition comment)

//        if(endDiff<0 || is_error==true)
//        {
//            common.marketClosed ("Bid is closed for today");
//
//        }
//        else
//        {
            try
            {

                // Toast.makeText(HomeActivity.this,""+Prevalent.Matka_count,Toast.LENGTH_LONG).show();

                // String st=txtStatus.getText().toString();
                String stat="";
                String m_id=model.getId().toString().trim();
                Log.e("matlasid",m_id);
                String matka_name=model.getName().toString().trim();
                String status = model.getStatus();
                if (status.equals( "active" )) {

                    Log.e(TAG, "onClick: "+stime+" == "+etime );
                    Log.e("timerss", "onClick: "+model.getStart_time().toString()+" == "+model.getEnd_time().toString());
                    Intent intent  = new Intent (context, SelectGameActivity.class);

                    //if (endDiff<0 || is_error==true)
                    if(model.getIs_market_open().equals ("0"))

                        {
                            intent.putExtra("market_status","close");
                            stat="close";
                            Log.e ("close", ": "+"ccc" );
                        }
                    else {
                        if (endDiff<0 || is_error==true){
                       // if (getValidTime(model.getStart_time().toString(), model.getEnd_time().toString())) {
                            intent.putExtra("market_status","close");
                            stat="close";
                               } else {
                            intent.putExtra("market_status","open");
                            stat="open";
                        }


                        Log.e ("open", ": "+"ooo" );
                    }

                    Log.e ("mar_sel", "onItemClick: "+model.getIs_market_open() );

                    intent.putExtra("matka_name",module.checkNull(model.getName()));
                    intent.putExtra("m_id", module.checkNull(m_id));
                    intent.putExtra("s_num",module.checkNull(model.getStarting_num()));
                    intent.putExtra("num",module.checkNull(model.getNumber()));
                    intent.putExtra("e_num",module.checkNull(model.getEnd_num()));
                    intent.putExtra("end_time",module.checkNull(model.getEnd_time()));
                    intent.putExtra("start_time",module.checkNull(model.getStart_time()));
                    //intent.putExtra ("market_status",module.checkNull (model.getStatus ()));
                    intent.putExtra ("is_market_open_nextday",module.checkNull (model.getIs_market_open_nextday()));
                    intent.putExtra ("is_market_open_nextday2",module.checkNull (model.getIs_market_open_nextday2()));

                    intent.putExtra("type","matka");
                    Log.e("xdcfvgbhjn",model.getIs_market_open_nextday());
                    Log.e("xdcfvgbhjn",model.getIs_market_open_nextday2());
                    sessionMangement.setnumValue(module.checkNull(model.getStarting_num()),
                            module.checkNull(model.getNumber()),
                            module.checkNull(model.getEnd_num()),"matka",stat,model.getIs_market_open_nextday(),model.getIs_market_open_nextday2());
                    Log.e("matkaadapter",model.getName());
                    Log.e(TAG, "matkaAdapter: "+intent.toString());

//                    AppCompatActivity activity = (AppCompatActivity) context;
                    context.startActivity(intent);
                }
                else
                {
                    common.marketClosed ("Bid closed");


                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(context,""+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
       // }
    }
    public void getStatus(MatkaModel model,TextView tv_sta){
        String dyClick=new SimpleDateFormat("EEEE").format(new Date());
//                String dyClick="Sunday";
        Log.e("asdaee",""+dyClick);
        String stime ="";
        String etime ="";
        int err=0;
        boolean is_error=false;
        if(dyClick.equalsIgnoreCase("Sunday"))
        {
            if(getValidTime(model.getStart_time(),model.getEnd_time()))
            {err=1;
                stime=model.getStart_time().toString();
                etime=model.getEnd_time().toString();
                Log.e(TAG, "onClick: "+etime );

            }else{
                err=2;
                is_error=true;
            }


        }
        else if(dyClick.equalsIgnoreCase("Saturday"))
        {
            if(getValidTime(model.getStart_time(),model.getEnd_time()))
            {
                err=3;
                stime=model.getStart_time().toString();
                etime=model.getEnd_time().toString();
            }else{
                err=4;
                is_error=true;

            }
        }
        else
        {
            stime=model.getStart_time().toString();
            etime=model.getEnd_time().toString();
        }

        long endDiff=common.getTimeDifference(etime);
//              common.showToast(""+err);
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//        if(endDiff<0 || is_error==true)
//        {
//
//            tv_sta.setText ("Bid is closed for today");
//        }
//        else
    //    {
            try
            {

                // Toast.makeText(HomeActivity.this,""+Prevalent.Matka_count,Toast.LENGTH_LONG).show();

                // String st=txtStatus.getText().toString();
                String m_id=model.getId().toString().trim();
                Log.e("matlasid",m_id);
                String matka_name=model.getName().toString().trim();
                String status = model.getStatus();

                if (status.equals( "active" )) {
                    tv_sta.setText ("Bid is running");


                }
                else
                {

                    tv_sta.setText ("Bid closed");

                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                Toast.makeText(context,""+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
      //  }
    }
}
