package in.games.gameskash.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.gameskash.Model.ShowVideoModel;
import in.games.gameskash.R;

public class ShowVideoAdapter  extends RecyclerView.Adapter<ShowVideoAdapter.Viewholder> {
    Context context;
    ArrayList<ShowVideoModel> list;

    public ShowVideoAdapter(Context context, ArrayList<ShowVideoModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ShowVideoAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_show_vedio,null);
        return new ShowVideoAdapter.Viewholder(view);    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ShowVideoAdapter.Viewholder holder, int position) {
        ShowVideoModel model = list.get(position);
        holder.tv_title.setText (model.getTitle ());
        holder.tv_description.setText (model.getDescription ());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tv_description,tv_title;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tv_description=itemView.findViewById (R.id.tv_description);
            tv_title=itemView.findViewById (R.id.tv_title);


        }
    }
}
