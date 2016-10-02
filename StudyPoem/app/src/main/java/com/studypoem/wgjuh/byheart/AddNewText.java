/*
package com.studypoem.wgjuh.byheart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class AddNewText extends AppCompatActivity {
    private String author;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        editText = (EditText)findViewById(R.id.new_text);
        author = getAuthor();
        toolbar.setTitle(author);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText();
            }
        });




    }
    private String getAuthor(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            return bundle.getString("author_name",null);
        return null;
    }

}
*/
