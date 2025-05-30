package com.github.lordrip.intellijtest.fileEditor

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBase
import com.intellij.ui.jcef.JBCefJSQuery
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandler
import org.cef.network.CefRequest

class MyFileWebViewController(
    private val parentDisposable: Disposable, private val file: VirtualFile
) : Disposable {
    val pluginDomain = "my-editor-jetbrains-plugin"
    val pluginUrl = "https://$pluginDomain/index.html"
    val webappPath = System.getProperty("excalidraw.internal.webappPath", "UNSET")
    val webappExcalidrawAssetsPath = System.getProperty("excalidraw.internal.webappExcalidrawAssetsPath", "UNSET")
    val jcefPanel =
        JBCefBrowser.createBuilder().setEnableOpenDevToolsMenuItem(true).setOffScreenRendering(false).build()

    val jsQuery = JBCefJSQuery.create(jcefPanel as JBCefBrowserBase)

    init {
        Disposer.register(parentDisposable, this)

        initJCefPanel()
    }

    override fun dispose() {
    }

    private fun initJCefPanel() {
        Disposer.register(this, jcefPanel)
        Disposer.register(this, jsQuery)

        jsQuery.addHandler { message ->
            println("📨 Received from JS: $message")
            null
        }

        jcefPanel.jbCefClient.addLoadHandler(object : CefLoadHandler {
            override fun onLoadingStateChange(
                browser: CefBrowser,
                isLoading: Boolean,
                canGoBack: Boolean,
                canGoForward: Boolean
            ) {
                // Handle loading state changes if needed
            }

            override fun onLoadStart(
                p0: CefBrowser?,
                p1: CefFrame?,
                p2: CefRequest.TransitionType?
            ) {
                // Handle load start if needed
            }

            override fun onLoadEnd(browser: CefBrowser?, frame: CefFrame?, httpStatusCode: Int) {
                if (frame?.isMain ?: false) {
                    browser?.executeJavaScript(
                        """
                                    window.sendToIntelliJ = function(data) {
                                        ${jsQuery.inject("data")}
                                    };
                                    console.log("Injected sendToIntelliJ");
                                    """.trimIndent(),
                        browser.url,
                        0
                    )
                }
            }

            override fun onLoadError(
                browser: CefBrowser,
                frame: CefFrame,
                errorCode: CefLoadHandler.ErrorCode,
                errorText: String,
                failedUrl: String
            ) {
                // Handle load errors if needed
            }
        }, jcefPanel.cefBrowser)

//        jcefPanel.loadURL("https://kaotoio.github.io/kaoto/")
        jcefPanel.loadURL("http://localhost:5173")
    }
}
