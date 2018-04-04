package android.seriously.com.bakingapp.activity;

import android.content.pm.ActivityInfo;
import android.seriously.com.bakingapp.utils.ViewUtils;
import android.support.v7.app.AppCompatActivity;

public abstract class BasicActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        if (ViewUtils.isTablet(this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }
}
