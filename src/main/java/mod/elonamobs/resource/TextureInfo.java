package mod.fbd.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import mod.fbd.core.Mod_FantomBlade;
import mod.fbd.core.log.ModLog;

public class TextureInfo {
	// ファイル名
	private String fileName;
	// モデル名
	private String modelName;
	// テクスチャ名
	private String textureName;
	// テクスチャのサイズ
	private int[] texSize = new int[2];
	// テクスチャイメージ
	BufferedImage img;

	// 使える？
	private boolean canUse;

	public TextureInfo(String path, InputStream istream) throws Exception{
		try {
			String[] tex = path.split("/");
			fileName = tex[tex.length-1];
			String[] work = fileName.split("_");
			// モデル名を確認
			if ((modelName=Mod_FantomBlade.checkModelName(work[0].replace(".png", ""))) == null){
				// モデル名がない場合登録しない
				ModLog.log().debug("path:"+path);
				canUse = false;
				return;
			}
			modelName = work[0].replace(".png", "");
			textureName = fileName.toString().replace(".png", "");

			// サイズの取得
			img = ImageIO.read(istream);
			texSize = new int[]{img.getWidth(),img.getHeight()};
			canUse = true;
		} catch (IOException e) {
			canUse = false;
			throw e;
		}

	}

	public boolean canUse(){
		return canUse;
	}

    // テクスチャのファイル名を返す
	public String FileName(){
		return this.fileName;
	}

	// テクスチャの名称を返す
	public String Name(){
		return textureName;
	}

	// テクスチャのサイズを返す
	public int[] Size(){
		return texSize;
	}

	// テクスチャの画像を返す
	public BufferedImage Image(){
		return img;
	}

	// モデル名を返す
	public String getTarget(){
		return modelName;
	}

	/***************************/
	/** デバッグ用            **/
	/***************************/
	public String toString(){
		StringBuilder resInf = new StringBuilder();
		resInf.append(this.fileName);
		resInf.append("\t");
		resInf.append(modelName);
		resInf.append("\t");
		resInf.append(textureName);
		resInf.append("\t");
		resInf.append(texSize[0]);
		resInf.append("\t");
		resInf.append(texSize[1]);
		return resInf.toString();
	}

}
