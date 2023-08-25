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

import in.games.Gameskash.Model.SingleDigitNumberModel;
import in.games.Gameskash.R;

public class SingleDigitNumberAdpter extends RecyclerView.Adapter<SingleDigitNumberAdpter.ViewHolder> {
   Context context;
   ArrayList<SingleDigitNumberModel> list;

    public SingleDigitNumberAdpter(Context context, ArrayList<SingleDigitNumberModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SingleDigitNumberAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_single_numberdigit,null);
        return new SingleDigitNumberAdpter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleDigitNumberAdpter.ViewHolder holder, int position) {
        SingleDigitNumberModel  model = list.get(position);
       holder.tv_number.setText(model.getNumber ());

       //holder.tv_point.setText ("point");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_point;
        TextView tv_point,tv_number;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_point=itemView.findViewById(R.id.lin_point);
            tv_point=itemView.findViewById(R.id.tv_point);
            tv_number=itemView.findViewById(R.id.tv_number);
        }
    }
}
