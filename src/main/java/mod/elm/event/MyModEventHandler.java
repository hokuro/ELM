package mod.elm.event;

import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.platform.GlStateManager;

import mod.elm.core.Mod_Elm;
import mod.elm.entity.EntityWanderingPeople;
import mod.elm.gui.GuiBloodOut;
import mod.elm.item.ItemCore;
import mod.elm.item.parts.ItemElmBrain;
import mod.elm.item.parts.ItemElmUterus;
import mod.elm.item.parts.ab.IItemElmParts;
import mod.elm.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MyModEventHandler {
	public static final List<Item> ITEMDROP = new ArrayList<Item>() {
		{add(ItemCore.item_stmach);}
		{add(ItemCore.item_bone);}
		{add(ItemCore.item_skin);}
		{add(ItemCore.item_skin);}
//		{add(ItemCore.item_blood);}		// i血液は心臓からだけ出るようにする？
		{add(ItemCore.item_eye);}
		{add(ItemCore.item_bone);}
		{add(ItemCore.item_foot);}
		{add(ItemCore.item_bone);}
		{add(ItemCore.item_meet);}
		{add(ItemCore.item_lung);}
		{add(ItemCore.item_hand);}
		{add(ItemCore.item_intestine);}
		{add(ItemCore.item_hart);}
		{add(ItemCore.item_blood);}
		{add(ItemCore.item_meet);}
		{add(ItemCore.item_blood);}
		{add(ItemCore.item_skin);}
		{add(ItemCore.item_brain);}
		{add(ItemCore.item_meet);}
	};

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event) {
		if(event.getEntity().world.isRemote) {
			return;
		}
		if (event.getSource().getTrueSource() instanceof PlayerEntity) {
			int x = event.getEntity().getPosition().getX();
			int y = event.getEntity().getPosition().getY();
			int z = event.getEntity().getPosition().getZ();
			ItemStack drop;
			int lp = ModUtil.random(200)%2+2;
			for (int i = 0; i < lp; i++) {
				drop = null;
				if (event.getEntityLiving() instanceof EntityWanderingPeople) {
					if (ModUtil.randomD() > (1.0D / (ITEMDROP.size() + 1))) {
						drop = new ItemStack(ITEMDROP.get(ModUtil.random(ITEMDROP.size())),1);
						((IItemElmParts)drop.getItem()).setPartsTarget(drop, event.getEntity().getType().getRegistryName().toString());
					} else {
						drop = new ItemStack(ItemCore.item_uterus,1);
						((IItemElmParts)drop.getItem()).setPartsTarget(drop, event.getEntity().getType().getRegistryName().toString());
					}
				} else if(event.getEntityLiving() instanceof MobEntity) {
					if (ModUtil.randomD() < 0.4) {
						drop = new ItemStack(ITEMDROP.get(ModUtil.random(ITEMDROP.size())),1);
						((IItemElmParts)drop.getItem()).setPartsTarget(drop, event.getEntity().getType().getRegistryName().toString());
					}
				}
				if (drop != null) {
					if (drop.getItem() == ItemCore.item_brain) {
						// i脳みそをドロップする場合最大攻撃力を決める
						ItemElmBrain.setMaxAttackDamage(drop, (MobEntity)event.getEntity().getEntity());
					} else if (drop.getItem() == ItemCore.item_uterus) {
						ItemElmUterus.setMaxChild(drop, ModUtil.random_1((int)event.getEntityLiving().getMaxHealth()));
					}
					ModUtil.spawnItemStack(event.getEntity().world, x, y, z, drop, Mod_Elm.rnd);
				}
			}
		} else if (event.getSource().getDamageType().equals(Mod_Elm.DamageSourceBrain.getDamageType())) {
			// i脳みそによる攻撃
			int x = event.getEntity().getPosition().getX();
			int y = event.getEntity().getPosition().getY();
			int z = event.getEntity().getPosition().getZ();
			ModUtil.spawnItemStack(event.getEntity().world, x, y, z, new ItemStack(ItemCore.item_dried_braincell), Mod_Elm.rnd);
		}
	}

	public static final GuiBloodOut gui = new GuiBloodOut(Minecraft.getInstance());
	@SubscribeEvent
	public void onRenderGameOverLay(RenderGameOverlayEvent.Post event){
		if (event.getType() == ElementType.ALL) {
			if (Minecraft.getInstance().player.isPotionActive(Mod_Elm.RegistryEvents.EFFECT_BLOODREADOUT)) {
				EffectInstance effect = Minecraft.getInstance().player.getActivePotionMap().get(Mod_Elm.RegistryEvents.EFFECT_BLOODREADOUT);
		        GlStateManager.pushMatrix();
		        int amp = effect.getAmplifier() + 1;
		        int dur = effect.getDuration();
				gui.render(amp * 50, Math.min(1.0F, amp * 0.3F), ((dur % 5) == 0));
		        GlStateManager.popMatrix();
			}
		}
	}

	@SubscribeEvent
	public void onPotionRemove(PotionRemoveEvent event) {
		if (event.getPotion() == Mod_Elm.RegistryEvents.EFFECT_BLOODPARASITE) {
			LivingEntity entity = event.getEntityLiving();
			if (entity.isPotionActive(Effects.POISON) || entity.isPotionActive(Effects.HUNGER)) {
			}else {
				event.setCanceled(true);
			}
		}
	}
}
