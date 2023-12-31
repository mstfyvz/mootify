package com.sebnem.mootify.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = AppDatabase::class, name = "LoginHistory")
data class LoginHistory(
    @JvmField
    @PrimaryKey(autoincrement = true)
    var id: Long = 0,

    @Column
    var username: String? = null,

    @Column
    var password: String? = null,

    @Column
    var date: String? = null
) : BaseModel() {}