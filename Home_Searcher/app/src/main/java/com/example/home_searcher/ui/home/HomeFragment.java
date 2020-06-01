package com.example.home_searcher.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.home_searcher.Adapter;
import com.example.home_searcher.MainActivity;
import com.example.home_searcher.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    Adapter adapter;
    String area;
    String firstDay;
    String lastDay;
    String ocupantes;
    View root;
    static List<JSONObject> housescache;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        Toolbar toolbar = root.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Button calbutton = (Button) root.findViewById(R.id.buttonCalendar);
        calbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(getActivity(), Pop.class),1);
            }
        });


        if (HomeFragment.housescache != null){
            adapter = new Adapter(getActivity(),housescache, ((MainActivity)getActivity()).getUserId(), ((MainActivity)getActivity()).getFavHouses());
            recyclerView.setAdapter(adapter);
        }

        FloatingActionButton searchButton = root.findViewById(R.id.buttonsearch);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            //String area = this.area;
            //String firstDay = this.firstDay;
            //String lastDay = this.lastDay;
            //String ocupantes = this.ocupantes;

            @Override
            public void onClick(View v)
            {
                EditText areaIn = root.findViewById(R.id.AreaInput);
                EditText ocupIn = root.findViewById(R.id.ocupInput);

                area = areaIn.getText().toString();
                ocupantes= ocupIn.getText().toString();

                if (area.equals("") || firstDay==null || lastDay==null || ocupantes.equals("")) {
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                final List<JSONObject> jsonObjects = new ArrayList<>();
                try {
                    getFromApi(area, firstDay, lastDay, ocupantes,new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONArray response) {

                            if (response != null) {     //tranform jsonArray to List
                                for (int i=0;i<response.length();i++){
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.getString(i));
                                        jsonObjects.add(jsonObject);
                                    } catch (JSONException e) { }
                                }
                            }
                            HomeFragment.housescache=jsonObjects;
                            adapter = new Adapter(getActivity(),jsonObjects, ((MainActivity)getActivity()).getUserId(), ((MainActivity)getActivity()).getFavHouses());
                            recyclerView.setAdapter(adapter);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    public interface VolleyCallback{
        void onSuccess(JSONArray jsonArray);
    }

    private void getFromApi(String area, String begin, String end, String ocup,final VolleyCallback callback ) throws IOException {
        String url = getString(R.string.apiAddr) + "/houses/city=" + area + "&start=" + begin + "&end=" + end + "&guests=" + ocup;
        System.out.println(url);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response.toString());
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(jsonObjectRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {

            this.firstDay = data.getStringExtra("firstDay");
            this.lastDay = data.getStringExtra("lastDay");

            TextView fromDate = root.findViewById(R.id.fromText2);
            TextView toDate = root.findViewById(R.id.toText2);

            fromDate.setText(this.firstDay);
            toDate.setText(this.lastDay);
        }
    }


}
