package com.example.myapplication.network

import android.os.Build
import com.example.myapplication.BuildConfig
import com.example.myapplication.MyApp
import com.example.myapplication.datamodle.chat.chatroom_list.ChatRoomList
import com.example.myapplication.datamodle.chat.history.ChatHistory
import com.example.myapplication.datamodle.chat.image_message.response.FileResponse
import com.example.myapplication.datamodle.chat.text_message.TextMessage
import com.example.myapplication.datamodle.chat.text_message.response.TextResponse
import com.example.myapplication.tools.PrefHelper
import io.reactivex.Observable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


interface ChatApiService {

    //Get ChatRoom List
    @GET("rooms.get")
    fun getChatRoom(): Observable<ChatRoomList>

    //Get ChatRoom History
    @GET("groups.history")
    fun getChatHistory(@Query("roomId") roomId: String?, @Query("count") count: Int?): Observable<ChatHistory>

    @GET("groups.history")
    fun getChatHistory(@Query("roomId") roomId: String?, @Query("count") count: Int?, @Query("latest") latest: Date?): Observable<ChatHistory>


    //Post ImageMessage
    @Multipart
    @POST("rooms.upload/{roomId}")
    fun postImageMessage(@Path("roomId")roomId: String?,
                         @Part file: MultipartBody.Part): Observable<FileResponse>

    //Post AudioMessage
    @Multipart
    @POST("rooms.upload/{roomId}")
    fun postAudioMessage(@Path("roomId")roomId: String?,
                         @Part file: MultipartBody.Part): Observable<FileResponse>

    @POST("chat.sendMessage")
    fun postTextMessage(@Body message: TextMessage?): Observable<TextResponse>

    companion object {
        fun create(addHeader: Boolean, rId: String): ChatApiService {
            fun getSTUserAgent(): String? {
                val sb = StringBuffer()
                sb.append("Android ")
                sb.append(Build.VERSION.RELEASE)
                sb.append("/")
                sb.append(Build.MANUFACTURER)
                sb.append("/")
                sb.append(Build.MODEL)
                return sb.toString()
            }

            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            //缓存
            val cacheFile = File(MyApp.get()?.cacheDir, "cache")
            val cache = Cache(cacheFile, 1024 * 1024 * 100) //100Mb


            val UserAgentInterc: Interceptor = object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = getSTUserAgent()?.let {
                        if(addHeader){
                            chain.request()
                                .newBuilder()
                                .addHeader("X-Auth-Token",PrefHelper.chatToken)
                                .addHeader("X-User-Id", PrefHelper.chatId!!)
                                .removeHeader("User-Agent") //移除旧的
                                .addHeader("User-Agent", it) //添加真正的头部
                                .build()
                        } else {
                            chain.request()
                                .newBuilder()
                                .removeHeader("User-Agent") //移除旧的
                                .addHeader("User-Agent", it) //添加真正的头部
                                .build()
                        }
                    }
                    return chain.proceed(request!!)
                }
            }
            

            //创建一个OkHttpClient并设置超时时间
            val client = OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                //.addInterceptor(mRewriteCacheControlInterceptor)
                //.addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(UserAgentInterc)
                .addInterceptor(logInterceptor)
                .cache(cache)
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build()

            val unSafeClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                //.addInterceptor(mRewriteCacheControlInterceptor)
                //.addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(UserAgentInterc)
                .addInterceptor(logInterceptor)
                .cache(cache)
                .build()

            val retrofit = Retrofit.Builder()
                .client(unSafeClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.CHATROOM_URL+"/api/v1/")
                .build()

            return retrofit.create(ChatApiService::class.java)
        }
    }
}