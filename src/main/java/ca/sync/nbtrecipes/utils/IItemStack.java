package ca.sync.nbtrecipes.utils;

import net.minecraft.nbt.NbtCompound;

public interface IItemStack {
    void setRawTag(NbtCompound tag);
}