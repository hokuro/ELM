package mod.elm.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import mod.elm.core.log.ModLog;
import mod.elm.entity.EntityWanderingPeople;
import mod.elm.util.SpawnBiomeInfo;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;

public class MyModConfig{
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final Map<String,Spawn> spawns = new HashMap<String, Spawn>(){
		{put(EntityWanderingPeople.NAME, new Spawn(BUILDER, EntityWanderingPeople.NAME));}
	};
	public static final WanderingPeople Setting_WanderingPeople = new WanderingPeople(BUILDER);
	public static final ForgeConfigSpec spec = BUILDER.build();
	public static final Map<String,List<SpawnBiomeInfo>> spawnInfo = Maps.newHashMap();

	public static List<SpawnBiomeInfo> getInfo(String key) {
		if (spawnInfo.isEmpty()) {
			makeSpawnInfo();
		}
		return spawnInfo.get(key);
	}

	private static void makeSpawnInfo() {
		for (String key : spawns.keySet()) {
			// aキー用のリストを追加
			spawnInfo.put(key, Lists.newArrayList());

			// aコンフィグ情報をゲット
			Spawn config = spawns.get(key);

			// aデフォルト値を取得
			int weight_d = config.weight.get();
			int max_d = config.max.get();
			int min_d = config.min.get();
			List<? extends String> spawanConfig = config.allows.get();

			// aスポーン情報を作成
			for (String spawan : spawanConfig) {
				try {
					String[] parts = spawan.split(",");
					String biomeName = parts[0];
					int weight = parts.length>1?Integer.parseInt(parts[1]):weight_d;
					int max = parts.length>2?Integer.parseInt(parts[2]):weight_d;
					int min = parts.length>3?Integer.parseInt(parts[3]):weight_d;

					spawnInfo.get(key).add(new SpawnBiomeInfo(key, weight, min, max, biomeName));
				}catch(Throwable ex) {
					ModLog.log().error("can't create spawan info :" + spawan);
				}
			}
		}
	}

	public static class WanderingPeople{
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> items;
		public WanderingPeople(Builder builder) {
			builder.push("Setting_WanderingPeople");
            this.items = builder.defineList("additional_item", Lists.newArrayList(), (o) -> {
            	try {
            		return Registry.ITEM.getValue(new ResourceLocation((String)o)).isPresent();
            	}catch(Throwable ex) {
            		return false;
            	}
    		});
			builder.pop();
		}

		public static List<Item> additionalItem = Lists.newArrayList();
		public List<Item> getAdditionalItem(){
			if (additionalItem.size() != 0) {
				for (String str : items.get()) {
					Optional<Item> item = Registry.ITEM.getValue(new ResourceLocation(str));
					if (item.isPresent()) {
						additionalItem.add(item.get());
					}
				}
			}
			return additionalItem;
		}
	}

	public static class Spawn{
		public final ForgeConfigSpec.ConfigValue<Integer> weight;
		public final ForgeConfigSpec.ConfigValue<Integer> min;
		public final ForgeConfigSpec.ConfigValue<Integer> max;
		public final ForgeConfigSpec.ConfigValue<List<? extends String>> allows;
		public Spawn(Builder builder, String name) {
			builder.push("Spawan_" + name);
			weight = builder.defineInRange("weight_default", 10, 0, 100);
			min = builder.defineInRange("min_default", 1, 0, 127);
			max = builder.defineInRange("max_default", 1, 0, 127);
			List<String> biomes =new ArrayList<String>(){
				{add(Biomes.PLAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.DESERT.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.MOUNTAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.FOREST.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.TAIGA.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SWAMP.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SNOWY_TUNDRA.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SNOWY_MOUNTAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.BEACH.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.DESERT_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.WOODED_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.TAIGA_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.MOUNTAIN_EDGE.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.JUNGLE.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.JUNGLE_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.JUNGLE_EDGE.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.STONE_SHORE.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SNOWY_BEACH.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.BIRCH_FOREST.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.BIRCH_FOREST_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.DARK_FOREST.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SNOWY_TAIGA.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SNOWY_TAIGA_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.GIANT_TREE_TAIGA.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.GIANT_TREE_TAIGA_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.WOODED_MOUNTAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SAVANNA.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SAVANNA_PLATEAU.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.BADLANDS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.WOODED_BADLANDS_PLATEAU.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.BADLANDS_PLATEAU.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SMALL_END_ISLANDS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SUNFLOWER_PLAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.GRAVELLY_MOUNTAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.FLOWER_FOREST.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.TAIGA_MOUNTAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SWAMP_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.ICE_SPIKES.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.MODIFIED_JUNGLE.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.MODIFIED_JUNGLE_EDGE.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.TALL_BIRCH_FOREST.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.TALL_BIRCH_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.DARK_FOREST_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SNOWY_TAIGA_MOUNTAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.GIANT_SPRUCE_TAIGA.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.GIANT_SPRUCE_TAIGA_HILLS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.MODIFIED_GRAVELLY_MOUNTAINS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SHATTERED_SAVANNA.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.SHATTERED_SAVANNA_PLATEAU.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.ERODED_BADLANDS.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.MODIFIED_BADLANDS_PLATEAU.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.BAMBOO_JUNGLE.getRegistryName().toString()+",10,1,1");}
				{add(Biomes.BAMBOO_JUNGLE_HILLS.getRegistryName().toString()+",10,1,1");}
			};
            this.allows = builder.defineList("canspawn", biomes, (o) -> {
            	try {
            		return Registry.BIOME.getValue(new ResourceLocation((String)o)).isPresent();
            	}catch(Throwable ex) {
            		return false;
            	}
    		});
			builder.pop();
		}
	}

}