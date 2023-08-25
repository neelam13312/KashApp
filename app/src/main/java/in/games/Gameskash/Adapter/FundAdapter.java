package in.games.Gameskash.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.Gameskash.Model.FundModel;
import in.games.Gameskash.R;


public class FundAdapter extends RecyclerView.Adapter<FundAdapter.Viewholder> {
    Context context;
    ArrayList<FundModel> list;

    public FundAdapter(Context context, ArrayList<FundModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FundAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_fund,null);
        return new FundAdapter.Viewholder(view);    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        FundModel model = list.get(position);
        holder.iv_fund.setImageResource(model.getImage());
        holder.tv_fund.setText(model.getTitle());

//        if (position%5==0){
//            holder.rel_backchange.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.pink2)));
//            holder.iv_fund.setColorFilter(ContextCompat.getColor(context, R.color.dark_pink));
//            holder.lin_addFund.setBackgroundTintList(context.getColorStateList(R.color.single_digit));
//
//
//        }
//        else if (position%5==1){
//            holder.rel_backchange.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr2)));
//            holder.iv_fund.setColorFilter(ContextCompat.getColor(context, R.color.dark_orangr));
//            holder.lin_addFund.setBackgroundTintList(context.getColorStateList(R.color.yellow));
////            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr)));
//
//        }
//        else if (position%5==2){
//            holder.rel_backchange.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_200)));
//            holder.iv_fund.setColorFilter(ContextCompat.getColor(context, R.color.purple));
//            holder.lin_addFund.setBackgroundTintList(context.getColorStateList(R.color.card3));
////            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_500)));
//
//        }
//        else if (position%5==3){
//            holder.rel_backchange.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent2)));
//            holder.iv_fund.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
//            holder.lin_addFund.setBackgroundTintList(context.getColorStateList(R.color.card2));
////            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent)));
//
//        }
//        else if (position%5==4) {
//            holder.rel_backchange.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green_light)));
//            holder.iv_fund.setColorFilter(ContextCompat.getColor(context, R.color.dark_Green));
//            holder.lin_addFund.setBackgroundTintList(context.getColorStateList(R.color.card_dpana));
//
//
//        }
//        else {
//            holder.rel_backchange.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.pink2)));
//            holder.iv_fund.setColorFilter(ContextCompat.getColor(context, R.color.dark_pink));
//            holder.lin_addFund.setBackgroundTintList(context.getColorStateList(R.color.single_digit));
//
//
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        RelativeLayout lin_addFund, rel_backchange;
        ImageView iv_fund;
        TextView tv_fund;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            lin_addFund= itemView.findViewById(R.id.lin_addFund);
            rel_backchange = itemView.findViewById(R.id.rel_backchange);
            iv_fund= itemView.findViewById(R.id.iv_fund);
            tv_fund= itemView.findViewById(R.id.tv_fund);
        }
    }
}
