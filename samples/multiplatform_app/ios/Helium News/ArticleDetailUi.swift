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
import SDWebImage
import SDWebImageSwiftUI

struct ArticleDetailUi: View {
    var state: ArticleDetailLogic.DetailState?
    var eventDispatcher: EventDispatcher<ArticleDetailLogic.DetailEvent>
    
    var body: some View {
        VStack {
            HeroImage(url: self.state?.article.urlToImage).onTapGesture {
                self.eventDispatcher.pushEvent(event: ArticleDetailLogic.DetailEventArticleClosed())
            }
            VStack(alignment: HorizontalAlignment.leading, spacing: 20.0) {
                Text(self.state?.article.title ?? "").fontWeight(Font.Weight.bold).font(.title)
                Text(self.state?.article.description ?? "")
            }.padding(20.0)
        }.frame(maxHeight: .infinity, alignment: .top)
    }
    
    struct HeroImage: View {
        var url: String?
        
        var body: some View {
            loadImage(url: url)
        }
        
        func loadImage(url: String?) -> some View {
            if let wrapper = URL(string: url ?? "") {
                return AnyView(
                    WebImage(url: wrapper)
                        .resizable()
                        .scaledToFit()
                        .frame(maxHeight: 300)
                    
                )
            } else {
                return AnyView(Rectangle()
                    .frame(maxWidth: .infinity, maxHeight: 300)
                    .foregroundColor(Color.gray)
                )
            }
        }
    }
}
