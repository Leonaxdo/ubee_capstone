package com.example.ubee

import com.google.gson.annotations.SerializedName


data class EmgMedResponse(
    @field:SerializedName("response")
    val response: Response?
)

data class Response(
    @field:SerializedName("body")
    val body: Body?,
    @field:SerializedName("header")
    val header: Header?
)

data class Header(
    @field:SerializedName("resultCode")
    val resultCode: String?,
    @field:SerializedName("resultMsg")
    val resultMsg: String?
)

data class Body(
    @field:SerializedName("dataType")
    val dataType: String?,
    @field:SerializedName("items")
    val items: Items?,
    @field:SerializedName("numOfRows")
    val numOfRows: Int?,
    @field:SerializedName("pageNo")
    val pageNo: Int?,
    @field:SerializedName("totalCount")
    val totalCount: Int?
)

data class Items(
    @field:SerializedName("item")
    val item: List<Item?>?
)

data class Item(
    @field:SerializedName("areaNo")
    val areaNo: String?,
    @field:SerializedName("code")
    val code: String?,
    @field:SerializedName("date")
    val date: String?,
    @field:SerializedName("h0")
    val h0: String?,
    @field:SerializedName("h12")
    val h12: String?,
    @field:SerializedName("h15")
    val h15: String?,
    @field:SerializedName("h18")
    val h18: String?,
    @field:SerializedName("h21")
    val h21: String?,
    @field:SerializedName("h24")
    val h24: String?,
    @field:SerializedName("h27")
    val h27: String?,
    @field:SerializedName("h3")
    val h3: String?,
    @field:SerializedName("h30")
    val h30: String?,
    @field:SerializedName("h33")
    val h33: String?,
    @field:SerializedName("h36")
    val h36: String?,
    @field:SerializedName("h39")
    val h39: String?,
    @field:SerializedName("h42")
    val h42: String?,
    @field:SerializedName("h45")
    val h45: String?,
    @field:SerializedName("h48")
    val h48: String?,
    @field:SerializedName("h51")
    val h51: String?,
    @field:SerializedName("h54")
    val h54: String?,
    @field:SerializedName("h57")
    val h57: String?,
    @field:SerializedName("h6")
    val h6: String?,
    @field:SerializedName("h60")
    val h60: String?,
    @field:SerializedName("h63")
    val h63: String?,
    @field:SerializedName("h66")
    val h66: String?,
    @field:SerializedName("h69")
    val h69: String?,
    @field:SerializedName("h72")
    val h72: String?,
    @field:SerializedName("h75")
    val h75: String?,
    @field:SerializedName("h9")
    val h9: String?
)