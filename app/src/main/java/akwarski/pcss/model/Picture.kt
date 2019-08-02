package akwarski.pcss.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//parcelable is faster than serializable
@Parcelize
data class Picture(val albumId: String, val thumbnailUrl: String): Parcelable