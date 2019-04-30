package com.instructure.student

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.instructure.canvasapi2.models.AccountDomain
import com.instructure.canvasapi2.utils.APIHelper
import com.instructure.loginapi.login.dialog.NoInternetConnectionDialog
import com.instructure.loginapi.login.util.Const
import com.instructure.pandautils.utils.onClick
import com.instructure.student.activity.SignInActivity
import kotlinx.android.synthetic.main.activity_made_easy_login_init.*

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
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MadeEasyLoginInit::class.java)
        }
    }
}
