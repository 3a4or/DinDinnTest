package com.example.dindinn.data.network

import com.example.dindinn.data.entities.BaseListResponse
import retrofit2.Response
import java.io.IOException
import com.example.dindinn.data.entities.Result

class BaseRepository {

    private suspend fun <T> handleErrors(func: suspend () -> Response<BaseListResponse<T>>): Result<T> {
        return try {
            val response = func()
            if (response.isSuccessful) {
                Result.Successful(response.body()!!.drinks)
            } else {
                when (response.code()) {
                    400 -> Result.ClientError("Error from api errors")
                    401 -> Result.AuthenticationError()
                    in 500..599 -> Result.ServerError()
                    404 -> Result.ServerError()
                    403 -> Result.AuthorizationError("Not Authorized")
                    else -> Result.ServerError()
                }
            }
        } catch (ex: IOException) {
           Result.NetworkError()
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.ServerError()
        }
    }
}