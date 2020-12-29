package com.illa.joliveapp.activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places.createClient
import com.google.android.libraries.places.api.Places.initialize
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.illa.joliveapp.R
import com.illa.joliveapp.adapter.Adapter_Map_Search_Result
import com.illa.joliveapp.adapter.CustomInfoWindowAdapter
import com.illa.joliveapp.custom_view.CardPopup
import com.illa.joliveapp.tools.Tools
import com.illa.joliveapp.viewmodle.MapsActivityVM
import kotlinx.android.synthetic.main.activity_maps.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    
    private val MapsActVM: MapsActivityVM by viewModel()
    
    private lateinit var mMap: GoogleMap
    private lateinit var geoCoder : Geocoder
    private lateinit var mGeoDataClient: GeoDataClient
    private lateinit var mPlaceDetectionClient: PlaceDetectionClient
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLastKnownLocation: LatLng
    private lateinit var searchAdapter: Adapter_Map_Search_Result
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var placesClient: PlacesClient

    private var mLocationPermissionGranted: Boolean = true
    private val DEFAULT_ZOOM = 15.0F
    private var getLocation = ""
    private var getLocationAddress = ""
    private var getLatitude = ""
    private var getLongitude = ""
    private var searchDataList = ArrayList<String>()
    private var placeIdList = ArrayList<String>()
    private var confirmLocation: Boolean = false


    private lateinit var popup: CardPopup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        tv_toolbar_title.text = "地點"
        title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        getIntentData()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initialize(applicationContext, "AIzaSyCYkun56DaA8wknm4LN57n_kF4lHZxccQs")

        placesClient = createClient(this)
        geoCoder = Geocoder(this)
        mGeoDataClient = Places.getGeoDataClient(this, null)
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        tv_confirm.setOnClickListener {
            val mIntent = Intent()
            getLocation = tv_location.text as String
            getLatitude = tv_latitude.text as String
            getLongitude = tv_longitude.text as String

            if(TextUtils.isEmpty(getLocation)){
                Tools.toast(this, "地點不能為空")
                return@setOnClickListener
            }
            mIntent.putExtra("location", getLocation)
            mIntent.putExtra("latitude", getLatitude)
            mIntent.putExtra("longitude", getLongitude)
            mIntent.putExtra("locationAddress", getLocationAddress)

            this.setResult(RESULT_OK, mIntent)
            finish()
        }
        tv_confirm.isClickable = false

        ed_event_title.setOnFocusChangeListener { view, b ->
            Log.e("peter","ed_event_title.setOnFocusChangeListene     $b")
            ll_search_result.visibility = View.VISIBLE
        }


        ed_event_title.setOnClickListener {
            ed_event_title.isFocusable = true
            ed_event_title.requestFocus()
            ed_event_title.isFocusableInTouchMode = true
            ed_event_title.isClickable = false
            ll_search_result.visibility = View.VISIBLE
        }

        tv_search.setOnClickListener {
            if(!TextUtils.isEmpty(ed_event_title.text)){
                search(ed_event_title.text.toString())

            }
        }

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Places.GEO_DATA_API)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        searchAdapter = Adapter_Map_Search_Result(this)
        searchAdapter.setOnItemClickListener(object : Adapter_Map_Search_Result.OnItemClickListener{
            override fun onItemClick(view: View?, position: Int, title: String, placeId: String) {
                Log.e("Peter","searchAdapter click")

                val arr = arrayOf(
                    com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME,
                    com.google.android.libraries.places.api.model.Place.Field.LAT_LNG,
                    com.google.android.libraries.places.api.model.Place.Field.ADDRESS
                )
                var placeFields = arr.asList()
                val request = FetchPlaceRequest.builder(placeId, placeFields)
                    .build();


                placesClient.fetchPlace(request).addOnSuccessListener {
                    Log.e("peter","placesClient    fetchPlace   ${it.place.name}")
                    Log.e("peter","placesClient    fetchPlace   ${it.place.latLng?.longitude}")
                    Log.e("peter","placesClient    fetchPlace   ${it.place.latLng?.latitude}")
                    Log.e("peter","placesClient    fetchPlace   ${it.place.address}")
                    Log.e("peter","placesClient    fetchPlace   ${it.place.addressComponents}")



                    mMap.clear()
                    val choseLocation = LatLng(it.place.latLng?.latitude!!,
                        it.place.latLng?.longitude!!
                    )

                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(choseLocation, DEFAULT_ZOOM))

                    Tools.hideSoftKeyboard(this@MapsActivity)

                    ll_search_result.visibility = View.GONE

                    tv_latitude.text = it.place.latLng?.latitude.toString()
                    tv_longitude.text = it.place.latLng?.longitude.toString()
                    tv_address.text = it.place.address
                    tv_location.text = it.place.name
                    getLocationAddress = it.place.address.toString()

                    val markerOptions = MarkerOptions()
                        .position(LatLng(it.place.latLng?.latitude!!, it.place.latLng?.longitude!!))
                        .title(it.place.name)
                        .snippet(it.place.address)
//
                    mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this@MapsActivity))
                    mMap.addMarker(markerOptions).showInfoWindow()
                    tv_confirm.setTextColor(this@MapsActivity.resources.getColor(R.color.white))
                    tv_confirm.background = this@MapsActivity.resources.getDrawable(R.drawable.bg_blue_confirm)
                    tv_confirm.isClickable = true
                }
            }

        })
        rv_search_result.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv_search_result.adapter = searchAdapter
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }
    override fun onBackPressed() {

        if(ll_search_result.isVisible){
            Log.e("peter","ll_search_result.isVisible")
            ll_search_result.visibility = View.GONE
            return
        }
        super.onBackPressed()
    }

    private fun getIntentData(){
        val b = intent.extras

        getLatitude = b?.getString("Latitude")!!
        getLongitude = b.getString("Longitude")!!
        getLocation= b.getString("Location")!!
        Log.e("peter","ll_search_result.getIntentData  $getLatitude")
        Log.e("peter","ll_search_result.getIntentData  $getLongitude")
        Log.e("peter","ll_search_result.getIntentData  $getLocation")

    }

    private fun search(title: String){
        val token = AutocompleteSessionToken.newInstance()
        val bounds = RectangularBounds.newInstance(
            LatLng(-33.880490, 151.184363),
            LatLng(-33.858754, 151.229596)
        )

        val request =
            FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestiction().f
//                .setLocationBias(bounds)
//                .setOrigin(LatLng(-33.8749937, 151.2041382))
                .setCountries("TW")
//                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(title)
                .build()

        initialize(applicationContext, "AIzaSyCYkun56DaA8wknm4LN57n_kF4lHZxccQs")


        placesClient.findAutocompletePredictions(request).addOnCompleteListener {
            Log.e("peter","placesClient     ${it.result.autocompletePredictions[0].placeId}")
            searchDataList.clear()
            placeIdList.clear()
            it.result.autocompletePredictions.forEach {

                Log.e("peter","placesClient     ${it.placeId}")

                placeIdList.add(it.placeId)
                searchDataList.add(it.getPrimaryText(null).toString())
            }

            searchAdapter.setIdList(placeIdList)
            searchAdapter.setData(searchDataList)

            ll_search_result.visibility = View.VISIBLE
        }



    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateLocationUI()
        if(TextUtils.isEmpty(getLatitude) || TextUtils.isEmpty(getLongitude)){return}

        val matches: List<Address> = geoCoder.getFromLocation(getLatitude.toDouble(), getLongitude.toDouble(), 1)
        val bestMatch: Address? = if (matches.isEmpty()) null else matches[0]


        val markerOptions = MarkerOptions()
            .position(LatLng(getLatitude.toDouble(), getLongitude.toDouble()))
            .title(getLocation)
            .snippet(bestMatch?.getAddressLine(0))


        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
        mMap.addMarker(markerOptions).showInfoWindow()

        tv_confirm.setTextColor(this.resources.getColor(R.color.white))
        tv_confirm.background = this.resources.getDrawable(R.drawable.bg_blue_confirm)
        tv_confirm.isClickable = true

    }


    private fun updateLocationUI() {
        try {
            if (mLocationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
//                mLastKnownLocation = null
//                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }

        val lat: Double
        val lng: Double
        val location: Location = this.getLastKnownLocation()!! // 設定定位資訊由 GPS提供

        lat = location.latitude
        lng = location.longitude

        mLastKnownLocation = LatLng(lat, lng)
        Log.e("Peter","mLastKnownLocation    $mLastKnownLocation")

        if(!TextUtils.isEmpty(getLatitude) && !TextUtils.isEmpty(getLongitude)){
            val choseLocation = LatLng(getLatitude.toDouble(), getLongitude.toDouble())

            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(choseLocation, DEFAULT_ZOOM))
        } else {
            getDeviceLocation()
        }

        mMap.setOnPoiClickListener {
            mMap.clear()

            val matches: List<Address> =
                geoCoder.getFromLocation(it.latLng.latitude, it.latLng.longitude, 1)
            val bestMatch: Address? = if (matches.isEmpty()) null else matches[0]

            tv_latitude.text = it.latLng.latitude.toString()
            tv_location.text = it.name
            tv_longitude.text = it.latLng.longitude.toString()
            tv_address.text = bestMatch?.getAddressLine(0)
            getLocationAddress = bestMatch?.getAddressLine(0).toString()

            val markerOptions = MarkerOptions()
                .position(LatLng(it.latLng.latitude, it.latLng.longitude))
                .title(it.name)
                .snippet(bestMatch?.getAddressLine(0))


            mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
            mMap.addMarker(markerOptions).showInfoWindow()

            Log.e("Peter","setOnMapClickListener bestMatch    $bestMatch")
            val viewLocation = IntArray(2)
            tv_confirm.setTextColor(this.resources.getColor(R.color.white))
            tv_confirm.background = this.resources.getDrawable(R.drawable.bg_blue_confirm)
            tv_confirm.isClickable = true

        }

        mMap.setOnMapClickListener {
            mMap.clear()

            tv_location.text = ""
            tv_latitude.text = it.latitude.toString()
            tv_longitude.text = it.longitude.toString()

            val matches: List<Address> =
                geoCoder.getFromLocation(it.latitude, it.longitude, 1)
            val bestMatch: Address? = if (matches.isEmpty()) null else matches[0]
            getLocationAddress = bestMatch?.getAddressLine(0).toString()
            tv_address.text = bestMatch?.getAddressLine(0)
            tv_location.text = bestMatch?.getAddressLine(0)


            val markerOptions = MarkerOptions()
                .position(it)
                .title(bestMatch?.getAddressLine(0))
                .snippet(bestMatch?.getAddressLine(0))

            mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(this))
            mMap.addMarker(markerOptions).showInfoWindow()
            tv_confirm.setTextColor(this.resources.getColor(R.color.white))
            tv_confirm.background = this.resources.getDrawable(R.drawable.bg_blue_confirm)
            tv_confirm.isClickable = true
            Log.e("Peter","setOnMapClickListener bestMatch    $bestMatch")
        }
    }


    private fun getDeviceLocation() {
        /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        try {
            if (mLocationPermissionGranted) {
                val locationResult =
                    mFusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener {
                    if (it.isSuccessful) {
                        mLastKnownLocation = LatLng(it.result.latitude, it.result.longitude)
                    } else {
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(mLastKnownLocation, DEFAULT_ZOOM))
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        val mLocationManager =
            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val location: Location = mLocationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || location.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = location
            }
        }
        return bestLocation
    }

    override fun onConnected(p0: Bundle?) {
        Log.e("Peter","placesClient  onConnected   $p0")
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("Peter","placesClient  onConnectionSuspended")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("Peter","placesClient  onConnectionFailed")
    }

}