package ca.sync.nbtrecipes.mixin.smelting;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractFurnaceBlockEntity.class)
public class MixinAbstractFurnaceBlockEntity {

    @Inject(
            method = "canAcceptRecipeOutput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;getCount()I",
                    ordinal = 0
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    private static void canAcceptRecipeOutputNBTCheck(DynamicRegistryManager registryManager, Recipe<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> infoReturnable, ItemStack recipeResult, ItemStack outputStack) {
        if (!ItemStack.canCombine(recipeResult, outputStack) || (recipeResult.getCount() + outputStack.getCount()) > outputStack.getMaxCount()) {
            infoReturnable.setReturnValue(false);
        }
    }

    @Inject(
            method = "craftRecipe",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;increment(I)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void incrementOutputPastOne(DynamicRegistryManager registryManager, Recipe<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> infoReturnable, ItemStack inputStack, ItemStack recipeResult, ItemStack outputStack){
        if(recipeResult.getCount() - 1 > 0) {
            outputStack.increment(recipeResult.getCount() - 1);
        }
    }
}
