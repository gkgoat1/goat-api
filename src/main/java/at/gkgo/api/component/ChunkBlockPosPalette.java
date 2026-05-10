package at.gkgo.api.component;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;
import net.minecraft.util.Util;


import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ChunkBlockPosPalette<T> {
    public CrudeIncrementalIntIdentityHashBiMap<T> palette;
    public Int2IntRBTreeMap contents;
    public<U> ChunkBlockPosPalette<U> map(BiFunction<BlockPos,T,U> mapper){
        var n = new ChunkBlockPosPalette<U>();
        for(var k: keys()){
            n.put(k, mapper.apply(k,get(k)));
        }
        return n;
    }
    public ChunkBlockPosPalette(CrudeIncrementalIntIdentityHashBiMap<T> palette, Int2IntRBTreeMap contents) {
        this.palette = palette;
        this.contents = contents;
    }
    public ChunkBlockPosPalette(){
        this(CrudeIncrementalIntIdentityHashBiMap.create(64),new Int2IntRBTreeMap());
    }
    public T get(BlockPos pos){
        if(!contents.containsKey(getId(pos))){
            return null;
        }
        return palette.byId(contents.get(getId(pos)));
    }
    public void put(BlockPos pos, T value){
        int i;
        if(palette.size() != 0 && palette.contains(value)){
            i = palette.getId(value);
        }else{
            i = palette.add(value);
        }
        contents.put(getId(pos),i);
    }
    public Set<BlockPos> keys(){return contents.keySet().intStream().mapToObj(ChunkBlockPosPalette::get).collect(Collectors.toSet());}
    public void remove(BlockPos pos){
        contents.remove(getId(pos));
    }
    public static int getId(BlockPos pos){
        return (pos.getY() * 16 + pos.getX()) * 16 + pos.getZ();
    }
    public static BlockPos get(int id){
        return new BlockPos(id >> 4 & 0xf,id >> 8,id & 0xf);
    }
    public void cleanup(){
        Int2IntRBTreeMap cache = new Int2IntRBTreeMap();
        CrudeIncrementalIntIdentityHashBiMap<T> newPalette = CrudeIncrementalIntIdentityHashBiMap.create(palette.size() / 2);
        contents.replaceAll((c, v) -> cache.computeIfAbsent(v, (b) -> {
            return newPalette.add(palette.byId(b));
        }));
        palette = newPalette;
    }
    public static <T> Codec<ChunkBlockPosPalette<T>> codec(Codec<T> wrapped){
        return RecordCodecBuilder.create(i -> //                    Canon.LOGGER.info("deserializing contents");
                i.group(
                Codec.unboundedMap(Codec.STRING.xmap(Integer::parseInt,Objects::toString),wrapped).xmap((m) -> {
//                    Canon.LOGGER.info("deserializing palette");
                    CrudeIncrementalIntIdentityHashBiMap<T> newPalette = CrudeIncrementalIntIdentityHashBiMap.create(m.size() + 1);
                    for(var n: m.entrySet()){
                        newPalette.addMapping(n.getValue(),n.getKey());
                    }
                    return newPalette;
                },(p) -> Util.make(new HashMap<>(),(m) -> {
                    if(p == null){
                        return;
                    }
//                    Canon.LOGGER.info("serializing palette");
                    int j = 0;
                    for (T a : p) {
                        if (a != null) m.put(j, a);
                        j++;
                    }
                })).fieldOf("palette").forGetter((x) -> x.palette),
                Codec.unboundedMap(Codec.STRING.xmap(Integer::parseInt,Objects::toString),Codec.INT).xmap(Int2IntRBTreeMap::new,(a) -> {
//                    Canon.LOGGER.info("serializing contents");
                    return a;
                }).fieldOf("entries").forGetter((x) -> x.contents)
        ).apply(i,ChunkBlockPosPalette::new));
    }
    public static <A extends ByteBuf,T> StreamCodec<A,ChunkBlockPosPalette<T>> streamCodec(StreamCodec<A,T> base){
        return StreamCodec.composite(ByteBufCodecs.<A,Integer,T,HashMap<Integer,T>>map(HashMap::new,ByteBufCodecs.INT.cast(),base).map((m) -> {
//                    Canon.LOGGER.info("deserializing palette");
            CrudeIncrementalIntIdentityHashBiMap<T> newPalette = CrudeIncrementalIntIdentityHashBiMap.create(m.size() + 1);
            for(var n: m.entrySet()){
                newPalette.addMapping(n.getValue(),n.getKey());
            }
            return newPalette;
        },(p) -> Util.make(new HashMap<Integer,T>(),(m) -> {
            if(p == null){
                return;
            }
//                    Canon.LOGGER.info("serializing palette");
            int j = 0;
            for (T a : p) {
                if (a != null) m.put(j, a);
                j++;
            }
        })),(a) -> a.palette,ByteBufCodecs.map(HashMap::new,ByteBufCodecs.INT.cast(),ByteBufCodecs.INT.cast()).map(Int2IntRBTreeMap::new,HashMap::new),(a) -> a.contents,ChunkBlockPosPalette::new);
    }
//    public static<T> Codec<ChunkBlockPosPalette<T>> codec(Codec<T> wrapped){
//        return RecordCodecBuilder.create((i) -> i.group(
//                Codec.unboundedMap(CodecUtils.STRINT,wrapped).xmap((m) -> {
//                    Canon.LOGGER.info("deserializing palette");
//                    Int2ObjectBiMap<T> newPalette = Int2ObjectBiMap.create(m.size());
//                    for(var n: m.entrySet()){
//                        newPalette.put(n.getValue(),n.getKey());
//                    }
//                    return newPalette;
//                },(p) -> Util.make(new HashMap<>(),(m) -> {
//                    if(p == null){
//                        return;
//                    }
//                    Canon.LOGGER.info("serializing palette");
//                    int j = 0;
//                    for (T a : p) {
//                        if (a != null) m.put(j, a);
//                        j++;
//                    }
//                })).fieldOf("palette").forGetter((p) -> p.palette),
//                Codec.unboundedMap(CodecUtils.STRINT,Codec.INT).xmap((a) -> {
//                    Canon.LOGGER.info("deserializing contents");
//                    return new Int2IntRBTreeMap(a);
//                },(a) -> {
//                    Canon.LOGGER.info("serializing contents");
//                    return a;
//                }).fieldOf("contents").forGetter((p) -> p.contents)
//        ).apply(i,ChunkBlockPosPalette::new));
//    }
}