import java.util.Vector;

/**
 * @author xwc
 * @version 1.0
 * @created 18-1-2
 */
interface  FatherSdt {
    public void compress(Vector<Point> undeal, Vector<Point> comp);
    public void uncompress(Vector<Point> comp, Vector<Point> dealt);
}
