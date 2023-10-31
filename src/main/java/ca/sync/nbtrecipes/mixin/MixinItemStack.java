package ca.sync.nbtrecipes.mixin;

import ca.sync.nbtrecipes.utils.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ItemStack.class, priority = 2000)
public class MixinItemStack implements IItemStack {
    @Shadow
    private NbtCompound nbt;

    @Override
    public void setRawTag(NbtCompound tag) {
        if (tag == null || tag.isEmpty()) {
            this.nbt = null;
        } else {
            this.nbt = tag;
        }
    }
}
