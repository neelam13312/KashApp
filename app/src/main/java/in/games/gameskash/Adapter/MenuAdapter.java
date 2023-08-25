package in.games.gameskash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gameskash.Model.MenuModel;
import in.games.gameskash.R;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context context;
    ArrayList<MenuModel> list;

    public MenuAdapter(Context context, ArrayList<MenuModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_menu,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MenuModel menuModel = list.get(position);
        String title = menuModel.getTitle();
        if (title.equalsIgnoreCase("Home"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.home));

        }

//        else  if (title.equalsIgnoreCase("My Profile"))
//        {
//            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.profile));
//        }
        else  if (title.equalsIgnoreCase("My Bids"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.my_bids));
        }
//        else  if (title.equalsIgnoreCase("Generate MPIN"))
//        {
//            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.lock));
//        }
        else  if (title.equalsIgnoreCase("MPIN"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.lock));
        }
        else  if (title.equalsIgnoreCase("My History"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.history));
        }else  if (title.equalsIgnoreCase("Account Statement"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.statement));
        }
        else  if (title.equalsIgnoreCase("Support"))

        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.chat));
        }
        else  if (title.equalsIgnoreCase("Funds"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.wallet));
        }else  if (title.equalsIgnoreCase("Notification"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.notification));
        }else  if (title.equalsIgnoreCase("How To Play"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.video));
        }else  if (title.equalsIgnoreCase("Game Rates"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.rate));
        }else  if (title.equalsIgnoreCase("Notice Board/Rules"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.notice));
        }else  if (title.equalsIgnoreCase("Logout"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.signout));
        }
        else if (title.equalsIgnoreCase("Submit Idea"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.idea));

        }
        else if(title.equalsIgnoreCase ("Alert"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.notice));
        }
        else if(title.equalsIgnoreCase ("Support"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.chat));
        }
        else if(title.equalsIgnoreCase ("Videos"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.video));
        }
        else if(title.equalsIgnoreCase ("Charts"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_chart));
        }
        else if(title.equalsIgnoreCase ("Setting"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.settings));
        }
        else if(title.equalsIgnoreCase ("Share Application"))
        {
            holder.img_icon.setImageDrawable(context.getResources().getDrawable(R.drawable.share));
        }


//        holder.img_icon.setImageDrawable(context.getResources().getDrawable(list.get(position).getIcon()));

        holder.tv_title.setText(list.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_icon;
        TextView tv_title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_icon);
            tv_title = itemView.findViewById(R.id.tv_title);
        }
    }
}
