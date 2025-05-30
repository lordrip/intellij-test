package com.github.lordrip.intellijtest.fileEditor

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.jcef.JBCefBrowser

class MyFileWebViewController(
    private val parentDisposable: Disposable,
    private val file: VirtualFile
) : Disposable {
    val pluginDomain = "my-editor-jetbrains-plugin"
    val pluginUrl = "https://$pluginDomain/index.html"
    val webappPath = System.getProperty("excalidraw.internal.webappPath", "UNSET")
    val webappExcalidrawAssetsPath = System.getProperty("excalidraw.internal.webappExcalidrawAssetsPath", "UNSET")

    val jcefPanel = JBCefBrowser.createBuilder()
        .setEnableOpenDevToolsMenuItem(false)
        .setOffScreenRendering(false)
        .build()

    init {
        Disposer.register(parentDisposable, this)

        initJCefPanel()
    }

    override fun dispose() {
    }

    private fun initJCefPanel() {
        Disposer.register(this, jcefPanel)
        jcefPanel.loadURL("https://kaotoio.github.io/kaoto/")
    }
}
