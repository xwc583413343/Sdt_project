/**
 * @author xwc
 * @version 1.0
 * @created 17-12-20
 */
public class Point implements Cloneable{
    public int time=0;
    public double y=0.0;
    Point(){
    }
    Point(int time,double y){
        this.time=time;
        this.y=y;
    }

    @Override
    protected Point clone(){
        Point p=null;
        try{
            p=(Point) super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return p;
    }
}
