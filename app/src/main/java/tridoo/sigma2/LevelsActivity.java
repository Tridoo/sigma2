package tridoo.sigma2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Map;
import java.util.TreeMap;

public class LevelsActivity extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        repository = new Repository(this);
        showAds();
    }


    @Override
    protected void onResume() {
        super.onResume();
        TreeMap<Integer, Integer> levelsResults = repository.getLevelsResults();
        createLayout(LevelsConfig.getLevelsQuantity(), levelsResults);
    }

    private void createLayout(int levelsQuantity, Map<Integer, Integer> levelsResults) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.root);
        gridLayout.removeAllViews();

        for (int levelNumber = 1; levelNumber <= levelsQuantity; levelNumber++) {
            int rank = getRankFromResults(levelsResults, levelNumber);
            ImageView imageView = generateImageView(rank);
            TextView textView = generateTextView(levelNumber, rank);
            FrameLayout frame = generateFrameLayout(levelNumber, rank);

            frame.addView(imageView);
            frame.addView(textView);
            gridLayout.addView(frame);

            if (levelNumber <= levelsResults.size() + 1) {
                frame.setOnClickListener(v -> startActivity(v));
            }
        }
    }

    private int getRankFromResults(Map<Integer, Integer> levelsResults, int levelNumber) {
        return levelsResults.containsKey(levelNumber) ? levelsResults.get(levelNumber) : 0;
    }

    private FrameLayout generateFrameLayout(int levelNumber, int rank) {
        FrameLayout frame = new FrameLayout(getApplicationContext());
        frame.setId(levelNumber);
        frame.setTag(rank);
        return frame;
    }

    private ImageView generateImageView(int rank) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), LevelsConfig.getTileBackground(0)));
        imageView.setImageResource(LevelsConfig.getLevelBackground(rank));
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    private TextView generateTextView(int levelNumber, int rank) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 40, 0, 0);
        layoutParams.gravity = Gravity.CENTER;

        TextView textView = new TextView(getApplicationContext());
        textView.setText((Integer.toString(levelNumber)));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
        textView.setTextColor(getTextColor(rank));
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private int getTextColor(int rank) {
        if (rank == 0) return Color.parseColor("#000000");
        return Color.parseColor("#20dF20");
    }

    private void startActivity(View button) {
        int level = button.getId();
        Intent intent = new Intent(button.getContext(), GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        bundle.putInt("rank", (int) button.getTag());
        intent.putExtras(bundle);

        startActivity(intent);
    }

    private void showAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListenerImpl());

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}