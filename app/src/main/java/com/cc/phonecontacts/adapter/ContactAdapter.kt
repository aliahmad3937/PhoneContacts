package com.cc.phonecontacts.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cc.phonecontacts.R
import com.cc.phonecontacts.databinding.ItemContactBinding
import com.cc.phonecontacts.model.Contact
import com.github.ramiz.nameinitialscircleimageview.MaterialColorGenerator
import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView
import dagger.Binds


class ContactAdapter(
    var mList: MutableList<Contact>,
    val context: Context,
    val clickListener: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var templist: MutableList<Contact> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContactBinding.inflate(LayoutInflater.from(context), parent, false)
        ) { pos ->
            clickListener(mList[pos])
        }
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mList[position])

    }

    fun filterContacts(query: String?) {
        if (templist.isEmpty())
            templist.addAll(mList)

        if (query != null && query.isNotEmpty()) {
            val filterList = templist.filter {
                it.name.toString().contains(query) || it.mobileNumber.toString().contains(query)
            }
            if (filterList != null && filterList.size > 0) {
                mList.clear()
                mList.addAll(filterList)
                notifyDataSetChanged()
            }

        } else {
            mList.clear()
            mList.addAll(templist)
            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }


    inner class ViewHolder(val binding: ItemContactBinding, onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(adapterPosition)
            }
        }


        fun bind(model: Contact) {
            binding.displayName = model.name
            binding.executePendingBindings()

                val imageInfo = NameInitialsCircleImageView.ImageInfo
                    .Builder("${model.name?.get(0)}")
                    .setImageUrl(model.photoURI.toString())
                    .setColorGenerator(MaterialColorGenerator())
                    .build()

                binding.initialsCircleImageView.setImageInfo(imageInfo)

        }
    }

}

