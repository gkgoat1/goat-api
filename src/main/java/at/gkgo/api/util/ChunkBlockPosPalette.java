package at.gkgo.api.util;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ChunkBlockPosPalette<T> {
    public Int2ObjectBiMap<T> palette;
    public Int2IntRBTreeMap contents;
    public<U> ChunkBlockPosPalette<U> map(BiFunction<BlockPos,T,U> mapper){
        var n = new ChunkBlockPosPalette<U>();
        for(var k: keys()){
            n.put(k, mapper.apply(k,get(k)));
        }
        return n;
    }
    public ChunkBlockPosPalette(Int2ObjectBiMap<T> palette, Int2IntRBTreeMap contents) {
        this.palette = palette;
        this.contents = contents;
    }
    public ChunkBlockPosPalette(){
        this(Int2ObjectBiMap.create(64),new Int2IntRBTreeMap());
    }
    public T get(BlockPos pos){
        if(!contents.containsKey(getId(pos))){
            return null;
        }
        return palette.get(contents.get(getId(pos)));
    }
    public void put(BlockPos pos, T value){
        int i;
        if(palette.size() != 0 && palette.contains(value)){
            i = palette.getRawId(value);
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
        Int2ObjectBiMap<T> newPalette = Int2ObjectBiMap.create(palette.size() / 2);
        contents.replaceAll((c, v) -> cache.computeIfAbsent(v, (b) -> {
            return newPalette.add(palette.get(b));
        }));
        palette = newPalette;
    }
    public static <T> Codec<ChunkBlockPosPalette<T>> codec(Codec<T> wrapped){
        return RecordCodecBuilder.create(i -> i.group(
                Codec.unboundedMap(Codec.STRING.xmap(Integer::parseInt,Objects::toString),wrapped).xmap((m) -> {
//                    Canon.LOGGER.info("deserializing palette");
                    Int2ObjectBiMap<T> newPalette = Int2ObjectBiMap.create(m.size() + 1);
                    for(var n: m.entrySet()){
                        newPalette.put(n.getValue(),n.getKey());
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
                Codec.unboundedMap(Codec.STRING.xmap(Integer::parseInt,Objects::toString),Codec.INT).xmap((a) -> {
//                    Canon.LOGGER.info("deserializing contents");
                    return new Int2IntRBTreeMap(a);
                },(a) -> {
//                    Canon.LOGGER.info("serializing contents");
                    return a;
                }).fieldOf("entries").forGetter((x) -> x.contents)
        ).apply(i,ChunkBlockPosPalette::new));
    }
    public static <A extends ByteBuf,T>PacketCodec<A,ChunkBlockPosPalette<T>> packetCodec(PacketCodec<A,T> base){
        return PacketCodec.tuple(PacketCodecs.<A,Integer,T,HashMap<Integer,T>>map(HashMap::new,PacketCodecs.INTEGER.cast(),base).xmap((m) -> {
//                    Canon.LOGGER.info("deserializing palette");
            Int2ObjectBiMap<T> newPalette = Int2ObjectBiMap.create(m.size() + 1);
            for(var n: m.entrySet()){
                newPalette.put(n.getValue(),n.getKey());
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
        })),(a) -> a.palette,PacketCodecs.map(HashMap::new,PacketCodecs.INTEGER.cast(),PacketCodecs.INTEGER.cast()).xmap(Int2IntRBTreeMap::new,HashMap::new),(a) -> a.contents,ChunkBlockPosPalette::new);
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
