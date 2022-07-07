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
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

func buttonPressed() {

    @AppStorage("surveySetupData") var surveySetupData = "blank"
    
    print(surveySetupData)

}
