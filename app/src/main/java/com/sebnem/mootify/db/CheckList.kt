package com.sebnem.mootify.db

import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

@Table(database = AppDatabase::class, name = "CheckList")
data class CheckList(
    @JvmField
    @PrimaryKey(autoincrement = true)
    var id: Long = 0,

    @Column
    var title: String? = null,

    @Column
    var isCompleted: Boolean = false,

) : BaseModel()