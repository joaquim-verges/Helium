import Foundation
import SwiftUI
import HeliumCore

@available(iOS 13.0, *)
class ObservableUiBlock<S, E> : ObservableObject where S: BlockState, E: BlockEvent {
    @Published var state : S? = nil
    var eventDispatcher = EventDispatcher<E>()
    lazy var block = UiBlock<S, E>(
        eventDispatcher: eventDispatcher,
        renderer: { state in
            self.state = state
        }
    )
}

@available(iOS 13.0, *)
public struct SwiftUiBlock<S : BlockState, E: BlockEvent, V: View>: View {
    var viewFactory: (S?, EventDispatcher<E>) -> V
    @ObservedObject var ob = ObservableUiBlock<S, E>()

    public init(view: @escaping (S?, EventDispatcher<E>) -> V) {
        self.viewFactory = view
    }

    public var body: some View {
        return self.viewFactory(self.ob.state, self.ob.eventDispatcher)
    }

    public func block() -> UiBlock<S,E> {
        return ob.block
    }
}

public extension UIWindowSceneDelegate {
    func assemble<S:BlockState, E:BlockEvent, V: View>(logic: LogicBlock<S,E>, ui: SwiftUiBlock<S,E,V>) {
        AppBlock<S, E>(logic: logic, ui: ui.block()).assemble(lifecycleWrapper: LifecycleWrapper())
        // TODO scene coroutine scope
    }
}
