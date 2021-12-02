package com.example.dindinn.data.network

import android.content.Context
import okhttp3.*
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/*
     * Step 1: We pass the context instance here,
     * since we need to get the ConnectivityStatus
     * to check if there is internet.
     * */
class NetworkInterceptor( private val context: Context) : Interceptor {
    private val networkEvent: NetworkEvent = NetworkEvent.instance!!
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var msg = ""
        /*
         * Step 2: We check if there is internet
         * available in the device. If not, pass
         * the networkState as NO_INTERNET.
         * */
        if (!ConnectivityStatus.isConnected(context)) {
            networkEvent.publish(NetworkState.NO_INTERNET)
        } else {
            try {
                val response: Response = chain.proceed(request)
                when (response.code) {
                    401 -> networkEvent.publish(NetworkState.UNAUTHORISED)
                    500 -> networkEvent.publish(NetworkState.SERVER_ERROR)
                    404 -> networkEvent.publish(NetworkState.API_NOT_FOUND)
                    405 -> networkEvent.publish(NetworkState.NOT_ALLOWED_METHOD)
                    503 -> networkEvent.publish(NetworkState.MAINTENANCE)
                    400, 403 -> {
                        // usually client error
                        networkEvent.publish(NetworkState.BAD_REQUEST)
                    }
                }
                return response
            } catch (e: Exception) {
                networkEvent.publish(NetworkState.NO_RESPONSE)
                e.printStackTrace()
                when (e) {
                    is SocketTimeoutException -> {
                        msg = "Timeout - Please check your internet connection"
                    }
                    is UnknownHostException -> {
                        msg = "Unable to make a connection. Please check your internet"
                    }
                    is ConnectionShutdownException -> {
                        msg = "Connection shutdown. Please check your internet"
                    }
                    is IOException -> {
                        msg = "Server is unreachable, please try again later."
                    }
                    is IllegalStateException -> {
                        msg = "${e.message}"
                    }
                    else -> {
                        msg = "${e.message}"
                    }
                }
            }
        }
        return Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(999)
            .message(msg)
            .body(ResponseBody.create(null, "error")).build()
    }
}