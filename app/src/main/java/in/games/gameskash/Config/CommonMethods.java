package in.games.gameskash.Config;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommonMethods {

    public static String convertToBase64String(Uri uri, Context context){
        String convertedString=null;
        String uriString = uri.toString();
        Log.e("base_data", "onActivityResult: uri"+uriString);
        //            myFile = new File(uriString);
        //            ret = myFile.getAbsolutePath();
        //Fpath.setText(ret);
        try {
            InputStream in = context.getContentResolver().openInputStream(uri);
            byte[]bytes=getBytes(in);
            Log.e("base64_data", " bytes size="+bytes.length);
            // Log.e("base64_data", " Base64string="+ Base64.encodeToString(bytes,Base64.DEFAULT));
            convertedString = Base64.encodeToString(bytes,Base64.DEFAULT);
            // Document=Base64.encodeToString(bytes,Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "onActivityResult: " + e.toString());
        }
        return convertedString;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
    public static String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
        return format.format(Calendar.getInstance().getTime());
    }

    public static  String changeDateTimeFmt(String orgFmt,String tgtFmt,String date_time){
        String convertedValue="";
        try {
        DateFormat originalFmt=new SimpleDateFormat(orgFmt, Locale.getDefault());
        DateFormat targetFmt=new SimpleDateFormat(tgtFmt);
        Date date= originalFmt.parse(date_time);
        convertedValue=targetFmt.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedValue;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkRecordPermission(Context context){
            if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                return false;
            }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestRecordPermission(Activity activity, int requestCode){
        activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                requestCode);
    }

}
