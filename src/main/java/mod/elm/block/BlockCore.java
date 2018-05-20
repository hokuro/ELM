package mod.elm.block;

import java.util.HashMap;
import java.util.Map;

import mod.elm.block.BlockImoutoHead.EnumImoutoHead;
import mod.elm.block.item.ItemBlockImoutoHead;
import mod.elm.block.material.MaterialImouto;
import mod.elm.core.ModCommon;
import mod.elm.core.Mod_ElonaMobs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BlockCore{

	public static final String NAME_IMOUTOHEAD_NORMAL = "imouto_head_normal";
	public static final String NAME_IMOUTOHEAD_ROTTEN = "imouto_head_rotten";
	public static final String NAME_IMOUTOHEAD_SKELTON = "imouto_head_skelton";

	private static final String[] NAME_LIST = new String[]{
			NAME_IMOUTOHEAD_NORMAL,
			NAME_IMOUTOHEAD_ROTTEN,
			NAME_IMOUTOHEAD_SKELTON
	};

	public static final Material material_imouto_block = new MaterialImouto();
	public static Block imouto_head_normal;
	public static Block imouto_head_rotten;
	public static Block imouto_head_skelton;

	private static Map<String,Block> blockMap;
	private static Map<String,Item> itemMap;
	private static Map<String,ResourceLocation[]> resourceMap;

	private static void init(){
		imouto_head_normal = new BlockImoutoHead(EnumImoutoHead.NORMAL)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_NORMAL)
				.setUnlocalizedName(NAME_IMOUTOHEAD_NORMAL)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		imouto_head_rotten = new BlockImoutoHead(EnumImoutoHead.ROTTEN)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_ROTTEN)
				.setUnlocalizedName(NAME_IMOUTOHEAD_ROTTEN)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		imouto_head_skelton = new BlockImoutoHead(EnumImoutoHead.SKALL)
				.setRegistryName(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_SKELTON)
				.setUnlocalizedName(NAME_IMOUTOHEAD_SKELTON)
				.setCreativeTab(Mod_ElonaMobs.tabElmWepon);

		blockMap = new HashMap<String,Block>(){
			//{put(NAME_EXPBANK,block_expbank);}
			{put(NAME_IMOUTOHEAD_NORMAL,imouto_head_normal);}
			{put(NAME_IMOUTOHEAD_ROTTEN,imouto_head_rotten);}
			{put(NAME_IMOUTOHEAD_SKELTON,imouto_head_skelton);}
		};

		itemMap = new HashMap<String,Item>(){
			//{put(NAME_EXPBANK,new ItemExpBank(block_expbank).setRegistryName(ModCommon.MOD_ID + ":" + NAME_EXPBANK));}
			{put(NAME_IMOUTOHEAD_NORMAL, new ItemBlockImoutoHead(imouto_head_normal).setRegistryName(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_NORMAL));}
			{put(NAME_IMOUTOHEAD_ROTTEN, new ItemBlockImoutoHead(imouto_head_rotten).setRegistryName(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_ROTTEN));}
			{put(NAME_IMOUTOHEAD_SKELTON, new ItemBlockImoutoHead(imouto_head_skelton).setRegistryName(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_SKELTON));}
		};


		resourceMap = new HashMap<String,ResourceLocation[]>(){
			//{put(NAME_EXPBANK, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID + ":" + NAME_EXPBANK)});}
			{put(NAME_IMOUTOHEAD_NORMAL, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_NORMAL)});}
			{put(NAME_IMOUTOHEAD_ROTTEN, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_ROTTEN)});}
			{put(NAME_IMOUTOHEAD_SKELTON, new ResourceLocation[]{new ResourceLocation(ModCommon.MOD_ID + ":" + NAME_IMOUTOHEAD_SKELTON)});}
		};
	}


	public static void register(FMLPreInitializationEvent event){
		init();
		for (String key: NAME_LIST){
			ForgeRegistries.BLOCKS.register(blockMap.get(key));
			ForgeRegistries.ITEMS.register(itemMap.get(key));
		}

		if (event.getSide().isClient()){
			for (String key : NAME_LIST){
				Item witem = itemMap.get(key);
				ResourceLocation[] wresource = resourceMap.get(key);
				if (wresource.length > 1){
					ModelLoader.registerItemVariants(witem, wresource);
				}
				for (int i = 0; i < wresource.length; i++){
					ModelLoader.setCustomModelResourceLocation(witem, i,
							new ModelResourceLocation(wresource[i], "inventory"));
				}
			}
		}
	}
}