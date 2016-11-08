package com.wgjuh.byheart;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wgjuh.byheart.myapplication.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    public void openMailDeveloper(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + getString(R.string.mail_developer)));
        startActivity(intent);

        /*try {
            startActivity(intent);
        }catch(IllegalStateException e){
            Toast.makeText(this,getString(R.string.have_no_any_mail_apps),Toast.LENGTH_SHORT).show();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("mail: ", getString(R.string.mail_developer));
            clipboard.setPrimaryClip(clip);
        }*/
    }
    public void openMailDesigner(View view){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + getString(R.string.mail_designer)));
        try {
            startActivity(intent);
        }catch(IllegalStateException e){
            Toast.makeText(this,getString(R.string.have_no_any_mail_apps),Toast.LENGTH_SHORT).show();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("mail: ", getString(R.string.mail_designer));
            clipboard.setPrimaryClip(clip);
        }
    }

}