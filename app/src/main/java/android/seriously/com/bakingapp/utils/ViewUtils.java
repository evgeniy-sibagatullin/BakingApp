package android.seriously.com.bakingapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public final class ViewUtils {

    private ViewUtils() {
    }

    public static boolean isOrientationLandscape(Resources resources) {
        return ORIENTATION_LANDSCAPE == resources.getConfiguration().orientation;
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
