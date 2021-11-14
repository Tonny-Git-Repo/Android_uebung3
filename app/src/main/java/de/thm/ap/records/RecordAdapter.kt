package de.thm.ap.records

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import de.thm.ap.records.model.Record


class RecordAdapter(ctx: Context, records: List<Record>): ArrayAdapter<Record>(ctx, VIEW_RESOURCE, records) {
    private val inflater = ctx.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private var recordList = emptyList<Record>()

    override fun getView(pos: Int, recycleView: View?, parent: ViewGroup): View {
        val view = recycleView ?: createView()

        getItem(pos)?.let { record ->
            (view.tag as ViewHolder).apply {
                name.text = record.moduleName
                subText.text = "${record.moduleNum} ${record.crp}crp"
                mark.text = if (record.mark == null) "" else "${record.mark}%"
            }
        }
        return view
    }

    private class ViewHolder(view: View) {
        val name: TextView = view.findViewById(R.id.name)
        val subText: TextView = view.findViewById(R.id.subtext)
        val mark: TextView = view.findViewById(R.id.mark)
    }

    private fun createView() = inflater.inflate(VIEW_RESOURCE, null).apply {
        tag = ViewHolder(this)
    }

    companion object {
        private const val VIEW_RESOURCE = R.layout.record_list_item
    }

}










































//class RecordAdapter : RecyclerView.Adapter<RecordAdapter.MyRecordView>(){
//
//    private var recordList = emptyList<Record>()
//
//    class MyRecordView(itemView: View): RecyclerView.ViewHolder(itemView) {
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecordView {
//        return MyRecordView(LayoutInflater.from(parent.context).inflate(R.layout.activity_records, parent, false))
//    }
//
//    override fun onBindViewHolder(holder: MyRecordView, position: Int) {
//        val currentRecord = recordList[position]
//        holder.itemView.mark
//    }
//
//    override fun getItemCount(): Int {
//        return recordList.size
//    }
//}


//class  RecordAdapter(var myCxt: Context, var resource: Int, var items:List<Record>): ArrayAdapter<Record>(myCxt, resource, items){
//
//    private var recordList = emptyList<Record>()
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val layoutInflater:LayoutInflater = LayoutInflater.from(myCxt)
//        val view:View = layoutInflater.inflate(resource, null)
//        val recodName:TextView = view.findViewById(R.id.name)
//        val recodNummer:TextView = view.findViewById(R.id.subtext)
//        val recodmark:TextView = view.findViewById(R.id.mark)
//
//        var myRecord = items[position]
//
//        recodName.text = myRecord.moduleName
//        recodNummer.text = myRecord.moduleNum
//        recodmark.text = myRecord.crp.toString()
//
//        return view
//    }
//
//    fun setDate(record: List<Record>){
//        this.recordList = record
//        notifyDataSetChanged()
//    }
//}