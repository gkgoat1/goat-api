package at.gkgo.api.hook;

import at.gkgo.api.util.Utils;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;

import java.util.function.Function;

public interface BlockApiLookupHook<A,C> {
    void registerAppend(BlockApiLookup.BlockApiProvider<Function<A,A>,C> append);
    static<A,C> BlockApiLookupHook<A,C> of(BlockApiLookup<A,C> apiLookup){
        return Utils.unsafeCoerce(apiLookup);
    }
    static<A,C> BlockApiLookup<A,C> of(BlockApiLookupHook<A,C> apiLookup){
        return Utils.unsafeCoerce(apiLookup);
    }
}
