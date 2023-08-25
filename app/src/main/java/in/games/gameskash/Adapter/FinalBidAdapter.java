package in.games.gameskash.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.games.gameskash.Model.TableModel;
import in.games.gameskash.R;

public class FinalBidAdapter extends RecyclerView.Adapter<FinalBidAdapter.ViewHolder> {
    List<TableModel> list;
    Context context;
    String number;
    public FinalBidAdapter(List<TableModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FinalBidAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_add_data,null);
        return new FinalBidAdapter.ViewHolder (view);
    }

        public Object getItem(int i) {
            return list.get (i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

                @Override
    public void onBindViewHolder(@NonNull FinalBidAdapter.ViewHolder holder, int position) {
                    final TableModel tableModel = list.get (position);
                    Log.e("TableModel", "onBindViewHolder: "+holder.tv_digit );
                        holder.tv_no.setText(tableModel.getNo());
                        holder.tv_game.setText(tableModel.getGame());
                        holder.tv_digit.setText(tableModel.getDigits());
                        holder.tv_point.setText(tableModel.getPoints());
                        holder.tv_type.setText(tableModel.getType());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_no,tv_game,tv_digit,tv_point,tv_type;
        Button btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_no = (TextView) itemView.findViewById(R.id.tv_no);
            tv_game = (TextView) itemView.findViewById(R.id.tv_game);
            tv_digit = (TextView) itemView.findViewById(R.id.tv_digit);
            tv_point = (TextView) itemView.findViewById(R.id.tv_point);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
            btn_delete.setVisibility(View.GONE);
        }
    }
}