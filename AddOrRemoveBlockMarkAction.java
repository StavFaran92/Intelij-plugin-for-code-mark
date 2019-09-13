import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
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


public class AddOrRemoveBlockMarkAction extends AnAction {

    protected RangeHighlightHandler.MarkerType type = null;

    public AddOrRemoveBlockMarkAction() {
        super("Add Marker");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        // Get access to the editor and caret model. update() validated editor's existence.
        final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        final Document document = editor.getDocument();
        final CaretModel caretModel = editor.getCaretModel();
        // Getting the primary caret ensures we get the correct one of a possible many.
        final Caret primaryCaret = caretModel.getPrimaryCaret();

        int offset = primaryCaret.getOffset();

        boolean isFound = false;

        List<RangeHighlighter> mList = RangeHighlightHandler.getHighlightList(type);

        for(RangeHighlighter rhl: mList){
            int start = rhl.getStartOffset();
            int end = rhl.getEndOffset();

            if(offset >= start && offset <= end){

                if(RangeHighlightHandler.getState(type)) {

                    CommentUncommentBlockAction.uncommentBlock(project, document, rhl);
                }

                RangeHighlightHandler.removeRangeHighlightFromList(rhl);
                editor.getMarkupModel().removeHighlighter(rhl);

                isFound = true;
                break;
            }
        }

        if(!isFound) {
            if(editor.getSelectionModel().hasSelection()) {
                int start = primaryCaret.getSelectionStart();
                int end = primaryCaret.getSelectionEnd();

                Color color = null;
                switch(type) {
                    case GREEN:
                        color = JBColorEx.green_light;
                        break;
                    case RED:
                        color = JBColorEx.red_light;
                        break;
                    case BLUE:
                        color = JBColorEx.blue_light;
                        break;
                }

                RangeHighlighter rhl = addRangeHighlighter(editor, start, end, HighlightLayerEx.CODE_BLOCK_LAYER_1, background(color));

                RangeHighlightHandler.addToRangeHighlighter(type, rhl);

                if(RangeHighlightHandler.getState(type)){
                    CommentUncommentBlockAction.commentBlock(project, document, rhl);
                }
            }
        }

        primaryCaret.removeSelection();
    }

    @Override
    public void update(@NotNull final AnActionEvent e) {
        //Get required data keys
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor and if a selection exists
        e.getPresentation().setEnabledAndVisible( project != null
                && editor != null);
    }


    private RangeHighlighter addRangeHighlighter(Editor editor, int startOffset, int endOffset, int layer, TextAttributes attributes) {
        return editor.getMarkupModel().addRangeHighlighter(startOffset, endOffset, layer, attributes, HighlighterTargetArea.EXACT_RANGE);
    }

    @NotNull
    private static TextAttributes background(Color color) {
        return new TextAttributes(null, color, null, null, Font.PLAIN);
    }
}
