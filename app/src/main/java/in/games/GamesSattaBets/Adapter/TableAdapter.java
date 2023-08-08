package in.games.GamesSattaBets.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;

public class TableAdapter extends BaseAdapter {
    public  static List<TableModel> list_t;
    Context context;
    String number;
    LinearLayout linSubmit;
    TextView tv_Bid,tv_Amount;
    Module module;
    LoadingBar loadingBar;


    public TableAdapter(List<TableModel> list_t, Context context, TextView tv_Bid,TextView tv_Amount,LinearLayout linSubmit ) {
        this.list_t = list_t;
        this.context = context;
        this.linSubmit = linSubmit;
        this.tv_Amount=tv_Amount;
        this.tv_Bid=tv_Bid;
        module = new Module(context);
        loadingBar = new LoadingBar(context);

    }

    @Override
    public int getCount() {
        return list_t.size ( );
    }

    @Override
    public Object getItem(int i) {
        return list_t.get (i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {


        View itemView = LayoutInflater.from (context).inflate (R.layout.row_add_data, null);
        TextView tv_no = (TextView) itemView.findViewById (R.id.tv_no);
        TextView tv_game = (TextView) itemView.findViewById (R.id.tv_game);
        TextView tv_digit = (TextView) itemView.findViewById (R.id.tv_digit);
        TextView tv_point = (TextView) itemView.findViewById (R.id.tv_point);
        TextView tv_type = (TextView) itemView.findViewById (R.id.tv_type);
        Button btn_delete = (Button) itemView.findViewById (R.id.btn_delete);

        final TableModel tableModel = list_t.get (i);
        {
           // tv_no.setText (num);
            Log.d ("digit_sppsp", "g"+tableModel.getDigits ());
            tv_no.setText (tableModel.getNo ());
            tv_game.setText (tableModel.getGame ( ));
            tv_digit.setText (tableModel.getDigits ( ));
            tv_point.setText (tableModel.getPoints ( ));
            tv_type.setText (tableModel.getType ( ));
            if (loadingBar.isShowing()){
                loadingBar.dismiss();
            }
            btn_delete.setOnClickListener (new View.OnClickListener ( ) {
                @Override
                public void onClick(View view) {
                    loadingBar.show();
                    try {
                        if (list_t.size ( ) == 1) {
                            tv_Bid.setText ("0");
                            tv_Amount.setText ("0");
                            linSubmit.setVisibility (View.INVISIBLE);
                        } else {
                            linSubmit.setVisibility (View.VISIBLE);
                        }
                        list_t.remove (i);

                        notifyDataSetChanged ( );
                        loadingBar.dismiss ( );
                        int we = list_t.size ( );
                        int points = Integer.parseInt (tableModel.getPoints ( ));
                        int tot_pnt = we * points;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace ();
                    }

                 //   final TableModel tableModel = list.get (i);

                    // btn_submit.setText("(BIDS="+we+")(Points="+tot_pnt+")");
                }
            });

            int we = list_t.size ( );
            if (we>0){
                linSubmit.setVisibility(View.VISIBLE);
            }else {
                linSubmit.setVisibility(View.INVISIBLE);
            }
            int tot_pnt = Integer.parseInt (module.getSumOfPoints (list_t));
            tv_Bid.setText(String.valueOf(we));
            tv_Amount.setText(String.valueOf(tot_pnt));
            notifyDataSetChanged ( );

            return itemView;


        }


    }
}
