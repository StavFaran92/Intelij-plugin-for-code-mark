import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.sun.istack.NotNull;

import java.awt.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;


public class AddOrRemoveBlockMarkAction extends AnAction {

    protected RangeHighlightHandler.MarkerType type = null;

    boolean isEnabled_typeOverlap = false;

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

        TreeMap<Integer, RangeHighlighter> mMap = RangeHighlightHandler.getHighlightMap(type);

        /* Checks the appropriate list if the caret is placed inside of it, if so it removes the highlight
         * from list and uncomment the block of code.*/
        if(!editor.getSelectionModel().hasSelection() && !mMap.isEmpty()) {
            RangeHighlighter rhl = mMap.floorEntry(primaryCaret.getOffset()).getValue();
            int start = rhl.getStartOffset();
            int end = rhl.getEndOffset();

            if (offset >= start && offset <= end) {

                if (RangeHighlightHandler.isBlockComment(type)) {

                    CommentUncommentBlockAction.uncommentBlock(project, document, rhl);
                }

                RangeHighlightHandler.removeRangeHighlightFromList(rhl);
                editor.getMarkupModel().removeHighlighter(rhl);
            }
        }

        /* If the caret is inside a region that is not in the blockList then it highlights the area
         * and adds it to the database. */
        else {
            int start = primaryCaret.getSelectionStart();
            int end = primaryCaret.getSelectionEnd();
            if( !isOverlapOtherRHLAlgorithm(start, end)) {

                Color color = null;
                switch (type) {
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


                if(isEnabled_typeOverlap) {
                    Map.Entry<Integer, RangeHighlighter> entry_start = mMap.floorEntry(start);
                    if (entry_start != null) {
                        RangeHighlighter nearestRhl = entry_start.getValue();

                        if (nearestRhl.getEndOffset() > start) {
                            start = nearestRhl.getStartOffset();
                        }
                    }
                    Map.Entry<Integer, RangeHighlighter> entry_end = mMap.floorEntry(end);
                    if (entry_end != null) {
                        RangeHighlighter nearestRhl = entry_end.getValue();
                        if (nearestRhl.getStartOffset() < end && nearestRhl.getStartOffset() < start)
                            end = nearestRhl.getEndOffset();
                    }

                    RangeHighlightHandler.removeRangeHighlightFromList(type, start, end);
                }

                //This creates the highlight area
                RangeHighlighter rhl = addRangeHighlighter(editor, start, end, HighlightLayerEx.CODE_BLOCK_LAYER_1, background(color));

                RangeHighlightHandler.addToRangeHighlightList(type, rhl);

                // If the BlockHighLight is in comment mode then you would want the now codeBlock
                //to be commented as well.
                if (RangeHighlightHandler.isBlockComment(type)) {
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
        e.getPresentation().setEnabledAndVisible(
                project != null
                && editor != null);
    }

    public static void foo(){
        return;
    }

    private RangeHighlighter addRangeHighlighter(Editor editor, int startOffset, int endOffset, int layer, TextAttributes attributes) {
        return editor.getMarkupModel().addRangeHighlighter(startOffset, endOffset, layer, attributes, HighlighterTargetArea.EXACT_RANGE);
    }

    @NotNull
    private static TextAttributes background(Color color) {
        return new TextAttributes(null, color, null, null, Font.PLAIN);
    }

    private boolean isOverlapOtherRHLAlgorithm(int start, int end){
        boolean isOverlap = false;

        Iterator<TreeMap<Integer, RangeHighlighter>> iter = RangeHighlightHandler.getRangeHighlighterIterator(type);
        while(iter.hasNext()){
            TreeMap<Integer, RangeHighlighter> map = iter.next();
            Integer nearestRhlToStart = map.floorKey(start);
            Integer nearestRhlToEnd = map.floorKey(end);

            // This means both the end and the start are null therefore no need to check overlap
            if(nearestRhlToEnd == null)
                continue;

            // This means the closest RHL to the start of the selection is null but the closest RHL to the end
            // of the selection is not null which means the is an RHL between them and there is an overlap
            if(nearestRhlToStart == null) {
                isOverlap = true;
                break;
            }

            // this part checks if at least 1 of the 2 conditions is met:
            // 1) the nearest RHL's end overlaps the beginning of the selection
            // 2) the start's nearest RHL and end's nearest RHL are not the same RHL
            //Both conditions imply there is an overlap.
            RangeHighlighter nearestRhl = map.floorEntry(start).getValue();

            if(nearestRhl.getEndOffset() > start || !nearestRhlToEnd.equals(nearestRhlToStart)) {
                isOverlap = true;
                break;
            }
        }
        return isOverlap;
    }

    private void handleInnertTypeOverlapsAlgorithm(TreeMap<Integer, RangeHighlighter> mMap, int start, int end){
        int nearestRhlToStartOffset = start;
        int nearestRhlToEndOffset = end;

        Map.Entry<Integer, RangeHighlighter> entry_start = mMap.floorEntry(start);
        if(entry_start != null) {
            RangeHighlighter nearestRhl = entry_start.getValue();
            nearestRhlToStartOffset = nearestRhl.getStartOffset();
            if (nearestRhl.getEndOffset() > start)
                start = nearestRhl.getStartOffset();
        }
        Map.Entry<Integer, RangeHighlighter> entry_end = mMap.floorEntry(end);
        if(entry_end != null) {
            RangeHighlighter nearestRhl = entry_end.getValue();
            nearestRhlToEndOffset = nearestRhl.getEndOffset();
            if (nearestRhl.getStartOffset() < end)
                end = nearestRhl.getEndOffset();
        }

        RangeHighlightHandler.removeRangeHighlightFromList(type, nearestRhlToStartOffset, nearestRhlToEndOffset);
    }
}
