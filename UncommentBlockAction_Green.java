/*
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.sun.istack.NotNull;

import java.util.List;

public class UncommentBlockAction_Green extends AnAction {

    public UncommentBlockAction_Green() {
        super("Uncomment block");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();

        List<RangeHighlighter> mList = RangeHighlightHandler.getHighlightList(RangeHighlightHandler.MarkerType.GREEN);

        for(RangeHighlighter rhl: mList){
            int start = rhl.getStartOffset();
            int end = rhl.getEndOffset();

            WriteCommandAction.runWriteCommandAction(project, () -> {
                document.deleteString(start-2, start);
                document.deleteString(end-2, end);
            });
        }
    }
}*/