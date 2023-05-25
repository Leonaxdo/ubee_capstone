package com.example.ubee

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// UV
interface UvService {
    @GET("getUVIdxV4")
    fun getUvData(@Query("ServiceKey") ServiceKey: String,
                      @Query("dataType") dataType: String,
                      @Query("areaNo") areaNo: String,
                      @Query("time") time: String): Call<UvResponse>
}

// BASE_URL/getUVIdxV4?ServiceKey="ServiceKey"&areaNo="areaNo"&time="time" 전달


interface RainService {
    @GET("getUltraSrtNcst")
    fun getRainData(@Query("ServiceKey") ServiceKey: String,
                      @Query("pageNo") pageNo: String,
                      @Query("numOfRows") numOfRows: String,
                      @Query("dataType") dataType: String,
                      @Query("base_date") base_date: String,
                      @Query("base_time") base_time: String,
                      @Query("nx") nx: String,
                      @Query("ny") ny: String,): Call<RainResponse>
}