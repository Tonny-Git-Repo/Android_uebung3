package de.thm.ap.records

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import de.thm.ap.records.persistence.AppDatabase

class AppContentProvider: ContentProvider() {
    override fun onCreate(): Boolean = true

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? {
        val builder = SupportSQLiteQueryBuilder
            .builder("Record")
            .columns(projection)
            .orderBy(sortOrder)
        if(URI_MATCHER.match(uri) == RECORD_ID){
            builder.selection("id = ?", arrayOf(uri.lastPathSegment))
        }
        return AppDatabase.getDb(context!!)
            .openHelper
            .readableDatabase
            .query(builder.create())
    }

    override fun getType(uri: Uri): String? = when (URI_MATCHER.match(uri)) {
        RECORDS -> "vnd.android.cursor.dir/vnd.thm.ap.record"
        RECORD_ID -> "vnd.android.cursor.item/vnd.thm.ap.record"
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    companion object {
        private const val AUTHORITY = "de.thm.ap.records"
        private const val RECORD_PATH = "records"
        private const val RECORDS = 1
        private const val RECORD_ID = 2

        private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
            // content://de.thm.ap.records/records
            addURI(AUTHORITY, RECORD_PATH, RECORDS);
            // content://de.thm.ap.records/records/# (# Nummernplatzhalter)
            addURI(AUTHORITY, RECORD_PATH + "/#", RECORD_ID)
        }
    }
    }