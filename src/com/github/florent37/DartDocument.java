package com.github.florent37;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import javax.annotation.Nullable;

public class DartDocument {

    private final Editor editor;
    private final Project project;
    private final Document document;
    private final CaretModel caret;
    @Nullable
    private String fileContent;
    @Nullable
    private String fileName;

    public DartDocument(AnActionEvent e) {
        editor = e.getRequiredData(CommonDataKeys.EDITOR);
        project = e.getProject();
        document = editor.getDocument();
        caret = editor.getCaretModel();
        final PsiFile mFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (mFile != null) {
            fileName = mFile.getName();
            int dotIndex = fileName.indexOf('.');
            fileName = fileName.substring(0, dotIndex);
            invalidate();
        }
    }

    private void invalidate() {
        this.fileContent = document.getText();
    }

    public void insertAt(int line, String text) {
        caret.moveToVisualPosition(new VisualPosition(line, 0));
        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.insertString(caret.getOffset(), text);
        });
        invalidate();
    }

    public boolean contains(String text){
        if(fileContent == null) {
            return false;
        }
        return fileContent.contains(text);
    }

    public int findLine(String text, StringUtils.IndexType indexType) {
        return StringUtils.findLine(this.fileContent, text, indexType);
    }

    @Nullable
    public String getFileContent() {
        return fileContent;
    }

    public String getFileName() {
        return fileName;
    }

}
