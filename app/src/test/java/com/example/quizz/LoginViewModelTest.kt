import androidx.lifecycle.Observer
import com.example.quizz.R
import com.example.quizz.model.LoginResult
import com.example.quizz.model.User
import com.example.quizz.repositories.UserRepository
import com.example.quizz.utils.PasswordUtils
import com.example.quizz.utils.ResourceProvider
import com.example.quizz.viewmodel.LoginViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.mockito.Mockito.`when` as whenever

@Config(manifest=Config.NONE)
@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    private lateinit var userRepository: UserRepository
    private lateinit var resourceProvider: ResourceProvider
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        resourceProvider = mock(ResourceProvider::class.java)
        viewModel = LoginViewModel(userRepository, resourceProvider)

        whenever(resourceProvider.getString(R.string.login_success)).thenReturn("Login successful")
        whenever(resourceProvider.getString(R.string.incorrect_credentials)).thenReturn("Incorrect credentials")
        whenever(resourceProvider.getString(R.string.login_error)).thenReturn("Error logging in")
    }

    @Test
    fun `validateLogin updates loginResult on successful login`() = runTest {

        val username = "Username"
        val password = "Password1234"
        val hashedPassword = PasswordUtils.hashPassword(password)
        val expectedUser = User(1, username, "lucas@parchap.com", hashedPassword)

        whenever(userRepository.validateUser(username, hashedPassword)).thenReturn(expectedUser)

        val observer = mock<Observer<LoginResult>>()
        viewModel.loginResult.observeForever(observer)

        viewModel.validateLogin(username, password)

        verify(observer).onChanged(LoginResult(true, expectedUser, "Login successful"))
        viewModel.loginResult.removeObserver(observer)
    }

    @Test
    fun `validateLogin with incorrect credentials returns error`() = runTest {
        val incorrectUsername = "FakeUser"
        val incorrectPassword = "FakePassword"
        val hashedPassword = PasswordUtils.hashPassword(incorrectPassword)

        whenever(userRepository.validateUser(incorrectUsername, hashedPassword)).thenReturn(null)

        val observer = mock<Observer<LoginResult>>()
        viewModel.loginResult.observeForever(observer)

        viewModel.validateLogin(incorrectUsername, incorrectPassword)

        val expectedLoginResult = LoginResult(success = false, user = null, message = resourceProvider.getString(R.string.incorrect_credentials))
        verify(observer).onChanged(expectedLoginResult)

        viewModel.loginResult.removeObserver(observer)
    }
    @Test
    fun `validateLogin handles exceptions correctly`() = runTest {
        val username = "testUser"
        val password = "password"
        val hashedPassword = PasswordUtils.hashPassword(password)

        whenever(userRepository.validateUser(username, hashedPassword)).thenThrow(RuntimeException("Mocked exception"))

        val observer = mock<Observer<LoginResult>>()
        viewModel.loginResult.observeForever(observer)

        viewModel.validateLogin(username, password)

        val expectedLoginResult = LoginResult(success = false, user = null, message = resourceProvider.getString(R.string.login_error))
        verify(observer).onChanged(expectedLoginResult)

        viewModel.loginResult.removeObserver(observer)
    }

}
