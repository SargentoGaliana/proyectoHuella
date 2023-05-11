public class FingerPrintImage {

    private int width;
    private int height;
    private char[][] img;
    private double max;
    private double min;
    private double med;

    FingerPrintImage(int width, int height){
        this.width = width;
        this.height = height;
        img = new char[width][height];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPixel(int x, int y, char color){
        img[x][y] = color;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMed() {
        return med;
    }

    public void setMed(double med) {
        this.med = med;
    }

    public char getPixel(int x, int y){
        return img[x][y];
    }
}
