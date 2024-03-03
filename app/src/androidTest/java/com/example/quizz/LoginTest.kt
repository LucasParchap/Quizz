
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.quizz.R
import com.example.quizz.view.Login
import com.example.quizz.view.Quizz
import com.example.quizz.view.Registration
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Login::class.java)

    @Before
    fun setUp() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }
    @Test
    fun successfulLoginNavigatesToQuizzActivity() {

        //Put correct login and password - Change
        val validUsername = "Lucas"
        val validPassword = "MotDePasse"

        onView(withId(R.id.username)).perform(typeText(validUsername), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(validPassword), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        intended(hasComponent(Quizz::class.java.name))
    }

    @Test
    fun loginWithIncorrectCredentials_StayOnLoginPage() {
        val invalidUsername = "invalidUsername"
        val invalidPassword = "invalidPassword"

        onView(withId(R.id.username)).perform(typeText(invalidUsername), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(invalidPassword), closeSoftKeyboard())
        onView(withId(R.id.loginButton)).perform(click())

        onView(withId(R.id.loginButton)).check(matches(isDisplayed()))
    }

    @Test
    fun clickingRegisterLinkOpensRegistrationActivity() {
        onView(withId(R.id.registerLink)).perform(click())
        intended(hasComponent(Registration::class.java.name))
    }
}
