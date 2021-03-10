package com.example.caroonline;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CreateNewRoomDialogFragment extends DialogFragment {
    CreateRoomListener listener;
    Button create;
    Button cancel;
    TextView roomName;

    public static CreateNewRoomDialogFragment newInstance() {
        CreateNewRoomDialogFragment dialogFragment = new CreateNewRoomDialogFragment();
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_create_room,
                container);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        create = view.findViewById(R.id.btn_submit);
        cancel = view.findViewById(R.id.btn_cancel);
        roomName = view.findViewById(R.id.tv_room_name);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = roomName.getText().toString();
                listener.onCreateRoom(name);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void addCreateRoomListener(CreateRoomListener listener){
        this.listener = listener;
    }
}
