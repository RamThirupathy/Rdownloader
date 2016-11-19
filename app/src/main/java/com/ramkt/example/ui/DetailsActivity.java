/**
 * Created by Ram_Thirupathy on 9/3/2016.
 */
package com.ramkt.example.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramkt.example.R;
import com.ramkt.example.utils.CommonUtils;

/**
 * Activity class to show the pin details
 */
public class DetailsActivity extends Activity {
    private TextView mTextPinTitle;
    private TextView mTextCreatedAt;
    private TextView mTextPins;
    private TextView mTextLikes;
    private ImageView mImagePinRaw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mTextPinTitle = (TextView) findViewById(R.id.tv_pin_title);
        mTextPins = (TextView) findViewById(R.id.tv_pins);
        mTextLikes = (TextView) findViewById(R.id.tv_pin_likes);
        mTextCreatedAt = (TextView) findViewById(R.id.tv_pin_created);
        mImagePinRaw = (ImageView) findViewById(R.id.iv_pin_detail_image);
    }

    /**
     * Loads data in this life cycle event
     */
    @Override
    public void onStart() {
        super.onStart();
        mTextPinTitle.setText(getIntent().getStringExtra("title"));//should make object parcelable
        mTextCreatedAt.setText(getIntent().getStringExtra("created"));
        mTextLikes.setText(getIntent().getStringExtra("likes"));
        mTextPins.setText(getIntent().getStringExtra("pins"));
        int width = getIntent().getIntExtra("width",0);
        int height = getIntent().getIntExtra("height",0);
        CommonUtils.getInstance().changeImageLayout(mImagePinRaw,height / 2,getIntent().getStringExtra("color"));
        CommonUtils.getInstance().downloadImage(this, mImagePinRaw, getIntent().getStringExtra("image"), width / 2, height / 2, width, height);
    }
}
