package com.example.caroonline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class MessageEndedGameDialogFragment extends DialogFragment {
    TextView message;
    Button btn_confirm;
    String mess;

    public MessageEndedGameDialogFragment(String mess) {
        this.mess = mess;
    }

    public static MessageEndedGameDialogFragment newInstance(String mess) {
        MessageEndedGameDialogFragment dialogFragment = new MessageEndedGameDialogFragment(mess);

        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_ended_game,
                container);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_confirm = view.findViewById(R.id.btn_confirm);
        message = view.findViewById(R.id.tv_message_ended_game);
        message.setText(mess);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
