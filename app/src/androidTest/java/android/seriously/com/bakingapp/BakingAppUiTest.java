package android.seriously.com.bakingapp;

import android.seriously.com.bakingapp.activity.RecipeSelectionActivity;
import android.seriously.com.bakingapp.utils.NetworkUtils;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BakingAppUiTest {

    private static final String INGREDIENT_CUP = "CUP";

    @Rule
    public ActivityTestRule<RecipeSelectionActivity> activityTestRule =
            new ActivityTestRule<>(RecipeSelectionActivity.class);

    @Test
    public void checkConnection() {
        assertTrue("No connection", NetworkUtils.isConnected(InstrumentationRegistry.getTargetContext()));
    }

    @Test
    public void checkSelectionActivityFragmentContainer() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSelectionFragmentRecyclerViewDisplay() {
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void checkSelectionFragmentRecyclerViewContent() {
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withId(R.id.card_recipe_icon))));
    }

    @Test
    public void checkRecipeDetailsForwarding() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_ingredients)).check(matches(isDisplayed()));
    }

    @Test
    public void checkRecipeIngredientsDialogDisplay() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipe_ingredients)).perform(ViewActions.click());
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText(INGREDIENT_CUP))));
    }

    @Test
    public void checkRecipeStepDetailsForwarding() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.back_to_list)).check(matches(isDisplayed()));
    }
}
