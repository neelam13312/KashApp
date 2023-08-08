package in.games.GamesSattaBets.Adapter;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import in.games.GamesSattaBets.Config.BaseUrls;
import in.games.GamesSattaBets.Config.CommonMethods;
import in.games.GamesSattaBets.Model.SupportModel;
import in.games.GamesSattaBets.R;
import nl.changer.audiowife.AudioWife;

public class SupportChatAdapter extends RecyclerView.Adapter<SupportChatAdapter.ChatHolder> {
    Context context;
    ArrayList<SupportModel>messageList;
    String previous_date="";
    String fromId;
    OnChatMessageClickListener listener;
    boolean isAudioPlaying=false;
    MediaPlayer mediaPlayer;
    AudioWife audioWife;
    boolean isImageFitToScreen;

    public SupportChatAdapter(Context context, ArrayList<SupportModel> messageList, String fromId,
                              OnChatMessageClickListener listener) {
        this.context = context;
        this.messageList = messageList;
        this.fromId = fromId;
        this.listener = listener;
    }

    public  interface OnChatMessageClickListener{
        //add and call methods to handle listener in support activity class here
        void onImageClick(int position,SupportModel model);
    }

    @NonNull
    @Override
    public SupportChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(context).inflate(R.layout.row_chat_message_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SupportChatAdapter.ChatHolder holder, int position) {

        int adapterPosition=position;
        Log.e("psition","ps "+adapterPosition);
        SupportModel model=messageList.get(adapterPosition);
        String date=model.getDate();


        //--set today and date wise message together--//

        //if (TextUtils.isEmpty(previous_date)) {
            if (CommonMethods.getCurrentDate().equals(date)) {
                holder.tv_date.setText("Today");
                previous_date = date;
            } else {
                holder.tv_date.setText(date);
                previous_date = date;
            }
//        }
//        else{
//            try {
//               if (date.equals (previous_date)) {
//                    holder.tv_date.setVisibility (View.GONE);
//                } else {
////                    if (CommonMethods.getCurrentDate ( ).equals (date)) {
////                        holder.tv_date.setText ("Today");
////                        // previous_date = date;
////                    } else {
////                        holder.tv_date.setText (date);
////                    }
//                    holder.tv_date.setVisibility (View.VISIBLE);
//                    previous_date = date;
//                }
//            }
//            catch (Exception e)
//            {
//
//            //}
//        }


        //--set parent layout left right according user--//
        Log.e("fromid",""+fromId);
        if (model.getFrom().equals(fromId)){
            Log.e("fromid","from id matched");
//            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.rel_parent.getLayoutParams();
//            params.removeRule(RelativeLayout.ALIGN_PARENT_END);
//            params.addRule(RelativeLayout.ALIGN_PARENT_START);
//            holder.rel_parent.setLayoutParams(params);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                 LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            holder.lin_msg.setLayoutParams(params);

            holder.tv_text_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            // holder.lin_text_msg.setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
            //holder.tv_text_msg.setTextColor(Color.BLACK);
            // holder.tv_teacher_msg.setText(model.getMessage_text());
        }else{
            Log.e("fromid","from id not matched");
//            RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.rel_parent.getLayoutParams();
//            params.removeRule(RelativeLayout.ALIGN_PARENT_START);
//            params.addRule(RelativeLayout.ALIGN_PARENT_END);
//            holder.rel_parent.setLayoutParams(params);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            holder.lin_msg.setLayoutParams(params);
            holder.tv_text_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            //  holder.tv_teacher_msg.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            //holder.tv_text_msg.setTextColor(Color.WHITE);
            // holder.tv_teacher_msg.setText(model.getMessage_text());



        }

        //--set message depending on type---//
        switch (model.getMessage_type()){
            case "Text":
                //for text type message
                Log.e("text","at "+adapterPosition);
                holder.lin_text_msg.setVisibility(View.VISIBLE);
                holder.rel_image_msg.setVisibility(View.GONE);
                holder.lin_audio_msg.setVisibility(View.GONE);
                holder.tv_text_msg.setText(Html.fromHtml(model.getMessage_text()));
                holder.tv_txt_time.setText(model.getTime());
            break;
            case "File":
                //for image type message
                Log.e("img","at "+adapterPosition);
                holder.lin_text_msg.setVisibility(View.GONE);
                holder.rel_image_msg.setVisibility(View.VISIBLE);
                holder.lin_audio_msg.setVisibility(View.GONE);
                Log.e("url",BaseUrls.BASE_IMAGE_URL+model.getMessage_file());
                Picasso.with(context).load(BaseUrls.BASE_URL_MEDIA+model.getMessage_file())
                        .placeholder(R.drawable.app_logo).into(holder.civ_image);
                holder.tv_img_time.setText(model.getTime());
                break;
            case "Audio":

                //for audio type message
                Log.e("audio","at "+adapterPosition);
                holder.lin_text_msg.setVisibility(View.GONE);
                holder.rel_image_msg.setVisibility(View.GONE);
                holder.lin_audio_msg.setVisibility(View.VISIBLE);
                setAudioMessage(holder,model);
                break;
        }

        //-- set listener on any item here--//

        holder.civ_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //manage play pause here by adding method in interface or in prent class
                Log.e("play audio","true");
//                playAudioFile(holder,model);

                fullControl(holder.civ_play_pause,holder.iv_pause_btn,holder.seekBar,BaseUrls.BASE_URL_MEDIA+model.getAudio_file());
            }
        });

