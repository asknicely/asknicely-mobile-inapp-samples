# AskNicely In-App Mobile Integrations

A set of sample code for integrating your AskNicely survey directly into your app, via the use of standard iOS/Android functionality and a little touch of AskNicely magic.

## Contents
There are three folders in this repository:
- `sample-server` contains an example of setting up the survey and, most importantly, generating a e-mail hash for your user to allow them to securely connect to AskNicely.
- `an-ios-inapp` contains a simple app showing the standard flow for retrieving the above e-mail hash from your backend, using that to generate a unique slug for the survey, and then presenting it to your user - and hiding the survey when they've completed it (positively, no doubt!)
- `an-android-inapp` does the same as the iOS example, but for Android!

## Installation

##### Example Server
Install your preferred form of [Node.js](https://nodejs.org/en/download/) (tested primarily on v16.0.0).

Make a copy of the included [config-template.json](/sample-server/config-template.json) and fill out the fields as follows:

For the request itself:
 - Domain Key:  Your tenant name as it appears before the .asknice.ly part of your AskNicely site.  For example, *foobar*.asknice.ly would have the Domain Key *foobar*.
 - Template Name:  The name of the survey template that you wish to use.  Please see your available templates on *domainkey*.asknice.ly/setting/email.
 - Name: The name of the user receiving the survey.
 - Email: The email of the user receiving the survey.
 - Joined: When the user was created/joined your organisation.

For getting the server up and running:
 - Port:  Which port the server should run on.
 - Email Hash:  The email hash *key* available via *domainkey*.asknice.ly/appstore/discover/websurvey/configure.  **As a security note, this hash should never be in any request or response made through your services.  It should only exist in two places - your servers, and in your asknice.ly environment.**

To start the server, simply run the following command from inside your /sample-server/ folder:
```
node server.js
```
The code will load up a simple Node-based server and begin listening to local connections; it provides a very basic example of the request/response format that AskNicely expects, as well as how to use a user's email to generate a secure email hash based on the email provided.  Your own implementation may vary heavily from this example.

##### iOS/Android Code Samples
You should be able to simply load the code into your workspace with no further troubles; just build and away you go.  The sample apps here have been built with the most standard libraries recommended by Apple and Google, and tested under their standard simulators.

## Process Flow High Level Overview
AskNicely's in-app survey flow requires a few steps done in sequence:
 - Firstly, generating a email hash using your email hash *key* (see above) and the user's email, which then needs to actually reach your user.  An example of this can be found in [server.js](/sample-server/server.js), which will respond to any request with said credentials.
 - Secondly, your mobile app needs to obtain that survey setup information from your server.  We have samples for [iOS](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-ios-inapp/an-ios-inapp/AskNicelyClient.swift#L10) and [Android](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-android-inapp/app/src/main/java/com/example/an_android_inapp/MainActivity.kt#L80) available.
 - Thirdly, your mobile app needs to take that response and request a *slug* from your AskNicely instance.  See [here for iOS](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-ios-inapp/an-ios-inapp/AskNicelyClient.swift#L29), and [here for Android](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-android-inapp/app/src/main/java/com/example/an_android_inapp/MainActivity.kt#L98).  This process prepares your AskNicely instance to receive a response specifically from this user, and helps to prevent spam.
 - Fourthly, your mobile app will now be able to display the survey by using the result of that request to display a Javascript enabled WebView appropriate for your app.  As shown [here for iOS](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-ios-inapp/an-ios-inapp/AskNicelySurveyView.swift#L23) and [here for Android](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-android-inapp/app/src/main/java/com/example/an_android_inapp/MainActivity.kt#L132).  Note the query string components that need to be part of the URL, they're necessary for the correct display of your survey.
 - Finally, when the user has finished with your survey, our Javascript code will attempt to send a message back to your app so that you may remove/hide the WebView.  To do this, make sure to have your WebView listen for messages from our `askNicelyInterface` ([iOS](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-ios-inapp/an-ios-inapp/AskNicelySurveyView.swift#L30) and [Android](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-android-inapp/app/src/main/java/com/example/an_android_inapp/MainActivity.kt#L125)), and listen for the `askNicelyDone` message ([iOS](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-ios-inapp/an-ios-inapp/AskNicelySurveyView.swift#L48) and [Android](https://github.com/asknicely/asknicely-mobile-inapp-samples/blob/c0d1796e35717cf6ecb01eb37bfd667901f8bd82/an-android-inapp/app/src/main/java/AskNicelyAppInterface.java#L27)), which will also provide a status message in the event of an error.


## Contributing
We are currently not accepting contributions from outside our organisation.

## License
[AskNicely not currently allowing the licensing of any part of this repository.  It is only being provided as an example of our survey process flow](https://choosealicense.com/no-permission/)