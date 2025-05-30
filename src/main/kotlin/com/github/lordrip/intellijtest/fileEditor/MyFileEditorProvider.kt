package com.github.lordrip.intellijtest.fileEditor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jetbrains.annotations.NonNls

class MyFileEditorProvider : FileEditorProvider {
    override fun accept(
        p0: Project, p1: VirtualFile
    ): Boolean {
        // TODO: Implement your logic to determine if the file should be handled by this editor
        return true;
    }

    override fun getEditorTypeId(): @NonNls String {
        return "my-file-editor"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }

    override fun createEditor(
        p0: Project, p1: VirtualFile
    ): FileEditor {
        return MyFileEditor(p0, p1)
    }

}
