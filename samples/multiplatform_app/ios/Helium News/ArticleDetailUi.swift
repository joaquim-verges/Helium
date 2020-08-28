//
//  ArticleDetailUi.swift
//  Helium News
//
//  Created by Verges, Joaquim on 8/28/20.
//  Copyright © 2020 Verges, Joaquim. All rights reserved.
//

import SwiftUI
import Foundation
import NewsCommon

struct ArticleDetailUi: View {
    var state: ArticleDetailLogic.DetailState?
    var eventDispatcher: EventDispatcher<ArticleDetailLogic.DetailEvent>
    
    var body: some View {
        Text(self.state?.article.title ?? "").onTapGesture {
            self.eventDispatcher.pushEvent(event: ArticleDetailLogic.DetailEventArticleClosed())
        }
    }
}
