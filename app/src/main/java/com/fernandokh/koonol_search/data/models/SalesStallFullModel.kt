package com.fernandokh.koonol_search.data.models
import com.google.gson.annotations.SerializedName

data class SalesStallFullModel(
    @SerializedName("_id")
    val id: String,
    val sellerId: SellerModel,
    val subCategoryId: SubCategoryModel,
    val name: String,
    val photos: List<String>,
    val description: String,
    val type: Boolean,
    val probation: Boolean,
    val active: Boolean,
    val creationDate: String,
    val updateDate: String,
    val locations: List<LocationModel>,
)

data class SellerModel(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val lastName: String,
    val email: String,
    val photo: Any?,
    val birthday: String,
    val gender: String,
    val phoneNumber: String,
    val creationDate: String,
    val updateDate: String,
)

data class SubCategoryModel(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val categoryId: CategoryModel,
    val creationDate: String,
)

data class CategoryModel(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val recommendedRate: Long,
    val creationDate: String,
)

data class LocationModel(
    @SerializedName("_id")
    val id: String,
    val markerMap: MarkerMap,
    val salesStallsId: String,
    val creationDate: String,
    val updateDate: String,
    val scheduleTianguisId: ScheduleTianguisModel,
)

data class ScheduleTianguisModel(
    @SerializedName("_id")
    val id: String,
    val tianguisId: String,
    val dayWeek: String,
    val indications: String,
    val startTime: String,
    val endTime: String,
    val creationDate: String,
    val updateDate: String,
)
