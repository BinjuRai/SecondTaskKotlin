package com.example.crud.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Phone

data class UserModel(
    var id: String = " ",
    var name: String = " ",
    var email: String = "",
    var phone:Int=0,
    var password: String = " ",
    var url: String = " ",
    var imageName: String=""
) : Parcelable {
    var number: Int=0

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: ""




    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeInt(phone)
        parcel.writeString(url)
        parcel.writeString(imageName)


    }

    override fun describeContents(): Int {
        return 0
    }

    fun uploadImage(imageName: String, it: Uri, any: Any) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }

}