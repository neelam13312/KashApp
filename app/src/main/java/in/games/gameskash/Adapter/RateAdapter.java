package in.games.gameskash.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import in.games.gameskash.Model.RateModel;
import in.games.gameskash.R;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {
    Context context;
    ArrayList<RateModel> list;
    DecimalFormat df = new DecimalFormat("0.0");

    public RateAdapter(Context context, ArrayList<RateModel> list) {
        this.context = context;
        this.list = list;
        df.setRoundingMode(RoundingMode.DOWN);
    }

    @NonNull
    @Override
    public RateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_rate,null);
        return new RateAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateAdapter.ViewHolder holder, int position) {
        holder.tv_gamename.setText(list.get(position).getName());
//        double range = Double.parseDouble(list.get(position).getRate_range().replace(",",""))/10;
//        double rate = Double.parseDouble(list.get(position).getRate().replace(",",""))/10;

        String range = list.get(position).getRate_range();
        String rate= list.get(position).getRate();
        Log.e("rate_game", "onBindViewHolder: "+rate +"-Ka-"+range );

        holder.tv_gamerate.setText((rate)+" "+context.getString(R.string.ka)+" "+(range));
//        holder.tv_gamerate.setText(list.get(position).getRate());

    }

    @Override
    public int getItemCount() {
        return list.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_gamename,tv_gamerate;
        public ViewHolder(@NonNull View itemView) {
            super (itemView);

            tv_gamename = itemView.findViewById(R.id.tv_gamename);
            tv_gamerate = itemView.findViewById(R.id.tv_gamerate);
        }
    }
}
