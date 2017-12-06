package com.isanechek.gdz.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by isanechek on 12/5/17.
 */
class RequestJob(val q: String, val bookTag: String, val jorq: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(q)
        parcel.writeString(bookTag)
        parcel.writeString(jorq)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RequestJob> {
        override fun createFromParcel(parcel: Parcel): RequestJob {
            return RequestJob(parcel)
        }

        override fun newArray(size: Int): Array<RequestJob?> {
            return arrayOfNulls(size)
        }
    }
}