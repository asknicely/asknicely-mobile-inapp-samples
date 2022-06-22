package com.example.an_android_inapp

import AskNicelySurveySetupResponse
import AskNicelySurveySlugRequest
import AskNicelySurveySlugResponse
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import com.example.an_android_inapp.databinding.ActivityMainBinding
import android.widget.Toast

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

import android.net.Uri
import android.view.View


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private var surveySetup: AskNicelySurveySetupResponse? = null
    private var surveySlugData: AskNicelySurveySlugResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            showAskNicelySurvey()
        }

        getAskNicelySurveySetup()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun getAskNicelySurveySetup() {
        val call: Call<AskNicelySurveySetupResponse> = GenericApiClient.getInstance().api.surveySetup
        call.enqueue(object : Callback<AskNicelySurveySetupResponse> {
            override fun onResponse(call: Call<AskNicelySurveySetupResponse>?, response: Response<AskNicelySurveySetupResponse>) {
                surveySetup = response.body()
                Log.d("Survey setup result: ", surveySetup.toString())
            }

            override fun onFailure(call: Call<AskNicelySurveySetupResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "An error has occurred", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showAskNicelySurvey() {
        if (surveySetup != null) {
            val uuid = UUID.randomUUID().toString().replace("-", "").take(16)
            val slugRequest = AskNicelySurveySlugRequest(
                surveySetup!!.domainKey,
                surveySetup!!.templateName,
                surveySetup!!.name,
                surveySetup!!.email,
                surveySetup!!.emailHashed,
                surveySetup!!.joined,
                "Something customised by you!"
            );
            Log.d("Survey slug request: ", "https://" + surveySetup!!.domainKey + ".asknice.ly/service/inapp.php " + uuid)
            val call: Call<AskNicelySurveySlugResponse> =
                GenericApiClient.getInstance().api.getSurveySlug(
                    "https://" + surveySetup!!.domainKey + ".asknice.ly/service/inapp.php",
                    uuid,
                    "3.4.1",
                    "",
                    slugRequest)
            call.enqueue(object : Callback<AskNicelySurveySlugResponse> {
                @SuppressLint("SetJavaScriptEnabled")
                override fun onResponse(
                    call: Call<AskNicelySurveySlugResponse>,
                    response: Response<AskNicelySurveySlugResponse>
                ) {
                    surveySlugData = response.body()
                    if(surveySlugData != null) {
                        Log.d("Survey setup result: ", if(surveySlugData!!.slug != null) surveySlugData!!.slug else "No Slug")
                        val myWebView: WebView = findViewById(R.id.webview)
                        myWebView.settings.javaScriptEnabled = true
                        val builder: Uri.Builder = Uri.Builder()
                        builder.scheme("https")
                            .authority(surveySetup!!.domainKey + ".asknice.ly")
                            .appendPath("email")
                            .appendPath("conversation")
                            .appendPath("in-app")
                            .appendPath(surveySlugData!!.slug)
                            .appendQueryParameter("id", uuid)
                            .appendQueryParameter("reloadcookie", "")
                            .appendQueryParameter("template_name", surveySetup!!.templateName)
                            .appendQueryParameter("source", "ajax")
                            .appendQueryParameter("anVersion", "3.4.1");
                        val anUrl: String = builder.build().toString();
                        Log.d("Survey URL generated:", anUrl)
                        myWebView.loadUrl(anUrl)
                        myWebView.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<AskNicelySurveySlugResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "An error has occurred", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}