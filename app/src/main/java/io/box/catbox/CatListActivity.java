package io.box.catbox;

import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;


public class CatListActivity extends ActionBarActivity {

    public final static String EXTRA_CAT_ID = "io.box.catbox.CAT_ID";
    private GridView gridView;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);

        imageAdapter = new ImageAdapter(this);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(imageAdapter);
    }

    protected void onResume () {
        Log.d("activity", "activity resumed");
        super.onResume();
    }

    protected void onRestart () {
        Log.d("activity", "activity restarted");
        super.onRestart();
        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
    }

    protected void onPause () {
        Log.d("activity", "activity paused");
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cat_list, menu);
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
}
