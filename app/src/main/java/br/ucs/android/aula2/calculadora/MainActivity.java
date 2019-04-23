package br.ucs.android.aula2.calculadora;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    private Integer player1, player2, playerTime;
    private Boolean gameOver;
    private CharSequence winner;
    private AlertDialog alertModal;
    private Boolean[] selectedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        Toast.makeText(this,
                "This is a Toast message", Toast.LENGTH_LONG).show();

    }

    private void alertMessage(String title, String message, int type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        if(type == 0){
            builder.setPositiveButton("Jogar Novamente", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    clear();
                }
            });
        }
        else{
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
        }

        alertModal = builder.create();
        alertModal.show();
    }

    private Button getPositionView(int id) {
        Button t = (Button) this.findViewById(id);
        return t;
    }

    private void init() {
        player1 = 1;
        player2 = 2;
        clear();
    }

    public void clear(){
        GridLayout rootLinearLayout = (GridLayout) findViewById(R.id.GridLayout1);

        for (int i = 0; i < 9; i++) {
            View v = rootLinearLayout.getChildAt(i);
            if (v instanceof Button) {
                ((Button)v).setText("");
            }
        }

        gameOver = false;
        winner = "";
        playerTime = 0;
        selectedImages = new Boolean[2];
        selectedImages[0] = selectedImages[1] = false;
    }

    public void compartilhar(View view) {

        Intent implicitIntent = new Intent(Intent.ACTION_SEND);
        implicitIntent.setType("text/plain");
        implicitIntent.putExtra(Intent.EXTRA_TEXT,
                "O resultado da sua operação é ");
        try {
            this.startActivity(implicitIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "Nenhum aplicativo de compartilhamento instalada.", Toast.LENGTH_LONG).show();
        }

    }

    public void compartilharWhats(View view) {

        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT,
                "O resultado da sua operação é ");
        try {
            this.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "Whatsapp não está instalado.", Toast.LENGTH_LONG).show();
        }
    }

    public void buttonClear(View view){
        clear();
    }

    public void selectPlayer(View view){
        if(playerTime != 0) return;

        switch (view.getId()) {
            case (R.id.player1):
                if(!selectedImages[0]){
                    view.setBackgroundResource(R.drawable.button1);
                    Array.set(selectedImages, 0, true);
                }
                else if(selectedImages[1]){
                    playerTime = 1;
                }
                break;
            case (R.id.player2):
                if(!selectedImages[1]){
                    view.setBackgroundResource(R.drawable.button2);
                    Array.set(selectedImages, 1, true);
                }
                else if(selectedImages[0]){
                    playerTime = 2;
                }
                break;
        }

        ((Button)view).setText("");
    }

    public void selectPosition(View view) {
        if(gameOver) return;

        if(playerTime == 0){
            alertMessage("Erro", "Você deve selecionar um jogador para inicar jogando", 1);
            return;
        }

        Button b = (Button) view;

        if(playerTime == 1){
            b.setText("X");
            playerTime = 2;
        }
        else{
            b.setText("O");
            playerTime = 1;
        }

        verifyWinner();
    }

    private void  verifyDone(){
        Boolean keepPlaying = false;
        GridLayout rootLinearLayout = (GridLayout) findViewById(R.id.GridLayout1);

        for (int i = 0; i < 9; i++) {
            View v = rootLinearLayout.getChildAt(i);
            if (v instanceof Button) {
                if(((Button)v).getText() == ""){
                    keepPlaying = true;
                }
            }
        }

        if(!keepPlaying) {
            gameOver = true;
            alertMessage("Empate", "O jogo terminou empatado", 0);
        }
    }

    private void verifyWinner(){
        //Row 1
        CharSequence a1 = getPositionView(R.id.pos1).getText();
        CharSequence a2 = getPositionView(R.id.pos2).getText();
        CharSequence a3 = getPositionView(R.id.pos3).getText();

        //Row 2
        CharSequence b1 = getPositionView(R.id.pos4).getText();
        CharSequence b2 = getPositionView(R.id.pos5).getText();
        CharSequence b3 = getPositionView(R.id.pos6).getText();

        //Row 3
        CharSequence c1 = getPositionView(R.id.pos7).getText();
        CharSequence c2 = getPositionView(R.id.pos8).getText();
        CharSequence c3 = getPositionView(R.id.pos9).getText();

        if((a1 == b1 && a1 == c1) || (a1 == a2 && a1 == a3) || (a1 == b2 && a1 == c3) && a1 != ""){
            winner = a1;
        }
        else if((b2 == b1 && b2 == b3) || (b2 == a2 && b2 == c2) || (b2 == a3 && b2 == c1) && b2 != ""){
            winner = b2;
        }
        else if((c3 == c2 && c3 == c1) || (c3 == a3 && c3 == b3) && c3 != ""){
            winner = c3;
        }
        else{
            verifyDone();
        }

        if(winner != ""){
            if(winner == "X") winner = "Jogador 1";
            if(winner == "O") winner = "Jogador 2";

            gameOver = true;
            alertMessage("Vencedor", "O ganhador da partida foi: " + winner, 0);
        }
    }
}