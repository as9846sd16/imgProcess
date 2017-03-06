package imgProcess.model;

import java.lang.reflect.Array;
import java.util.Arrays;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public abstract class ImageProcess {
	public static Image avgBlur(Image img, int size){
		int imgW = (int)img.getWidth(), imgH = (int)img.getHeight();
		int r = size/2;
		WritableImage newImg = new WritableImage(imgW,imgH);
		PixelReader pxr = img.getPixelReader();
		PixelWriter pxw = newImg.getPixelWriter();
		for(int hh = 0; hh < imgH; hh++){
			for(int ww = 0; ww < imgW; ww++){
				double rr = 0, gg = 0, bb = 0;
				Color c = pxr.getColor(ww, hh);
				for(int i = -r; i <= r; i++){
					for(int j = -r; j<= r ; j++){
						int x ,y;
						x = Math.min(imgW-1, Math.max(0, ww+j));
						y = Math.min(imgH-1, Math.max(0, hh+i));
						Color cc = pxr.getColor(x, y);
						rr = rr + cc.getRed();
						gg = gg + cc.getGreen();
						bb = bb + cc.getBlue();
					}
				}
				int d = (2*r+1)*(2*r+1);
				c = Color.color(rr/d, gg/d, bb/d);
				pxw.setColor(ww, hh, c);
			}
		}
		return newImg;
	}
	public static Image gaussBlur(Image img, int size, double sigma){
		int imgW = (int)img.getWidth(), imgH = (int)img.getHeight();
		int r = size/2;
		WritableImage newImg = new WritableImage(imgW,imgH);
		PixelReader pxr = img.getPixelReader();
		PixelWriter pxw = newImg.getPixelWriter();
		double[][] gBox = gauessBox(r, sigma);
		for(int hh = 0; hh < imgH; hh++){
			for(int ww = 0; ww < imgW; ww++){
				double rr = 0, gg = 0, bb = 0;
				Color c = pxr.getColor(ww, hh);
				double sum = 0;
				for(int i = -r; i <= r; i++){
					for(int j = -r; j<= r ; j++){
						int x ,y;
						x = Math.min(imgW-1, Math.max(0, ww+j));
						y = Math.min(imgH-1, Math.max(0, hh+i));
						Color cc = pxr.getColor(x, y);
						double gValue = gBox[r + i][ r + j];
						rr = rr + gValue * cc.getRed();
						gg = gg + gValue * cc.getGreen();
						bb = bb + gValue * cc.getBlue();
						sum = sum + gValue;
					}
				}
				c = Color.color(rr/sum,gg/sum,bb/sum);
				pxw.setColor(ww, hh, c);
			}
		}
		return newImg;
	}
	private static double[][] gauessBox(int r, double sigma){
    	double[][] box = new double[2*r+1][2*r+1];
    	double s = 2*sigma*sigma;
    	for(int i = -r; i<=r; i++){
    		for(int j =-r; j<=r; j++){
    			box[r+i][r+j] = Math.exp(-1 *((i*i+j*j)/(s))) / (s*Math.PI);
    		}
    	}
    	return box;
    }
	public static Image medianBlur(Image img, int size){
		int imgW = (int)img.getWidth(), imgH = (int)img.getHeight();
		int r = size/2;
		WritableImage newImg = new WritableImage(imgW,imgH);
		PixelReader pxr = img.getPixelReader();
		PixelWriter pxw = newImg.getPixelWriter();
		for(int hh = 0; hh < imgH; hh++){
			for(int ww = 0; ww < imgW; ww++){
				double rr = 0, gg = 0, bb = 0;
				double[] rl = new double[size*size], gl=new double[size*size], bl=new double[size*size];
				Color c = pxr.getColor(ww, hh);
				int k = 0;
				for(int i = -r; i <= r; i++){
					for(int j = -r; j<= r ; j++){
						int x ,y;
						x = Math.min(imgW-1, Math.max(0, ww+j));
						y = Math.min(imgH-1, Math.max(0, hh+i));
						Color cc = pxr.getColor(x, y);
						rl[k] = cc.getRed();
						gl[k] = cc.getGreen();
						bl[k] = cc.getBlue();
						k++;
					}
				}
				Arrays.sort(rl); Arrays.sort(gl); Arrays.sort(bl);
				rr = rl[(size*size)/2];
				gg = gl[(size*size)/2];
				bb = bl[(size*size)/2];
				c = Color.color(rr,gg,bb);
				pxw.setColor(ww, hh, c);
			}
		}
		return newImg;
	}
	//ismM == true => min filter, ismM == false => max filter
	public static Image minMaxFilter(Image img, boolean isMin){
		int imgW = (int)img.getWidth(), imgH = (int)img.getHeight();
		int r = 3/2;
		WritableImage newImg = new WritableImage(imgW,imgH);
		PixelReader pxr = img.getPixelReader();
		PixelWriter pxw = newImg.getPixelWriter();
		for(int hh = 0; hh < imgH; hh++){
			for(int ww = 0; ww < imgW; ww++){
				Color c = pxr.getColor(ww, hh);
				double rmM = c.getRed(),gmM = c.getGreen(), bmM=c.getBlue();
				for(int i = -r; i <= r; i++){
					for(int j = -r; j<= r ; j++){
						int x ,y;
						x = Math.min(imgW-1, Math.max(0, ww+j));
						y = Math.min(imgH-1, Math.max(0, hh+i));
						Color cc = pxr.getColor(x, y);
						rmM = getmM(isMin, rmM,cc.getRed());
						gmM = getmM(isMin, gmM, cc.getGreen());
						bmM = getmM(isMin, bmM,cc.getBlue());
					}
				}
				c = Color.color(rmM, gmM, bmM);
				pxw.setColor(ww, hh, c);
			}
		}
		return newImg;
	}
	public static Image peakValleyFilter(Image img, boolean isPeak){
		int imgW = (int)img.getWidth(), imgH = (int)img.getHeight();
		int r = 3/2;
		WritableImage newImg = new WritableImage(imgW,imgH);
		PixelReader pxr = img.getPixelReader();
		PixelWriter pxw = newImg.getPixelWriter();
		for(int hh = 1; hh < imgH-1; hh++){
			for(int ww = 1; ww < imgW-1; ww++){
				Color c = pxr.getColor(ww, hh);
				Color ccc = pxr.getColor(ww-1, hh-1);
				double rmM = ccc.getRed(),gmM = ccc.getGreen(), bmM=ccc.getBlue();
				for(int i = -r; i <= r; i++){
					for(int j = -r; j<= r ; j++){
						if(i==0 && j==0){continue;}
						int x = ww+j ,y = hh+i;
						Color cc = pxr.getColor(x, y);
						rmM = getmM(isPeak, rmM,cc.getRed());
						gmM = getmM(isPeak, gmM, cc.getGreen());
						bmM = getmM(isPeak, bmM,cc.getBlue());
					}
				}
				rmM = getmM(!isPeak, rmM, c.getRed());
				gmM = getmM(!isPeak, gmM, c.getGreen());
				bmM = getmM(!isPeak, bmM, c.getBlue());
				c = Color.color(rmM, gmM, bmM);
				pxw.setColor(ww, hh, c);
			}
		}
		return newImg;
	}
	private static double getmM(boolean ismin, double mM, double n){
		if(ismin){
			return Math.min(mM, n);
		}
		else{
			return Math.max(mM, n);
		}
	}
	public static Image highBoost(Image img, double alpha){
		int imgW = (int)img.getWidth(), imgH = (int)img.getHeight();
		int r = 1;
		WritableImage newImg = new WritableImage(imgW,imgH);
		PixelReader pxr = img.getPixelReader();
		PixelWriter pxw = newImg.getPixelWriter();
		for(int hh = 0; hh < imgH; hh++){
			for(int ww = 0; ww < imgW; ww++){
				double bbb = 0, sss = 0;
				Color c = pxr.getColor(ww, hh);
				for(int i = -r; i <= r; i++){
					for(int j = -r; j<= r ; j++){
						int x ,y;
						x = Math.min(imgW-1, Math.max(0, ww+j));
						y = Math.min(imgH-1, Math.max(0, hh+i));
						Color cc = pxr.getColor(x, y);
						bbb = bbb + cc.getBrightness();
						sss = sss + cc.getSaturation();
					}
				}
				int d = (2*r+1)*(2*r+1);
				double BB = c.getBrightness(), SS = c.getSaturation();
				c = Color.hsb(c.getHue(), Math.min(1, alpha*SS + Math.max(0, SS - sss/d)), Math.min(1, alpha*BB + Math.max(0, BB - bbb/d)));
				pxw.setColor(ww, hh, c);
			}
		}
		return newImg;
	}
	public static Image difference(Image img, int peak){
		int imgW = (int)img.getWidth(), imgH = (int)img.getHeight();
		WritableImage newImg = new WritableImage(imgW,imgH);
		PixelReader pxr = img.getPixelReader();
		PixelWriter pxw = newImg.getPixelWriter();
		for(int hh = 0; hh < imgH; hh++){
			for(int ww = 0; ww < imgW; ww++){
				Color c = pxr.getColor(ww, hh), xpc, ypc;
				double xr, yr, xg, yg, xb, yb, rr, gg, bb;
				xr = yr = c.getRed();
				xg = yg = c.getGreen();
				xb = yb = c.getBlue();
				if(ww-1 >= 0){
					xpc = pxr.getColor(ww-1, hh);
					xr = xr - xpc.getRed();
					xg = xg - xpc.getGreen();
					xb = xb - xpc.getBlue();
				}
				if(hh + 1 < imgH){
					ypc = pxr.getColor(ww, hh+1);
					yr = yr - ypc.getRed();
					yg = yg - ypc.getGreen();
					yb = yb - ypc.getBlue();
				}
				double rd,gd,bd;
				rd = xr*xr + yr*yr;
				gd = xg*xg + yg*yg;
				bd = xb*xb + yb*yb;
				int dd;
				dd = (int)(256*Math.sqrt(rd + gd + bd));
				rr = c.getRed();
				gg = c.getGreen();
				bb = c.getBlue();
				double ss = c.getSaturation(), bbb = c.getBrightness();
				if(dd >= peak){
					ss = Math.min(1.0, 1.5*ss);
					bbb = Math.min(1.0, 1.1*bbb);
				}
				c = Color.hsb(c.getHue(), ss, bbb);
				pxw.setColor(ww, hh, c);
			}
		}
		return newImg;
	}
}
