package mod.elm.item;

import mod.elm.core.log.ModLog;
import mod.elm.entity.ab.IMobInfomationGetter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemMobAnalyzer extends Item {

	public ItemMobAnalyzer(Item.Properties property) {
		super(property);
	}


	public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
		if (target instanceof IMobInfomationGetter) {
			IMobInfomationGetter getter = (IMobInfomationGetter)target;
			String infromation = getter.getInformation();
			if (target.world.isRemote) {
				ModLog.log().info("####### Client Mob Information #######");
				ModLog.log().info(infromation);
			} else {
				ModLog.log().info("####### Client Mob Information #######");
				ModLog.log().info(infromation);
			}
			return true;
		} else if (target instanceof LivingEntity){
			ModLog.log().info("Health:" + target.getHealth() + "/" + target.getMaxHealth());
		}
		return false;
	}
}
