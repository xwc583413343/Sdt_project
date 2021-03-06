import java.util.Vector;

/**
 * @author xwc
 * @version 1.0
 * @created 17-12-20
 */
public class Sdt implements FatherSdt{
    double m_Acc;    //压缩精度

    Sdt(double m_Acc) {
        this.m_Acc = m_Acc;
    }

    public void compress(Vector<Point> undeal, Vector<Point> comp) {
        //上门和下门，初始时门时关着的。
        double MAX_DOUBLE = 1.79769e+308;
        double slope1 = -MAX_DOUBLE;
        double slope2 = MAX_DOUBLE;

        //当前数据的上斜率和下斜率
        double now_slope1, now_slope2;

        Point pEnd=undeal.get(0);//保存压缩段结束点
        Point pCurrent;//当前点

        //save the first data
        comp.add(undeal.get(0));

        //double last_stored_time = undeal.get(0).time;  //最近保存数据的时间
        Vector<Point> tempVector=new Vector<>();
        //循环处理数据
        double t;
        int size = undeal.size(), i;
        for (i = 1; i < size; ++i) {
            pCurrent=undeal.get(i);
            now_slope1 = (pCurrent.y - pEnd.y - m_Acc) / (pCurrent.time - pEnd.time);
            if (now_slope1 > slope1)    //上门的斜率只能变大
                slope1 = now_slope1;

            now_slope2 =(pCurrent.y - pEnd.y + m_Acc) / (pCurrent.time - pEnd.time);
            if (now_slope2 < slope2)    //下门的斜率只能变小
                slope2 = now_slope2;

            if (slope1 >= slope2) {    //当上门的斜率大于或者等于下门的斜率时，即两门的夹角大于或者等于180度。
                tempVector.clear();
                //保存前一个节点
                comp.add(undeal.get(i - 1));
                pEnd=undeal.get(i-1);//修改最近保存数据时间点

                //初始化两扇门为当前点与上个点的斜率
                slope1 = (pCurrent.y - pEnd.y - m_Acc) / (pCurrent.time - pEnd.time);
                slope2 = (pCurrent.y - pEnd.y + m_Acc) / (pCurrent.time - pEnd.time);
            }
            tempVector.add(undeal.get(i));
            //  last_read_data = data;
        }

        // sava end point
        comp.add(undeal.get(i - 1));
    }

    public void uncompress(Vector<Point> comp, Vector<Point> dealt) {
        Point a = (Point) comp.get(0).clone(),b=new Point();
        int i, size = comp.size();
        for (i = 1; i < size; ++i) {
            b = comp.get(i);
            //Step.1
            dealt.add((Point)a.clone());

            //Step.2
            if (a.time + 1 != b.time) {
                double k = (b.y - a.y) / (b.time - a.time); //计算斜率
                for (int j =(int)a.time + 1; j < b.time; ++j) {
                    //线性差值求被压缩掉的数据
                    dealt.add(new Point(j, k * (j - a.time) + a.y));
                }
            }

            a.time = b.time;
            a.y = b.y;
        }
        dealt.add((Point)b.clone());
    }
}
