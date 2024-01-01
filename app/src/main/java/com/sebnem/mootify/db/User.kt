package com.sebnem.mootify.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.Database
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
object AppDatabase {
    const val NAME = "MyDatabase"
    const val VERSION = 1
}

@Table(database = AppDatabase::class, name = "Users")
data class User(
    @JvmField
    @PrimaryKey(autoincrement = true)
    var id: Long = 0,

    @Column
    var username: String? = null,

    @Column
    var password: String? = null,

    @Column
    var time: Int? = null,

    @Column
    var remainingTime: Int? = null
) : BaseModel()
