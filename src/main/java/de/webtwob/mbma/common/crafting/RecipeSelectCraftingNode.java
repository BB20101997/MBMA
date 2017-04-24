package de.webtwob.mbma.common.crafting;

import de.webtwob.mbma.api.RecipePage;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRecipe;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.exceptions.CraftingException;
import de.webtwob.mbma.api.interfaces.ICraftingAccessor;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
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
     * @param parent
     * @param tree
     * @param request
     * @param icnip
     */
    public RecipeSelectCraftingNode(@Nullable CraftingNode parent, @Nonnull CraftingTree tree, @Nonnull ICraftingRequest request, @Nonnull ICraftingAccessor icnip) {
        super(parent, tree, request, icnip);
    }
    
    @Override
    protected void pushItemStackDown(int stackID) {
        if (parent != null) {
            parent.pushItemStackDown(stackID);
        } else {
            iCraftingAccessor.dropExcess(stackID);
        }
    }
    
    @Override
    protected Optional<CraftingNode> cycle() throws CraftingException {
        if (request.isCompleted()) {
            //if request is done the parent should be put in charge again
            return Optional.ofNullable(parent);
        }
        tryToGather();
        if (!request.isCompleted()) {
            return Optional.ofNullable(tryToCraft());
        }
        return Optional.ofNullable(parent);
    }
    
    protected CraftingNode tryToCraft() throws CraftingException {
        if (recipes == null || recipes.isEmpty()) {
            if (pages == null) {
                pages = iCraftingAccessor.getRecipePages().stream()
                        .filter((page) -> page.recipes.stream() //filter out pages that don't contain matching recipes
                                .flatMap(this::flatMapRecipes)
                                .flatMap(icr -> icr.getOutputs().stream())
                                .filter(ICraftingRecipe.OutputTuple::isGuaranteed) //for now only guaranteed results matter
                                .map(ICraftingRecipe.OutputTuple::getResult)
                                .anyMatch(item -> request.getRequest().isItemEqual(item))
                        ).collect(Collectors.toList());
            }
            if (pages.isEmpty()) {
                //not able to craft RequestedItem and already gathered all we could, parent needs to do something else
                return parent;
            } else {
                currentPage = pages.remove(0);
                recipes = currentPage.recipes.stream().flatMap(this::flatMapRecipes).collect(Collectors.toList());
            }
        } else {
            //try first recipe in list
            ICraftingRecipe recipe = recipes.remove(0);
            if (recipe.isVanilla()) {
                return new VanillaCraftingNode(this, tree, request, iCraftingAccessor, recipe);
            } else {
                return new ExternalCraftingNode(this, tree, request, iCraftingAccessor, recipe, currentPage);
            }
        }
        return null;
    }
    
    private Stream<ICraftingRecipe> flatMapRecipes(ItemStack item) {
        ICraftingRecipe icr = item.getCapability(APICapabilities.CAPABILITY_CRAFTING_RECIPE, null);
        return icr != null ? Stream.of(icr) : Stream.empty();
    }
    
    protected void tryToGather() {
        if (parent != null) {
            Arrays.stream(iCraftingAccessor.gatherMatchingStacks(stack -> stack.isItemEqual(request.getRequest()), request.getQuantity())).forEach(parent::pushItemStackDown);
        }
    }
}
