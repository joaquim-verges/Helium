//
//  AppUi.swift
//  Helium News
//
//  Created by Verges, Joaquim on 8/27/20.
//  Copyright © 2020 Verges, Joaquim. All rights reserved.
//

import SwiftUI
import Foundation
import NewsCommon

struct AppUi: View {
    
    var state: AppRouter.Screen?
    var eventDispatcher: EventDispatcher<BlockEvent>
    var appRouter: AppRouter
    
    var body: some View {
        generateScreen
    }
    
    private var generateScreen: some View {
        switch self.state {
        case is AppRouter.ScreenArticleList: return AnyView(ListScreen())
        case let detailState as AppRouter.ScreenArticleDetail: return AnyView(DetailScreen(article: detailState.article))
        default: return AnyView(Text("No App state received"))
        }
    }
    
    private func ListScreen() -> some View {
        let logic = ArticleListLogic(appRouter: appRouter, repo: NewsRepository(api: NewsApi()))
        return AppBlockSwiftUi(logic: logic) { state, eventDispatcher in
                ArticleListUi(state: state, eventDispatcher: eventDispatcher)
        }
    }
    
    private func DetailScreen(article: Article) -> some View {
        let logic = ArticleDetailLogic(article: article, appRouter: appRouter)
        return AppBlockSwiftUi(logic: logic) { state, eventDispatcher in
            ArticleDetailUi(state: state, eventDispatcher: eventDispatcher)
        }
    }
    
}
