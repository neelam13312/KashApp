package in.games.Gameskash.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Model.FundHistoryModel;
import in.games.Gameskash.R;

public class FundHistoryAdapter extends RecyclerView.Adapter<FundHistoryAdapter.ViewHolder> {
    Context context;
    ArrayList<FundHistoryModel> list;
    Module module;

    public FundHistoryAdapter(Context context, ArrayList<FundHistoryModel> list) {
        this.context = context;
        this.list = list;
        module = new Module(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_fund_history,null);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FundHistoryModel model=list.get (position);
        try {
            holder.tv_req_no.setText(module.alphaNumric( list.get(position).getTransaction_id()));
        }catch (Exception ex){
            holder.tv_req_no.setText("");
            ex.printStackTrace();
        }

        if (model.getType().equalsIgnoreCase("Add")){
              holder.txtreq_no.setText("Fund ID");
            Log.e("checktype", "onBindViewHolder: "+model.getType() );
        } else{
            holder.txtreq_no.setText("Withdraw");
        }
        holder.tv_point.setText(list.get(position).getRequest_points());
        holder.tv_status.setText("Your request is "+list.get(position).getRequest_status());
       if(list.get(position).getRequest_status().equalsIgnoreCase ("pending")) {
           holder.tv_status.setCompoundDrawablesWithIntrinsicBounds (R.drawable.icons8_neutral_16px, 0, 0, 0);
           holder.tv_status.setTextColor (context.getResources ().getColor (R.color.dark_orangr));
           holder.tv_status.setCompoundDrawableTintList(context.getResources().getColorStateList(R.color.dark_orangr));

       }
       else if(list.get(position).getRequest_status().equalsIgnoreCase ("approved"))
       {
           holder.tv_status.setTextColor (context.getResources ().getColor (R.color.green));
           holder.tv_status.setCompoundDrawablesWithIntrinsicBounds (R.drawable.icons8_happy_16px, 0, 0, 0);
           holder.tv_status.setCompoundDrawableTintList(context.getResources().getColorStateList(R.color.green));

       }
       else
       {
           holder.tv_status.setTextColor (context.getResources ().getColor (R.color.lightRed));
           holder.tv_status.setCompoundDrawablesWithIntrinsicBounds (R.drawable.icons8_sad_16px, 0, 0, 0);
           holder.tv_status.setCompoundDrawableTintList(context.getResources().getColorStateList(R.color.lightRed));

       }
       String time = list.get(position).getTime();
        String[] dt = time.split(" ");
        String dat = dt[0];
        String tim = dt[1];
        Log.e("check_date",dat+"\n"+tim);

        holder.tv_date.setText(dat+" "+module.get24To12FormatJackport(tim));

        holder.tv_type.setText(list.get(position).getType()+" - ");
//        if(model.getMode ().equalsIgnoreCase ("null")&& model.getMode ()==null)
//        {
//            holder.tv_fundmode.setText ("--");
//        }
//        else {
//            holder.tv_fundmode.setText (model.getMode ( ));
//        }
        if(model.getType ().equalsIgnoreCase ("Add"))
        {
//            if(list.get(position).getTransaction_id ().equals ("null")||
//              list.get(position).getTransaction_id ().equals (null)||
//            list.get(position).getTransaction_id ().isEmpty ())
//            {
//                holder.tv_req_no.setText(list.get(position).getRequest_id());
//            }
//            else
//            {
             //   holder.tv_req_no.setText (list.get (position).getTransaction_id ( ));
           // }
            holder.tv_fundmode.setText ("Credit");
        }
        else {
          // holder.tv_req_no.setText(list.get(position).getRequest_id());
            holder.tv_fundmode.setText ("Debit");
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type,tv_point,tv_status,tv_date,tv_req_no,tv_fundmode,txtreq_no;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type=itemView.findViewById (R.id.tv_type);
            tv_point=itemView.findViewById (R.id.tv_point);
            tv_status=itemView.findViewById (R.id.tv_status);
            tv_date=itemView.findViewById (R.id.tv_date);
            tv_req_no=itemView.findViewById (R.id.tv_req_no);
            tv_fundmode=itemView.findViewById (R.id.tv_fundmode);
            txtreq_no=itemView.findViewById (R.id.txtreq_no);

        }
    }
}
