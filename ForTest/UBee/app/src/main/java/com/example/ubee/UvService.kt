package com.example.ubee

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UvService {
    @GET("getUVIdxV4")
    fun getEmgMedData(@Query("ServiceKey") ServiceKey: String,
                      @Query("dataType") dataType: String,
                      @Query("areaNo") areaNo: String,
                      @Query("time") time: String): Call<EmgMedResponse>
}

// BASE_URL/getUVIdxV4?ServiceKey="ServiceKey"&areaNo="areaNo"&time="time" 전달