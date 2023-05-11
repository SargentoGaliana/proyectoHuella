import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static FingerPrintImage RGBaGris(BufferedImage imagenentrada){
        FingerPrintImage imagensalida = new FingerPrintImage(imagenentrada.getWidth(), imagenentrada.getHeight());
        for (int x = 0; x < imagenentrada.getWidth(); ++x) {
            for (int y = 0; y < imagenentrada.getHeight(); ++y) {
                int rgb = imagenentrada.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                //metodo A
                int nivelGrisA = (r + g + b) / 3;
                //metodo B
                double nivelGrisB= 0.2126*r + 0.7152*g + 0.0722*b;
                imagensalida.setPixel(x, y, (char) nivelGrisB);
            }
        }
        return imagensalida;
    }

    public static BufferedImage GrisaRGB(FingerPrintImage imagenentrada, int modo){
        BufferedImage imagensalida = new BufferedImage(imagenentrada.getWidth(), imagenentrada.getHeight(),
        BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < imagenentrada.getWidth(); ++x) {
            for (int y = 0; y < imagenentrada.getHeight(); ++y) {
                int valor = imagenentrada.getPixel(x, y);
                if (modo == 0) {
                    valor = valor * 255;
                }
                int pixelRGB = (255 << 24 | valor << 16 | valor << 8 | valor);
                imagensalida.setRGB(x, y, pixelRGB);
            }
        }

        return imagensalida;
    }

    public static FingerPrintImage histograma(FingerPrintImage imagenentrada){
        int width = imagenentrada.getWidth();
        int height = imagenentrada.getHeight();
        int tampixel = width*height;
        int[] histograma = new int[256];
        int i = 0;
        FingerPrintImage imagenactualizada = new FingerPrintImage(width, height);

        //Calculamos frecuencia relativa de ocurrencia de los niveles de gris
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                int valor = imagenentrada.getPixel(x,y);
                histograma[valor]++;
            }
        }
        int sum = 0;

        //Construimos la Lookup table LUT
        float[] lut = new float[256];
        for(i = 0; i < 256; i++){
            sum += histograma[i];
            lut[i] = sum*255/tampixel;
        }

        // Se transforma la imagen utilizando la tabla LUT
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int valor= imagenentrada.getPixel(x, y);
                int valorNuevo= (int) lut[valor];
                imagenactualizada.setPixel(x, y, (char) valorNuevo);
            }
        }
        return imagenactualizada;
    }

    public static double[] nivelesGris(BufferedImage imagenentrada){
        //Calculamos los niveles max, min y med de gris
        double nivelGris = 0;
        double max = 0;
        double min = 255;
        double med = 0;
        int cont = 0;
        double[] v = new double[3];

        for (int x = 0; x < imagenentrada.getWidth(); ++x) {
            for (int y = 0; y < imagenentrada.getHeight(); ++y) {
                int rgb = imagenentrada.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                nivelGris = 0.2126 * r + 0.7152 * g + 0.0722 * b;
                med = med + nivelGris;
                cont++;
                if(nivelGris > max){
                    max = nivelGris;
                }
                if(nivelGris < min){
                    min = nivelGris;
                }
            }
        }
        med = med / cont;
        v[0] = min;
        v[1] = max;
        v[2] = med;
        System.out.println("Niveles de gris de la imagen ecualizada:");
        System.out.println("Min = "+min);
        System.out.println("Max = "+max);
        System.out.println("Med = "+med);
        return v;
    }

    public static FingerPrintImage umbralizar(FingerPrintImage imagenentrada, double umbral){
        FingerPrintImage imagensalida = new FingerPrintImage(imagenentrada.getWidth(), imagenentrada.getHeight());

        for(int x = 0; x < imagenentrada.getWidth(); x++){
            for(int y = 0; y < imagenentrada.getHeight(); y++){
                double valor = imagenentrada.getPixel(x, y);
                if(valor < umbral){
                    imagensalida.setPixel(x, y, (char) 0);
                }else{
                    imagensalida.setPixel(x, y, (char) 1);
                }
            }
        }
        return  imagensalida;
    }

    public static FingerPrintImage filtrar( FingerPrintImage imagenentrada ){
        FingerPrintImage imagensalida = new FingerPrintImage( imagenentrada.getWidth() , imagenentrada.getHeight() );
        FingerPrintImage imagenaux = new FingerPrintImage( imagenentrada.getWidth() , imagenentrada.getHeight() );
        int a, b, c ,d, e, f, g, h, p, B1, B2;

        for(int x = 0; x < imagenentrada.getWidth(); x++ ){
            for(int y = 0; y < imagenentrada.getHeight(); y++ ){
                if(x > 0 && x < imagenentrada.getWidth()-1 && y > 0 && y < imagenentrada.getHeight()-1){
                    b = imagenentrada.getPixel(x,y-1);
                    d = imagenentrada.getPixel(x-1, y);
                    e = imagenentrada.getPixel(x+1, y);
                    g = imagenentrada.getPixel( x, y+1);
                    p = imagenentrada.getPixel( x, y);
                    //Filtro binario 1
                    B1 = p | b & g & (d|e) | d & e & (b|g);
                } else {
                    B1 = imagenentrada.getPixel(x, y);
                }
                imagenaux.setPixel(x, y, (char) B1);
            }
        }

        for(int x = 0; x < imagenaux.getWidth(); x++ ){
            for(int y = 0; y < imagenaux.getHeight(); y++ ){
                if(x > 0 && x < imagenaux.getWidth()-1 && y > 0 && y < imagenaux.getHeight()-1){
                    a = imagenaux.getPixel(x-1 ,y-1);
                    b = imagenaux.getPixel(x, y-1);
                    c = imagenaux.getPixel(x+1,y-1);
                    d = imagenaux.getPixel(x-1, y);
                    e = imagenaux.getPixel(x+1, y);
                    f = imagenaux.getPixel(x-1, y+1);
                    g = imagenaux.getPixel(x,y+1);
                    h = imagenaux.getPixel(x+1,y+1);
                    p = imagenaux.getPixel( x, y);
                    //Filtro binario 2
                    B2 = p & ((a|b|d) & (e|g|h) & (b|c|e) & (d|f|g));
                } else {
                    B2 = imagenaux.getPixel(x , y);
                }
                imagensalida.setPixel(x , y , (char) B2);
            }
        }
        return imagensalida;
    }


    public static void main(String[] args) {

        BufferedImage imagenentrada = null;
        try {
            //Cargamos una imagen a color desde un archivo
            imagenentrada = ImageIO.read(new File("img/101_3.jpg"));
            System.out.println("Cargada la imagen");

            //Se convierte la imagen a escala de grises
            FingerPrintImage img = RGBaGris(imagenentrada);
            BufferedImage imagensalida = GrisaRGB(img, 1);
            File outputfile = new File("img/imagen_en_grises.png");
            ImageIO.write(imagensalida, "png", outputfile);
            System.out.println("Imagen salvada");

            //Se realiza el histograma de la imagen en escala de grises
            FingerPrintImage imgHisto = RGBaGris(imagensalida);
            imgHisto = histograma(imgHisto);
            BufferedImage imagensalidaHisto = GrisaRGB(imgHisto, 1);

            //Se calculan y se almacenan los niveles de gris de la imagen obtenida
            double[] niveles = nivelesGris(imagensalidaHisto);
            imgHisto.setMin(niveles[0]);
            imgHisto.setMax(niveles[1]);
            imgHisto.setMed(niveles[2]);
            File outputfileHisto = new File("img/imagen_histograma.png");
            ImageIO.write(imagensalidaHisto, "png", outputfileHisto);
            System.out.println("Imagen histograma salvada");

            //Se convierte la imagen a ByN mediante el valor de la media como umbral
            FingerPrintImage imgUmbral = umbralizar(imgHisto, imgHisto.getMed());
            BufferedImage imagensalidaUmbral= GrisaRGB(imgUmbral, 0);
            File outputfileUmbral = new File("img/imagen_umbralizada.png");
            ImageIO.write(imagensalidaUmbral, "png", outputfileUmbral);
            System.out.println("Imagen umbralizada salvada");

            //Se filtra la imagen en ByN con los filtros binarios 1 y 2
            FingerPrintImage imgFiltro = filtrar(imgUmbral);
            BufferedImage imagensalidaFiltro= GrisaRGB(imgFiltro, 0);
            File outputfileFiltro = new File("img/imagen_filtrada.png");
            ImageIO.write(imagensalidaFiltro, "png", outputfileFiltro);
            System.out.println("Imagen filtrada salvada");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}