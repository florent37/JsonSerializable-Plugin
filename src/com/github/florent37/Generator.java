package com.github.florent37;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generator extends AnAction {
    private static final String JSON_PACKAGE_IMPORT = "import 'package:json_annotation/json_annotation.dart';";
    private static final String ANNOTATION = "@JsonSerializable()";
    private static final String CLASS_REGEX = "class(.*?)(extends(.*?))?(implements(.*?))?\\{";
    private static final Pattern CLASS_PATTERN = Pattern.compile(CLASS_REGEX);

    private DartDocument dartDocument;
    private String className = "";

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        e.getPresentation().setEnabled(false);

        //only display on .dart files
        final Project project = e.getProject();
        if (project != null) {
            final Editor editor = e.getData(CommonDataKeys.EDITOR);
            final PsiFile file = PsiUtilBase.getPsiFileInEditor(editor, project);
            if (file != null) {
                String fileName = file.getName();
                if (fileName.contains(".dart")) {
                    e.getPresentation().setEnabled(true);
                }
            }
        }
    }

    private void injectImport() {
        if (!dartDocument.contains(JSON_PACKAGE_IMPORT)) {
            dartDocument.insertAt(0, JSON_PACKAGE_IMPORT + "\n");
        }
    }

    private void injectPart() {
        final String partImport = "part '" + dartDocument.getFileName() + ".g.dart';";
        if (!dartDocument.contains(partImport)) {
            final int line = dartDocument.findLine("import ", StringUtils.IndexType.Last) + 1;
            dartDocument.insertAt(line, "\n" + partImport + "\n");
        }
    }

    private void injectAnnotation() {
        if (!dartDocument.contains(ANNOTATION)) {
            final int classLine = getClassLine() - 1;
            dartDocument.insertAt(classLine, ANNOTATION + "\n");
        }
    }

    private void injectFactory() {
        final StringBuilder jsonMethods = new StringBuilder();
        if (!dartDocument.contains("_$" + className + "FromJson(json);")) {
            final String fromJson = "\tfactory " + className
                    + ".fromJson(Map<String, dynamic> json) => _$" + className
                    + "FromJson(json);";
            jsonMethods.append(fromJson);
        }
        if (!dartDocument.contains("_$" + className + "ToJson(this);")) {
            final String toJson = "\tMap<String, dynamic> toJson( instance) => _$"
                    + className
                    + "ToJson(this);";
            if (jsonMethods.length() == 0) {
                jsonMethods.append(toJson);
            } else {
                jsonMethods.append("\n").append(toJson);
            }
        }

        if (jsonMethods.length() > 0) {
            final int line = getClassLine();
            dartDocument.insertAt(line, "\n" + jsonMethods.toString() + "\n");
        }

    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        dartDocument = new DartDocument(e);
        className = findClassName(dartDocument.getFileContent());
        if (className != null && !className.trim().equals("")) {
            injectImport();
            injectAnnotation();
            injectPart();
            injectFactory();
        }
    }

    private int getClassLine() {
        return dartDocument.findLine(className + " ", StringUtils.IndexType.First);
    }

    private String findClassName(String fileContent) {
        if (fileContent == null || fileContent.equals("")) {
            return null;
        }

        final Matcher m = CLASS_PATTERN.matcher(fileContent);
        while (m.find()) {
            return m.group(1).trim();
        }

        return null;
    }
}
