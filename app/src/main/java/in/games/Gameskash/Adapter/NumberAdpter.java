package in.games.Gameskash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.Gameskash.Model.NumberModel;
import in.games.Gameskash.R;

public class NumberAdpter extends RecyclerView.Adapter<NumberAdpter.ViewHolder> {
   Context context;
   ArrayList<NumberModel> list;

    public NumberAdpter(Context context, ArrayList<NumberModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NumberAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_number,null);
        return new NumberAdpter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberAdpter.ViewHolder holder, int position) {
        NumberModel  model = list.get(position);
holder.tv_point.setText(model.getNumber ());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_point;
        TextView tv_point;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_point=itemView.findViewById(R.id.lin_point);
            tv_point=itemView.findViewById(R.id.tv_point);
        }
    }
}
