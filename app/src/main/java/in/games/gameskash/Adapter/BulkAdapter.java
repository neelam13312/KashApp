package in.games.gameskash.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.games.gameskash.Config.Module;
import in.games.gameskash.Model.TableModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.LoadingBar;

public class BulkAdapter extends RecyclerView.Adapter<BulkAdapter.ViewHolder> {
    List<TableModel> list;
    Context context;
    String number,type="";
    ArrayList<String> removeList;
    LinearLayout linSubmit;
    TextView tv_Bid,tv_Amount;
    Module module;
    LoadingBar loadingBar;

    public BulkAdapter(List<TableModel> list, Context context,String type,ArrayList<String> removeList, TextView tv_Bid,TextView tv_Amount,LinearLayout linSubmit) {
        this.list = list;
        this.context = context;
        this.type = type;
        this.removeList = removeList;
        this.linSubmit = linSubmit;
        this.tv_Amount=tv_Amount;
        this.tv_Bid=tv_Bid;
        module = new Module(context);
        loadingBar = new LoadingBar(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (context).inflate (R.layout.row_add_data, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final TableModel tableModel = list.get(i);
        holder.tv_no.setText(tableModel.getNo());
        holder.tv_game.setText(tableModel.getGame());
        holder.tv_digit.setText(tableModel.getDigits());
        holder.tv_point.setText(tableModel.getPoints());
        holder.tv_type.setText(tableModel.getType());
        if (loadingBar.isShowing()){
            loadingBar.dismiss();
        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.show();
                try {
                    if (list.size ( ) == 1) {
                        tv_Bid.setText ("0");
                        tv_Amount.setText ("0");
                        linSubmit.setVisibility (View.INVISIBLE);
                    } else {
                        linSubmit.setVisibility (View.VISIBLE);
                    }
                    list.remove (i);
                    notifyDataSetChanged ( );
                    loadingBar.dismiss ( );
                    int we = list.size ( );
                    int points = Integer.parseInt (tableModel.getPoints ( ));
                    int tot_pnt = we * points;
                    //   final TableModel tableModel = list.get (i);
                }catch (Exception e)
                {
                    e.printStackTrace ();

                }                            // btn_submit.setText("(BIDS="+we+")(Points="+tot_pnt+")");
            }
        });


//
//        try {
//            for (int num = 0; num < 10; num++) {
//                Log.e("cfvbnm", String.valueOf(num));
//                String number = String.valueOf(num);
//                if (!removeList.contains(number)) {
//                    for (int t = 0; t < list.size(); t++) {
//                        Log.e("987ygbhnju", "defgtyhujik");
//                        if (list.get(t).getSelectedNumber().equals(number)) {
//                            Log.e("sdcfvgbhj", number);
//                            list.remove(t);
////                                   Handler handler = new Handler();
////                                   handler.postDelayed(new Runnable() {
////                                       @Override
////                                       public void run() {
////                                           try {
////                                               notifyDataSetChanged ( );
////                                           }catch (Exception e){
////                                               e.printStackTrace();
////                                           }
////                                       }
////                                   },4000);
//                        }
//                    }
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }




        int we = list.size ( );
        if (we>0){
            linSubmit.setVisibility(View.VISIBLE);
        }else {
            linSubmit.setVisibility(View.INVISIBLE);
        }
        int tot_pnt = Integer.parseInt (module.getSumOfPoints (list));
        tv_Bid.setText(String.valueOf(we));
        tv_Amount.setText(String.valueOf(tot_pnt));



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
             tv_no = (TextView) itemView.findViewById (R.id.tv_no);
             tv_game = (TextView) itemView.findViewById (R.id.tv_game);
             tv_digit = (TextView) itemView.findViewById (R.id.tv_digit);
             tv_point = (TextView) itemView.findViewById (R.id.tv_point);
             tv_type = (TextView) itemView.findViewById (R.id.tv_type);
             btn_delete = (Button) itemView.findViewById (R.id.btn_delete);
        }
    }
}
