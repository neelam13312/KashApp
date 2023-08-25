package in.games.Gameskash.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Model.PassbookHistoryModel;
import in.games.Gameskash.R;

public class PassbookAdapter extends RecyclerView.Adapter<PassbookAdapter.ViewHolder> {
    Context context;
    ArrayList<PassbookHistoryModel> list;
    String show_key="";
    Module module;

    public PassbookAdapter(Context context, ArrayList<PassbookHistoryModel> list) {
        this.context = context;
        this.list = list;
        module = new Module(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rowpassbook_history,null);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       PassbookHistoryModel model=list.get (position);

         show_key=model.getBid_played ();
//0-tran ,1-game


        if(show_key.equalsIgnoreCase ("1"))
        {
            holder.tv_no.setVisibility (View.GONE);
            holder.lin_game.setVisibility (View.VISIBLE);
            holder.lin_other.setVisibility (View.GONE);
            holder.lin_date.setVisibility (View.VISIBLE);
            holder.lin_status.setVisibility (View.VISIBLE);
            holder.tv_particular.setText (model.getParticuler_text ());
        }
        else if(show_key.equalsIgnoreCase ("0"))
        {
            holder.tv_no.setVisibility (View.GONE);
            holder.lin_other.setVisibility (View.VISIBLE);
            holder.lin_game.setVisibility (View.GONE);
            holder.lin_date.setVisibility (View.VISIBLE);
            holder.lin_status.setVisibility (View.VISIBLE);
            holder.tv_particular.setText (model.getParticuler_text ()+"\nTransaction Id : "+module.alphaNumric(list.get(position).getTransaction_id()));

        }
        else
        {
            holder.lin_date.setVisibility (View.GONE);
            holder.lin_status.setVisibility (View.GONE);
            holder.lin_game.setVisibility (View.GONE);
            holder.lin_other.setVisibility (View.GONE);
            holder.tv_no.setVisibility (View.VISIBLE);
            holder.tv_particular.setText (model.getParticuler_text ()+"\nTransaction Id : "+module.alphaNumric(list.get(position).getTransaction_id()));

        }

        String types=model.getGame_name ().replace ("_" ," ");
        holder.tv_type.setText (model.getBet_type ());

        String g_name=model.getGame_name ();
        holder.tv_game_name.setText (types);
        String matka_name=model.getMatka_name ();
        holder.tv_provider_name.setText (model.getMatka_name ());

        String onlyDate=model.getDate ();
//        20220913000170,2022091300032658
//        20220914000169,2022091400033203


        String[] separated = onlyDate.split(" ");
//        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            Date d = (Date) simpleDate.parse(separated[0]);
//        } catch (ParseException e) {
//            e.printStackTrace ( );
//        }

        String converted=changeDateTimeFmt("yyyy-MM-dd hh:mm:ss","dd-MM-yyyy hh:mm:ss",model.getDate());
        Log.d ("cknn", "onBindViewHolder: "+converted);
        String[] dt = converted.split(" ");
        //holder.tv_trnsdate.setText (separated[0]);
        holder.tv_trnsdate.setText (dt[0]);
//        holder.tv_played.setText (separated[0]);
        holder.tv_played.setText (dt[0]);
//        holder.tv_particular.setText (model.getParticuler_text ()+"("+module.alphaNumric(list.get(position).getTransaction_id())+")");

        if(model.getMode ().equals ("null"))
        {
            holder.tv_fundmode.setText ("--");
        }
        else {
            holder.tv_fundmode.setText (model.getMode ( ));
        }

        if (model.getAdded_by().equals("1")){
//           money added by admin
            holder.tv_trans_amount.setText (model.getTrans_damt ()+" by admin");
        }else if (model.getAdded_by().equals("0")){
//            money added by self
            holder.tv_trans_amount.setText (model.getTrans_damt ()+" by self");
        }



//        if(model.getTrans_id ().equals (""))
//        {
//            holder.tv_trans_id.setText ("--");
//        }
//        else {
//            holder.tv_trans_id.setText (model.getTransaction_id ());

        try {
            holder.tv_trans_id.setText(module.alphaNumric(list.get(position).getTransaction_id()));
        }catch (Exception ex){
            holder.tv_trans_id.setText("");
            ex.printStackTrace();
        }

     //   }

        holder.tv_trans_date.setText (model.getDate_and_time ());
        String status=model.getStatus ();

        holder.tv_status.setText (status.substring (0,1).toUpperCase ()+status.substring (1));

        if(model.getPrev_amount ().equalsIgnoreCase ("null"))
        {
            holder.tv_p_amount.setText ("0");
        }
        else {
            holder.tv_p_amount.setText (model.getPrev_amount ( ));
        }
        if (model.getAmount_type().equalsIgnoreCase("Add")) {
            holder.tv_t_amount.setText("+" + model.getTrnasaction_amt());
            holder.tv_t_amount.setTextColor(context.getResources().getColor(R.color.dark_Green));
        }else {
            holder.tv_t_amount.setText("-" + model.getTrnasaction_amt());
            holder.tv_t_amount.setTextColor(context.getResources().getColor(R.color.lightRed));


        }

        if(model.getCurrent_amt ().equalsIgnoreCase ("null"))
        {
            holder.tv_cur_amount.setText ("--");
        }
        else {
            holder.tv_cur_amount.setText (model.getCurrent_amt ( ));
        }

        holder.lin_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.img_down.getVisibility()==View.VISIBLE){
                    holder.img_down.setVisibility(View.GONE);
                    holder.cv_list.setVisibility(View.VISIBLE);
                    holder.img_up.setVisibility(View.VISIBLE);
                }
                else {
                    holder.img_up.setVisibility(View.GONE);

                    holder.cv_list.setVisibility(View.GONE);
                    holder.img_down.setVisibility(View.VISIBLE);
                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_trnsdate,tv_particular,tv_fundmode,tv_no
                ,tv_trans_amount,tv_trans_id,tv_trans_date,tv_status
                ,tv_p_amount,tv_t_amount,tv_cur_amount,tv_type,tv_game_name,tv_provider_name,tv_played;
        LinearLayout lin_arrow ,lin_other,lin_game,lin_status,lin_date;
        CardView cv_list;
        ImageView img_down,img_up;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_status=itemView.findViewById (R.id.lin_status);
            lin_date=itemView.findViewById (R.id.lin_date);
            tv_no=itemView.findViewById (R.id.tv_no);
            tv_played=itemView.findViewById (R.id.tv_played);
            tv_provider_name=itemView.findViewById (R.id.tv_provider_name);
            tv_game_name=itemView.findViewById (R.id.tv_game_name);
            tv_type=itemView.findViewById (R.id.tv_type);
            lin_other=itemView.findViewById (R.id.lin_other);
            lin_game=itemView.findViewById (R.id.lin_game);
            cv_list=itemView.findViewById (R.id.cv_list);
            lin_arrow=itemView.findViewById (R.id.lin_arrow);
            img_down=itemView.findViewById (R.id.img_down);
            img_up=itemView.findViewById (R.id.img_up);
            tv_trnsdate=itemView.findViewById (R.id.tv_trnsdate);
            tv_particular=itemView.findViewById (R.id.tv_particular);
            tv_status=itemView.findViewById (R.id.tv_status);
            tv_fundmode=itemView.findViewById (R.id.tv_fundmode);
            tv_trans_amount=itemView.findViewById (R.id.tv_trans_amount);
            tv_trans_id=itemView.findViewById (R.id.tv_trans_id);
            tv_trans_date=itemView.findViewById (R.id.tv_trans_date);

            tv_p_amount=itemView.findViewById (R.id.tv_p_amount);
            tv_t_amount=itemView.findViewById (R.id.tv_t_amount);
            tv_cur_amount=itemView.findViewById (R.id.tv_cur_amount);


        }
    }
    public static  String changeDateTimeFmt(String orgFmt,String tgtFmt,String date_time){
        String convertedValue="";
        try {
            DateFormat originalFmt=new SimpleDateFormat(orgFmt, Locale.getDefault());
            DateFormat targetFmt=new SimpleDateFormat (tgtFmt);
            Date date= originalFmt.parse(date_time);
            convertedValue=targetFmt.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedValue;

    }
}
