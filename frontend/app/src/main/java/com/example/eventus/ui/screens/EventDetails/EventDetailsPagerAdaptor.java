package com.example.eventus.ui.screens.EventDetails;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class EventDetailsPagerAdaptor extends FragmentPagerAdapter {
    private EventDetailsActivity myContext;
    int totalTabs;

    public EventDetailsPagerAdaptor(EventDetailsActivity context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                EventDetailsTabFragment eventDetailsFragment = new EventDetailsTabFragment(myContext);
                return eventDetailsFragment;
            case 1:
                EventParticipantsTabFragment eventParticipantsFragment = new EventParticipantsTabFragment(myContext);
                return eventParticipantsFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }


}
