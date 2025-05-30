package com.github.lordrip.intellijtest.fileEditor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.NonNls

class MyFileEditorProvider : FileEditorProvider {
    override fun accept(
        project: Project, virtualFile: VirtualFile
    ): Boolean {
        return virtualFile.name.endsWith(".camel.yaml")
                || virtualFile.name.endsWith(".camel.xml")
                || virtualFile.name.endsWith(".kamelet.yaml")
                || virtualFile.name.endsWith(".pipe.yaml")
    }

    override fun getEditorTypeId(): @NonNls String {
        return "my-file-editor"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    override fun createEditor(
        project: Project, virtualFile: VirtualFile
    ): FileEditor {
        return MyFileEditor(project, virtualFile)
    }

}
