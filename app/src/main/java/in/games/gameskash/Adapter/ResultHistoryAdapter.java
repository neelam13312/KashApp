package in.games.gameskash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gameskash.Model.StarlineResultModel;
import in.games.gameskash.R;

public class ResultHistoryAdapter extends RecyclerView.Adapter<ResultHistoryAdapter.ViewHolder> {
    ArrayList<StarlineResultModel> list;
    Context context;

    public ResultHistoryAdapter(ArrayList<StarlineResultModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_result,null);
        return new ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_time.setText(list.get(i).getTime());
        viewHolder.tv_result.setText(list.get(i).getStarting_num()+"-"+list.get(i).getResult_num());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time,tv_result;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_result = itemView.findViewById(R.id.tv_result);
            tv_time = itemView.findViewById(R.id.tv_time);

        }
    }
}

