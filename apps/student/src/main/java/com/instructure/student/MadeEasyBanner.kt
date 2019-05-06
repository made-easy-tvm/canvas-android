package com.instructure.student

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.instructure.pandautils.utils.GlideApp
import com.instructure.pandautils.utils.onClick
import kotlinx.android.synthetic.main.dialog_madeeasy_banner.*

class MadeEasyBanner(context: Context, val imageURL: String) : Dialog(context, android.R.style.Theme_Light) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        getWindow()?.setBackgroundDrawable(
                ColorDrawable(Color.parseColor("#DD000000")))

        setContentView(R.layout.dialog_madeeasy_banner)

        bt_cancel.onClick {
            cancel()
        }

        GlideApp
            .with(context)
            .load(imageURL)
            .placeholder(R.drawable.anim_progress)
            .fitCenter()
            .into(iv_banner)
    }

}