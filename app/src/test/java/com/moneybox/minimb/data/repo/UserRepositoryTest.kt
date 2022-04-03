package com.moneybox.minimb.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moneybox.minimb.data.models.login.LoginRequest
import com.moneybox.minimb.data.models.login.LoginResponse
import com.moneybox.minimb.data.models.login.SessionDataResponse
import com.moneybox.minimb.data.models.login.UserResponse
import com.moneybox.minimb.data.models.products.AllProductsResponse
import com.moneybox.minimb.data.models.products.ProductDetailsResponse
import com.moneybox.minimb.data.models.products.ProductResponse
import com.moneybox.minimb.data.networking.ApiMoneyBox
import com.moneybox.minimb.data.utility.Resource
import com.moneybox.minimb.utility.CoroutineTestRule
import com.moneybox.minimb.utility.getListEmitted
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException


@ExperimentalCoroutinesApi
class UserRepositoryTest {

    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repo: UserRepository

    @MockK(relaxed = true)
    lateinit var apiMoneyBox: ApiMoneyBox

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = UserRepository(
            apiMoneyBox,
            mainCoroutineRule.testDispatcherProvider
        )
    }

    //---------------------------------------
    //login api
    //---------------------------------------
    @Test
    fun login_shouldReturnLoadingAtFirst() = mainCoroutineRule.testDispatcher.runBlockingTest {
        coEvery {
            apiMoneyBox.login(
                LoginRequest(
                    email = sampleUserName,
                    password = sampleUserPass
                )
            )
        } returns fakerResponseLogin

        val emittedList = getListEmitted(repo.login(sampleUserName, sampleUserPass))
        Assert.assertEquals(emittedList.first().status, Resource.Status.LOADING)
    }

    @Test
    fun login_shouldCallApiAndReturnSuccess_whenApiReturnsSuccessfully() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            coEvery {
                apiMoneyBox.login(
                    LoginRequest(
                        email = sampleUserName,
                        password = sampleUserPass
                    )
                )
            } returns fakerResponseLogin

            val emittedList = getListEmitted(repo.login(sampleUserName, sampleUserPass))
            coVerify {
                apiMoneyBox.login(
                    LoginRequest(
                        email = sampleUserName,
                        password = sampleUserPass
                    )
                )
            }
            Assert.assertEquals(emittedList.first().status, Resource.Status.LOADING)
            Assert.assertEquals(emittedList.last().status, Resource.Status.SUCCESS)
            Assert.assertEquals(emittedList.size, 2)
            Assert.assertEquals(emittedList.last().data, fakerResponseLogin)
        }

    @Test
    fun login_shouldCallApiAndReturnError_whenApiReturnsError() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            coEvery {
                apiMoneyBox.login(
                    LoginRequest(
                        email = sampleUserName,
                        password = sampleUserPass
                    )
                )
            } throws sampleError

            val emittedList = getListEmitted(repo.login(sampleUserName, sampleUserPass))
            coVerify {
                apiMoneyBox.login(
                    LoginRequest(
                        email = sampleUserName,
                        password = sampleUserPass
                    )
                )
            }
            Assert.assertEquals(emittedList.first().status, Resource.Status.LOADING)
            Assert.assertEquals(emittedList.last().status, Resource.Status.ERROR)
            Assert.assertEquals(emittedList.size, 2)
            Assert.assertEquals(emittedList.last().error, sampleError)
        }

    //---------------------------------------
    //product api
    //---------------------------------------


    @Test
    fun investorProducts_shouldReturnLoadingAtFirst() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            coEvery {
                apiMoneyBox.investorproducts(
                    sampleUserToken
                )
            } returns fakerProductResponse

            val emittedList = getListEmitted(repo.investorProducts(sampleUserToken))
            Assert.assertEquals(emittedList.first().status, Resource.Status.LOADING)
        }

    @Test
    fun investorProducts_shouldCallApiAndReturnSuccess_whenApiReturnsSuccessfully() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            coEvery {
                apiMoneyBox.investorproducts(
                    sampleUserTokenValid
                )
            } returns fakerProductResponse

            val emittedList = getListEmitted(repo.investorProducts(sampleUserToken))
            coVerify {
                apiMoneyBox.investorproducts(
                    sampleUserTokenValid
                )
            }
            Assert.assertEquals(emittedList.first().status, Resource.Status.LOADING)
            Assert.assertEquals(emittedList.last().status, Resource.Status.SUCCESS)
            Assert.assertEquals(emittedList.size, 2)
            Assert.assertEquals(emittedList.last().data, fakerProductResponse)
        }

    @Test
    fun investorProducts_shouldCallApiAndReturnError_whenApiReturnsError() =
        mainCoroutineRule.testDispatcher.runBlockingTest {
            coEvery {
                apiMoneyBox.investorproducts(
                    sampleUserTokenValid
                )
            } throws sampleError

            val emittedList = getListEmitted(repo.investorProducts(sampleUserToken))
            coVerify {
                apiMoneyBox.investorproducts(
                    sampleUserTokenValid
                )
            }
            Assert.assertEquals(emittedList.first().status, Resource.Status.LOADING)
            Assert.assertEquals(emittedList.last().status, Resource.Status.ERROR)
            Assert.assertEquals(emittedList.size, 2)
            Assert.assertEquals(emittedList.last().error, sampleError)
        }
}

val sampleUserName = "keivan"
val sampleUserPass = "123456"
val sampleUserToken = "qwdknowdiuwhedoiqwmpqowk"
val sampleUserTokenValid = "Bearer qwdknowdiuwhedoiqwmpqowk"

const val sampleErrorText = "this is an error"
val sampleError = IOException(sampleErrorText)

val fakerResponseLogin = LoginResponse(
    SessionDataResponse("bearerToken"),
    UserResponse("userID", "firstName", "lastName", "email")
)

val fakerProductResponse = AllProductsResponse(
    1.1f, 2.2f, 3.3f, listOf(ProductResponse(1, ProductDetailsResponse("friendlyName"), 6.6f, 7.7f))
)