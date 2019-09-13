import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import com.intellij.ui.JBColor;
import com.sun.istack.NotNull;

import java.awt.*;
import java.util.List;


public class AddOrRemoveBlockMarkAction_Green extends AddOrRemoveBlockMarkAction {
    public AddOrRemoveBlockMarkAction_Green() {
        type = RangeHighlightHandler.MarkerType.GREEN;
    }
}
