package io.box.catbox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private CatDataSource datasource;
    private int catCount = 1;
    private List<Cat> cats;

    public ImageAdapter(Context c) {

        try {
            datasource = new CatDataSource(c);
            datasource.open();

            cats = datasource.getAllCats();
            catCount = cats.size() + 1;
        } catch (SQLException ex) {

        }

        mContext = c;
    }

    public int getCount() {
        return catCount;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout;

        linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        // if it's not recycled, initialize some attributes
        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setPadding(0, 0, 0, 10);

        TextView catNameTextView = new TextView(mContext);

        if (position == this.getCount() - 1) {
            imageView.setImageResource(R.drawable.additem);
            linearLayout.addView(imageView);

            catNameTextView.setText("New Cat");

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, NewCatActivity.class);
                    mContext.startActivity(intent);
                }
            });

        } else {

            final Cat cat = cats.get(position);
            final String catName = cat.getName();
            String catImageUrl = cat.getPicture();

            if (catImageUrl.length() > 0) {
                imageView.setImageURI(Uri.parse(catImageUrl));
            } else {
                imageView.setImageResource(R.drawable.cat);
            }

            linearLayout.addView(imageView);

            catNameTextView.setText(catName);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, CatDetailsActivity.class);
                    intent.putExtra(CatListActivity.EXTRA_CAT_ID, cat.getId());
                    mContext.startActivity(intent);
                }
            });
        }

        linearLayout.addView(catNameTextView);

        return linearLayout;
    }
}
