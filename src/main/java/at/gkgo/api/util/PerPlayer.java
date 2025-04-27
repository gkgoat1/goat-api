package at.gkgo.api.util;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Uuids;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PerPlayer<T> {
    public final HashMap<UUID,T> per;

    public PerPlayer(Map<UUID, T> per) {
        this.per = new HashMap<>(per);
    }

    public static <T>Codec<PerPlayer<T>> codec(Codec<T> base){
        return Codec.unboundedMap(Uuids.CODEC,base).xmap(PerPlayer::new,(a) -> a.per);
    }
    public static <T>PacketCodec<RegistryByteBuf,PerPlayer<T>> packetCodec(PacketCodec<RegistryByteBuf,T> base){
        return PacketCodecs.map(HashMap::new,Uuids.PACKET_CODEC,base).xmap(PerPlayer::new,(a) -> a.per);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PerPlayer<?> perPlayer = (PerPlayer<?>) o;
        return Objects.equals(per, perPlayer.per);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(per);
    }
}
