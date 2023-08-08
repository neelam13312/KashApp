package in.games.GamesSattaBets.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.GamesSattaBets.Model.VedioLanguageModel;
import in.games.GamesSattaBets.R;

public class VedioLanguageAdapter extends RecyclerView.Adapter<VedioLanguageAdapter.Viewholder> {
    Context context;
    ArrayList<VedioLanguageModel> list;

    public VedioLanguageAdapter(Context context, ArrayList<VedioLanguageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VedioLanguageAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_vedio_language,null);
        return new VedioLanguageAdapter.Viewholder(view);    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull VedioLanguageAdapter.Viewholder holder, int position) {
        VedioLanguageModel model = list.get(position);
        holder.tv_fund.setText(Html.fromHtml(model.getTitle () ));
         holder.iv_fund.setText(Html.fromHtml (model.getSubtitile ()));
        if (position%5==0){
            holder.tv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.blue)));
            holder.iv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.blue)));
            holder.lin_addFund.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.single_digit)));
            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_pink)));

        }else if (position%5==1){
            holder.tv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr)));
            holder.iv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr)));
            holder.lin_addFund.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.yellow)));
            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr)));

        }else if (position%5==2){
            holder.tv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_500)));
            holder.iv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_500)));
            holder.lin_addFund.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.card3)));
            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_500)));

        }else if (position%5==3){
            holder.tv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
            holder.iv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
            holder.lin_addFund.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.card2)));
            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));

        }else if (position%5==4) {
            holder.tv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
            holder.iv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
            holder.lin_addFund.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.card_dpana)));
            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));

        }else {
            holder.tv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_pink)));
            holder.iv_fund.setTextColor(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_pink)));
            holder.lin_addFund.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.single_digit)));
            holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_pink)));

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        RelativeLayout lin_addFund;
        TextView iv_fund;
        TextView tv_fund;
        View  view;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            view=itemView.findViewById (R.id.view);
            lin_addFund= itemView.findViewById(R.id.lin_addFund);
            iv_fund= itemView.findViewById(R.id.iv_fund);
            tv_fund= itemView.findViewById(R.id.tv_fund);
        }
    }
}
