package terpword;
import java.io.File;
import java.io.FileOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
class testgeticon {
	public static void main(String[] args){
		//terpword.Ekit.getIcon(new File("test.html"));
	
		try {
        FileOutputStream fos = new FileOutputStream("out.jpg");
        JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(fos);
        jpeg.encode(terpword.Ekit.getIcon(new File("test.html")));
        fos.close();
		}catch(Exception e ){ System.out.println(e); }
	}
}