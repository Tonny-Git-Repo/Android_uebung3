package de.thm.ap.records.model

import android.app.Application
import androidx.lifecycle.*
import de.thm.ap.records.RecordsActivity
import de.thm.ap.records.persistence.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty

class RecordsViewModel(app: Application) : AndroidViewModel(app) {

    private val recordDao = AppDatabase.getDb(app).recordDao()
    val records: LiveData<List<Record>> = recordDao.findAllSync()

    //statt Transformation -> lifecycleScope
    val statistic: LiveData<Stats> = MutableLiveData<Stats>().apply {
        records.observeForever {
            value = Stats(it)
        }
    }

    /* val statistic: LiveData<Stats> = Transformations.map(records) {
        Stats(it)
    } */
}