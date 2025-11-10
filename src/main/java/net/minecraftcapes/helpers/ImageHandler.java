package net.minecraftcapes.helpers;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ImageHandler {

    public static BufferedImage legacyTransparencyFix(InputStream oldImage) throws IOException {
        // Step 1: Create input streams
        ImageInputStream iis = ImageIO.createImageInputStream(oldImage);

        Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
        if (!readers.hasNext()) throw new IOException("No image reader found");

        ImageReader reader = readers.next();
        reader.setInput(iis, true);

        // Step 2: Read the image (opaque by default)
        BufferedImage original = reader.read(0);

        // Step 3: Extract tRNS chunk from metadata
        IIOMetadata metadata = reader.getImageMetadata(0);
        String nativeFormat = metadata.getNativeMetadataFormatName();
        Node root = metadata.getAsTree(nativeFormat);

        String transparentRGB = null;
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if ("tRNS".equals(node.getNodeName())) {
                NodeList tRNSChildren = node.getChildNodes();
                for(int j = 0; j < tRNSChildren.getLength(); j++) {
                    Node rgbNode = tRNSChildren.item(j);
                    if ("tRNS_RGB".equals(rgbNode.getNodeName())) {
                        NamedNodeMap attrs = rgbNode.getAttributes();
                        String red = attrs.getNamedItem("red").getNodeValue();
                        String green = attrs.getNamedItem("green").getNodeValue();
                        String blue = attrs.getNamedItem("blue").getNodeValue();
                        transparentRGB = red + "," + green + "," + blue;
                    }
                }
            }
        }

        // If no tRNS found, return original
        if (transparentRGB == null) {
            return original;
        }

        // Step 4: Apply transparency manually
        String[] rgbParts = transparentRGB.split(",");
        int tr = Integer.parseInt(rgbParts[0]);
        int tg = Integer.parseInt(rgbParts[1]);
        int tb = Integer.parseInt(rgbParts[2]);

        BufferedImage argbImage = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int rgb = original.getRGB(x, y) & 0x00FFFFFF; // Strip alpha
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                if (r == tr && g == tg && b == tb) {
                    argbImage.setRGB(x, y, 0x00000000); // Transparent
                } else {
                    argbImage.setRGB(x, y, 0xFF000000 | rgb); // Opaque
                }
            }
        }

        return argbImage;
    }

}
