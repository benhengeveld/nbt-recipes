package ca.sync.nbtrecipes.mixin.stonecutting;

import ca.sync.nbtrecipes.NbtRecipes;
import ca.sync.nbtrecipes.utils.IItemStack;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CuttingRecipe.Serializer.class)
public class MixinCuttingRecipeSerializer {
    private static NbtCompound currentNbtData;

    @Redirect(
            method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/CuttingRecipe;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/JsonHelper;getString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;"
            )
    )
    public String getItemIdentifier(JsonObject jsonObject, String resultPropertyName) {
        currentNbtData = null;

        if (!jsonObject.has(resultPropertyName) || !jsonObject.get(resultPropertyName).isJsonObject()) {
            return JsonHelper.getString(jsonObject, resultPropertyName);
        }

        ItemStack output = ShapedRecipe.outputFromJson(jsonObject.getAsJsonObject(resultPropertyName));
        currentNbtData = output.getNbt();

        return Registries.ITEM.getId(output.getItem()).toString();
    }

    @Redirect(
            method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/CuttingRecipe;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/JsonHelper;getInt(Lcom/google/gson/JsonObject;Ljava/lang/String;)I"
            )
    )
    public int getItemCount(JsonObject jsonObject, String element) {
        if (jsonObject.has("result") && jsonObject.get("result").isJsonObject()) {
            JsonObject resultItemJson = jsonObject.getAsJsonObject("result");
            if(resultItemJson.has(element)) {
                return JsonHelper.getInt(resultItemJson, element);
            }
        }

        return JsonHelper.getInt(jsonObject, element);
    }

    @Inject(
            method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/CuttingRecipe;",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void setRecipesNbtData(Identifier identifier, JsonObject jsonObject, CallbackInfoReturnable<CuttingRecipe> infoReturnable, String string, Ingredient ingredient, String string2, int i, ItemStack stack) {
        if(currentNbtData != null) {
            NbtCompound nbtData = currentNbtData.copy();
            currentNbtData = null;

            ((IItemStack) (Object) stack).setRawTag(nbtData);
        }
    }
}
