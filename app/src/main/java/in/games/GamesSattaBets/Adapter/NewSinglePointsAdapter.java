package in.games.GamesSattaBets.Adapter;

import static in.games.GamesSattaBets.Fragment.AllTable.SingleDigirtFragment.bet_list;
import static in.games.GamesSattaBets.Fragment.AllTable.SingleDigirtFragment.btnReset;
import static in.games.GamesSattaBets.Fragment.AllTable.SingleDigirtFragment.tv_type;

import static in.games.GamesSattaBets.Activity.SplashActivity.max_bet_amount;
import static in.games.GamesSattaBets.Activity.SplashActivity.min_bet_amount;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.games.GamesSattaBets.Config.Module;
import in.games.GamesSattaBets.Model.TableModel;
import in.games.GamesSattaBets.R;


public class NewSinglePointsAdapter extends RecyclerView.Adapter<NewSinglePointsAdapter.ViewHolder> {

    public static ArrayList<TableModel> b_list ;
    List<String> digit_list ;
    Activity activity;
    public static ArrayList<String> ponitsList;
    Module common;

    int tot = 0;
    int index =0 ;
    String beforeTextChangeValue="";

    public static Boolean is_empty = true , is_error = false ;

    public NewSinglePointsAdapter(List<String> digit_list, Activity activity) {
        this.digit_list = digit_list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.row_single_number,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        viewHolder.txt_digits.setText(digit_list.get(i));
        bet_list.clear();

        for(int j=0; j<digit_list.size();j++)
        {
            ponitsList.add("0");
            Log.e ("checktype", "o"+tv_type.getText ().toString ());
            bet_list.add(new TableModel("","",digit_list.get(j).toString(),"0",tv_type.getText ().toString ()));
        }

        Log.d ("hscvsdvcdcv", "onBindViewHolder: "+max_bet_amount);
//        viewHolder.et_points.setFilters(new InputFilter[]{ new InputFilterMinMax(String.valueOf(min_bet_amount), String.valueOf(
//                max_bet_amount))});


        viewHolder.et_points.addTextChangedListener(new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextChangeValue=s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                common.sessionOut();
                if(s.toString().equals("0")){
                    viewHolder.et_points.setText("");
                }

                boolean backSpace = false;
                if (beforeTextChangeValue.length() > s.toString().length()) {
                    backSpace = true;
                }

                if (backSpace) {
                    String pnts = s.toString();
                    deleteFromList(pnts, i, beforeTextChangeValue);
                } else {
                    String points = s.toString();
                    Log.e("points",points);
//                    if (Integer.parseInt(points)<min_bet_amount) {
//                        viewHolder.et_points.setError(activity.getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
////                        common.errorToast(activity.getResources().getString(R.string.min_bet_value)+" "+min_bet_amount);
//                    }else
                        if (Integer.parseInt(points)>max_bet_amount){
                        viewHolder.et_points.setText(beforeTextChangeValue.trim());

                        int pos =  viewHolder.et_points.getText().length();
                        viewHolder.et_points.setSelection(pos);
                    }else {
                        viewHolder.et_points.setError(null);
                        if (!tv_type.getText ().toString ().equalsIgnoreCase("SELECT GAME TYPE")) {
                            addToBetList(points, i);
                        }else {
                            viewHolder.et_points.setText("");
                            common.fieldRequired ("Please Select Game Type");
                        }
                        }
                    }

            }
        });
//

    }

    private void deleteFromList(String pts,int pos,String beforeTextChangeValue) {
        String pnts="";
        if(!pts.isEmpty()){
            pnts=String.valueOf(Integer.parseInt(pts));
        }

        if(!pnts.isEmpty())
        {
            if(tot!=0)
            {


                int tx= Integer.parseInt(pnts);
                int beforeValue=Integer.parseInt(beforeTextChangeValue);
                Log.e("beforeValue",""+beforeTextChangeValue+" - Next Value - " + tx+"- Total Amount"+ tot);
                Log.e("leeeeeee",""+pnts.length());

                if(pnts.length()==1)
                {
                    tot = (tot)-beforeValue;
                }
                else if(pnts.length()==2)
                {
                    tot = (tot+tx)-beforeValue;
                }
                else if(pnts.length() == 3)
                {
                    tot = (tot+tx)-beforeValue;
                }
                else if(pnts.length()==4)
                {

                    tot = (tot+tx)-beforeValue;
                }
                else if(pnts.length()==5)
                {

                    tot = (tot+tx)-beforeValue;
                }else if (pnts.length()==6){

                    tot = (tot+tx)-beforeValue;
                }else if (pnts.length()==7){

                    tot = (tot+tx)-beforeValue;
                }

                btnReset.setText("TOTAL   "+String.valueOf(tot));
                ponitsList.set(pos,"0");
                if(pnts.length()>1)
                {
                    common.updatePoints(bet_list,pos,pnts,tv_type.getText ().toString ());
                }else
                {
                    common.updatePoints(bet_list,pos,"0",tv_type.getText ().toString ());
                }

//


            }
        }

    }

    private void addToBetList(String pts,int pos) {
        String points="";
        if(!pts.isEmpty()){
            points=String.valueOf(Integer.parseInt(pts));

        }
        int p =0;
        if(!points.isEmpty())
        {
            p = Integer.parseInt(points);

        }

        if (points.length() != 0) {

            if (points.isEmpty()) {
                is_empty = true;
            } else {
                is_empty = false;
                int pints = Integer.parseInt(points);
                if ( pints < min_bet_amount) {
                    if(tot==0)
                    {
                        is_error = true;
                    }

                }
                else {
                    int ps = Integer.parseInt(points);
                    Log.e("digit_sfgh",""+points+"pointLength"+points.length());
                    if(points.length()==2)
                    {
                        Log.e("digits2",""+points);
                        tot = tot + ps;
                    }
                    else if(points.length() == 3)
                    {
                        tot = (tot + ps)-Integer.parseInt(bet_list.get(pos).getPoints());
                        Log.e("digits3",""+points);
                    }
                    else if(points.length()==4)
                    {
                        tot = (tot + ps)-Integer.parseInt(bet_list.get(pos).getPoints());

                        Log.e("digits4",""+points);
                    }
                    else if(points.length()==5)
                    {
                        tot = (tot + ps)-Integer.parseInt(bet_list.get(pos).getPoints());

                        Log.e("digits5",""+points);
                    }else if (points.length()==6){
                        tot = (tot + ps)-Integer.parseInt(bet_list.get(pos).getPoints());

                        Log.e("digits6",""+points);
                    }else if (points.length()==7){
                        tot = (tot + ps)-Integer.parseInt(bet_list.get(pos).getPoints());

                        Log.e("digits6",""+points);
                    }

                    is_empty = false;
                    is_error = false;
                    ponitsList.set(pos,String.valueOf(ps));
                    btnReset.setText("TOTAL   "+String.valueOf(tot));

                    common.updatePoints(bet_list,pos,points,tv_type.getText ().toString ());
//                                    bet_list.add(new TableModel(digit_list.get(i), points, txt_type.getText().toString()));

                }


            }
        }
    }


    @Override
    public int getItemCount() {
        return digit_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_digits ;
        EditText et_points;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_digits = itemView.findViewById(R.id.txt_digit);
            et_points = itemView.findViewById(R.id.et_points);
            ponitsList=new ArrayList<>();
            b_list = new ArrayList<>();
            common=new Module (activity);
        }
    }
}

