package com.example.rene.abicap;

import android.os.Build;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Miguel on 18/04/2018.
 */

public class Encriptador {
    final String serialDispositivo = Build.SERIAL;
    char caracter;
    int codigoASCII;
    String serialEncriptado="";
    String serialEncriptadoFinal="";
    String ultimosCuatroDigitosDelSerial = "";
    String fecha="";
    String encriptadoA64="";

    public void encriptar() throws UnsupportedEncodingException {
        /*
        //esto era una manera antigua de activar la licencia (los ultimos 4 digitos de la serial del dispositivo + dia + mes + año)
        ultimosCuatroDigitosDelSerial = serialDispositivo.substring(serialDispositivo.length()-4, serialDispositivo.length());
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMHH");
        fecha = dateFormat.format(new Date());
        serialEncriptado = ultimosCuatroDigitosDelSerial+fecha;

        for (int i = 0; i < serialEncriptado.length() ; i++) {
            caracter = serialEncriptado.charAt(i);
            codigoASCII = (caracter);  //sabemos simbologia en ASCII
            serialEncriptado = serialEncriptado + codigoASCII;
        }

        for (int i = 0; i < serialEncriptadoFinal.length(); i++) {
            if (i%2 != 0){
                caracter=serialEncriptadoFinal.charAt(i);
                encriptacionFinal=encriptacionFinal+caracter;
            }
        }
*/
        encriptadoA64=cifrarBase64(serialDispositivo);

    }


    public static String cifrarBase64(String texto) throws UnsupportedEncodingException {

        byte[] encrpt= texto.getBytes("UTF-8");
        //String base64 = Base64.encodeToString(encrpt, Base64.NO_WRAP);//con un = al final
        String base64 = Base64.encodeToString(encrpt, Base64.NO_WRAP);

        //NO_PADDING
        // Base64.Encoder encoder = Base64.getEncoder();
        // String b = encoder.encodeToString(a.getBytes(StandardCharsets.UTF_8) );
        return base64;
    }

    /*
    public static String descifrarBase64(String texto){

        byte[] decrypt= Base64.decode(base64, Base64.DEFAULT);
        String text = new String(decrypt, "UTF-8");

        //Base64.Decoder decoder = Base64.getDecoder();
        //byte[] decodedByteArray = decoder.decode(a);
        //String b = new String(decodedByteArray);

        return texto;
    }
    */
}