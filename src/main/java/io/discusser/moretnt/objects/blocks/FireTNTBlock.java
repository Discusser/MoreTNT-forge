package io.discusser.moretnt.objects.blocks;

import io.discusser.moretnt.objects.entities.BasePrimedTNT;
import io.discusser.moretnt.objects.entities.PrimedDomeTNT;
import io.discusser.moretnt.objects.entities.PrimedFireTNT;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class FireTNTBlock extends BaseTNTBlock {
    public FireTNTBlock() {
        super();
    }

    public FireTNTBlock(float size, boolean fire) {
        super(size, fire);
    }

    @Override
    public BasePrimedTNT createPrimed(Level level, BlockPos blockPos, float size, boolean fire) {
        return new PrimedFireTNT(level, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, size, fire,
                level.getBlockState(blockPos).getValue(BaseTNTBlock.FACING));
    }
}
