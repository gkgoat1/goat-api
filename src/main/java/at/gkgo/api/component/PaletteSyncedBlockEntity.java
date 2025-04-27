package at.gkgo.api.component;

import at.gkgo.api.util.ChunkBlockPosPalette;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;

public class PaletteSyncedBlockEntity extends BlockEntity {
    public final PaletteFrame frame;
    public PaletteSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, PaletteFrame frame) {
        super(type, pos, state);
        this.frame = frame;
    }
    public <T> T get(AttachmentType<ChunkBlockPosPalette<T>> palette){
        return frame.getFromFrame(getWorld(),getPos(),palette);
    }
    public <T> void put(AttachmentType<ChunkBlockPosPalette<T>> palette, T value){
        frame.putFromFrame(getWorld(),getPos(),palette,value);
    }
}
