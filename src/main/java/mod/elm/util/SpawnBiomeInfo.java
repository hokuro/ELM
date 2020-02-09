package mod.elm.util;

import java.util.Optional;

import mod.elm.core.log.ModLog;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class SpawnBiomeInfo {
	private String _name;
	private int _weight;
	private int _min;
	private int _max;
	private Biome _allowBiome;

	public SpawnBiomeInfo(String name, int weight, int min, int max, String biomeName) {
		_name = name;
		_weight = weight;
		_min = min;
		_max = max;
		try {
			Optional<Biome> opt_biome = Registry.BIOME.getValue(new ResourceLocation(biomeName));
			if (opt_biome.isPresent()) {
				_allowBiome = opt_biome.get();
			}
		}catch(Throwable ex) {
			ModLog.log().error("no allow biome : " + biomeName);
		}
	}

	public String getName() {return _name;}
	public int getWeight() {return _weight;}
	public int getMin() {return _min;}
	public int getMax() {return _max;}
	public Biome getBiome(){return _allowBiome;}
}