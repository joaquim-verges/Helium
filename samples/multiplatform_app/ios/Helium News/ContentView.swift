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
    
    var state: CommonListLogic.State?
    var eventDispatcher: EventDispatcher<BlockEvent>
    
    
    var body: some View {
//        let s = self.state as? CommonListLogic.State
        return Text(state?.data ?? "")
    }
}

//struct ContentView_Previews: PreviewProvider {
//    static var previews: some View {
//        ContentView()
//    }
//}
