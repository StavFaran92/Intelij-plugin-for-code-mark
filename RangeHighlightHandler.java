import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.*;
import java.util.List;

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

    public static void addToRangeHighlighter(MarkerType type, RangeHighlighter rh){
        blockListMap.get(type).addToList(rh);
    }

    public static void removeRangeHighlightFromList(RangeHighlighter toRemove){
        for(BlockList block : blockListMap.values()){
            ArrayList<RangeHighlighter> mList = block.getList();
            mList.remove(toRemove);

            if(mList.isEmpty())
                block.setState(false);
        }
    }

    public static ArrayList<RangeHighlighter> getHighlightList(MarkerType type){
        return blockListMap.get(type).getList();
    }

    public static Set<Map.Entry<MarkerType, BlockList>> getRangeMapEntrySet(){
        return blockListMap.entrySet();
    }

    public static boolean getState(MarkerType type){
        return blockListMap.get(type).getState();
    }

    public static void setState(MarkerType type, boolean value){
        blockListMap.get(type).setState(value);
    }

    private static class BlockList{
        private ArrayList<RangeHighlighter> mList;
        private boolean state;

        public BlockList(){
            mList = new ArrayList<>();
            state = false;
        }

        public boolean getState(){
            return state;
        }

        public void setState(boolean value){
            state = value;
        }

        public void addToList(RangeHighlighter rhl){
            mList.add(rhl);
        }

        public ArrayList<RangeHighlighter> getList(){
            return mList;
        }
    }
}
