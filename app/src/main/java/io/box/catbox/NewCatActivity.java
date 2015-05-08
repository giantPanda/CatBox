package io.box.catbox;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NewCatActivity extends ActionBarActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private EditText newCatName;
    private EditText newCatBirthday;
    private ImageView newCatImage;
    private Spinner newCatRace;
    private String selectedCatRace = "";
    private Button newCatSaveButton;
    private Button newCatCancelButton;
    private DatePickerDialog newCatBirthdateDatePickerDialog;
    private SimpleDateFormat dateFormat;
    private Intent imageCaptureIntent;
    private Uri catImageUri;
    private String catImageUriToStore = "";
    private CatDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cat);

        dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

        newCatName = (EditText) findViewById(R.id.new_cat_name);
        newCatRace = (Spinner) findViewById(R.id.new_cat_race);

        newCatBirthday = (EditText) findViewById(R.id.new_cat_birthday);
        newCatBirthday.setInputType(InputType.TYPE_NULL);
        newCatBirthday.setOnClickListener(this);

        newCatImage = (ImageView) findViewById(R.id.new_cat_image);
        newCatImage.setImageResource(R.drawable.additem);
        newCatImage.setOnClickListener(this);

        newCatSaveButton = (Button) findViewById(R.id.new_cat_save);
        newCatSaveButton.setOnClickListener(this);

        newCatCancelButton = (Button) findViewById(R.id.new_cat_cancel);
        newCatCancelButton.setOnClickListener(this);

        imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        catImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, catImageUri);

        Calendar newCalendar = Calendar.getInstance();
        newCatBirthdateDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                newCatBirthday.setText(dateFormat.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cat_races, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        newCatRace.setAdapter(adapter);
        newCatRace.setOnItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_cat, menu);
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
        if (v == newCatBirthday) {
            newCatBirthdateDatePickerDialog.show();
        } else if (v == newCatImage) {
            startActivityForResult(imageCaptureIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else if (v == newCatSaveButton) {
            try {
                datasource = new CatDataSource(this);
                datasource.open();

                Log.d("database", "newCatName: " + newCatName.getText().toString());
                Log.d("database", "selectedCatRace: " + selectedCatRace);
                Log.d("database", "newCatBirthday: " + newCatBirthday.getText().toString());
                Log.d("database", "catImageUri: " + catImageUriToStore);

                datasource.createCat(
                    newCatName.getText().toString(),
                    selectedCatRace,
                    newCatBirthday.getText().toString(),
                    catImageUriToStore);

            } catch (SQLException ex) {
                Log.e("database", "sql exception", ex);
            }

            finish();
        } else if (v == newCatCancelButton) {
            finish();
        }
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CatBox");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CatBox", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                catImageUriToStore = catImageUri.toString();
                newCatImage.setImageURI(catImageUri);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedCatRace = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        selectedCatRace = "";
    }
}
