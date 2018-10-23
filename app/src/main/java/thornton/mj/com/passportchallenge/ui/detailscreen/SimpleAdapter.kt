package thornton.mj.com.passportchallenge.ui.detailscreen

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_list_content.view.*
import thornton.mj.com.passportchallenge.R

// Used to display the profile's hobbies within the "Profile View"
class SimpleAdapter(private val items: ArrayList <String>) : RecyclerView.Adapter<SimpleAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
        holder.idView.text = items[position]

    }

    override fun getItemCount(): Int = items.size

    public fun getItems() = items



    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        //TODO Remove item from REPO and DB -> Do this through a VM????
    }

    fun addItem(hobby : String){
        val index : Int = items.size
        items.add(index, hobby)
        notifyItemChanged(index)
    }


    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_content, parent, false)) {

        val idView: TextView = itemView.id_text

        fun bind(name: String) = with(itemView) {

        }
    }
}