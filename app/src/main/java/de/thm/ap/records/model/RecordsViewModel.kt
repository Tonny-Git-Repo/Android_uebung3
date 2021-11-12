package de.thm.ap.records.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import de.thm.ap.records.RecordsActivity
import de.thm.ap.records.persistence.AppDatabase
import kotlin.properties.ReadOnlyProperty

class RecordsViewModel(app: Application) : AndroidViewModel(app) {

    private val recordDao = AppDatabase.getDb(app).recordDao()

    val records: LiveData<List<Record>> = recordDao.findAllSync()

    //statt Transformation -> lifecycleScope
    val statistic: LiveData<Stats> = Transformations.map(records) {
        Stats(it)
    }

//    fun getValue():LiveData<List<Record>>{
//        return records
//    }

}