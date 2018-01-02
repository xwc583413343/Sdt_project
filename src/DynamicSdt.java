import java.util.Vector;

/**
 * @author xwc
 * @version 1.0
 * @created 17-12-25
 */
public class DynamicSdt implements FatherSdt{
    double m_Acc;    //压缩精度

    DynamicSdt(double m_Acc) {
        this.m_Acc = m_Acc;
    }

    public void compress(Vector<Point> undeal, Vector<Point> comp) {
        //上门和下门，初始时门时关着的。
        double MAX_DOUBLE = 1.79769e+308;
        double slope1 = -MAX_DOUBLE;
        double slope2 = MAX_DOUBLE;

        //当前数据的上斜率和下斜率
        double now_slope1, now_slope2;
        double E0=this.m_Acc,Emax=E0*1.5,Emin=E0/1.5;
        double T0=0,T1;
        double aRadio=0;
        boolean flag=true;

        Point pEnd=undeal.get(0).clone();//保存压缩段结束点
        Point pCurrent;//当前点

        //save the first data
        comp.add(undeal.get(0).clone());

        Vector<Point> tempVector=new Vector<>();
        //循环处理数据
        int size = undeal.size(), i;
        for (i = 1; i < size; ++i) {
            tempVector.add(undeal.get(i).clone());
            pCurrent=undeal.get(i).clone();
            now_slope1 = (pCurrent.y - pEnd.y - m_Acc) / (pCurrent.time - pEnd.time);
            if (now_slope1 > slope1)    //上门的斜率只能变大
                slope1 = now_slope1;

            now_slope2 =(pCurrent.y - pEnd.y + m_Acc) / (pCurrent.time - pEnd.time);
            if (now_slope2 < slope2)    //下门的斜率只能变小
                slope2 = now_slope2;

            if (slope1 >= slope2) {    //当上门的斜率大于或者等于下门的斜率时，即两门的夹角大于或者等于180度。
                //tempVector.clear();

                if(flag){//第一次压缩
                    T0=tempVector.size();
                    flag=false;
                }else{//第二次压缩,调整压缩参数
                    T1=tempVector.size();
                    aRadio=(T1-T0)/T0;
                    E0=E0/(1+aRadio);
                    if(E0>=Emax||E0<=Emin){
                        if(E0>=Emax){
                            E0=Emax;
                        }else{
                            E0=Emin;
                        }
                    }
                    this.m_Acc=E0;
                    T0=T1;
                }
               // System.out.println("m_Acc:"+m_Acc+"=========T0:"+T0);
                tempVector.clear();
                //保存前一个节点
                comp.add(undeal.get(i - 1).clone());
                pEnd=undeal.get(i-1).clone();//修改最近保存数据时间点

                //初始化两扇门为当前点与上个点的斜率
                slope1 = (pCurrent.y - pEnd.y - m_Acc) / (pCurrent.time - pEnd.time);
                slope2 = (pCurrent.y - pEnd.y + m_Acc) / (pCurrent.time - pEnd.time);
            }
           // tempVector.add(undeal.get(i));
            //  last_read_data = data;
        }

        // sava end point
        comp.add(undeal.get(i - 1).clone());
    }

    public void uncompress(Vector<Point> comp, Vector<Point> dealt) {
        Point a = new Point(comp.get(0).time,comp.get(0).y),b=new Point();
        int i, size = comp.size();
        for (i = 1; i < size; ++i) {
            b = comp.get(i);
            //Step.1
            dealt.add(new Point(a.time,a.y));

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
        dealt.add(new Point(b.time,b.y));
    }
}
