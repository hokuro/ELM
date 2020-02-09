package mod.elm.block;

import java.util.HashMap;
import java.util.Map;

import mod.elm.block.item.ItemBlockElmHead;
import mod.elm.core.Mod_Elm;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class BlockCore {

	public static final String NAME_ELM_HEAD = "elm_head";
	public static final String NAME_MAERGERPOT = "maergerpot";
	private static final String[] NAME_LIST = new String[]{
			NAME_ELM_HEAD,
			NAME_MAERGERPOT
	};

	public static Block block_elmhead = new BlockElmHead(Block.Properties.create(Material.WOOD).sound(SoundType.SLIME).lightValue(7)).setRegistryName(NAME_ELM_HEAD);
	public static Item item_elmhead = new ItemBlockElmHead(block_elmhead, new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_ELM_HEAD);

	public static Block block_maergerpot = new BlockMaegerPot(Block.Properties.create(Material.EARTH).sound(SoundType.GLASS).hardnessAndResistance(0.5F,5F).lightValue(7)).setRegistryName(NAME_MAERGERPOT);
	public static Item item_margerpot = new BlockItem(block_maergerpot, new Item.Properties().group(Mod_Elm.GROUPELM)).setRegistryName(NAME_MAERGERPOT);

	private static Map<String,Block> blockMap;
	private static Map<String,Item> itemMap;
	private static Map<String,ResourceLocation[]> resourceMap;

	public static void init(){
		blockMap = new HashMap<String,Block>(){
			{put(NAME_ELM_HEAD,block_elmhead);}
			{put(NAME_MAERGERPOT,block_maergerpot);}
		};

		itemMap = new HashMap<String,Item>(){
			{put(NAME_ELM_HEAD,item_elmhead);}
			{put(NAME_MAERGERPOT,item_margerpot);}
		};
	}


	public static void registerBlock(final RegistryEvent.Register<Block> event){
		for (String name : NAME_LIST){
			if (blockMap.containsKey(name)) {
				event.getRegistry().register(blockMap.get(name));
			}
		}
	}

	public static void registerBlockItem(final RegistryEvent.Register<Item> event){
		for (String name : NAME_LIST){
			if (itemMap.containsKey(name)) {
				event.getRegistry().register(itemMap.get(name));
			}
		}
	}

}
