package com.moneybox.minimb.data.ui.accounts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moneybox.minimb.data.models.products.AllProductsResponse
import com.moneybox.minimb.data.repo.*
import com.moneybox.minimb.data.ui.login.LoginViewModel
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

    lateinit var viewModel: AccountsViewModel

    @MockK(relaxed = true)
    lateinit var repo: UserRepository


    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)

    }
    @Test
    fun investorProducts_shouldFetchItems_whenRepoReturnsSuccess() = mainCoroutineRule.testDispatcher.runBlockingTest {
        coEvery {
            repo.investorProducts(
                sampleUserToken
            )
        } returns flow {
            emit(Resource.success(fakerProductResponse))
        }

        // getInvestorProducts() called in init{}
        viewModel = AccountsViewModel(
            repo,
            sampleUserToken
        )

        val emittedList = getListEmitted(viewModel.products)
        Assert.assertEquals(emittedList.first().status, Resource.Status.SUCCESS)
        Assert.assertEquals(emittedList.first().data, fakerProductResponse)
    }
    @Test
    fun investorProducts_shouldShowError_whenRepoReturnsError() = mainCoroutineRule.testDispatcher.runBlockingTest {
        coEvery {
            repo.investorProducts(
                sampleUserToken
            )
        } returns flow {
            emit(Resource.error<AllProductsResponse>(sampleError))
        }

        // getInvestorProducts() called in init{}
        viewModel = AccountsViewModel(
            repo,
            sampleUserToken
        )

        val emittedList = getListEmitted(viewModel.products)
        Assert.assertEquals(emittedList.first().status, Resource.Status.ERROR)
        val emittedError = getLastEmitted(viewModel.error)
        Assert.assertEquals(emittedError?.getContentIfNotHandled(), sampleErrorText)
    }
}