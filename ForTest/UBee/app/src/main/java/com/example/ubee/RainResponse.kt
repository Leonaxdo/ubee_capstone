package com.example.ubee


import com.google.gson.annotations.SerializedName

data class RainResponse(
    @SerializedName("response")
    val response: ResponseR?
)

data class ResponseR(
    @SerializedName("body")
    val body: BodyR?,
    @SerializedName("header")
    val header: HeaderR?
)

data class HeaderR(
    @SerializedName("resultCode")
    val resultCode: String?,
    @SerializedName("resultMsg")
    val resultMsg: String?
)

data class BodyR(
    @SerializedName("dataType")
    val dataType: String?,
    @SerializedName("items")
    val items: ItemsR?,
    @SerializedName("numOfRows")
    val numOfRows: Int?,
    @SerializedName("pageNo")
    val pageNo: Int?,
    @SerializedName("totalCount")
    val totalCount: Int?
)

data class ItemsR(
    @SerializedName("item")
    val item: List<ItemR?>?
)

data class ItemR(
    @SerializedName("baseDate")
    val baseDate: String?,
    @SerializedName("baseTime")
    val baseTime: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("nx")
    val nx: Int?,
    @SerializedName("ny")
    val ny: Int?,
    @SerializedName("obsrValue")
    val obsrValue: String?
)