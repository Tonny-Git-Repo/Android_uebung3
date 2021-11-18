package de.thm.ap.records


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import de.thm.ap.records.databinding.ActivityRecordsBinding
import de.thm.ap.records.model.Record
import de.thm.ap.records.model.RecordsViewModel
import de.thm.ap.records.model.Stats
import de.thm.ap.records.persistence.AppDatabase
import de.thm.ap.records.persistence.RecordDAO
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import androidx.activity.viewModels
import androidx.lifecycle.*
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecordsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityRecordsBinding

    private var records= listOf<Record>()
    private lateinit var adapter : ArrayAdapter<Record>
    private lateinit var recordDAO: RecordDAO
    private val viewModel: RecordsViewModel by viewModels()
    private val executer = Executors.newSingleThreadExecutor()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appDb = Room.databaseBuilder(this, AppDatabase::class.java, "app_database").build()

        lifecycleScope.launch(Dispatchers.IO) {
            records = appDb.recordDao().findAll()
        }
        recordDAO = AppDatabase.getDb(this).recordDao()

        binding = ActivityRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recordListView.emptyView = binding.recordListEmptyView

        adapter = RecordAdapter(this, records)

        binding.recordListView.adapter = adapter

        viewModel.records.observe(this, {
            adapter.clear()
            adapter.addAll(it)
        })

    }

    override fun onStart() {
        super.onStart()

      //  adapter.clear()
      //  adapter.addAll(RecordDAO.get(this).findAll())

        binding.recordListView.setOnItemClickListener { parent: AdapterView<*>, view, position, id ->
            Intent(this, RecordFormActivity::class.java).also {
                //val idRecordToUpdate = records.get(position).id;
                val idRecordToUpdate = records[position].id;
                //Log.d("RecordInfo", records.get(position).id.toString())
                it.putExtra("idRecordToUpdate", idRecordToUpdate)
                startActivity(it)
            }
        }


        binding.recordListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE_MODAL

        binding.recordListView.setMultiChoiceModeListener(object : AbsListView.MultiChoiceModeListener {

            val checkedRecordsList = ArrayList<Record>()

            override fun onItemCheckedStateChanged( mode: ActionMode?, position: Int, id: Long, checked: Boolean) {
                if(checked) {
                    adapter.getItem(position)?.let { checkedRecordsList.add(it) }
                }else{
                    checkedRecordsList.remove(adapter.getItem(position))
                }
                mode?.title = "${checkedRecordsList.size} ausgewählt"
            }

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                val menuInflater: MenuInflater = mode!!.menuInflater
                menuInflater.inflate(R.menu.menu_actions, menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
               // val checkedItems: SparseBooleanArray = binding.recordListView.checkedItemPositions

                return when (item?.itemId) {
                    R.id.action_delete -> {
                        AlertDialog.Builder(this@RecordsActivity).apply{
                            setMessage("Sollen die Leistungen wircklich gelöscht werden?")
                            setPositiveButton("löschen"){ _, _ ->
                                checkedRecordsList.forEach{
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        if(checkedRecordsList.size > 0){
                                            recordDAO.delete(it)
                                        }
                                    }
                                }
                                checkedRecordsList.forEach {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        if (!adapter.isEmpty){
                                            adapter.remove(it)
                                        }
                                    }
                                }
                                viewModel.records.observe(this@RecordsActivity) {
                                    adapter.clear()
                                    adapter.addAll(it)
                                }
                                checkedRecordsList.clear()
                            }
                            setNegativeButton("cancel", null)
                            show()
                        }
                        mode?.finish()
                        true
                    }
                    R.id.action_email -> {
                        Intent(Intent.ACTION_SEND).also {
                            var text = StringBuilder()
                            checkedRecordsList.forEach {
                                text.append(it.toString())
                                text.append(" ")
                            }

                            it.data = Uri.parse("mailto")
                            it.type = "text/plain"
                            it.putExtra(Intent.EXTRA_SUBJECT, "Meine Leistungen ${checkedRecordsList.size}")
                            it.putExtra(Intent.EXTRA_TEXT, "\n\n ${text.toString()}")
                            startActivity(it)
                        }
                        mode?.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                //
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu
        // This adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.records, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {

            R.id.action_add -> {

                val i = Intent(this, RecordFormActivity::class.java)
                startActivity(i)
                true
            }

            R.id.action_stats -> {

                viewModel.statistic.observe(this, {
                    var alertDia = AlertDialog.Builder(this)
                        .setTitle(R.string.stats)
                        .setMessage(it.toString())
                        .setNeutralButton(R.string.close, null)
                        .show()

                    alertDia.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getColor(R.color.dialogCloseColor))
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    fun onSave(view: android.view.View) {
    }

}


