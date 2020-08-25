//
//  ContentView.swift
//  Helium News
//
//  Created by Verges, Joaquim on 8/13/20.
//  Copyright © 2020 Verges, Joaquim. All rights reserved.
//

import SwiftUI
import NewsCommon
import SDWebImage
import SDWebImageSwiftUI

struct ContentView: View {

    var state: DataLoadState<ArticleResponse>?
    var eventDispatcher: EventDispatcher<BlockEvent>
    
    
    var body: some View {
        NavigationView {
            generateView.navigationBarTitle(Text("Helium News"))
        }
    }
    
    // cannot have a switch statement inside the body var direclty :|
    private var generateView: some View {
        switch self.state {
            case is DataLoadStateLoading<ArticleResponse>: return AnyView(Text("Loading..."))
            case let loaded as DataLoadStateReady<ArticleResponse>: return AnyView(
                List(loaded.data!.articles, id: \.title) { item in
                    ArticleItemView(article: item as Article)
                })
            default: return AnyView(Text("Error"))
        }
    }
    
    struct ArticleItemView: View {
        var article: Article
        
        var body: some View {
            VStack(alignment: HorizontalAlignment.leading) {
                HStack(spacing: 20) {
                    ImageView(url: article.urlToImage)
                    VStack(alignment: HorizontalAlignment.leading, spacing: 5) {
                        Text(article.source?.name ?? "").foregroundColor(Color.gray)
                        Text(article.title ?? "")
                    }
                }
                
            }.padding([.vertical], 5)
        }
    }
    
    struct ImageView: View {
        var url: String?
        
        var body: some View {
            loadImage(url: url)
        }
        
        func loadImage(url: String?) -> some View {
            if let wrapper = URL(string: url ?? "") {
                return AnyView(
                        WebImage(url: wrapper)
                        .resizable()
                        .scaledToFill()
                        .frame(minWidth: 100.0, maxWidth: 100.0, minHeight: 100.0, maxHeight: 100.0)
                        .clipShape(RoundedRectangle(cornerRadius: 8.0))
                )
            } else {
                return AnyView(Rectangle().frame(minWidth: 100.0, maxWidth: 100.0, minHeight: 100.0, maxHeight: 100.0).foregroundColor(Color.gray))
            }
        }
    }
}
