package com.github.lordrip.intellijtest.fileEditor

import com.google.gson.JsonObject
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.jcef.JBCefBrowser
import com.intellij.ui.jcef.JBCefBrowserBase
import com.intellij.ui.jcef.JBCefJSQuery
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.handler.CefLoadHandler
import org.cef.network.CefRequest
import java.nio.charset.StandardCharsets

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

        val content: String = ApplicationManager.getApplication().runReadAction<String> {
            file.contentsToByteArray().toString(StandardCharsets.UTF_8)
        }

        jsQuery.addHandler { message ->
            println("📨 Received from JS: $message")
            null
        }

        jcefPanel.jbCefClient.addLoadHandler(object : CefLoadHandler {
            override fun onLoadingStateChange(
                browser: CefBrowser, isLoading: Boolean, canGoBack: Boolean, canGoForward: Boolean
            ) {
                // Handle loading state changes if needed
            }

            override fun onLoadStart(
                p0: CefBrowser?, p1: CefFrame?, p2: CefRequest.TransitionType?
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
                                    """.trimIndent(), browser.url, 0
                    )

                    val json = JsonObject()
                    json.addProperty("code", content)
                    json.addProperty("path", file.name)
                    browser?.executeJavaScript(
                        """
                            setTimeout(() => {
                              window.dispatchEvent(new CustomEvent("FILE_CONTENT_RECEIVED", {
                                detail: $json
                              }));
                              console.log("Dispatched FILE_CONTENT_RECEIVED");
                            }, 2000); // 2000ms delay
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
