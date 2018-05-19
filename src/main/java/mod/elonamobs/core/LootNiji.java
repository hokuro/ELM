package mod.fbd.core;

import java.util.Random;

import mod.fbd.item.ItemBladePiece.EnumBladePieceType;
import mod.fbd.item.ItemCore;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class LootNiji extends LootFunction {

    private final RandomValueRange countRange;
	protected LootNiji(LootCondition[] conditionsIn, RandomValueRange countRangeIn) {
        super(conditionsIn);
        this.countRange = countRangeIn;
    }

    public ItemStack apply(ItemStack stack, Random rand, LootContext context)
    {
        return new ItemStack(ItemCore.item_katana_niji,this.countRange.generateInt(rand), EnumBladePieceType.NIJI.getIndex());
    }
}
