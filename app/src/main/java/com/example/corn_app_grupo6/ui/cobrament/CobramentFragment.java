package com.example.corn_app_grupo6.ui.cobrament;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.corn_app_grupo6.MainActivity;
import com.example.corn_app_grupo6.R;
import com.example.corn_app_grupo6.utils.UtilsHTTP;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class CobramentFragment extends Fragment {

    private Button button;
    private EditText amount;
    private int iduser;
    private int amo;

    private CobramentViewModel mViewModel;

    public static CobramentFragment newInstance() {
        return new CobramentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cobrament, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CobramentViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        View vista = this.getView();
        button = vista.findViewById(R.id.crear);
        button.setEnabled(true);
        iduser = MainActivity.user;
        System.out.println(iduser);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (MainActivity.user != 0) {

                        button.setEnabled(false);
                        JSONObject obj = new JSONObject("{}");
                        amount = vista.findViewById(R.id.cantidad);
                        obj.put("phone", iduser);
                        obj.put("amount", amount.getText());
                        UtilsHTTP.sendPOST(MainActivity.protocol + "://" + MainActivity.host + "/API/setup_payment", obj.toString(), (response) -> {

                            JSONObject objResponse = null;
                            try {
                                objResponse = new JSONObject(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            try {
                                Log.i("c", objResponse.getString("result"));
                                if (objResponse.getString("status").equals("OK")) {
                                    System.out.println("Ha salido bien");
                                    QRGenerate(objResponse.getString("result"));
                                } else {
                                    System.out.println("Ha salido mal");
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                                System.out.println("EXCEPCION: NO SE HA PODIDO HACER CONEXION");
                            }

                        });

                    } else {
                        final Activity activity = getActivity();
                        String text = "ERROR: inici de sessió";
                        Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } catch (Exception w) {
                    // TODO: handle exception
                    Log.i("i", w.toString());
                    button.setEnabled(true);

                }
            }
        });
    }
    public void QRGenerate(String token) {
        final Activity activity = getActivity();
        activity.runOnUiThread(()->{ QRCodeWriter writer = new QRCodeWriter();
            try {
                BitMatrix bitMatrix = writer.encode(token, BarcodeFormat.QR_CODE, 512, 512);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                ImageView viewQR = this.getView().findViewById(R.id.QR);
                viewQR.setImageBitmap(bmp);
            } catch (WriterException e) {
                e.printStackTrace();
            }});
    }
}