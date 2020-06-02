package com.example.home_searcher;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    List<JSONObject> data;
    Context context;
    Long userId;
    List<JSONObject> favHouses;

    public Adapter(Context context, List<JSONObject> data, Long userId, List<JSONObject> favHouses){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
        this.context=context;
        this.userId = userId;
        this.favHouses = favHouses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        // bind the textview with data received
        String title = null;
        String price = null;
        Float rating = null;
        Long houseid = null;

        //READ VALS
        try {
            title = data.get(i).get("houseName").toString();
            price = data.get(i).get("pricePerNight").toString();
            rating = Float.parseFloat(data.get(i).get("rating").toString());
            houseid = Long.parseLong(data.get(i).get("houseId").toString());
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
        for (JSONObject house : favHouses){
            try {
                if (house.get("id").toString().equals(finalHouseid.toString())){
                    viewHolder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.redfavicon));
                    listener.setState(true);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //other stats
        viewHolder.textTitle.setText(title);
        viewHolder.textPrice.setText(price + "€");
        viewHolder.ratingBar.setRating(rating);

        // similarly you can set new image for each card and descriptions

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle,textPrice;
        RatingBar ratingBar;
        Button toggleButton;
        Long userId;
        Long houseId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(),Details.class);
                    i.putExtra("title",data.get(getAdapterPosition()));
                    v.getContext().startActivity(i);
                }
            });*/
            textTitle = itemView.findViewById(R.id.title);
            textPrice = itemView.findViewById(R.id.priceText);
            ratingBar = itemView.findViewById(R.id.rating);
            toggleButton = itemView.findViewById(R.id.myToggleButton);

            /*
            this.clickListner= new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.redfavicon));

                        //BODY
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userId", userId.toString());
                        params.put("houseId", finalHouseid.toString());

                        String url = context.getResources().getString(R.string.apiAddr) + "/addBookmark";
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(jsonObjectRequest);
                    }
                    else {
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.greyfavicon));

                        String url = context.getResources().getString(R.string.apiAddr) + "/deleteBookmark/userId=" + userId + "&houseId=" + finalHouseid;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                            }
                        });
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(jsonObjectRequest);
                    }
                }



            };
            toggleButton.setOnCheckedChangeListener(clickListner);*/
        }

        public void setUserId(Long userId){ this.userId=userId;}

        public void setHouseId(Long houseId){ this.houseId=houseId;}


    }

}