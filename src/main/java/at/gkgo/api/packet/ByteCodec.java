package at.gkgo.api.packet;

import org.apache.commons.lang3.tuple.Pair;

public interface ByteCodec <T>{
    Pair<T,Integer> decode(byte[] bytes, int offset);
    byte[] encode(T value);
}
