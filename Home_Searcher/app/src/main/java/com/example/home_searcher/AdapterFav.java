package com.example.home_searcher;

import android.content.Context;
import android.view.View;

import com.example.home_searcher.Adapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class AdapterFav extends Adapter {

    public AdapterFav(Context context, List<JSONObject> data, Long id, List<JSONObject> favHouses){
        super(context, data, id, favHouses);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ratingBar.setVisibility(View.INVISIBLE);

        String title = null;
        String price = null;
        Long houseid = null;

        //READ VALS
        try {
            title = data.get(i).get("houseName").toString();
            price = data.get(i).get("pricePerNight").toString();
            houseid = Long.parseLong(data.get(i).get("id").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //FAV Houses BUTTON
        final Long finalHouseid = houseid;


        MyListener listener = new MyListener(context, userId, houseid, data.get(i), viewHolder.toggleButton);
        viewHolder.toggleButton.setOnClickListener(listener);

        //Set color
        viewHolder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.greyfavicon));
        listener.setState(false);

        viewHolder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.redfavicon));
        listener.setState(true);

        //other stats
        viewHolder.textTitle.setText(title);
        viewHolder.textPrice.setText(price + "€");

    }
}
