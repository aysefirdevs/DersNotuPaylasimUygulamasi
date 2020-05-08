package com.example.notuygulamam.Utils;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.notuygulamam.R;

public class ChangeFragment {
    private Context context;

    public ChangeFragment(Context context) {
        this.context = context;
    }

    public void change(Fragment fragment) {
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()

                .replace(R.id.fragmentlayout, fragment, "fragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
               // .addToBackStack(null)
                .commit();

    }

    public void changeWithParameter(Fragment fragment,String userid)
    {
        Bundle bundle = new Bundle();
        bundle.putString("userid",userid);
        fragment.setArguments(bundle);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()

                .replace(R.id.fragmentlayout, fragment, "fragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
               // .addToBackStack(null)
                .commit();
    }

    public void changeWithParameter(Fragment fragment,String userName,String id)
    {
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
        bundle.putString("id",id);
        fragment.setArguments(bundle);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()

                .replace(R.id.fragmentlayout, fragment, "fragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(null)
                .commit();
    }
}
