package com.example.dindinn.data.entities

sealed class Result<out T> {
    data class Successful<out T>(val drinks: T) : Result<T>()
    abstract class BaseError : Result<Nothing>()
    class AuthenticationError : BaseError()
    data class ClientError(val msg:String) : BaseError()
    class NetworkError : BaseError()
    class ServerError : BaseError()
    class AuthorizationError(val msg:String) : BaseError()
}