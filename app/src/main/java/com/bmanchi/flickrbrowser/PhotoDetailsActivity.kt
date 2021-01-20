package com.bmanchi.flickrbrowser

import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.browse.*
import kotlinx.android.synthetic.main.content_photo_details.*


class PhotoDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_details)
//        setSupportActionBar(findViewById(R.id.toolbar))
//
//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        activateToolbar(true)
        
//        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        val photo = intent.extras?.getParcelable<Photo>(PHOTO_TRANSFER) as Photo
        photo_title.text = resources.getString(R.string.photo_title_text, photo.title)
        photo_tags.text = resources.getString(R.string.photo_tag_text, photo.tags)
        photo_author.text = photo.author
//        photo_author.text = resources.getString(R.string.photo_author_text, "my", "red", "car" )

        Picasso.with(this).load(photo.link)
            .error(R.drawable.baseline_image_black_48dp)
            .placeholder((R.drawable.baseline_image_black_48dp))
            .into(photo_image)
    }
}