package dev.domkss.blocks;

import dev.domkss.UnderGround;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;

public abstract class CustomBlock extends Block {


    protected Identifier identifier;

    public CustomBlock(Identifier identifier,Settings settings) {
        super(settings);
        this.identifier=identifier;
    }

    protected Identifier getIdentifier(){
        return identifier;
    }

}
