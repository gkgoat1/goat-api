package at.gkgo.api.component;

import at.gkgo.api.util.ChunkBlockPosPalette;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PaletteFrame {
    public <T> T getFromFrame(World world, BlockPos blockPos, AttachmentType<ChunkBlockPosPalette<T>> palette);
    public <T> void putFromFrame(World world, BlockPos blockPos, AttachmentType<ChunkBlockPosPalette<T>> palette, T value);
    public static PaletteFrame ROOT = new PaletteFrame() {
        @Override
        public <T> T getFromFrame(World world, BlockPos blockPos, AttachmentType<ChunkBlockPosPalette<T>> palette) {
           return world.getChunk(blockPos).getAttachedOrCreate(palette).get(blockPos);
        }

        @Override
        public <T> void putFromFrame(World world, BlockPos blockPos, AttachmentType<ChunkBlockPosPalette<T>> palette, T value) {
            var k = world.getChunk(blockPos);
            var e = k.getAttachedOrCreate(palette);
            e.put(blockPos,value);
            k.setAttached(palette,e);
        }
    };
}
