package cat.udl.tidic.amd.a7mig;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Handler;

import cat.udl.tidic.amd.a7mig.models.Carta;
import cat.udl.tidic.amd.a7mig.models.Jugador;
import cat.udl.tidic.amd.a7mig.models.Partida;


public class GameActivity extends AppCompatActivity {

    private static final String GAME_BEGIN_DIALOG_TAG = "game_dialog_tag";
    private static final String GAME_END_DIALOG_TAG = "game_end_dialog_tag";

    public TextView nomText;
    public TextView apostaText;
    private Button plantarse;
    private Button seguir;
    private TextView puntuacion;

    public ImageView imagenCarta;
    
    private int index = 0;
    private double puntuacio = 0.0;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        nomText = findViewById(R.id.textView);
        apostaText = findViewById(R.id.textView2);
        plantarse = findViewById(R.id.button2);
        seguir = findViewById(R.id.seguirButton);
        puntuacion = findViewById(R.id.textView3);
        imagenCarta = findViewById(R.id.UltimaCarta);
    }

    public void initView(){
        promptForPlayer();
    }

    private void promptForPlayer() {
        GameBeginDialog dialog = GameBeginDialog.newInstance(this);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), GAME_BEGIN_DIALOG_TAG);
    }

    private void finalPartida(List listJugadores){
        GameEndDialog dialog = GameEndDialog.newInstance(this,
                listJugadores);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), GAME_END_DIALOG_TAG);
    }


    public void partida(List noms, List apostes, List listJugadores){

        Carta carta = GameBeginDialog.partida.cogerCarta();
        imagenCarta.setImageResource(carta.getResource());

        nomText.setText(noms.get(index).toString());
        apostaText.setText(apostes.get(index).toString());

        puntuacio += carta.getValue();
        Jugador jugador = (Jugador) listJugadores.get(index);
        jugador.setPuntuacion(puntuacio);
        
        puntuacion.setText("" + puntuacio);

        if(puntuacio > 7.5){

            if(index < noms.size()-1) {
                index++;
                puntuacio = 0;

                partida(noms, apostes, listJugadores);
            }
            else {
                index = 0;
                puntuacio = 0;

                finalPartida(listJugadores);
            }


        }

        plantarse.setOnClickListener(v -> {
            if(index < noms.size()-1) {
                index++;
                puntuacio = 0;

                partida(noms, apostes, listJugadores);
            }
            else {
                puntuacio = 0;
                index = 0;

                finalPartida(listJugadores);
            }
        });

        seguir.setOnClickListener(v -> {
            partida(noms,apostes,listJugadores);
        });
        }



}