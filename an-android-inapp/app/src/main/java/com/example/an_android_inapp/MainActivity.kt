package com.example.an_android_inapp

import AskNicelyAppInterface
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
import android.webkit.WebViewClient

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
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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

    //Get your particular survey setup.  The exact nature of the request will depend on your server's setup and requirements.
    private fun getAskNicelySurveySetup() {
        val call: Call<AskNicelySurveySetupResponse> = GenericApiClient.getInstance().api.surveySetup
        call.enqueue(object : Callback<AskNicelySurveySetupResponse> {
            override fun onResponse(call: Call<AskNicelySurveySetupResponse>?, response: Response<AskNicelySurveySetupResponse>) {
                surveySetup = response.body()
            }

            override fun onFailure(call: Call<AskNicelySurveySetupResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "An error has occurred", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showAskNicelySurvey() {
        if (surveySetup != null) {
            //For the slug request, we require a one-use dash-free 16 length uuid to prevent duplicate spam.
            val uuid = UUID.randomUUID().toString().replace("-", "").take(16)
            //Do the actual slug request.
            val slugRequest = AskNicelySurveySlugRequest(
                surveySetup!!.domainKey,
                surveySetup!!.templateName,
                surveySetup!!.name,
                surveySetup!!.email,
                surveySetup!!.emailHashed,
                surveySetup!!.joined,
                "Something customised by you!"
            )
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
                        //Set up your WebView to allow it to connect to AskNicely's webpage.
                        val askNicelyWebView: WebView = findViewById(R.id.webview)
                        //Our surveys expect a Javascript Interface by the name of 'askNicelyInterface'
                        askNicelyWebView.addJavascriptInterface(AskNicelyAppInterface(applicationContext, askNicelyWebView), "askNicelyInterface")
                        //Our surveys run on Javascript, so make sure to enable it.
                        askNicelyWebView.settings.javaScriptEnabled = true

                        //Build the URL that will display your survey.
                        val builder: Uri.Builder = Uri.Builder()
                        //We serve everything over https for security.
                        builder.scheme("https")
                            //This can be baked into your app, but we suggest providing it with your survey setup response.
                            .authority(surveySetup!!.domainKey + ".asknice.ly")
                            .appendPath("email")
                            .appendPath("conversation")
                            //Provide the slug generated ealier here.
                            .appendPath(surveySlugData!!.slug)
                            //The template that you wish to display.
                            .appendQueryParameter("template_name", surveySetup!!.templateName)
                            //Prevents certain web-only actions from being taken.
                            .appendQueryParameter("inapp", "")
                        val anUrl: String = builder.build().toString()
                        askNicelyWebView.loadUrl(anUrl)
                        askNicelyWebView.visibility = View.VISIBLE
                    }
                }

                override fun onFailure(call: Call<AskNicelySurveySlugResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "An error has occurred", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}