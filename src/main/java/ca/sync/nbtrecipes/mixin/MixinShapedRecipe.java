package ca.sync.nbtrecipes.mixin;

import ca.sync.nbtrecipes.utils.IItemStack;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ShapedRecipe.class)
public class MixinShapedRecipe {
    private static NbtCompound currentNbtData;

    @Inject(method = "outputFromJson", at = @At(value = "INVOKE", target = "com/google/gson/JsonObject.has(Ljava/lang/String;)Z", remap = false))
    private static void getRecipesNbtData(JsonObject json, CallbackInfoReturnable<ItemStack> infoReturnable) {
        currentNbtData = null;

        if(json.has("data")) {
            String nbtString;

            if(JsonHelper.hasString(json, "data")) {
                nbtString = json.get("data").getAsString();
            } else {
                nbtString = JsonHelper.getObject(json, "data").toString();
            }

            try{
                currentNbtData = new StringNbtReader(new StringReader(nbtString)).parseCompound();
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }

            json.remove("data");
        }
    }

    @Inject(method = "outputFromJson", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void setRecipesNbtData(JsonObject json, CallbackInfoReturnable<ItemStack> infoReturnable, Item item, int amount) {
        ItemStack stack = new ItemStack(item, amount);

        if(currentNbtData != null) {
            NbtCompound nbtData = currentNbtData.copy();
            currentNbtData = null;

            ((IItemStack) (Object) stack).setRawTag(nbtData);
        }

        infoReturnable.setReturnValue(stack);
    }
}
