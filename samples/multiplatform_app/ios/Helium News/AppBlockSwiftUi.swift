//
//  AppBlockSwiftUi.swift
//  Helium News
//
//  Created by Verges, Joaquim on 8/30/20.
//  Copyright © 2020 Verges, Joaquim. All rights reserved.
//

import Foundation
import SwiftUI
import NewsCommon

// TODO move to shared lib
@available(iOS 13.0, *)
class ObservableUiBlock<S, E> : ObservableObject where S: BlockState, E: BlockEvent {
    @Published var state : S? = nil
    var eventDispatcher = EventDispatcher<E>()
    lazy var block = UiBlock<S, E>(
        eventDispatcher: eventDispatcher,
        renderer: { state in
            self.state = state
    })
}

// TODO move to shared lib
@available(iOS 13.0, *)
public struct AppBlockSwiftUi<S : BlockState, E: BlockEvent, V: View> : View {
    var logic: LogicBlock<S,E>
    var viewFactory: (S?, EventDispatcher<E>) -> V
    @ObservedObject var ob = ObservableUiBlock<S, E>()
    
    init(logic: LogicBlock<S,E>, view: @escaping (S?, EventDispatcher<E>) -> V) {
        self.logic = logic
        self.viewFactory = view
        AppBlock<S, E>(logic: logic, ui: ob.block).assemble(lifecycleWrapper: LifecycleWrapper())
    }
    
    public var body: some View {
        return self.viewFactory(self.ob.state, self.ob.eventDispatcher)
    }
}

public func toEnvObject<T>(value: T) -> ObservableHolder<T> {
    return ObservableHolder(value: value)
}

public class ObservableHolder<T>: ObservableObject {
    var value : T
    
    init(value: T) {
        self.value = value
    }
}
