package com.consultica.techapalooza.ui.fragments.tickets;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consultica.techapalooza.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.ByteMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


public class TicketsLoggedInFragment extends Fragment {

    public static final String TAG = "com.consultica.techapalooza.fragment.TicketsLoggedInFragment";

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tickets_logged_in, container, false);



        return view;
    }

    public static Bitmap generateQrCode(String myCodeText) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();

        ByteMatrix bitMatrix = writer.encode(myCodeText, BarcodeFormat.QR_CODE, 512, 512);
        int width = 512, height = 512;

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (bitMatrix.get(x, y) == 0)
                    bmp.setPixel(x, y, Color.BLACK);
                else
                    bmp.setPixel(x, y, Color.WHITE);
            }
        }

        return bmp;
    }
}
