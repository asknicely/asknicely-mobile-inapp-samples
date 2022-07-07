//
//  an_ios_inappApp.swift
//  an-ios-inapp
//
//  Created by Alexander Maehl on 7/07/22.
//

import SwiftUI

@main
struct an_ios_inappApp: App {
    
    init() {
        let url = URL(string: "http://localhost:8080")!
    
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
    
        let task = URLSession.shared.dataTask(with: url) { data, response, error in
            if let data = data, let string = String(data: data, encoding: .utf8){
                UserDefaults.standard.register(defaults: [
                    "surveySetupData": string
                ])
            } else if let error = error {
                print("HTTP Request Failed \(error)")
            }
        }
    
        task.resume()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