//holder.civ_image.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        if(isImageFitToScreen) {
//            isImageFitToScreen=false;
//            holder.civ_image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            holder.civ_image.setAdjustViewBounds(true);
//        }else{
//            isImageFitToScreen=true;
//            holder.civ_image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//            holder.civ_image.setScaleType(ImageView.ScaleType.FIT_XY);
//        }
//
//    }
//});
        holder.civ_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //manage here view image type message logic here by adding method in interface

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow();
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                // dialog.getWindow().getAttributes().windowAnimations = R.style.DailoAnimation;
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.show();

                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dailoge_chatimage);
                ImageView div_chatimage = (ImageView) dialog.findViewById(R.id.div_chatimage);
                ImageView iv_close = (ImageView) dialog.findViewById(R.id.iv_close);

                Picasso.with(context).load(BaseUrls.BASE_URL_MEDIA+model.getMessage_file())
                        .placeholder(R.drawable.app_logo).into(div_chatimage);
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }

        });
    }

    private void playAudioFile(ChatHolder holder, SupportModel model) {
        Log.e("sdfsdefrtyu", String.valueOf(isAudioPlaying));
        if (isAudioPlaying){
            stopPalying(holder,model);

        }else{
            mediaPlayer=new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                Picasso.with(context).load(R.drawable.pause_button).into(holder.civ_play_pause);
                isAudioPlaying=true;
                mediaPlayer.setDataSource(BaseUrls.BASE_URL_MEDIA+model.getAudio_file());
                Log.e("sdefrgt",BaseUrls.BASE_URL_MEDIA+model.getAudio_file());
                mediaPlayer.prepare();
                mediaPlayer.start();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void stopPalying(ChatHolder holder, SupportModel model) {
       try {
           if (mediaPlayer.isPlaying()){
               Picasso.with(context).load(R.drawable.c_play).into(holder.civ_play_pause);
               isAudioPlaying=false;
               mediaPlayer.stop();
               mediaPlayer.reset();
               mediaPlayer.release();

           }else {
               Picasso.with(context).load(R.drawable.c_play).into(holder.civ_play_pause);
               isAudioPlaying=false;
               mediaPlayer.stop();
               mediaPlayer.reset();
               mediaPlayer.release();
           }
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    private void setAudioMessage(ChatHolder holder, SupportModel model) {
        String audioFileUrl=model.getMessage_file();
        holder.tv_audio_time.setText(model.getTime());
        if (fromId.equals(model.getFrom())){
            //set sender audio msg
            holder.rel_sender.setVisibility(View.VISIBLE);
            holder.rel_receiver.setVisibility(View.GONE);
            //--set sender profile image by setting url in supportmodel class--//
//            Picasso.with(context).load("place url").placeholder(R.drawable.profile)
//                    .into(holder.civ_sender_img);

        }else{
            //set receiver side audio msg
            holder.rel_sender.setVisibility(View.GONE);
            holder.rel_receiver.setVisibility(View.VISIBLE);
            //--set sender profile image by setting url in supportmodel class--//
//            Picasso.with(context).load("place url").placeholder(R.drawable.profile)
//                    .into(holder.civ_receiver_img);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        RelativeLayout rel_image_msg,rel_sender,rel_receiver,lin_msg;
        LinearLayout  rel_parent,lin_text_msg,lin_audio_msg;
        TextView tv_date,tv_text_msg,tv_txt_time,tv_img_time,tv_audio_time,tv_duration;
        CircleImageView civ_sender_img,civ_receiver_img,civ_play_pause,iv_pause_btn;
        RoundedImageView civ_image;
        SeekBar seekBar;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

           lin_msg=itemView.findViewById(R.id.rel_msg);
                    rel_parent=itemView.findViewById(R.id.lin_parent);

            rel_image_msg=itemView.findViewById(R.id.lin_image);
            rel_sender=itemView.findViewById(R.id.rel_sender);
            rel_receiver=itemView.findViewById(R.id.rel_receiver);

            lin_text_msg=itemView.findViewById(R.id.lin_tex_mgs);
            lin_audio_msg=itemView.findViewById(R.id.lin_audio_mgs);

            tv_date=itemView.findViewById(R.id.tv_date);
            tv_text_msg=itemView.findViewById(R.id.tv_recieve);
            tv_txt_time=itemView.findViewById(R.id.tv_txt_time);
            tv_img_time=itemView.findViewById(R.id.tv_img_time);
            tv_audio_time=itemView.findViewById(R.id.tv_audio_time);
            tv_duration=itemView.findViewById(R.id.tv_duration);
            civ_image=itemView.findViewById(R.id.img_recieve);
            civ_play_pause=itemView.findViewById(R.id.iv_play_btn);
            civ_sender_img=itemView.findViewById(R.id.civ_sender);
            civ_receiver_img=itemView.findViewById(R.id.civ_receiver);
            iv_pause_btn=itemView.findViewById(R.id.iv_pause_btn);
            seekBar=itemView.findViewById(R.id.seekbar);
        }
    }
    public void fullControl(CircleImageView civ_play_pause,CircleImageView iv_pause_btn,SeekBar seekBar,String uri)
    {
        audioWife= AudioWife.getInstance();
        audioWife.init(context, Uri.parse(uri))
                .setPlayView(civ_play_pause)
                .setPauseView(iv_pause_btn)
                .setSeekBar(seekBar);
//                .setRuntimeView(duration_time)
//                .setTotalTimeView(total_time);

        audioWife.play();
    }
}
