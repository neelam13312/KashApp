package in.games.Gameskash.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


import in.games.Gameskash.Config.Module;
import in.games.Gameskash.Config.PicassoImageGetter;
import in.games.Gameskash.Model.NotifyModel;
import in.games.Gameskash.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    ArrayList<NotifyModel> list;
    Module module;

    public NotificationAdapter(Context context, ArrayList<NotifyModel> list) {
        this.context = context;
        this.list = list;
        module = new Module(context);
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_notification,null);
        return new NotificationAdapter.ViewHolder (view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotifyModel postion=list.get(position);

        String info = postion.getNotification();
        Log.e ("imggg", "onBindViewHolder: "+info );

//        PicassoImageGetter imageGetter = new PicassoImageGetter ( holder.tv_info,context);
//        Spannable html;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//            html = (Spannable) Html.fromHtml (info, Html.FROM_HTML_MODE_LEGACY, imageGetter, null);
//
//        } else {
//            html = (Spannable) Html.fromHtml (info, imageGetter, null);
//
//        }

        holder.tv_info.setText (module.htmlString(postion.getNotification()));

        //holder.tv_info.setText(Html.fromHtml(postion.getNotification()));
        holder.tv_title.setText(postion.getTitle());
        Log.e("tv_title", "onBindViewHolder: "+postion.getTitle());

        String dateTime = postion.getTime();
        String date = dateTime.substring(0,10);
        String time = module.get24To12Format(dateTime.substring(11,19));
//        Log.e("time",dateTime.substring(11,19));
        holder.tv_time.setText(date+" "+time);


//
//        switch (position%3)
//        {
//            case 0: holder.tv_title.setBackgroundColor(context.getResources().getColor(R.color.card2));
//                break;
//            case 1: holder.tv_title.setBackgroundColor(context.getResources().getColor(R.color.card7));
//                break;
//            case 2: holder.tv_title.setBackgroundColor(context.getResources().getColor(R.color.card_pana));
//                break;        }

    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_info,tv_time;
        CardView card_notification;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);
            card_notification = itemView.findViewById(R.id.card_notification);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_info = itemView.findViewById(R.id.tv_info);
        }
    }
}
