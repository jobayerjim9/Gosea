package com.gosea.captain.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.gosea.captain.R;
import com.gosea.captain.controller.adapter.ViewPagerAdapter;

public class TripsFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_trips, container, false);
        ViewPager2 tripsViewPager=v.findViewById(R.id.tripsViewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager(),getLifecycle());
        viewPagerAdapter.addFragment(new AcceptedTripFragment());
        viewPagerAdapter.addFragment(new RejectedTripFragment());
        viewPagerAdapter.addFragment(new CompletedTripFragment());
        tripsViewPager.setAdapter(viewPagerAdapter);
        TabLayout tripTabLayout=v.findViewById(R.id.tripTabLayout);
        tripTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tripsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tripsViewPager.setCurrentItem(tab.getPosition());
            }
        });
        return v;
    }
}