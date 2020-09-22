package com.example.myapplication.network

//import android.database.Observable
import android.os.Build
import com.example.myapplication.BuildConfig
import com.example.myapplication.MyApp
import com.example.myapplication.datamodle.ErrorLogApi
import com.example.myapplication.datamodle.authorization.LoginData
import com.example.myapplication.datamodle.authorization.LoginResponse
import com.example.myapplication.datamodle.authorization.ResendResponse
import com.example.myapplication.datamodle.authorization.ResendSMS
import com.example.myapplication.datamodle.authorization.register.Register
import com.example.myapplication.datamodle.authorization.register.RegisterResponse
import com.example.myapplication.datamodle.dating.DatingSearch
import com.example.myapplication.datamodle.profile.MyInfo
import com.example.myapplication.datamodle.profile.delete_photo.DeleteMyPhoto
import com.example.myapplication.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse
import com.example.myapplication.datamodle.profile.update_photo.UpdatePhotoResponse
import com.example.myapplication.datamodle.profile.update.UpdateMtInfo
import com.example.myapplication.datamodle.profile.update.UpdateMyInfoResponse
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
import java.util.concurrent.TimeUnit


interface ApiService {

    //ErrorLogApi
    @POST("/api/report")
    fun setErrorLog(@Body errorLogApi: ErrorLogApi?): Observable<String>

    //Login
    @POST("login")
    fun login(@Body loginData: LoginData?): Observable<LoginResponse>

    //Resend captcha
    @POST("resendSMS")
    fun resendSMS(@Body resendSMS: ResendSMS?): Observable<ResendResponse>

    //register
    @POST("register")
    fun register(@Body register: Register?): Observable<RegisterResponse>

    //Update MyInfo
    @POST("me/info")
    fun updateMyInfo(@Body updateMtInfo: UpdateMtInfo?): Observable<UpdateMyInfoResponse>

    //Update Profile Photo
    @Multipart
    @POST("me/create_photos")
    fun updateMyPhoto(@Part photos: MultipartBody.Part, @Part("sort")sort: RequestBody?): Observable<UpdatePhotoResponse>

    //Delete Photo
    @POST("me/delete_photo")
    fun deleteMyPhoto(@Body deleteMyPhoto: DeleteMyPhoto?): Observable<DeleteMyPhotoResponse>

    //Get Profile Information
    @GET("me/info")
    fun getMyInfo(): Observable<MyInfo>

    //Dating Search
    @GET("dating/search")
    fun getDatingSearch(@Query("gender") gender: Int,
                 @Query("min_age") minAge: Int,
                 @Query("max_age") maxAge: Int,
                 @Query("max_km") maxKm: Int): Observable<DatingSearch>?








    companion object {
        fun create(addHeader: Boolean): ApiService {
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
                                .addHeader("Authorization","Bearer "+PrefHelper.getApiHeader())
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
//                .baseUrl("https://logcenter.zanstartv.com/")
                .baseUrl(BuildConfig.RESTFUL_URL)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}