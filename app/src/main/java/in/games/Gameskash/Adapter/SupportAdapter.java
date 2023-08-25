package in.games.Gameskash.Adapter;

import static in.games.Gameskash.Config.BaseUrls.BASE_IMAGE_URL;
import static in.games.Gameskash.Config.Constants.KEY_ID;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Model.SupportModel;
import in.games.Gameskash.R;
import com.squareup.picasso.Picasso;
import in.games.Gameskash.Util.SessionMangement;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.ViewHolder> {
   Context context;
   ArrayList<SupportModel>list;
    String[] myArr;
    Module common;
    SessionMangement sessionMangement;

    public SupportAdapter(Context context, ArrayList<SupportModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SupportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_support_message,null);
        return new SupportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupportAdapter.ViewHolder holder, int position)
    {
        common=new Module (context);
        sessionMangement=new SessionMangement (context);
        Log.e("asxdfgh",list.get(position).getMessage_text());
        SupportModel messageModel = list.get(position);
        myArr = messageModel.getCreated_at().split(" ");
        String date = myArr[0];
        String time= myArr[1];

////        SimpleDateFormat sm = new SimpleDateFormat("mm-dd-yyyy");
//        SimpleDateFormat sm = new SimpleDateFormat("yyyy-mm-dd");
//// myDate is the java.util.Date in yyyy-mm-dd format
//// Converting it into String using formatter
//        Log.
//        String strDate = sm.format(date);
//        Log.e("asfdgrthyj",strDate);
////Converting the String back to java.util.Date
////        Date dt = sm.parse(strDate);
//        for (int i =1;i<=messageModel.getSr_no().le)

        RelativeLayout.LayoutParams msg = (RelativeLayout.LayoutParams) holder.tv_recieve.getLayoutParams();
        RelativeLayout.LayoutParams img = (RelativeLayout.LayoutParams) holder.img_recieve.getLayoutParams();

        holder.tv_date.setText(date+", "+common.get24To12Format(time));
        if (messageModel.getFrom().equals("admin")){
            msg.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            img.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            msg.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            img.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            msg.rightMargin = 80;
            msg.leftMargin = 5;

            if (messageModel.getMessage_text().equals("")){

            }else {
                holder.img_recieve.setVisibility(View.GONE);
                holder.tv_recieve.setVisibility(View.VISIBLE);
                holder.tv_recieve.setText(messageModel.getMessage_text());
            }

            if (!messageModel.getMessage_file().equals(""))
            {
                String imageUrl= BASE_IMAGE_URL +messageModel.getMessage_file();
                Log.e("Profile_img_url",imageUrl);

                Picasso.with(context).load(imageUrl).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(holder.img_recieve);
                holder.tv_recieve.setVisibility(View.GONE);
                holder.img_recieve.setVisibility(View.VISIBLE);
            }else {
                holder.img_recieve.setVisibility(View.GONE);
            }

        }
        else if (messageModel.getFrom().equals(sessionMangement.getUserDetails().get(KEY_ID)))
        {
            msg.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            img.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            msg.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            img.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            msg.leftMargin = 80;
            msg.rightMargin = 5;

            if (messageModel.getMessage_text().equals("")){
//                holder.img_recieve.setVisibility(View.GONE);
            }else {
                holder.tv_recieve.setVisibility(View.VISIBLE);
                holder.img_recieve.setVisibility(View.GONE);
                holder.tv_recieve.setText(messageModel.getMessage_text());
            }

            if (!messageModel.getMessage_file().equals(""))
            {
                String imageUrl= BASE_IMAGE_URL+messageModel.getMessage_file();
                Log.e("Profile_img_url",imageUrl);
                Picasso.with(context).load(imageUrl).placeholder(R.drawable.app_logo).error(R.drawable.app_logo).into(holder.img_recieve);
                holder.img_recieve.setVisibility(View.VISIBLE);
                holder.tv_recieve.setVisibility(View.GONE);
            }else {
                holder.img_recieve.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_recieve,tv_date;
        RoundedImageView img_recieve;
        RelativeLayout rel_msg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_recieve = itemView.findViewById(R.id.tv_recieve);
            tv_date = itemView.findViewById(R.id.tv_date);
            img_recieve = itemView.findViewById(R.id.img_recieve);
            rel_msg = itemView.findViewById(R.id.rel_msg);
        }
    }
}
