package com.cc.phonecontacts.ui.contacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.cc.phonecontacts.R
import com.cc.phonecontacts.databinding.ActivityContactDetailBinding
import com.cc.phonecontacts.model.Contact
import com.github.ramiz.nameinitialscircleimageview.MaterialColorGenerator
import com.github.ramiz.nameinitialscircleimageview.NameInitialsCircleImageView

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_detail)
        val contact = intent.getParcelableExtra<Contact>("contact")
        binding.contact = contact
        binding.back.setOnClickListener {
            finish()
        }

        val imageInfo = NameInitialsCircleImageView.ImageInfo
            .Builder("${contact?.name?.get(0)}")
            .setImageUrl(contact?.photoURI.toString())
            .setColorGenerator(MaterialColorGenerator())
            .build()

        binding.initialsCircleImageView.setImageInfo(imageInfo)
    }
}