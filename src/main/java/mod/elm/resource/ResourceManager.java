package mod.elm.resource;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import mod.elm.core.ModCommon;
import mod.elm.core.log.ModLog;
import mod.elm.util.ModUtil;

public class ResourceManager {

    // カスタムテクスチャ用のディレクトリ
	public static final String DirName = "texture";
    // デフォルトリソースのパス
	private static final String ORIGINAL_RESOURCE = "assets/fbd/textures/entity";

	// カスタムリソースの使用ができるか
	private boolean canCustomResource = false;

	// マップ
	private final Map<String,List<TextureInfo>> texturemaps;

	// png ファイルかどうか確認するフィルタ
	private static final FileFilter png = new FileFilter(){
		@Override
		public boolean accept(File pathname){
			try{
				if (pathname.isFile()){
					if (pathname.getName().endsWith(".png")){
						return true;
					}
				}
			}catch(Exception ex){
				ModLog.log().warn("[Texture Find Error] : "+pathname.getName() );
			}
			return false;
		}
	};

	public TextureInfo getTexture(String model, String fileName){
		TextureInfo retTexture = null;
		if (texturemaps.containsKey(model)){
			List<TextureInfo> map = texturemaps.get(model);
			if (map.size() >= 1){
				for (TextureInfo tex : map){
					if (tex.FileName().equals(fileName)){
						retTexture = tex;
						break;
					}
				}
			}
		}
		return retTexture;
	}

	public TextureInfo getRandomTexture(String model){
		TextureInfo retTexture = null;
		if (texturemaps.containsKey(model)){
			List<TextureInfo> map = texturemaps.get(model);
			if (map.size() >= 1){
				int idx = ModUtil.random(map.size());
				if (idx >= map.size()){
					retTexture = map.get(0);
				}else{
					retTexture = map.get(idx);
				}
			}
		}
		return retTexture;
	}

	public ResourceManager(){
		texturemaps = new HashMap<String,List<TextureInfo>>();
//		for (String name : Mod_ElonaMobs.modelNames){
//			texturemaps.put(name, new ArrayList<TextureInfo>());
//		}
	}

	public void initResource() throws Exception {
		// デフォルトテクスチャ、デフォルトオプションパーツをマップに登録
		File dir;
		Class target = this.getClass();
		if (ModCommon.isDevelop){
			// 自分のクラス自身のパス(URL)を取得する
			URL location = target.getResource("/" + ORIGINAL_RESOURCE);
			dir = new File(location.getPath());
			ModLog.log().info("[Default Resource Path]:" + location.getPath());

			// マップ作製
			this.searchPng(dir);
		}else{
			// 自分のクラス自身のパス(URL)を取得する
			URL location = target.getResource("");
			ModLog.log().debug(location.getPath());
			dir = new File(location.getPath().substring(location.getPath().indexOf(":")+1, location.getPath().lastIndexOf(".jar")+4));
			if (ModCommon.isDebug)ModLog.log().debug("[Default Resource Path]:" + location.getPath());
			if (ModCommon.isDebug)ModLog.log().debug("[ZipFile]: " + dir.getPath());

			//マップ作製
			ZipFile zip = null;
			try{
				if (dir.exists()){
					zip = new ZipFile(dir);
					for (Enumeration<? extends ZipEntry> e = zip.entries(); e.hasMoreElements();) {
						ZipEntry entry = e.nextElement();
						if (ModCommon.isDebug)ModLog.log().debug("[check target]:"+entry.getName());
						if (entry.isDirectory()) continue;

						// パーツフォルダの中身だけ検索
						if (entry.getName().startsWith(ORIGINAL_RESOURCE)){
							if (ModCommon.isDebug)ModLog.log().debug("[read start]:"+entry.getName());
							try(InputStream istream = zip.getInputStream(entry)){
								if (entry.getName().endsWith(".png")){
									// ping用
									if (ModCommon.isDebug)ModLog.log().debug("png");
									TextureInfo info = new TextureInfo(entry.getName(),istream);
									addTexture(info);
								}
							}catch(Exception ex){
								ex.printStackTrace();
								ModLog.log().info("[Error add] : " + entry.getName());
							}
						}
					}
				}else{
					ModLog.log().fatal("[jarfile not found]");
					throw new FileNotFoundException();
				}
			}catch(Exception ex)
			{
				ModLog.log().fatal("[Default Texture Error]");
				ex.printStackTrace();
				throw ex;
			}finally{
				if(zip != null){
					try{
						zip.close();
					}catch(Exception ex){

					}
				}
			}
		}
		this.canCustomResource = true;
	}


	/**
	 * textureフォルダのpngファイルを検索してマップに登録する
	 * @param dir
	 * @return
	 */
	private boolean searchPng(File dir){
		// png リスト取得
		File[] pngs = dir.listFiles(png);
		for(File pngfile : pngs){
			try{
				try(InputStream istream = new FileInputStream(pngfile)){
					// テクスチャを追加
					addTexture(new TextureInfo(pngfile.getName(), istream));
				}
			}catch(Exception ex){
				// テクスチャエラー
				ModLog.log().warn("[Texture Error] :" + pngfile.getName());
			}
		}
		return true;
	}

	/**
	 * マップにテクスチャを登録する
	 * @param texture
	 */
	private void addTexture(TextureInfo texture){
		if (texture.canUse()){
			List<TextureInfo> map = texturemaps.get(texture.getTarget());
			if (map != null && !map.contains(texture.Name())){
				map.add(texture);
			}else{
				ModLog.log().info("[Texture duplicate] : " + texture.Name());
			}
		}else{
			ModLog.log().debug("texture not canuse:"+texture.FileName());
		}
	}
}
