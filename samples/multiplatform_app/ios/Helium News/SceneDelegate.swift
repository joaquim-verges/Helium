//
//  SceneDelegate.swift
//  Helium News
//
//  Created by Verges, Joaquim on 8/13/20.
//  Copyright © 2020 Verges, Joaquim. All rights reserved.
//

import UIKit
import SwiftUI
import NewsCommon

// TODO in lib
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

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?


    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        // Use this method to optionally configure and attach the UIWindow `window` to the provided UIWindowScene `scene`.
        // If using a storyboard, the `window` property will automatically be initialized and attached to the scene.
        // This delegate does not imply the connecting scene or session are new (see `application:configurationForConnectingSceneSession` instead).

        // Create the SwiftUI view that provides the window contents.
        UITableView.appearance().separatorStyle = .none
        let appRouter = AppRouter()
        let ui = AppBlockSwiftUi(logic: appRouter) { state, dispatcher in
            AppUi(state: state, eventDispatcher: dispatcher, appRouter: appRouter)
        }

        // Use a UIHostingController as window root view controller.
        if let windowScene = scene as? UIWindowScene {
            let window = UIWindow(windowScene: windowScene)
            window.rootViewController = UIHostingController(rootView: ui)
            self.window = window
            window.makeKeyAndVisible()
        }
    }

    func sceneDidDisconnect(_ scene: UIScene) {
        // Called as the scene is being released by the system.
        // This occurs shortly after the scene enters the background, or when its session is discarded.
        // Release any resources associated with this scene that can be re-created the next time the scene connects.
        // The scene may re-connect later, as its session was not neccessarily discarded (see `application:didDiscardSceneSessions` instead).
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
        // Called when the scene has moved from an inactive state to an active state.
        // Use this method to restart any tasks that were paused (or not yet started) when the scene was inactive.
    }

    func sceneWillResignActive(_ scene: UIScene) {
        // Called when the scene will move from an active state to an inactive state.
        // This may occur due to temporary interruptions (ex. an incoming phone call).
    }

    func sceneWillEnterForeground(_ scene: UIScene) {
        // Called as the scene transitions from the background to the foreground.
        // Use this method to undo the changes made on entering the background.
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
        // Called as the scene transitions from the foreground to the background.
        // Use this method to save data, release shared resources, and store enough scene-specific state information
        // to restore the scene back to its current state.
    }


}

