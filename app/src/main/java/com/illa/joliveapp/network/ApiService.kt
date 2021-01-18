package com.illa.joliveapp.network

import android.os.Build
import com.illa.joliveapp.BuildConfig
import com.illa.joliveapp.MyApp
import com.illa.joliveapp.datamodle.ErrorLogApi
import com.illa.joliveapp.datamodle.authorization.LoginData
import com.illa.joliveapp.datamodle.authorization.LoginResponse
import com.illa.joliveapp.datamodle.authorization.ResendResponse
import com.illa.joliveapp.datamodle.authorization.ResendSMS
import com.illa.joliveapp.datamodle.authorization.register.Register
import com.illa.joliveapp.datamodle.authorization.register.RegisterResponse
import com.illa.joliveapp.datamodle.chat_room.Token.ChatRoomToken
import com.illa.joliveapp.datamodle.dating.DatingSearch
import com.illa.joliveapp.datamodle.event.detailv2.EventDetailV2
import com.illa.joliveapp.datamodle.event.event_list.EventList
import com.illa.joliveapp.datamodle.event.index.EventIndex
import com.illa.joliveapp.datamodle.event.list.TypeLists
import com.illa.joliveapp.datamodle.event.my_events.MyEvents
import com.illa.joliveapp.datamodle.event.review.EventReview
import com.illa.joliveapp.datamodle.event.review_cancel.PostReviewCancel
import com.illa.joliveapp.datamodle.event.review_member.ReviewMember
import com.illa.joliveapp.datamodle.event.set_full_join.SetFullJoinDataBody
import com.illa.joliveapp.datamodle.firebase.SetFCM
import com.illa.joliveapp.datamodle.follows.Follows
import com.illa.joliveapp.datamodle.instagram.IgDataBody
import com.illa.joliveapp.datamodle.notice.notice_data.Notice
import com.illa.joliveapp.datamodle.notice.template.NoticeTemplate
import com.illa.joliveapp.datamodle.profile.MyInfo
import com.illa.joliveapp.datamodle.profile.delete_photo.DeleteMyPhoto
import com.illa.joliveapp.datamodle.profile.delete_photo.response.DeleteMyPhotoResponse
import com.illa.joliveapp.datamodle.profile.interest.interest
import com.illa.joliveapp.datamodle.profile.job.job
import com.illa.joliveapp.datamodle.profile.sort_photo.SortMyPhoto
import com.illa.joliveapp.datamodle.profile.sort_photo.SortPhotoDataBody
import com.illa.joliveapp.datamodle.profile.update_photo.UpdatePhotoResponse
import com.illa.joliveapp.datamodle.profile.update.UpdateMtInfo
import com.illa.joliveapp.datamodle.profile.update.UpdateMyInfoResponse
import com.illa.joliveapp.datamodle.profile.user_info.UserInfo
import com.illa.joliveapp.tools.PrefHelper
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

    @POST("me/info")
    fun updateMyInfo_v2(@Body body: RequestBody): Observable<UpdateMyInfoResponse>

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

    //get User Information
    @GET("user/{userLabel}")
    fun getUserInfo(@Path("userLabel") userLabel: String): Observable<UserInfo>

    //Check Events
    @GET("event")
    fun getEvents(@Query("page") page: Int,
                  @Query("limit") limit: Int,
                  @Query("country_id") countryId: Int,
                  @Query("sort_type") sortType: String, @Query("events_categorys_id") eventsCategorysId: String): Observable<EventList>

    //get Events Index
    @GET("event/index")
    fun getEventIndex(): Observable<EventIndex>

    //get My Events
    @GET("me/info/events")
    fun getMyEvents(): Observable<MyEvents>


    //get User Events
    @GET("user/{userLabel}/events")
    fun getUserEvents(@Path("userLabel") userLabel: String): Observable<MyEvents>


    //Create Event
    @POST("event/create")
    fun createEvent(@Body body: RequestBody): Observable<String>

    //Update Event
    @POST("event/update/{event_id}")
    fun updateEvent(@Path("event_id") eventId: String, @Body body: RequestBody): Observable<String>

    //Event Detail
    @GET("event/detail/{label}")
    fun getEventDetail(@Path("label") label: String): Observable<EventDetailV2>

    //Event Detail V2
    @GET("event/detail/{label}")
    fun getEventDetailV2(@Path("label") label: String): Observable<EventDetailV2>

    //Event Detail By ID
    @GET("event/detail/id/{eventId}")
    fun getEventDetailById(@Path("eventId") label: String): Observable<EventDetailV2>

    //Cancel Join Event
    @POST("event/cancel/{id}")
    fun cancelJoinEvent(@Path ("id") id: String): Observable<String>

    //Join Event
    @POST("event/join/{id}")
    fun joinEvent(@Path ("id") id: String): Observable<String>

    //Review Sign Up Member
    @GET("event/review/{id}")
    fun getReviewList(@Path ("id") id: String): Observable<EventReview>

    //Pass Sign Up Member
    @POST("event/review")
    fun postEventReview(@Body body: ReviewMember?): Observable<String>

    //rollCall Sign Up Member
    @POST("event/rollCall")
    fun postEventRollCall(@Body body: ReviewMember?): Observable<String>

    //list paymentMethod 消費模式
    @GET("list/paymentMethod")
    fun getPaymentMethod(): Observable<TypeLists>

    //list currency 幣種
    @GET("list/currency")
    fun getCurrencyType(): Observable<TypeLists>

    //list eventCategory 開團種類
    @GET("list/eventCategory")
    fun getEventCategory(): Observable<TypeLists>

    //Dating Search
    @GET("dating/search")
    fun getDatingSearch(@Query("gender") gender: Int,
                 @Query("min_age") minAge: Int,
                 @Query("max_age") maxAge: Int,
                 @Query("max_km") maxKm: Int): Observable<DatingSearch>?

    //Job List
    @GET("list/job")
    fun getJobList(): Observable<job>

    //Job List
    @GET("list/interest")
    fun getInterestList(): Observable<interest>

    //Refresh Chat Token
    @POST("chatroom/getToken")
    fun getChatRoomToken(): Observable<ChatRoomToken>

    //Notice Template
    @GET("notification/template")
    fun getNoticeTemplate(): Observable<NoticeTemplate>

    //Notice
    @GET("notification?page=1&limit=50")
    fun getNotice(): Observable<Notice>

    //Notice Single Read
    @POST("notification/read/{id}")
    fun setNoticeRead(@Path ("id") id: String): Observable<String>

    //Get Follows
    @GET("me/follows")
    fun getFollows(): Observable<Follows>

    //Follow someBody
    @POST("user/{label}/follow")
    fun postFollow(@Path ("label") label: String): Observable<String>

    //UnFollow someBody
    @POST("user/{label}/unfollow")
    fun postUnFollow(@Path ("label") label: String): Observable<String>

    //Close event
    @POST("event/close/{eventId}")
    fun closeEvent(@Path ("eventId") eventId: String): Observable<String>

    //Notice All Read
    @POST("notification/read_all")
    fun noticeAllRead():Observable<String>

    //Instagram Set Token
    @POST("instagram/auth")
    fun setIgToken(@Body igBody: IgDataBody?):Observable<String>

    //Instagram Disconnect
    @POST("instagram/disconnect")
    fun igDisconnect():Observable<String>

    //Sort My Photo
    @POST("me/sort_photos")
    fun sortMyPhoto(@Body dataBody: SortPhotoDataBody?):Observable<SortMyPhoto>

    //Set Firebase Id
    @POST("fcm/register")
    fun setFcmToken(@Body dataBody: SetFCM?):Observable<String>

    //Delete Firebase Id
    @POST("fcm/logout ")
    fun deleteFcmToken(@Body dataBody: SetFCM?):Observable<String>

    //Cancel Review
    @POST("event/cancel_review")
    fun cancelReview(@Body dataBody: PostReviewCancel?):Observable<String>

    //Set Full Join
    @POST("event/full_join/{eventId}")
    fun setFullJoin(@Path ("eventId") eventId: String, @Body dataBody: SetFullJoinDataBody?): Observable<String>

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
                                .addHeader("Authorization","Bearer "+PrefHelper.apiHeader)
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