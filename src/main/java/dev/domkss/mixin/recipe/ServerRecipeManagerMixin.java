package dev.domkss.mixin.recipe;

import dev.domkss.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Mixin(ServerRecipeManager.class)
public abstract class ServerRecipeManagerMixin {


    @Inject(method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/recipe/PreparedRecipes;",
            at = @At("RETURN"), cancellable = true)
    private void onPrepare(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<PreparedRecipes> cir) {
        PreparedRecipes original = cir.getReturnValue();
        cir.setReturnValue(modifyPreparedRecipes(original));
    }

    @Unique
    private PreparedRecipes modifyPreparedRecipes(PreparedRecipes original) {

        Collection<RecipeEntry<?>> newRecipes = new ArrayList<>();

        for (var entry : original.recipes()) {
            Recipe<?> recipe = entry.value();

            // only inspect crafting recipes
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                if (shapedRecipe.getCategory().asString().equals("equipment")) {
                    RawShapedRecipe raw = ((ShapedRecipeAccessor) shapedRecipe).getRaw();
                    RawShapedRecipe newRaw = replaceSticksInRawRecipe(raw);

                    ShapedRecipe newRecipe = new ShapedRecipe(shapedRecipe.getGroup(), shapedRecipe.getCategory(), newRaw,
                            shapedRecipe.craft(null, null));

                    newRecipes.add(new RecipeEntry<>(entry.id(), newRecipe));

                    continue;

                }
            }

            //default: Add unmodified items
            newRecipes.add(entry);
        }


        return PreparedRecipes.of(newRecipes);
    }

    @Unique
    private RawShapedRecipe replaceSticksInRawRecipe(RawShapedRecipe raw) {
        int width = raw.getWidth();
        int height = raw.getHeight();

        List<Optional<Ingredient>> oldIngredients = raw.getIngredients();
        List<Optional<Ingredient>> newIngredients = new ArrayList<>(oldIngredients.size());

        for (Optional<Ingredient> optIngredient : oldIngredients) {
            if (optIngredient.isPresent()) {
                Ingredient ingredient = optIngredient.get();
                if (ingredient.test(new ItemStack(Items.STICK))) {
                    // Replace STICK with an ingredient that accepts STICK or COPPER_ROD
                    Ingredient replacement = Ingredient.ofItems(Items.STICK, ModItems.COPPER_ROD);
                    newIngredients.add(Optional.of(replacement));
                } else {
                    newIngredients.add(Optional.of(ingredient));
                }

            } else {
                newIngredients.add(Optional.empty());
            }
        }

        return new RawShapedRecipe(width, height, newIngredients, Optional.empty());
    }


}
