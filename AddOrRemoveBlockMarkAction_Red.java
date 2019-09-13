import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.sun.istack.NotNull;

import java.awt.*;
import java.util.List;


public class AddOrRemoveBlockMarkAction_Red extends AddOrRemoveBlockMarkAction {
    public AddOrRemoveBlockMarkAction_Red() {
        type = RangeHighlightHandler.MarkerType.RED;
    }
}
