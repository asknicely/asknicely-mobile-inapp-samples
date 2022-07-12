//
//  ContentView.swift
//  an-ios-inapp
//
//  Created by Alexander Maehl on 7/07/22.
//

import SwiftUI

struct ContentView: View {
    
    var body: some View {
        Button {
            buttonPressed()
        } label: {
            Image("in-app-survey_icon")
        }
    }
    
    func buttonPressed() {
        let task = getANSlug(force: true, aCustomProperty: "Some custom value")
        task.resume()
        NotificationCenter.default.post(name: NSNotification.ShowAskNicelySurvey, object: nil)
    }

}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
