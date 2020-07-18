package com.debts.debtstracker.data

enum class Status {
    RUNNING,
    OK,
    FAILED,
    EMPTY_LIST
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.OK)
        val LOADING = NetworkState(Status.RUNNING)
        val EMPTY_LIST = NetworkState(Status.EMPTY_LIST)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}