package com.user.experience.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserEvent(
    @JsonProperty("addCount") // if you have a different name like addToCart that needs to mapped to
    // variable 'add' you should use jsonProperty by default the variable name is sufficient if the json attribute is of same name
    val add: Long,
    @JsonProperty("auctionId")
    val auctionId: String,
    @JsonProperty("click")
    val click: Long?,
    @JsonProperty("impressions")
    val impressions: Int?,
    @JsonProperty("keyword")
    val keyword: String?,
    @JsonProperty("position")
    val position: String?,
    @JsonProperty("productListName")
    val productListName: String?,
    @JsonProperty("webShopId")
    val webShopId: String?
)
