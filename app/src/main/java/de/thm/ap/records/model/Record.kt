package de.thm.ap.records.model

import androidx.room.ColumnInfo
import java.io.Serializable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Record")
class Record(@ColumnInfo(name = "module Nummer") var moduleNum: String, @ColumnInfo(name = "module Name") var moduleName: String, @ColumnInfo(name = "year") var year: Int,
             @ColumnInfo(name = "isSummer")var isSummerTerm: Boolean, @ColumnInfo(name = "isHafWeighted") var isHalfWeighted: Boolean, @ColumnInfo(name = "crp") var crp: Int,
             @ColumnInfo(name = "mark") var mark: Int,@ColumnInfo(name = "id")  @PrimaryKey(autoGenerate = true) var id: Int? = null): Serializable {

//    override fun toString(): String {
//        super.toString()
//
//        return "$moduleName $moduleNum (${ if ( mark > 0 ) mark else ""} ${ if ( mark > 0 ) "%" else ""} ${crp}crp)"
//    }
}