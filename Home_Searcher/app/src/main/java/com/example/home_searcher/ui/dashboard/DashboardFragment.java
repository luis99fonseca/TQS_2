package com.example.home_searcher.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.home_searcher.AdapterFav;
import com.example.home_searcher.MainActivity;
import com.example.home_searcher.R;

import org.json.JSONObject;

import java.util.List;

public class DashboardFragment extends Fragment {
    View root;
    RecyclerView recyclerView;
    AdapterFav adapter;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);

        recyclerView = root.findViewById(R.id.recyclerViewFav);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new AdapterFav(getActivity(),((MainActivity)getActivity()).getFavHouses(), ((MainActivity)getActivity()).getUserId(), ((MainActivity)getActivity()).getFavHouses());
        recyclerView.setAdapter(adapter);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
}
