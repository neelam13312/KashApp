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

import java.util.ArrayList;
import java.util.List;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;
import in.games.GamesSattaBets.Util.LoadingBar;

public class BulkTableAdapter extends BaseAdapter {
        List<TableModel> list;
        Context context;
        String number,type="";
        ArrayList<String> removeList;
        LinearLayout linSubmit;
        TextView tv_Bid,tv_Amount;
        Module module;
        LoadingBar loadingBar;

        public BulkTableAdapter(List<TableModel> list, Context context,String type,ArrayList<String> removeList, TextView tv_Bid,TextView tv_Amount,LinearLayout linSubmit) {
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


//            if (list.size()>0) {

//            }

            {
                // tv_no.setText (num);
                Log.e("dfsdfr", String.valueOf(list.size())+"--"+i);
                if (list.size()>0) {
                    final TableModel tableModel = list.get(i);
                    tv_no.setText(tableModel.getNo());
                    tv_game.setText(tableModel.getGame());
                    tv_digit.setText(tableModel.getDigits());
                    tv_point.setText(tableModel.getPoints());
                    tv_type.setText(tableModel.getType());
                    if (loadingBar.isShowing()){
                        loadingBar.dismiss();
                    }
                    btn_delete.setOnClickListener(new View.OnClickListener() {
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
                }

//                    for (int num = 0; num < 10; num++) {
//                        Log.e("cfvbnm", String.valueOf(num));
//                        String number = String.valueOf(num);
//                        if (!removeList.contains(number)) {
//                            for (int t=0;t<list.size();t++){
//                                Log.e("987ygbhnju", "defgtyhujik");
//                               if (list.get(t).getSelectedNumber().equals(number)){
//                                   Log.e("sdcfvgbhj",number);
//                                   list.remove(t);
//                                   notifyDataSetChanged();
//                               }
//                            }
//                        }
//                    }


                int we = list.size ( );
                if (we>0){
                    linSubmit.setVisibility(View.VISIBLE);
                }else {
                    linSubmit.setVisibility(View.INVISIBLE);
                }
                int tot_pnt = Integer.parseInt (module.getSumOfPoints (list));
                tv_Bid.setText(String.valueOf(we));
                tv_Amount.setText(String.valueOf(tot_pnt));

                return itemView;
            }
        }
    }

