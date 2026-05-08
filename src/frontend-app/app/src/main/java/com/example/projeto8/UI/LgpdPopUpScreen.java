package com.example.projeto8.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projeto8.R;
import com.example.projeto8.api.patient.PatientService;
import com.example.projeto8.remote.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LgpdPopUpScreen extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.layout_lgpd_bottom_sheet, container, false);

        Button btnAccept = view.findViewById(R.id.btn_accept_lgpd);
        Button btnDecline = view.findViewById(R.id.btn_decline_lgpd);

        btnAccept.setOnClickListener(v -> {
            updateLgpdOnServer();
        });

      btnDecline.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Consentimento necessário.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), LoadScreenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            dismiss();
            if (getActivity() != null) getActivity().finish();
        });

        return view;
    }

    private void updateLgpdOnServer() {
        // Recupera o ID do paciente que salvamos no LoginActivity
        SharedPreferences sp = getActivity().getSharedPreferences("STORAGE", Context.MODE_PRIVATE);
        String patientId = sp.getString("patient_id", null);

        if (patientId != null) {
            PatientService service = RetrofitClient.getPatientService();
            // Chama o PUT que criamos no seu PatientController
            service.updateLgpdStatus(patientId, true).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // SUCESSO: Agora podemos ir para a MainActivity
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        dismiss();
                        if (getActivity() != null) getActivity().finish();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Erro de conexão", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}