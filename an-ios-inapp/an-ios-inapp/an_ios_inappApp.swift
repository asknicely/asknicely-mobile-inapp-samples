//
//  an_ios_inappApp.swift
//  an-ios-inapp
//
//  Created by Alexander Maehl on 7/07/22.
//

import SwiftUI

extension NSNotification {
    static let ShowAskNicelySurvey = Notification.Name.init("ShowAskNicelySurvey")
    static let HideAskNicelySurvey = Notification.Name.init("HideAskNicelySurvey")
}

@main
struct an_ios_inappApp: App {
    @State private var showingSurvey = false
    
    init() {
        let task = getANSetup()
    
        task.resume()
    }
    
    var body: some Scene {
        WindowGroup {
            if showingSurvey {
                AskNicelySurveyView(messageHandler: AskNicelyMessageHandler())
                    .onReceive(NotificationCenter.default.publisher(for: NSNotification.HideAskNicelySurvey)) { _ in
                        showingSurvey = false
                    }
            } else {
                ContentView()
                    .onReceive(NotificationCenter.default.publisher(for: NSNotification.ShowAskNicelySurvey)) { _ in
                        showingSurvey = true
                    }
            }
        }
    }
}
