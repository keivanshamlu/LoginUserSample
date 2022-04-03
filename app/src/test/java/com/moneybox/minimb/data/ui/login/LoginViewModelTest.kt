package com.moneybox.minimb.data.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moneybox.minimb.data.models.login.LoginRequest
import com.moneybox.minimb.data.models.login.LoginResponse
import com.moneybox.minimb.data.repo.*
import com.moneybox.minimb.data.utility.Resource
import com.moneybox.minimb.utility.CoroutineTestRule
import com.moneybox.minimb.utility.getLastEmitted
import com.moneybox.minimb.utility.getListEmitted
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var viewModel: LoginViewModel

    @MockK(relaxed = true)
    lateinit var repo: UserRepository


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = LoginViewModel(
            repo
        )
    }

    @Test
    fun loginButtonClicked_shouldFetchItems_whenRepoReturnsSuccess() = mainCoroutineRule.testDispatcher.runBlockingTest {
        coEvery {
            repo.login(
                sampleUserName, sampleUserPass
            )
        } returns flow {
            emit(Resource.success(fakerResponseLogin))
        }

        viewModel.emailEntered(sampleUserName)
        viewModel.passEntered(sampleUserPass)
        viewModel.loginButtonClicked()
        val emittedList = getListEmitted(viewModel.login)
        Assert.assertEquals(emittedList.first().status, Resource.Status.SUCCESS)
        Assert.assertEquals(emittedList.first().data, fakerResponseLogin)
    }
    @Test
    fun loginButtonClicked_shouldNavigateUserToAccounts_whenRepoReturnsSuccess() = mainCoroutineRule.testDispatcher.runBlockingTest {
        coEvery {
            repo.login(
                sampleUserName, sampleUserPass
            )
        } returns flow {
            emit(Resource.success(fakerResponseLogin))
        }

        viewModel.emailEntered(sampleUserName)
        viewModel.passEntered(sampleUserPass)
        viewModel.loginButtonClicked()
        val emittedList = getListEmitted(viewModel.navigateToAccounts)
        Assert.assertEquals(emittedList.first()?.getContentIfNotHandled(), fakerResponseLogin)
    }
    @Test
    fun loginButtonClicked_shouldSetErrorOnLogin_whenRepoThrowsError() = mainCoroutineRule.testDispatcher.runBlockingTest {
        coEvery {
            repo.login(
                sampleUserName, sampleUserPass
            )
        } returns flow {
            emit(Resource.error<LoginResponse>(sampleError))
        }

        viewModel.emailEntered(sampleUserName)
        viewModel.passEntered(sampleUserPass)
        viewModel.loginButtonClicked()

        val emittedList = getListEmitted(viewModel.login)
        Assert.assertEquals(emittedList.first().status, Resource.Status.ERROR)
        Assert.assertEquals(emittedList.first().error, sampleError)
    }
    @Test
    fun loginButtonClicked_shouldShowToastError_whenRepoThrowsError() = mainCoroutineRule.testDispatcher.runBlockingTest {
        coEvery {
            repo.login(
                sampleUserName, sampleUserPass
            )
        } returns flow {
            emit(Resource.error<LoginResponse>(sampleError))
        }

        viewModel.emailEntered(sampleUserName)
        viewModel.passEntered(sampleUserPass)
        viewModel.loginButtonClicked()

        val emitted = getLastEmitted(viewModel.error)
        Assert.assertEquals(emitted?.getContentIfNotHandled() , sampleErrorText)
    }
    @Test
    fun continueButtonValidations_invalidChecks() = mainCoroutineRule.testDispatcher.runBlockingTest {

        val validEmail = "keivan@gmail.com"
        val invalidEmail = "keivangmail.com"

        val validPass = "123456"
        val invalidPass = "1234"

        //valid email, invalid pass
        viewModel.emailEntered(validEmail)
        viewModel.passEntered(invalidPass)

        val emitted = getLastEmitted(viewModel.continueButtonEnable)
        Assert.assertEquals(emitted , false)

        //invalid email, valid pass
        viewModel.emailEntered(invalidEmail)
        viewModel.passEntered(validPass)

        val emitted2 = getLastEmitted(viewModel.continueButtonEnable)
        Assert.assertEquals(emitted2 , false)

        //invalid email, invalid pass
        viewModel.emailEntered(invalidEmail)
        viewModel.passEntered(invalidPass)

        val emitted3 = getLastEmitted(viewModel.continueButtonEnable)
        Assert.assertEquals(emitted3 , false)
    }

}