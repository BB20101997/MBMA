package de.webtwob.mbma.core.common.crafting;

import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import de.webtwob.mbma.api.interfaces.ICraftingNode;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRecipe;
import de.webtwob.mbma.api.interfaces.capability.ICraftingRequest;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 23. Apr. 2017.
 * This nodes once tries to gather all requested Items if that fails tries to craft.
 * It will try to craft until the requested amount is met or no recipes are left.
 */
public class RecipeSelectCraftingNode extends CraftingNode {

    private List<RecipePage> pages;
    private List<ICraftingRecipe> recipes;
    private RecipePage currentPage;

    /**
     * All requests past to this constructor will be assumed to be modifiable.
     * If you do NOT want your request to be changed copy it first!
     *
     * @param parent  should only be null if root node
     * @param request
     * @param icnip
     */
    public RecipeSelectCraftingNode(@Nullable CraftingNode parent, @Nonnull ICraftingRequest request, @Nonnull ICraftingAccessor icnip) {
        super(parent, request, icnip);
    }

    @Override
    public void pushItemStackDown(int stackID) {
        if (parent != null) {
            parent.pushItemStackDown(stackID);
        } else {
            iCraftingAccessor.returnStackToPerm(stackID);
        }
    }

    @Override
    public Optional<ICraftingNode> cycle() throws CraftingException {
        if (!request.isCompleted()) {
            tryToGather();
            if (!request.isCompleted()) {
                return Optional.ofNullable(tryToCraft());
            }
        }
        return Optional.ofNullable(parent);
    }

    protected CraftingNode tryToCraft() throws CraftingException {
        if (recipes == null || recipes.isEmpty()) {
            loadNextPage();
            if (recipes.isEmpty()) {
                return parent;
            }
        } else {
            //try first recipe in list
            ICraftingRecipe recipe = recipes.remove(0);
            if (recipe.isVanilla()) {
                return new VanillaCraftingNode(this, request, iCraftingAccessor, recipe, currentPage);
            } else {
                return new ExternalCraftingNode(this, request, iCraftingAccessor, recipe, currentPage);
            }
        }
        return null;
    }

    protected void tryToGather() {
        if (parent != null) {
            Arrays.stream(iCraftingAccessor.gatherMatchingStacks(stack -> stack.isItemEqual(request.getRequest()), request.getQuantity())).forEach(parent::pushItemStackDown);
        }
    }

    private List<RecipePage> getPages() {
        if (pages == null) {
            /*
            pages = iCraftingAccessor.getRecipePages().stream()
                    .filter((page) -> page.recipes.stream() //filter out pages that don't contain matching recipes
                            .flatMap(this::flatMapRecipes)
                            .flatMap(icr -> icr.getOutputs().stream())
                            .filter(ICraftingRecipe.OutputTuple::isGuaranteed) //for now only guaranteed results matter
                            .map(ICraftingRecipe.OutputTuple::getResult)
                            .anyMatch(item -> request.getRequest().isItemEqual(item))
                    ).collect(Collectors.toList());
                    */
        }
        return pages;
    }

    private void loadNextPage() {
        if (getPages().isEmpty()) {
            recipes = Collections.emptyList();
        } else {
            currentPage = getPages().remove(0);
            recipes = currentPage.recipes.stream()
                    .flatMap(this::flatMapRecipes)
                    .filter((icr) -> icr.getOutputs().stream()
                            .filter(ICraftingRecipe.OutputTuple::isGuaranteed)
                            .map(ICraftingRecipe.OutputTuple::getResult)
                            .anyMatch(itemStack -> request.getRequest().isItemEqual(itemStack))
                    ).collect(Collectors.toList());
        }
    }

    private Stream<ICraftingRecipe> flatMapRecipes(ItemStack item) {
        ICraftingRecipe icr = item.getCapability(APICapabilities.CAPABILITY_CRAFTING_RECIPE, null);
        return icr != null ? Stream.of(icr) : Stream.empty();
    }
}
