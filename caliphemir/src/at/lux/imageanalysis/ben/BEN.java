package ben;

import at.lux.imageanalysis.ColorLayoutImpl;
import at.lux.imageanalysis.ColorStructureImplementation;
import at.lux.imageanalysis.EdgeHistogramImplementation;
import at.lux.imageanalysis.ScalableColorImpl;
import at.lux.imageanalysis.VisualDescriptor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class BEN {

    HashMap<String, Object> _features = new HashMap<String, Object>();

    public static void main(String[] args ) {
        BEN b = new BEN();
        try {
            b.foo();
        } catch (Exception ex) {
            Logger.getLogger(BEN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void foo() throws Exception {
        File dir = new File("C:\\work\\data\\images\\Slovenia");
        for (File child : dir.listFiles()) {
            testExtraction(child.getAbsolutePath());
        }
    }
    
    public float computeSimilarity(String file1, String file2) {
        ArrayList<VisualDescriptor> get1 = (ArrayList<VisualDescriptor>)_features.get(file1);
        ArrayList<VisualDescriptor> get2 = (ArrayList<VisualDescriptor>)_features.get(file2);
        
        float low = 0;
        for (int i = 0; i < get1.size(); i++) {
            low = low +get1.get(i).getDistance(get2.get(i));
        }
        
        low = low / get1.size();
        
return low;        
    }

    public void testExtraction(String fileName) throws IOException {
        BufferedImage img = ImageIO.read(new FileInputStream(fileName));
        long time;
        int countRuns = 5;

        ArrayList<VisualDescriptor> metadata = new ArrayList<VisualDescriptor>();
        
        
//        EdgeHistogramImplementation e2 = null;
//        time = System.currentTimeMillis();
//        for (int i = 0; i < countRuns; i++) {
//            e2 = new EdgeHistogramImplementation(img);
//            metadata.add(e2);
//        }
//        System.out.println("EdgeHistogram took " + ((float) (System.currentTimeMillis() - time) / (float) countRuns) + " ms each");
//
//        ScalableColorImpl sc = null;
//        time = System.currentTimeMillis();
//        for (int i = 0; i < countRuns; i++) {
//            sc = new ScalableColorImpl(img);
//            metadata.add(sc);
//        }
//        System.out.println("ScalableColor took " + ((float) (System.currentTimeMillis() - time) / (float) countRuns) + " ms each");

        ColorLayoutImpl cl = null;
        time = System.currentTimeMillis();
        for (int i = 0; i < countRuns; i++) {
            cl = new ColorLayoutImpl(img);
            metadata.add(cl);
        }
        System.out.println("ColorLayout took " + ((float) (System.currentTimeMillis() - time) / (float) countRuns) + " ms each");

//        ColorStructureImplementation dc = null;
//        time = System.currentTimeMillis();
//        for (int i = 0; i < countRuns; i++) {
//            dc = new ColorStructureImplementation(img);
//            metadata.add(dc);
//        }
//        System.out.println("DominantColor took " + ((float) (System.currentTimeMillis() - time) / (float) countRuns) + " ms each");
//        
        _features.put(fileName, metadata);
    }
}
