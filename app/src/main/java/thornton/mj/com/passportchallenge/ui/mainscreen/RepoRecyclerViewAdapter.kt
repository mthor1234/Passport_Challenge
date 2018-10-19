package thornton.mj.com.passportchallenge.ui.mainscreen

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import thornton.mj.com.passportchallenge.databinding.RvItemProfileBinding
import thornton.mj.com.passportchallenge.repo.Profile


class RepositoryRecyclerViewAdapter(private var items: ArrayList<Profile>,
                                    private var listener: OnItemClickListener)
    : RecyclerView.Adapter<RepositoryRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = RvItemProfileBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
            = holder.bind(items[position], listener)

    override fun getItemCount(): Int = items.size


    fun replaceData(newItems: ArrayList<Profile>){ items = newItems
        notifyDataSetChanged() }


    interface OnItemClickListener {
        fun onItemClick(position: Int, profile : Profile)
    }

    class ViewHolder(private var binding: RvItemProfileBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: Profile, listener: OnItemClickListener?) {
            binding.profile = profile
            if (listener != null) {
                binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition, profile)
                    println("Pressed ViewHolder!")
                })


            }

            binding.executePendingBindings()
        }
    }

}