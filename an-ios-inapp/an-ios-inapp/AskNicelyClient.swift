//
//  AskNicelyClient.swift
//  an-ios-inapp
//
//  Created by Nick Robinson on 11/07/22.
//

import Foundation

func getANSetup() -> URLSessionDataTask {
    let url = URL(string: "http://localhost:8083")!

    var request = URLRequest(url: url)
    request.httpMethod = "GET"
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")

    return URLSession.shared.dataTask(with: request) { data, response, error in
        if let data = data {
            let json = try? JSONSerialization.jsonObject(with: data, options: [])
            if let dictionary = json as? [String: Any] {
                UserDefaults.standard.set(dictionary, forKey: "surveySetupData")
            }
        } else if let error = error {
            print("HTTP Request Failed \(error)")
        }
    }
}

func getANSlug(force: Bool, aCustomProperty: String) -> URLSessionDataTask {
    let surveySetupData: Dictionary<String, Any> = UserDefaults.standard.object(forKey: "surveySetupData") as? [String: Any] ?? [:]
    let body: [String: Any] = [
        "domain_key": surveySetupData["domain_key"]!,
        "template_name": surveySetupData["template_name"]!,
        "name": surveySetupData["name"]!,
        "email": surveySetupData["email"]!,
        "email_hash": surveySetupData["email_hash"]!,
        "created": surveySetupData["created"]!,
        "force": force,
        "a_custom_property": aCustomProperty
    ]
            
    let jsonData = try? JSONSerialization.data(withJSONObject: body)
    let domainKey : String = surveySetupData["domain_key"] as! String
    var uuid = UUID().uuidString
    uuid.removeAll(where: { $0 == "-" })
    uuid = String(uuid.prefix(16))
    let queryItems = [URLQueryItem(name: "id", value: uuid), URLQueryItem(name: "anVersion", value: "3.4.1"), URLQueryItem(name: "reloadCookie", value: "")]
    var urlComps = URLComponents(string: "https://\(domainKey).asknice.ly/service/inapp.php")!
    urlComps.queryItems = queryItems
    let url = urlComps.url!

    var request = URLRequest(url: url)
    request.httpMethod = "POST"
    request.setValue("\(String(describing: jsonData?.count))", forHTTPHeaderField: "Content-Length")
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    request.httpBody = jsonData

    return URLSession.shared.dataTask(with: request) { data, response, error in
        if let data = data {
            let json = try? JSONSerialization.jsonObject(with: data, options: [])
            if let dictionary = json as? [String: Any] {
                UserDefaults.standard.set(dictionary, forKey: "surveySlugData")
            }
        } else if let error = error {
            print("HTTP Request Failed \(error)")
        }
    }
}
