import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.sun.istack.NotNull;

import java.util.List;

public class CommentUncommentBlockAction_Red extends CommentUncommentBlockAction {

    public CommentUncommentBlockAction_Red() {
        type = RangeHighlightHandler.MarkerType.RED;
    }
}