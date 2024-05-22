package com.example.rene.abicap;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.camilodesarrollo.abicap.R;

/**
 * Created by CamiloDesarrollo on 22/09/2016.
 */

public class Beeper {

    private MediaPlayer reproductor;

    public void activar(Context con){

        reproductor = MediaPlayer.create(con, R.raw.beep4);
        reproductor.setLooping(false);
        reproductor.start();
    }

    public void activarIncorrecto(Context con){
        reproductor = MediaPlayer.create(con,R.raw.beep03);
        reproductor.setLooping(false);
        reproductor.start();
    }
}
