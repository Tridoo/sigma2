package tridoo.sigma2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class StartActivity extends AppCompatActivity {

    private int lastCompletedLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Repository repository = new Repository(this);
        lastCompletedLevel = repository.getLastCompletedLevel();

        findViewById(R.id.btn_play).setOnClickListener(new PlayOnClickListener());
        findViewById(R.id.btn_list).setOnClickListener(new ListOnClickListener());

        showAds();
    }

    private void showAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListenerImpl());

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private class PlayOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("level", lastCompletedLevel + 1);
            bundle.putInt("rank", 0);

            Intent intentPlay = new Intent(getBaseContext(), GameActivity.class);
            intentPlay.putExtras(bundle);
            startActivity(intentPlay);
        }
    }

    private class ListOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intentLevels = new Intent(getBaseContext(), LevelsActivity.class);
            startActivity(intentLevels);
        }
    }

}
