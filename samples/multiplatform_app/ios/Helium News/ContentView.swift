//
//  ContentView.swift
//  Helium News
//
//  Created by Verges, Joaquim on 8/13/20.
//  Copyright © 2020 Verges, Joaquim. All rights reserved.
//

import SwiftUI
import NewsCommon

struct ContentView: View {
    
    var state: DataLoadState<NSString>?
    var eventDispatcher: EventDispatcher<BlockEvent>
    
    
    var body: some View {
        switch self.state {
        case is DataLoadStateLoading<NSString>:
            return Text("Loading...")
        case let loaded as DataLoadStateReady<NSString>:
            return Text(loaded.data as? String ?? "")
        default:
            return Text("Error")
        }
    }
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
