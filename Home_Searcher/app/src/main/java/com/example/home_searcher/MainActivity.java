package com.example.home_searcher;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    long id;
    List<JSONObject> favHouses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivityForResult(new Intent(this, Login.class), 1);

        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_search, R.id.navigation_bookmark)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==0)
            finish();

        if (data != null) {
            Long idresult =data.getLongExtra("id",0);
            if (idresult == 0){
                return;
            }

            this.id=idresult;
            //CALL API
            getFavs();
        }
    }
    public void getFavs(){
        final List<JSONObject> jsonObjects = new ArrayList<>();

        getUserInfo(new Login.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                System.out.println(response);

                JSONArray jsonArray = new JSONArray(response.get("bookmarkedHouses").toString());

                if (response != null) {     //tranform jsonArray to List
                    for (int i=0;i<jsonArray.length();i++){
                        try {
                            JSONObject jsonObject = new JSONObject(jsonArray.getString(i));
                            jsonObjects.add(jsonObject);
                        } catch (JSONException e) { }
                    }
                }
                System.out.println(jsonObjects);
                setFavHouses(jsonObjects);
            }
        });
    }

    public void getUserInfo(final Login.VolleyCallback callback){
        String url = getString(R.string.apiAddr) + "/userInfo/user=" + this.id;
        System.out.println(url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }

    public void setFavHouses(List<JSONObject> favHouses){
        this.favHouses = favHouses;
    }

    public List<JSONObject> getFavHouses(){ return this.favHouses;}

    public Long getUserId(){ return this.id;}

    public void addToFavs(JSONObject j) throws JSONException {
        if (j.has("houseId")){
            Object id = j.get("houseId");
            j.remove("houseId");
            j.put("id", id);
        }
        this.favHouses.add(j);
    }

    public void removeFromFavs(Long houseid){
        for (JSONObject house : favHouses){
            try {
                if (house.get("id").toString().equals(houseid.toString())){
                    favHouses.remove(house);
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
