package de.webtwob.mbma.common.crafting;

import de.webtwob.mbma.api.RecipePage;
import de.webtwob.mbma.api.capability.APICapabilities;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRecipe;
import de.webtwob.mbma.api.capability.interfaces.ICraftingRequest;
import de.webtwob.mbma.api.interfaces.ICraftingNameInProgress;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by BB20101997 on 22. Apr. 2017.
 */
public class CraftingTree extends CraftingNode {
    
    private ICraftingRequest request;
    private CraftingNode currentNode = this;
    private List<ICraftingRecipe> possibleRecipes;
    private List<ICraftingRequest> subRequests;
    
    public CraftingTree(ICraftingRequest request, ICraftingNameInProgress icnip) {
        super(null, null, request,icnip);
        tree = this;
    }
    
    @Override
    protected CraftingNode cycle() {
        if (currentNode == null) {
            if (request.isCompleted()) {
                //request competed nothing left to-do
                doneStatus = true;
                return null;
            } else {
                //start at root
                return this;
            }
        } else {
            if (possibleRecipes == null || possibleRecipes.isEmpty()) {
                //gather all known and matching recipes
                List<RecipePage> pages = iCraftingNameInProgress.getRecipePages();
                possibleRecipes = pages.stream()
                        .flatMap((page) -> page.recipes.stream())
                        .map(item -> item.getCapability(APICapabilities.CAPABILITY_CRAFTING_RECIPE, null))
                        .filter(
                                //filter recipes
                                recipe -> recipe != null && recipe.getOutputs()
                                        .stream()
                                        .anyMatch(outputTuple -> outputTuple.isGuaranteed() && outputTuple.getResult().isItemEqual(request.getRequest()))
                        )
                        .collect(Collectors.toList());
                //stay in root will be creating child nodes next cycle
                return this;
            } else {
                if (subRequests == null || subRequests.isEmpty()) {
                    possibleRecipes.get(0).getRequirements().forEach(req -> {
                        ICraftingRequest iCraftingRequest = APICapabilities.CAPABILITY_CRAFTING_REQUEST.getDefaultInstance();
                        if (iCraftingRequest != null) {
                            iCraftingRequest.setQuantity(req.getCount());
                            iCraftingRequest.setRequest(req.copy());
                            subRequests.add(iCraftingRequest);
                        }else{
                            //how can we be handling a Crafting Request if there are no Crafting Requests?
                            throw new IllegalStateException("Capability Crafting Request not found, while handling a Crafting Request!");
                        }
                    });
                    //still not done in root
                    return this;
                } else {
                    return new CraftingNode(this,this,subRequests.get(0),iCraftingNameInProgress);
                }
            }
        }
    }
}
