package muhlenberg.edu.bergdining;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.squareup.picasso.Picasso;

import muhlenberg.edu.bergdining.picasso.ImageSwitcherPicasso;

public class HomeActivity extends AppCompatActivity implements ImageView.OnClickListener {

    ImageSwitcher imageSwitcher;
    ImageSwitcherPicasso imageSwitcherPicasso;
    int[] slideshowImages = {R.raw.apple_big, R.raw.cake_big};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //uncomment for centered title
//        TextView title = (TextView) findViewById(R.id.toolbar_title);
//        title.setText(getString(R.string.title_activity_home));
//        if(getSupportActionBar() != null)
//            getSupportActionBar().setDisplayShowTitleEnabled(false);



        imageSwitcher = (ImageSwitcher) findViewById(R.id.home_slideshow);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(HomeActivity.this);
                imageView.setLayoutParams(new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                ));
                return imageView;
            }
        });

        imageSwitcherPicasso = new ImageSwitcherPicasso(this, imageSwitcher);

        Picasso.with(this)
                .load(slideshowImages[0])
                .into(imageSwitcherPicasso);

        imageSwitcher.postDelayed(new Runnable() {
            int i = 1;

            @Override
            public void run() {
                Picasso.with(HomeActivity.this)
                        .load(slideshowImages[i++ % slideshowImages.length])
                        .into(imageSwitcherPicasso);
                imageSwitcher.postDelayed(this, 5000);
            }
        }, 5000);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
