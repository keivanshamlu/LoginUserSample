package com.moneybox.minimb.data.utility


data class Resource<out T>(val status: Status, val data: T?, val error: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun <T> error(error: Throwable?, data: T?=null): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                error
            )
        }

        fun <T> loading(data: T?=null): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }

    fun isSuccess() = status == Status.SUCCESS
    fun isLoading() = status == Status.LOADING
    fun isError() = status == Status.ERROR
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }
}
