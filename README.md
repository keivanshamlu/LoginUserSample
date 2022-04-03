# Moneybox Technical Task

## The Brief…
Allow a user to login to their moneybox account and display their total plan value.

**Duration: 2 hours**

## What we are looking for…
- A working Android application that meets the criteria required
- Clean coding style
- Demonstration of architecture decisions
- UI considerations (including validation)
- Unit Tests

## The Project…
The project includes the networking code and models needed to complete the task, you will just need to integrate them and implement the logic.

- The `networking/Networking` class contains the **OkHttp** and **Retrofit** instances with the required headers for an unauthenticated request.
- The `models` package contains the request and response models with all the fields required to complete the task.

## Part 1 - Login Screen…
Create a screen that allows the user to enter their email address and password and login to our app.  Please design your screen according to the wireframes below:

<img src="/images/login.png" alt="Login" width="300"/>

To login please use the endpoint and credentials below:
```
Endpoint: @POST("users/login")

Email: jaeren+androidtest@moneyboxapp.com
Password: P455word12
```
You can use `LoginRequest` to make your request and `LoginResponse to parse your response.

## Part 2 - Display Plan Value
Create a simple screen that displays after a user has logged in and displays the user's `TotalPlanValue`.  Please design your screen according to the wireframe below:

<img src="/images/accounts.png" alt="Accounts" width="300"/>

To retrieve this data please use the endpoint below with an additional authorization header, replacing `{BEARER_TOKEN_HERE}` with the bearer token you retrieved from the login response.

```
Endpoint: @GET("investorproducts")

Header name: Authorization
Header value: Bearer {BEARER_TOKEN_HERE}
```
You can use the `AllProductsResponse` to parse this response and retrieve the plan value.

## Submitting your test…
Please push your project to a provide Github repository and invite android@moneyboxapp.com to read your project.

Once sent please let the people team know and we will review your project as soon as possible.

We wish you all the best!
