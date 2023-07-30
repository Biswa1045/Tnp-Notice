package com.biswa1045.tnpnotice


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class BranchListAdapter(context: Context, apps: ArrayList<StreamList>) :
    RecyclerView.Adapter<BranchListAdapter.MultiViewHolder>() {
    private val context: Context
    private  var options: ArrayList<StreamList>
    fun setOptionList(options: ArrayList<StreamList>) {
        this.options = ArrayList()
        this.options = options
        notifyDataSetChanged()
    }

    @NonNull
    override fun onCreateViewHolder(@NonNull viewGroup: ViewGroup, i: Int): MultiViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.option_item, viewGroup, false)
        return MultiViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull multiViewHolder: MultiViewHolder, position: Int) {

        multiViewHolder.bind(options[position])
    }

    override fun getItemCount(): Int {
        return options.size
    }

    inner class MultiViewHolder(@NonNull itemView: View) :
        RecyclerView.ViewHolder(itemView) {
         val select_logo: ImageView
        val name: TextView

        fun bind(option: StreamList) {
            name.text=option.name

            select_logo.visibility = if (option.isChecked) View.VISIBLE else View.GONE


            itemView.setOnClickListener {
                val v= !option.isChecked

                select_logo.visibility = if (v) View.VISIBLE else View.GONE
                option.isChecked = v
            }
        }

        init {
            select_logo = itemView.findViewById(R.id.select_logo)
            name = itemView.findViewById(R.id.text_name)

        }
    }


    val all: ArrayList<StreamList>
        get() = options
    val selected: ArrayList<StreamList>
        get() {
            val selected: ArrayList<StreamList> = ArrayList()
            for (i in 0 until options.size) {
                if (options[i].isChecked) {
                    selected.add(options[i])
                }
            }
            return selected
        }

    init {
        this.context = context
        this.options = apps
    }
}