package com.hearxgroup.tilttowin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hearxgroup.tilttowin.R
import com.hearxgroup.tilttowin.enum.ArrowColors

class ArrowColorAdapter(var context: Context, private val arrowColors: Array<ArrowColors>) : RecyclerView.Adapter<ArrowColorAdapter.ViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var colorClickListener: ColorClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.arrow_color_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = arrowColors[position]
        holder.parentCl.setBackgroundColor(ContextCompat.getColor(context, color.colorRes))
        holder.colorNameTv.setTextColor(ContextCompat.getColor(context, color.textColor))
        holder.colorNameTv.text = color.colorName
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var parentCl = itemView.findViewById<ConstraintLayout>(R.id.clColorParent)
        internal var colorNameTv = itemView.findViewById<TextView>(R.id.tvColorName)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            colorClickListener?.onColorClicked(view, adapterPosition)
        }
    }

    internal fun getItem(id: Int) = arrowColors?.get(id)

    interface ColorClickListener {
        fun onColorClicked(view: View, position: Int)
    }

    fun setOnHeroClickListener(colorClickListener: ColorClickListener) {
        this.colorClickListener = colorClickListener
    }

    override fun getItemCount() = arrowColors?.size

}
