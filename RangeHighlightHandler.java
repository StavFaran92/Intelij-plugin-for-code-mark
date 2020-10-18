import com.intellij.diff.Block;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.Range;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@State(
        name = "ProfileState", storages = {
        @Storage("StackInTheFlow-profile.xml")
})
public class RangeHighlightHandler implements PersistentStateComponent<RangeHighlightHandler> {

    @Nullable
    @Override
    public RangeHighlightHandler getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull RangeHighlightHandler rangeHighlightHandler) {
        XmlSerializerUtil.copyBean(rangeHighlightHandler, this);
    }

    public enum MarkerType{
        GREEN,
        RED,
        BLUE
    }

    private static final Map<MarkerType, BlockList> blockListMap = new HashMap<MarkerType, BlockList>(){
        {
            put(MarkerType.GREEN, new BlockList());
            put(MarkerType.RED, new BlockList());
            put(MarkerType.BLUE, new BlockList());
        }
    };

    public static void addToRangeHighlightList(MarkerType type, RangeHighlighter rh){
        blockListMap.get(type).addToList(rh);
    }

    public static void removeRangeHighlightFromList(MarkerType type, int from, int to){
        blockListMap.get(type).getMap().subMap(from, to).clear();
    }

    public static void removeRangeHighlightFromList(RangeHighlighter toRemove){
        for(BlockList block : blockListMap.values()){
            TreeMap<Integer, RangeHighlighter> mMap = block.getMap();
            mMap.remove(toRemove.getStartOffset());

            if(mMap.isEmpty())
                block.setBlockComment(false);
        }
    }

    public static TreeMap<Integer, RangeHighlighter> getHighlightMap(MarkerType type){
        return blockListMap.get(type).getMap();
    }

    public static boolean isBlockComment(MarkerType type){
        return blockListMap.get(type).getBlockComment();
    }

    public static void setBlockComment(MarkerType type, boolean value){
        blockListMap.get(type).setBlockComment(value);
    }

    public static Iterator<TreeMap<Integer, RangeHighlighter>> getRangeHighlighterIterator(MarkerType filter){

        ArrayList<TreeMap<Integer, RangeHighlighter>> aList = new ArrayList<>();
        for(Map.Entry<MarkerType, BlockList> entry: blockListMap.entrySet()) {
            if(entry.getKey() == filter)
                continue;
            aList.add(entry.getValue().getMap());
        }
        return aList.iterator();
    }

    private static class BlockList{
        private TreeMap<Integer, RangeHighlighter> mMap;
        private boolean isBlockComment;

        public BlockList(){
            mMap = new TreeMap<>();
            isBlockComment = false;
        }

        public boolean getBlockComment(){
            return isBlockComment;
        }

        public void setBlockComment(boolean value){
            isBlockComment = value;
        }

        public void addToList(RangeHighlighter rhl){
            mMap.put(rhl.getStartOffset(), rhl);
        }

        public TreeMap<Integer, RangeHighlighter> getMap(){
            return mMap;
        }
    }
}
