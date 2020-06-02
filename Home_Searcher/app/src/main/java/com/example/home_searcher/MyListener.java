package com.example.home_searcher;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;

public class MyListener implements View.OnClickListener {
    boolean state;
    Context context;
    Long userId;
    Long houseId;
    Button button;
    JSONObject housedata;

    MyListener(Context context, Long userId, Long houseId, JSONObject housedata, Button button){
        state=false;
        this.context=context;
        this.houseId=houseId;
        this.userId=userId;
        this.button = button;
        this.housedata = housedata;
    }

    public void setRed(View v){
        v.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.redfavicon));
        state=true;
    }

    public void setGrey(View v){
        v.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.greyfavicon));
        state=false;
    }

    public void setState(boolean state){this.state=state;}
    @Override
    public void onClick(View v) {
        if (!state) {
            state=true;
            this.button.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.redfavicon));
            try {
                ((MainActivity)context).addToFavs(this.housedata);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //BODY
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", userId.toString());
            params.put("houseId", houseId.toString());

            String url = context.getResources().getString(R.string.apiAddr) + "/addBookmark";
            System.out.println(url);
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
            state=false;
            this.button.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.greyfavicon));
            ((MainActivity)context).removeFromFavs(houseId);

            String url = context.getResources().getString(R.string.apiAddr) + "/deleteBookmark/userId=" + userId + "&houseId=" + houseId;
            System.out.println(url);
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
}

