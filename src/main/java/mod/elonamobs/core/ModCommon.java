package mod.fbd.core;

public final class ModCommon {
	// デバッグモードかどうか
	public static boolean isDebug = false;

	public static boolean isDevelop= false;

	// モッドID
	public static final String MOD_ID = "fbd";
	// モッド名
	public static final String MOD_NAME = "FantomBlads";
	public static final String MOD_PACKAGE = "mod.fbd";
	public static final String MOD_CLIENT_SIDE = ".client.ClientProxy";
	public static final String MOD_SERVER_SIDE = ".core.CommonProxy";
	public static final String MOD_FACTRY = ".client.config.FantomBladsFactory";
    // 前に読み込まれるべき前提MODをバージョン込みで指定
    public static final String MOD_DEPENDENCIES = "required-after:Forge@[1.9-12.16.0.1853,)";
    // 起動出来るMinecraft本体のバージョン。記法はMavenのVersion Range Specificationを検索すること。
    public static final String MOD_ACCEPTED_MC_VERSIONS = "[1.12]";
	// モッドバージョン
	public static final String MOD_VERSION = "1.12.0";
	// コンフィグファイル名
	public static final String MOD_CONFIG_FILE = "";
	// コンフィグ
	public static final String MOD_CONFIG_LANG = "";
	// コンフィグリロード間隔
	public static final long MOD_CONFIG_RELOAD = 500L;

	// コンフィグ カテゴリー general
	public static final String MOD_CONFIG_CAT_GENELAL = "general";
	public static final String MOD_CHANEL ="Mod_Channel_FantomBlads";

	// MessageID
	public static final int MESID_BLADEFORGESTARTSMELTING = 0;
	public static final int MESID_SETRUNAIRPOMP = 1;
	public static final int MESID_SETRUNBLADEFORGE = 2;
	public static final int MESID_CREATEBLADE = 3;
	public static final int MESID_REPAIREBLADE = 4;
	public static final int MESID_BLADESTANDUPDATE = 5;
	public static final int MESID_BLADELEVELUPDATE = 6;






























}
