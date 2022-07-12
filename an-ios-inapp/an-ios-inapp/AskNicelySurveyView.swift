//
//  AskNicelySurveyView.swift
//  an-ios-inapp
//
//  Created by Nick Robinson on 11/07/22.
//

import SwiftUI
import WebKit

struct AskNicelySurveyView: UIViewRepresentable {
    private let messageHandler: AskNicelyMessageHandler
    
    var url: URL
    
    init(messageHandler: AskNicelyMessageHandler) {
        self.messageHandler = messageHandler
        let surveySetupData: Dictionary<String, Any> = UserDefaults.standard.object(forKey: "surveySetupData") as? [String: Any] ?? [:]
        let surveySlugData: Dictionary<String, Any> = UserDefaults.standard.object(forKey: "surveySlugData") as? [String: Any] ?? [:]
        let slug: String = surveySlugData["slug"] as! String
        let domainKey: String = surveySetupData["domain_key"] as! String
        let queryItems = [URLQueryItem(name: "template_name", value: surveySetupData["template_name"] as? String ?? ""), URLQueryItem(name: "inapp", value: "")]
        var urlComps = URLComponents(string: "https://\(domainKey).asknice.ly/email/conversation/\(slug)")!
        urlComps.queryItems = queryItems
        url = urlComps.url!
    }

    func makeUIView(context: Context) -> WKWebView {
        let config = WKWebViewConfiguration()
        config.userContentController.add(messageHandler, name: "askNicelyInterface")
        let webView = WKWebView(frame: CGRect.zero, configuration: config)
        return webView
    }
 
    func updateUIView(_ webView: WKWebView, context: Context) {
        let request = URLRequest(url: url)
        webView.load(request)
    }
}

class AskNicelyMessageHandler: NSObject, WKScriptMessageHandler {
    func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        let anMessage = message.body as? String
        if let anData = anMessage!.data(using: .utf8) {
            let anJson = try? JSONSerialization.jsonObject(with: anData, options: [])
            if let anResult = anJson as? [String : String] {
                if let event = anResult["event"], let msg = anResult["msg"] {
                    if event == "askNicelyDone" && msg == "Success" {
                        NotificationCenter.default.post(name: NSNotification.HideAskNicelySurvey, object: nil)
                    }
                }
            }
        }
    }
}
