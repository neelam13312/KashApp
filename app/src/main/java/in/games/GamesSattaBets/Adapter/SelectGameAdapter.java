package in.games.GamesSattaBets.Adapter;

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

import in.games.GamesSattaBets.Model.SelectGameModel;
import in.games.GamesSattaBets.R;

public class SelectGameAdapter extends RecyclerView.Adapter<SelectGameAdapter.ViewHolder> {
    Context context;
    ArrayList<SelectGameModel> list;

    public SelectGameAdapter(Context context, ArrayList<SelectGameModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_select_game,null);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.tv_gameName.setText(list.get(position).getName().toUpperCase ( ));
      SelectGameModel model = list.get(position);

//      if (position%5==0){
//          holder.rel_selectgame.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.pink2)));
//          holder.iv_selectgame.setColorFilter(ContextCompat.getColor(context, R.color.dark_pink));
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.single_digit));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_pink)));
//
//      }
//      else if (position%5==1){
//          holder.rel_selectgame.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr2)));
//          holder.iv_selectgame.setColorFilter(ContextCompat.getColor(context, R.color.dark_orangr));
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.yellow));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr)));
//
//      }
//      else if (position%5==2){
//          holder.rel_selectgame.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_200)));
//          holder.iv_selectgame.setColorFilter(ContextCompat.getColor(context, R.color.purple));
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card3));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_500)));
//
//      }
//      else if (position%5==3){
//          holder.rel_selectgame.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorAccent2)));
//          holder.iv_selectgame.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card2));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
//
//      }
//      else if (position%5==4) {
//          holder.rel_selectgame.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green_light)));
//          holder.iv_selectgame.setColorFilter(ContextCompat.getColor(context, R.color.dark_Green));
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card_dpana));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_Green)));
//
//      }
//      else {
//          holder.rel_selectgame.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.pink2)));
//          holder.iv_selectgame.setColorFilter(ContextCompat.getColor(context, R.color.dark_pink));
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.single_digit));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_pink)));
//
//      }
//

//      if (model.getName().equalsIgnoreCase("SIngle GharT"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.single_digit));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_pink)));
//
//      }else  if (model.getName().equalsIgnoreCase("JODI DIGIT"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.yellow));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.dark_orangr)));
//
//      }else  if (model.getName().equalsIgnoreCase("SINGLE PANA"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card3));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_500)));
//
//      }else  if (model.getName().equalsIgnoreCase("Double Panu"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card2));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
//
//
//      }else  if (model.getName().equalsIgnoreCase("TRIPLE PANA"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card_dpana));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
//
//      }else  if (model.getName().equalsIgnoreCase("HALF SANGAM"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card3));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.purple_500)));
//
//      }else  if (model.getName().equalsIgnoreCase("FULL SANGAM"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.full_sangam));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
//
//      }else  if (model.getName().equalsIgnoreCase("SP Motor"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card5));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
//
//      }else  if (model.getName().equalsIgnoreCase("DP Motor"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.dp_motor));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
//
//      }else  if (model.getName().equalsIgnoreCase("LEFT DIGIT"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.card_dpana));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
//
//      }else  if (model.getName().equalsIgnoreCase("RIGHT DIGIT"))
//      {
//          holder.lin_game.setBackgroundTintList(context.getColorStateList(R.color.single_digit));
//          holder.view.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.green)));
//
//      }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_gameName;
        ImageView iv_selectgame;
        RelativeLayout lin_game,rel_selectgame;
        View view;
        //ImageView img_game;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_gameName = itemView.findViewById(R.id.tv_gameName);
            iv_selectgame= itemView.findViewById(R.id.iv_selectgame);
            rel_selectgame = itemView.findViewById(R.id.rel_selectgame);
            lin_game = itemView.findViewById(R.id.lin_game);
            view = itemView.findViewById(R.id.view);

        }
    }
}
