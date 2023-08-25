package in.games.gameskash.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import in.games.gameskash.Activity.MainActivity;
import in.games.gameskash.Activity.StarlineResultHistoryActivity;
import in.games.gameskash.Adapter.FundAdapter;
import in.games.gameskash.Model.FundModel;
import in.games.gameskash.R;
import in.games.gameskash.Util.RecyclerTouchListener;

public class MyBidsFragment extends Fragment {
RecyclerView rec_myBid;
ArrayList<FundModel> modellist;
FundAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_bids, container, false);
        ((MainActivity)getActivity()).setTitles("History");
        rec_myBid = view.findViewById(R.id.rec_myBid);
        rec_myBid.setHasFixedSize(true);
        rec_myBid.setLayoutManager(new LinearLayoutManager(getActivity()));
        initRecyclerview();
        return view;
    }
    private void initRecyclerview() {

        modellist= new ArrayList<>();
        modellist.add(new FundModel(R.drawable.my_bids,"My Bid History"));
        modellist.add(new FundModel(R.drawable.my_bids,"Starline Bid History"));
        modellist.add(new FundModel(R.drawable.my_bids,"Starline Result History"));
        modellist.add(new FundModel(R.drawable.rupee,"Fund Request History"));
//        modellist.add(new FundModel(R.drawable.credit_history,"Approved Credit History"));
//        modellist.add(new FundModel(R.drawable.debit_history,"Approved Debit History"));

        adapter = new FundAdapter(getActivity(),modellist);
        rec_myBid.setAdapter(adapter);

//        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
//        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
////                if (position % 4 == 2) {
////                    return 2;
////                }
//                switch (position %3) {
//                    case 0:
//                    case 3:
//                        return 2;
//                    case 1:
//                    case 2:
//                        return 1;
//                    default:
//                        //never gonna happen
//                        return  -1 ;
//                }
//            }
//        });
//
//        rec_myBid.setLayoutManager (glm);

        rec_myBid.addOnItemTouchListener (new RecyclerTouchListener(getActivity ( ), rec_myBid, new RecyclerTouchListener.OnItemClickListener ( ) {
            @Override
            public void onItemClick(View view, int position) {
                FundModel model = modellist.get(position);
                String title = modellist.get(position).getTitle();
                Fragment fragment = null;
                Intent intent=null;
                switch (title) {

                    case "My Bid History":
                         intent=new Intent ( getActivity (),AllHistoryActivity.class );
                        intent.putExtra ("name","mList.get(position).getName()");
                        intent.putExtra ("type","matka");
                        intent.putExtra ("matka_id","0");

                        break;

                    case "Starline Bid History":
                         intent=new Intent ( getActivity (),AllHistoryActivity.class );
                        intent.putExtra ("name","mList.get(position).getName()");
                        intent.putExtra ("type","starline");
                        intent.putExtra ("matka_id","0");
                        break;

                    case "Starline Result History":
//                        fragment = new WithdrawFundHistoryFragment();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        fragmentManager.beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                        intent=new Intent ( getActivity (), StarlineResultHistoryActivity.class );
//                        intent.putExtra ("name","mList.get(position).getName()");
//                        intent.putExtra ("type","starline_win");
//                        intent.putExtra ("matka_id","0");

                        break;

                    case "Fund Request History":
                        intent = new Intent(getActivity(),WithdrawFundHistoryFragment.class);
                        intent.putExtra("name","a");
                        intent.putExtra ("status","");



                        break;

//
//                    case "Approved Credit History":
//                        intent = new Intent(getActivity(),WithdrawFundHistoryFragment.class);
//                        intent.putExtra("name","a");
//                        intent.putExtra ("status","approved");
////                        fragment = new ProfileFragment();
////                        Intent in_add= new Intent(getActivity(), AddBankAccountActivity.class);
////                        startActivity(in_add);
//
//                        break;
//                    case  "Approved Debit History":
//                        intent = new Intent(getActivity(),WithdrawFundHistoryFragment.class);
//                        intent.putExtra("name","w");
//                        intent.putExtra ("status","approved");
////                        fragment = new ProfileFragment();
////                        Intent in_add= new Intent(getActivity(), AddBankAccountActivity.class);
////                        startActivity(in_add);
//                        break;


                }
//             fragment.setArguments(bundle);
                if (intent!=null)
                {
                    startActivity (intent);
                }


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));





    }

}