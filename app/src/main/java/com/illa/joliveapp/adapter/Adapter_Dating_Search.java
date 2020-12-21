package com.illa.joliveapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.illa.joliveapp.BuildConfig;
import com.illa.joliveapp.R;
import com.illa.joliveapp.datamodle.dating.DatingSearchUser;
import com.illa.joliveapp.tools.ImgHelper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class Adapter_Dating_Search extends ArrayAdapter<DatingSearchUser> {
    Context mContext;


    public Adapter_Dating_Search(@NonNull Context context, int resource, List<DatingSearchUser> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NotNull
    public View getView(final int position, View convertView, @NotNull ViewGroup parent) {
        final DatingSearchUser card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_dating_search, parent, false);
        }


        TextView name = (TextView) convertView.findViewById(R.id.tv_chat_room_name);
        ImageView image = (ImageView) convertView.findViewById(R.id.img_photo);
        String imgUrl = "";
        assert card_item != null;
        if(card_item.getPhotos() != null && card_item.getPhotos().size() != 0){
            imgUrl = BuildConfig.IMAGE_URL+card_item.getPhotos().get(0).getUrl();
        }



        imgUrl = "https://static.raccoontv.com/smtv//uploads/banner/19/6Ul4J8zah40BAUZn.jpeg";
//        imgUrl = "http://live.illa.me/images/ff36e62eee53304476677b193de905df.jpg";
        Log.e("Peter","IMAGE  URL"+imgUrl+"KKKK");

        ImgHelper.INSTANCE.loadNormalImg(mContext, imgUrl, image);


        String finalImgUrl = imgUrl;
        new Thread(new Runnable() {
            @Override
            public void run() {

                InputStream is = null;
                try {
                    Log.e("Peter","IMAGE  URL"+ finalImgUrl);

                    is = (InputStream) new URL(finalImgUrl).getContent();
                } catch (IOException e) {
                    Log.e("Peter","InputStream   IOException"+e);

                    e.printStackTrace();
                }
                Drawable d = Drawable.createFromStream(is, "src name");
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        image.setImageDrawable(d);
                    }
                });
            }
        }).start();


        name.setText(card_item.getName());

        return convertView;
    }

}
