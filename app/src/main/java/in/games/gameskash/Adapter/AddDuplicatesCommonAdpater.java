
package in.games.gameskash.Adapter;

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

import in.games.gameskash.Config.Module;
import in.games.gameskash.Model.TableModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.LoadingBar;

public class AddDuplicatesCommonAdpater extends BaseAdapter {
    List<TableModel> list;
    Context context;
    String number;
    LinearLayout linSubmit;
    TextView tv_Bid,tv_Amount;
    Module module;
    LoadingBar loadingBar;

    public AddDuplicatesCommonAdpater(List<TableModel> list, Context context, TextView tv_Bid,TextView tv_Amount,LinearLayout linSubmit ) {
        this.list = list;
        this.context = context;
        this.linSubmit = linSubmit;
        this.tv_Amount=tv_Amount;
        this.tv_Bid=tv_Bid;
        module = new Module(context);
        loadingBar = new LoadingBar(context);

    }

    @Override
    public int getCount() {
        return list.size ( );
    }

    @Override
    public Object getItem(int i) {
        return list.get (i);
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
            try {
                final TableModel tableModel = list.get(i);
                {
                    tv_no.setText(tableModel.getNo());
                    tv_game.setText(tableModel.getGame());
                    tv_digit.setText(tableModel.getDigits());
                    tv_point.setText(tableModel.getPoints());
                    tv_type.setText(tableModel.getType());
                    if (loadingBar.isShowing()) {
                        loadingBar.dismiss();
                    }
                    btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("bulk_btn_delete", "onClick: "+btn_delete );
                            loadingBar.show();
                            if (list.size() == 1) {
                                tv_Bid.setText("0");
                                tv_Amount.setText("0");
                                linSubmit.setVisibility(View.INVISIBLE);
                            } else {
                                linSubmit.setVisibility(View.VISIBLE);
                            }
                             list.remove(i);

                            notifyDataSetChanged();
                            loadingBar.dismiss();
                            int we = list.size();
                            int points = Integer.parseInt(tableModel.getPoints());
                            int tot_pnt = we * points;
                            //   final TableModel tableModel = list.get (i);

                            // btn_submit.2dsetText("(BIDS="+we+")(Points="+tot_pnt+")");
                        }
                    });

                    for (int p = 0; p < list.size(); p++) {
                        for (int t = p + 1; t < list.size(); t++) {
                            if (list.get(p).getDigits().equals(list.get(t).getDigits())) {
                                list.get(p).setPoints(String.valueOf(Integer.parseInt(list.get(p).getPoints()) + Integer.parseInt(list.get(t).getPoints())));
                                list.remove(list.get(t));
                            }
                        }
                    }

                    int we = list.size();
                    if (we > 0) {
                        linSubmit.setVisibility(View.VISIBLE);
                    } else {
                        linSubmit.setVisibility(View.INVISIBLE);
                    }
                    int tot_pnt = Integer.parseInt(module.getSumOfPoints(list));
                    tv_Bid.setText(String.valueOf(we));
                    tv_Amount.setText(String.valueOf(tot_pnt));

                    return itemView;
                }
            }
            catch( IndexOutOfBoundsException e ) {
                Log.e("error", "Exception");
            }

            return itemView;
        }



}
