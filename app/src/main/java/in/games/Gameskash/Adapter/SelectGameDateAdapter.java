package in.games.Gameskash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.Gameskash.Model.SelectGameDateModel;
import in.games.Gameskash.R;

public class SelectGameDateAdapter extends RecyclerView.Adapter<SelectGameDateAdapter.ViewHolder> {
    Context context;
    ArrayList<SelectGameDateModel> list;

    public SelectGameDateAdapter(Context context, ArrayList<SelectGameDateModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SelectGameDateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_showdate,null);
        return new SelectGameDateAdapter.ViewHolder (view);    }

    @Override
    public void onBindViewHolder(@NonNull SelectGameDateAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
