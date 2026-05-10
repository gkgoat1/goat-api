package at.gkgo.api.component;

import at.gkgo.api.Goatapi;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;

import java.util.HashSet;
import java.util.Set;

public class BlockComponents {
    public static final AttachmentType<ChunkBlockPosPalette<DataComponentPatch>> PALETTE = AttachmentRegistry.<ChunkBlockPosPalette<DataComponentPatch>>builder().initializer(ChunkBlockPosPalette::new).persistent(ChunkBlockPosPalette.codec(DataComponentPatch.CODEC)).syncWith(ChunkBlockPosPalette.streamCodec(DataComponentPatch.STREAM_CODEC), (a, b) -> true).buildAndRegister(Goatapi.id("block_components"));
    public static final Set<DataComponentType<?>> BLOCK_COMPONENTS = new HashSet<>();
}
