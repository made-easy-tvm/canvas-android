package com.instructure.student

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.instructure.canvasapi2.models.AccountDomain
import com.instructure.canvasapi2.utils.APIHelper
import com.instructure.loginapi.login.dialog.NoInternetConnectionDialog
import com.instructure.loginapi.login.util.Const
import com.instructure.pandautils.utils.onClick
import com.instructure.student.activity.SignInActivity
import kotlinx.android.synthetic.main.activity_made_easy_login_init.*
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.instructure.canvasapi2.utils.tryOrNull
import com.instructure.pandautils.utils.GlideApp
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random


class MadeEasyLoginInit : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_made_easy_login_init)
        bindViews()
    }

    private fun bindViews() {
        bt_login.onClick {
            if (!APIHelper.hasNetworkConnection()) {
                NoInternetConnectionDialog.show(supportFragmentManager)
            } else {
                val intent = SignInActivity.createIntent(this, AccountDomain(Const.URL_CANVAS_NETWORK))
                startActivity(intent)
            }
        }

        bt_phone.onClick {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+919495993606")
            startActivity(intent)
        }

        bt_map.onClick {
            val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=Made+Easy&query_place_id=ChIJ5afYLDe7BTsRhIxz6D4Welg"))
            startActivity(intent)
        }

        FirebaseRemoteConfig.getInstance().fetch().addOnCompleteListener {
            FirebaseRemoteConfig.getInstance().activate()
            showBanner()
        }
    }

    fun showBanner() {
        val config = FirebaseRemoteConfig.getInstance().getString("banner_url")
        val json = tryOrNull { JSONArray(config) } ?: return
        if (json.length() < 1) return
        val url = json.get(Random.nextInt(json.length())).toString()
        MadeEasyBanner(this, url).show()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MadeEasyLoginInit::class.java)
        }
    }
}
