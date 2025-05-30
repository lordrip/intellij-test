package com.github.lordrip.intellijtest.fileEditor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.Nls
import java.awt.BorderLayout
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

class MyFileEditor(
    private val project: Project, private val virtualFile: VirtualFile
) : FileEditor {
    val webViewController = MyFileWebViewController(this, virtualFile)

    private val webView = object : JPanel(BorderLayout()) {
        init {
            add(webViewController.jcefPanel.component, BorderLayout.CENTER)
        }
    }

    override fun getComponent(): JComponent {
        return webViewController.jcefPanel.component
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return webViewController.jcefPanel.component
    }

    override fun getFile(): VirtualFile? {
        return virtualFile;
    }

    override fun getName(): @Nls(capitalization = Nls.Capitalization.Title) String {
        return "My File Editor"
    }

    override fun setState(p0: FileEditorState) {

    }

    override fun isModified(): Boolean {
        return false
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun addPropertyChangeListener(p0: PropertyChangeListener) {

    }

    override fun removePropertyChangeListener(p0: PropertyChangeListener) {

    }

    override fun <T : Any?> getUserData(p0: Key<T?>): T? {
        return null
    }

    override fun <T : Any?> putUserData(p0: Key<T?>, p1: T?) {

    }

    override fun dispose() {

    }
}
