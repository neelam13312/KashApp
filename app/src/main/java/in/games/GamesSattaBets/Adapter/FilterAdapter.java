package in.games.GamesSattaBets.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.games.GamesSattaBets.Model.FilterModel;
import in.games.GamesSattaBets.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    Context context;
    ArrayList<FilterModel> list;
    //ArrayList<MatkaModel> list;
   // ArrayList<String> list;
    OnChoiceListener listener;

    public FilterAdapter(Context context, ArrayList<FilterModel> list, OnChoiceListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    //    public FilterAdapter(Context context, ArrayList<MatkaModel> list) {
//        this.context = context;
//        this.list = list;
//    }

    public  interface OnChoiceListener{
       void onSelection(int position,String name);
        void onRemove(int position,String name);
    }

    @NonNull
    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_filer_dailoge, null);
        return new FilterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterAdapter.ViewHolder holder, int position) {
        int adapterPosition=position;
        //MatkaModel model = list.get(adapterPosition);
        FilterModel model=list.get(adapterPosition);
        holder.cb_game.setText(model.getGame());
        if (model.isSelected()){
            holder.cb_game.setChecked(true);
        }else{
            holder.cb_game.setChecked(false);
        }
       holder.cb_game.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   model.setSelected(true);
                   listener.onSelection(adapterPosition,model.getGame());

               }else{
                   model.setSelected(false);
                   listener.onRemove(adapterPosition, model.getGame());

               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_game;
        CheckBox cb_game;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_game = itemView.findViewById(R.id.lin_game);
            cb_game = itemView.findViewById(R.id.cb_game);

        }
    }

    public ArrayList<String> getSelectedGameType(){
        ArrayList<String>nameList=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            FilterModel model=list.get(i);
            if (model.isSelected()){
                nameList.add(model.getGame());
            }
        }
        return nameList;

    }
}
