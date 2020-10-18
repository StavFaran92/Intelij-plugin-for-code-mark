import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.project.Project;
import com.sun.istack.NotNull;

import java.util.List;
import java.util.TreeMap;

public class CommentUncommentBlockAction extends AnAction {

    protected RangeHighlightHandler.MarkerType type = null;

    public CommentUncommentBlockAction() {

        super("Comment block");
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        final Editor editor = anActionEvent.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = anActionEvent.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();

        TreeMap<Integer, RangeHighlighter> mMap = RangeHighlightHandler.getHighlightMap(type);

        /**
         * if the blockList is commented then iterate&uncomment and vice-versa.
         */
        if(!mMap.isEmpty()) {

            if (!RangeHighlightHandler.isBlockComment(type)) {

                for (RangeHighlighter rhl : mMap.values()) {

                    commentBlock(project, document, rhl);
                }

                RangeHighlightHandler.setBlockComment(type, true);
            } else {

                for (RangeHighlighter rhl : mMap.values()) {

                    uncommentBlock(project, document, rhl);
                }

                RangeHighlightHandler.setBlockComment(type, false);
            }
        }
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

    public static void commentBlock(Project project, Document document, RangeHighlighter rhl){
        int start = rhl.getStartOffset();
        int end = rhl.getEndOffset();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.insertString(start, "/*");
            document.insertString(end + 2, "*/");

            document.createGuardedBlock(start,start+2);
            document.createGuardedBlock(end + 2,end+4);
        });
    }

    public static void uncommentBlock(Project project, Document document, RangeHighlighter rhl){
        int start = rhl.getStartOffset();
        int end = rhl.getEndOffset();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.deleteString(start - 2, start);
            document.deleteString(end - 2, end);

            RangeMarker rm_start = document.createRangeMarker(start, start+2);
            RangeMarker rm_end = document.createRangeMarker(end-2, end);

            document.removeGuardedBlock(rm_start);
            document.removeGuardedBlock(rm_end);
        });
    }
}