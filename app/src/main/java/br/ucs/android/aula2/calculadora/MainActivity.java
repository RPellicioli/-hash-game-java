package br.ucs.android.aula2.calculadora;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Integer player1, player2, playerTime;
    private Boolean gameOver;
    private CharSequence winner;
    private AlertDialog alertModal;
    private Boolean[] selectedImages;
    private File fileImage = null;

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
                v.setBackgroundColor(0xff888888);
            }
        }

        gameOver = false;
        winner = "";
        playerTime = 0;
        selectedImages = new Boolean[2];
        selectedImages[0] = selectedImages[1] = false;

        Button p1 = getPositionView(R.id.player1);
        Button p2 = getPositionView(R.id.player2);

        p1.setText("X");
        p2.setText("O");

        p1.setBackground(null);
        p2.setBackground(null);
    }

    public void buttonClear(View view){
        clear();
    }

    public void selectPlayer(View view){
        if(playerTime != 0) return;

        switch (view.getId()) {
            case (R.id.player1):
                if(!selectedImages[0]){
                    Intent pic = getPic();
                    view.setBackgroundResource(R.drawable.button1);
                    Array.set(selectedImages, 0, true);
                }
                else if(selectedImages[1]){
                    playerTime = 1;
                }
                break;
            case (R.id.player2):
                if(!selectedImages[1]){
                    Intent pic = getPic();
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

    private Intent getPic(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                fileImage = createFileImage();
            } catch (IOException ex) {
                alertMessage("Erro", "Ocorreu um erro ao salvar a foto", 1);
            }

            if (fileImage != null) {
                Uri photoURI = FileProvider.getUriForFile(getBaseContext(),
                        getBaseContext().getApplicationContext().getPackageName() +
                                ".provider", fileImage);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, 3);
            }
        }

        return takePictureIntent;
    }

    private File createFileImage() throws IOException {
        String timeStamp = new
                SimpleDateFormat("yyyyMMdd_Hhmmss").format(
                new Date());
        File pasta = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imagem = new File(pasta.getPath() + File.separator
                + "JPG_" + timeStamp + ".jpg");
        return imagem;
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

        if(((a1 == b1 && a1 == c1) || (a1 == a2 && a1 == a3) || (a1 == b2 && a1 == c3)) && a1 != ""){
            if(a1 == b1){
                getPositionView(R.id.pos4).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos7).setBackgroundColor(0xFF00FF00);
            }
            else if(a1 == a2){
                getPositionView(R.id.pos2).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos3).setBackgroundColor(0xFF00FF00);
            }
            else{
                getPositionView(R.id.pos5).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos9).setBackgroundColor(0xFF00FF00);
            }

            getPositionView(R.id.pos1).setBackgroundColor(0xFF00FF00);
            winner = a1;
        }
        else if(((b2 == b1 && b2 == b3) || (b2 == a2 && b2 == c2) || (b2 == a3 && b2 == c1)) && b2 != ""){
            if(b2 == b1){
                getPositionView(R.id.pos4).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos6).setBackgroundColor(0xFF00FF00);
            }
            else if(b2 == a2){
                getPositionView(R.id.pos2).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos8).setBackgroundColor(0xFF00FF00);
            }
            else{
                getPositionView(R.id.pos5).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos7).setBackgroundColor(0xFF00FF00);
            }

            getPositionView(R.id.pos5).setBackgroundColor(0xFF00FF00);
            winner = b2;
        }
        else if(((c3 == c2 && c3 == c1) || (c3 == a3 && c3 == b3)) && c3 != ""){
            if(c3 == c2){
                getPositionView(R.id.pos8).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos7).setBackgroundColor(0xFF00FF00);
            }
            else{
                getPositionView(R.id.pos3).setBackgroundColor(0xFF00FF00);
                getPositionView(R.id.pos6).setBackgroundColor(0xFF00FF00);
            }

            getPositionView(R.id.pos9).setBackgroundColor(0xFF00FF00);
            winner = c3;
        }
        else{
            verifyDone();
        }

        if(winner != ""){
            if(winner == "X") winner = "Jogador 1";
            if(winner == "O") winner = "Jogador 2";

            gameOver = true;

            new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        alertMessage("Vencedor", "O ganhador da partida foi: " + winner, 0);
                    }
                },
            3000);
        }
    }
}