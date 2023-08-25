package in.games.Gameskash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.Gameskash.Model.NoticeBordModel;
import in.games.Gameskash.R;

public class NoticeBordAdapter extends RecyclerView.Adapter<NoticeBordAdapter.Noticeholder> {
  Context context;
  ArrayList<NoticeBordModel> list;

    public NoticeBordAdapter(Context context, ArrayList<NoticeBordModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NoticeBordAdapter.Noticeholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_notice_bord,null);
        return new NoticeBordAdapter.Noticeholder(view);    }

    @Override
    public void onBindViewHolder(@NonNull NoticeBordAdapter.Noticeholder holder, int position) {
      NoticeBordModel model = list.get(position);
      holder.tv_title.setText(model.getTitle());
      holder.tv_wdescription.setText(model.getDiscription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Noticeholder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_wdescription;
        public Noticeholder(@NonNull View itemView) {
            super(itemView);
            tv_title= itemView.findViewById(R.id.tv_title);
             tv_wdescription= itemView.findViewById(R.id.tv_wdescription);
        }
    }
}
