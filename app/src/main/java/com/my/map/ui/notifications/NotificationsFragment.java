package com.my.map.ui.notifications;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.my.map.constant.Information;
import com.my.map.R;
import com.my.map.history;
import com.my.map.person;
import com.my.map.personmessage;
import com.my.map.personupdata;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        TextView textView01 =(TextView) root.findViewById(R.id.per_message);//切记用root不能用getActivity
        TextView textView02= (TextView) root.findViewById(R.id.per_history);
        TextView textView03=(TextView) root.findViewById(R.id.per_revise);
        final TextView textViewusername=(TextView) root.findViewById(R.id.username);
        final TextView textViewid=(TextView)root.findViewById(R.id.pass_id) ;
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textViewusername.setText(Information.getUsername());
                textViewid.setText(Information.getId());
            }
        });
        textView01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),personmessage.class);
                startActivity(i);
            }
        });
        textView02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getActivity(),history.class);
                startActivity(i);
            }
        });
       textView03.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getActivity(),personupdata.class);
               startActivity(intent);
           }
       });
        return root;
    }
}