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

public class CommentUncommentBlockAction_Green extends CommentUncommentBlockAction {

    public CommentUncommentBlockAction_Green() {
        type = RangeHighlightHandler.MarkerType.GREEN;
    }
}