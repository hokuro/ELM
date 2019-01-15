package mod.elm.potion;

import mod.elm.core.ModCommon;
import mod.elm.core.SoundManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;

public class PotionImoutoConfiusion extends Potion {

	protected PotionImoutoConfiusion() {
		super(false,0xFFFFFF);
		setPotionName(PotionCore.NAME_POTION_IMOUTOCONFIUSION);
	}

	@Override
	 public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier){
		if (entityLivingBaseIn instanceof EntityPlayer){
			entityLivingBaseIn.world.playSound(((EntityPlayer)entityLivingBaseIn), entityLivingBaseIn.getPosition(), SoundManager.item_imouto_use, SoundCategory.PLAYERS, 1.0F + entityLivingBaseIn.world.rand.nextFloat(), entityLivingBaseIn.world.rand.nextFloat() * 0.7F + 0.3F);
		}
		//　残りHPをランダムに変化させる
		entityLivingBaseIn.setPositionAndRotationDirect(entityLivingBaseIn.posX, entityLivingBaseIn.posY, entityLivingBaseIn.posZ,
				MathHelper.wrapDegrees(entityLivingBaseIn.world.rand.nextFloat() * 360.0F), entityLivingBaseIn.rotationPitch, 1, false);
	}

	@Override
	public boolean isReady(int duration, int amplifier){
		return ((duration%30)==0);
	}

    @Override
    public int getStatusIconIndex(){
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(ModCommon.MOD_ID, "textures/gui/container/inventory.png"));//ポーションのアイコンのテクスチャの場所を指定する。
        return 0;
    }

    @Override
    public boolean hasStatusIcon(){
        return true;
    }
}
