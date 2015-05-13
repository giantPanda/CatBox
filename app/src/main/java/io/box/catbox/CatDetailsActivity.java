package io.box.catbox;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;


public class CatDetailsActivity extends ActionBarActivity implements View.OnClickListener {

    private Button cancelButton;
    private EditText catName;
    private EditText catBirthday;
    private ImageView catImage;
    private Spinner catRace;
    private CatDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_details);

        try {
            datasource = new CatDataSource(this);
            datasource.open();
        } catch (SQLException ex) {

        }

        Intent intent = getIntent();

        long catId = intent.getLongExtra(CatListActivity.EXTRA_CAT_ID, -1);

        Cat cat = datasource.getById(catId);

        catImage = (ImageView) findViewById(R.id.details_cat_image);

        String catImageUrl = cat.getPicture();

        if (catImageUrl.length() > 0) {
            catImage.setImageURI(Uri.parse(catImageUrl));
        } else {
            catImage.setImageResource(R.drawable.cat);
        }

        catName = (EditText) findViewById(R.id.details_cat_name);
        catName.setText(cat.getName());

        catBirthday = (EditText) findViewById(R.id.details_cat_birthday);
        catBirthday.setText(cat.getBirthday());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cat_races, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        catRace = (Spinner) findViewById(R.id.details_cat_race);
        catRace.setAdapter(adapter);

        int spinnerPostion = adapter.getPosition(cat.getRace());
        catRace.setSelection(spinnerPostion);

        cancelButton = (Button) findViewById(R.id.details_cat_cancel);
        cancelButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cat_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v == cancelButton) {
            finish();
        }
    }
}
