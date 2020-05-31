package com.example.home_searcher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.home_searcher.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorInt;

public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        int blue = Color.parseColor("#063045");
        getWindow().setNavigationBarColor(blue);
        getWindow().setStatusBarColor(blue);

    }

    public void checkLogin(View v) {
        EditText user = findViewById(R.id.usernameEdit);
        EditText pass = findViewById(R.id.passwordEdit);

        String username = user.getText().toString();
        String password = pass.getText().toString();

        //Check
        callApilogin(username, password, new VolleyCallback(){

            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                System.out.println(response);
                long id = Long.valueOf(response.get("userID").toString());
                Intent resultItent = new Intent();
                resultItent.putExtra("id", id);
                setResult(RESULT_OK, resultItent);
                finish();
            }
        });

    }


    public void callApilogin(final String username, final String password, final VolleyCallback callback ){
        String url = getString(R.string.apiAddr) + "/login";
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_SHORT).show();
                    }
                }){
            //Req Headers
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("username", username);
                headers.put("password", password);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    public interface VolleyCallback{
        void onSuccess(JSONObject jsonObject) throws JSONException;

    }

    @Override
    public void onBackPressed() {
        Intent resultItent = new Intent();
        setResult(RESULT_CANCELED, resultItent);
        finish();
        super.onBackPressed();
    }
}
