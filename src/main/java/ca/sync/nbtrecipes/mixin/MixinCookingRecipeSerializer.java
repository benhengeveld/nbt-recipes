package ca.sync.nbtrecipes.mixin;

import ca.sync.nbtrecipes.utils.IItemStack;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CookingRecipeSerializer.class)
public class MixinCookingRecipeSerializer {
    private static Integer currentItemCount;
    private static NbtCompound currentNbtData;

    @Redirect(
            method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/AbstractCookingRecipe;",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/util/JsonHelper.getString(Lcom/google/gson/JsonObject;Ljava/lang/String;)Ljava/lang/String;",
                    ordinal = 0
            )
    )
    public String getItemIdentifier(JsonObject jsonObject, String resultPropertyName) {
        currentItemCount = null;
        currentNbtData = null;

        if (!jsonObject.has(resultPropertyName) || !jsonObject.get(resultPropertyName).isJsonObject()) {
            return JsonHelper.getString(jsonObject, resultPropertyName);
        }

        ItemStack output = ShapedRecipe.outputFromJson(jsonObject.getAsJsonObject(resultPropertyName));

        if(output.getCount() < 1){
            throw new JsonSyntaxException("Invalid output count: " + output.getCount());
        }

        currentItemCount = output.getCount();
        currentNbtData = output.getNbt();

        return Registries.ITEM.getId(output.getItem()).toString();
    }

    @Inject(
            method = "read(Lnet/minecraft/util/Identifier;Lcom/google/gson/JsonObject;)Lnet/minecraft/recipe/AbstractCookingRecipe;",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void setRecipesNbtData(Identifier identifier, JsonObject jsonObject, CallbackInfoReturnable<AbstractCookingRecipe> infoReturnable, String string, CookingRecipeCategory cookingRecipeCategory, JsonElement jsonElement, Ingredient ingredient, String string2, Identifier identifier2, ItemStack stack, float f, int i) {
        if(currentNbtData != null) {
            ((IItemStack) (Object) stack).setRawTag(currentNbtData);
        }

        if(currentItemCount != null) {
            stack.setCount(currentItemCount);
        }
    }
}
