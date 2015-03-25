package io.box.catbox;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout;

        //if (convertView == null) {

            linearLayout = new LinearLayout(mContext);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(10, 10, 10, 10);

            // if it's not recycled, initialize some attributes
            ImageView imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 10);

            imageView.setImageResource(mThumbIds[position]);

            linearLayout.addView(imageView);

            TextView catNameTextView = new TextView(mContext);

            if (position == this.getCount() - 1) {
                catNameTextView.setText("New Cat");

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, NewCatActivity.class);
                        mContext.startActivity(intent);
                    }
                });

            } else {
                final String catName = new String("Cat name #" + position);

                catNameTextView.setText(catName);

                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CatDetailsActivity.class);
                        intent.putExtra(CatListActivity.EXTRA_CAT_NAME, catName);
                        mContext.startActivity(intent);
                    }
                });
            }

            linearLayout.addView(catNameTextView);

        //} else {
        //    linearLayout = (LinearLayout) convertView;
        //}

        return linearLayout;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.cat,
            R.drawable.additem
    };
}
