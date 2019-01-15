package mod.elm.potion;

import mod.elm.core.ModCommon;
import mod.elm.core.SoundManager;
import mod.elm.util.ModUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class PotionImoutoInsanity extends Potion {
	private final boolean isCooked;
	protected PotionImoutoInsanity(boolean isCooked) {
		super(false,0xFFFFFF);
		this.isCooked = isCooked;
		if (!isCooked){
			setPotionName(PotionCore.NAME_POTION_IMOUTOINSANITY);
		}else{
			setPotionName(PotionCore.NAME_POTION_IMOUTOINSANITYBURN);
		}
	}

	@Override
	 public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier){
		if (entityLivingBaseIn instanceof EntityPlayer){
			if (this.isCooked){
				entityLivingBaseIn.world.playSound(((EntityPlayer)entityLivingBaseIn), entityLivingBaseIn.getPosition(), SoundManager.item_imouto_cookedmeet, SoundCategory.PLAYERS, 1.0F + entityLivingBaseIn.world.rand.nextFloat(), entityLivingBaseIn.world.rand.nextFloat() * 0.7F + 0.3F);
			}else{
				entityLivingBaseIn.world.playSound(((EntityPlayer)entityLivingBaseIn), entityLivingBaseIn.getPosition(), SoundManager.item_imouto_meet, SoundCategory.PLAYERS, 1.0F + entityLivingBaseIn.world.rand.nextFloat(), entityLivingBaseIn.world.rand.nextFloat() * 0.7F + 0.3F);
			}
			// 空腹度、腹持ちを最大まで回復
			((EntityPlayer)entityLivingBaseIn).getFoodStats().addStats(20, 20F);
		}
		//　残りHPを1.5固定
		entityLivingBaseIn.setHealth(1.5F);

		// ランダムで移動速度を変更
		if (ModUtil.random(100)<50){
			entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS,30,ModUtil.random(10)));
			entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,30,-1*ModUtil.random(10)));
		}else{
			entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.SPEED,30,ModUtil.random(10)));
			entityLivingBaseIn.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST,30,ModUtil.random(10)));
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier){
		return ((duration%30)==0);
	}

    @Override
    public int getStatusIconIndex(){
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModCommon.MOD_ID, "textures/gui/container/inventory.png"));//ポーションのアイコンのテクスチャの場所を指定する。
        if (this.isCooked){
        	return 2;
        }else{
        	return 1;
        }
    }

    @Override
    public boolean hasStatusIcon(){
        return true;
    }
}