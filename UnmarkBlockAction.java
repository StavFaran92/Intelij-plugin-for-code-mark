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
import org.apache.commons.collections.iterators.EntrySetMapIterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UnmarkBlockAction extends AnAction {

    public UnmarkBlockAction() {
        super("Remove Marker");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);

        final Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();

        int offset = primaryCaret.getOffset();

        List<RangeHighlighter> mList = RangeHighlightHandler.getHighlightMap(RangeHighlightHandler.MarkerType.GREEN);

        for(RangeHighlighter rhl: mList){
            int start = rhl.getStartOffset();
            int end = rhl.getEndOffset();

            if(offset > start && offset < end){
                RangeHighlightHandler.removeRangeHighlightFromList(rhl);
                editor.getMarkupModel().removeHighlighter(rhl);
                break;
            }
        }

*/
/*        Iterator<Map.Entry<RangeHighlightHandler.MarkerType, ArrayList<RangeHighlighter>>> iter =
                RangeHighlightHandler.getRangeMapEntrySet().iterator();

        while (iter.hasNext()) {
            for(RangeHighlighter rhl: iter.next().getValue()){
                int start = rhl.getStartOffset();
                int end = rhl.getEndOffset();

                if(offset > start && offset < end){
                    RangeHighlightHandler.removeRangeHighlightFromList(rhl);
                    editor.getMarkupModel().removeHighlighter(rhl);
                    break;
                }
            }
        }
        iter.remove();*//*

    }
}*/
