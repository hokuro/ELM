package mod.elm.item;

import net.minecraft.item.ItemFood;

public class ItemCandy extends ItemFood {

	private static boolean isMillk;
	public ItemCandy(boolean millk) {
		super(0, false);
		isMillk = millk;
	}

	public boolean isMillk(){
		return this.isMillk;
	}

	public enum EnumCandyFlavor{
		CANDY_NORMAL(0,5,0,0,0,"normal"),
		CANDY_APLE(1,3,0,0,2,"apple"),
		CANDY_WATERMELLON(2,1,0,0,4,"watermellon"),
		CANDY_CACAO(3,2,0,5,0,"cacao"),
		CANDY_PUMPKIN(4,2,1,1,0,"pumpkin"),
		CANDY_CALLOT(5,1,3,3,0,"callot"),
		CANDY_CACTUS(6,0,0,3,3,"cactus");

		private int id;
		private String name;
		private int sweety;
		private int bitter;
		private int sour;
		private int salty;

		private EnumCandyFlavor(int idIn, int seval, int bival, int soval, int saval, String nameIn){
			id = idIn;
			name = nameIn;
			sweety=seval;
			bitter=bival;
			sour=soval;
			salty=saval;
		}

		public int getID(){return id;}
		public String getName(){return name;}
		public int getSweet(){return sweety;}
		public int getSalt(){return salty;}
		public int getBitter(){return bitter;}
		public int getSour(){return sour;}

		private static EnumCandyFlavor[] values ={CANDY_NORMAL,CANDY_APLE,CANDY_WATERMELLON,CANDY_CACAO,CANDY_PUMPKIN,CANDY_PUMPKIN,CANDY_CALLOT,CANDY_CACTUS};

		public static EnumCandyFlavor getFromIndex(int idx){
			if(idx < 0 || idx >= values.length){
				return CANDY_NORMAL;
			}
			return values[idx];
		}

		public static EnumCandyFlavor[] Values(){
			return values;
		}
	}

}
