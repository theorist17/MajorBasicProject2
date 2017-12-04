package org.androidtown.MajorBasicProject2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Hongin Lee on 2017-11-29.
 */

public class HandlerImage extends AsyncTask<String,Void,Bitmap> {

    public DBResponse dbResponse = null;
    public String log = null;

    String serverUrl = "http://1.240.26.183/";
    String phpFileName = "download_image.php";

    public interface DBResponse {
        void onResepones(Bitmap bmpResult);
    }

    public void setDbResponse(DBResponse dbResponse) {
        this.dbResponse = dbResponse;
    }

    @Override
    protected Bitmap doInBackground(String... id_image) {
        URL url = null;
        Bitmap image = null;
        try {
            url = new URL(serverUrl+phpFileName+"?id_image="+id_image[0]);
            InputStream inputStream = url.openConnection().getInputStream();
            image = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap output) {
        super.onPostExecute(output);
        dbResponse.onResepones(output);
    }
}