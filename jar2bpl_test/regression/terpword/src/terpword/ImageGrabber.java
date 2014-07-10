package terpword;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ImageGrabber {

	/**
	 * If an image is on the system clipboard, this method returns it; otherwise
	 * it returns null
	 * 
	 * @return Image - image from clipboard
	 */
	public static Image getImageFromClipboard() {

		// get the system clipboard
		Clipboard systemClipboard = Toolkit.getDefaultToolkit()
				.getSystemClipboard();

		// get the contents on the clipboard in a
		// Transferable object
		Transferable clipboardContents = systemClipboard.getContents(null);

		// check if contents are empty, if so, return null
		if (clipboardContents == null)
			return null;
		else
			try {
				// make sure content on clipboard is
				// falls under a format supported by the
				// imageFlavor Flavor
				if (clipboardContents
						.isDataFlavorSupported(DataFlavor.imageFlavor)) {
					// convert the Transferable object
					// to an Image object
					Image image = (Image) clipboardContents
							.getTransferData(DataFlavor.imageFlavor);
					return image;
				}
			} catch (UnsupportedFlavorException ufe) {
				ufe.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		return null;
	}
}