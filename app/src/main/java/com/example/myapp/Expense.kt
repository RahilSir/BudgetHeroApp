package com.example.myapp

//
//
//import android.os.Parcel
//import android.os.Parcelable
//
//data class Expense(
//    val name: String,
//    val amount: Double,
//    val date: String,
//    val startTime: String,
//    val endTime: String,
//    val description: String,
//    val category: String
//) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: "",
//        parcel.readDouble(),
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readString() ?: ""
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(name)
//        parcel.writeDouble(amount)
//        parcel.writeString(date)
//        parcel.writeString(startTime)
//        parcel.writeString(endTime)
//        parcel.writeString(description)
//        parcel.writeString(category)
//    }
//
//    override fun describeContents(): Int = 0
//
//    companion object CREATOR : Parcelable.Creator<Expense> {
//        override fun createFromParcel(parcel: Parcel): Expense = Expense(parcel)
//        override fun newArray(size: Int): Array<Expense?> = arrayOfNulls(size)
//    }
//}

//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "expenses")
//data class Expense(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val name: String,
//    val amount: Double,
//    val date: String,
//    val startTime: String,
//    val endTime: String,
//    val description: String,
//    val category: String
//)


//package com.example.myapp // Or wherever your data package is
//
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "expenses")
//data class Expense(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val name: String,
//    val amount: Double,
//    val date: String,
//    val startTime: String,
//    val endTime: String,
//    val description: String?,
//    val category: String
//
//
//
//)




