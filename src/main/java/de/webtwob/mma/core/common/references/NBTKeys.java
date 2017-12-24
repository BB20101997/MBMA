package de.webtwob.mma.core.common.references;

import static de.webtwob.mma.core.MMACore.MODID;

/**
 * Created by BB20101997 on 16. Mär. 2017.
 */
@SuppressWarnings("StringConcatenationMissingWhitespace")
public class NBTKeys {

    public static final String TOKEN_GENERATOR_MUSTER   = MODID + ":TokenGeneratorMuster";
    public static final String TOKEN_GENERATOR_COMBINED = MODID + ":TokenGeneratorCombined";

    public static final String CRAFTING_STORAGE_LIST           = MODID + ":cslist";
    public static final String QUEUE_STACKS                    = MODID + ":queueStacks";
    public static final String CRAFTING_CONTROLLER_QUEUE_LINKS = MODID + ":queueLinks";

    private NBTKeys() {
    }
}
