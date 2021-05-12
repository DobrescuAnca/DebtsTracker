package com.debts.debtstracker.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ResponseStatus<out T> {

    data class Success<out T>(val data: T) : ResponseStatus<T>()


    data class Error(
        val exception: Exception? = null,
        val message: String? = null,
        val code: Int = ErrorCode.DEFAULT.code,
        val errorObject: String? = null
    ) : ResponseStatus<Nothing>()

    object Loading : ResponseStatus<Nothing>()

    object None : ResponseStatus<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
            None -> "None"
        }
    }
}

val ResponseStatus<*>.succeeded
    get() = this is ResponseStatus.Success && data != null

enum class ErrorCode(val code: Int) {
    DEFAULT(-1),
    NO_USER(0),
    NO_DATA_CONNECTION(1),
    INVALID_TOKEN(401),
    DATA_NOT_FOUND(404),
    INVALID_DATA(409),
    SERVER_ERROR(500),
    JSON_PARSING_ERROR(2)
}