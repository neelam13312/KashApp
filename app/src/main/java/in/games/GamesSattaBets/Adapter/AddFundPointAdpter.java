package in.games.GamesSattaBets.Adapter;

import static in.games.GamesSattaBets.Fragment.AddFundFragment.minAmount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.GamesSattaBets.R;

public class AddFundPointAdpter extends RecyclerView.Adapter<AddFundPointAdpter.ViewHolder> {
   Context context;
   ArrayList<String> list;

    public AddFundPointAdpter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AddFundPointAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_add_point,null);
        return new AddFundPointAdpter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFundPointAdpter.ViewHolder holder, int position) {
//AddFundPointModel  model = list.get(position);
       holder.tv_point.setText(list.get(position));
        if (Integer.parseInt(list.get(position))<Integer.parseInt(minAmount)) {
         holder.lin_point.setAlpha(0.4f);
        }
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
