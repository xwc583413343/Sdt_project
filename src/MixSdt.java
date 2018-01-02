import java.util.Vector;

/**
 * @author xwc
 * @version 1.0
 * @created 17-12-25
 */
public class MixSdt implements FatherSdt{
    double m_Acc;    //压缩精度

    MixSdt(double m_Acc) {
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
        double aRadio;
        Point pBegin=undeal.get(0).clone();//保存压缩区间段开始点
        Point pEnd=undeal.get(0).clone();//保存压缩段结束点
        Point pCurrent;//当前点
        //存储压缩区间临时点
        Vector<Point> pTempVector=new Vector<>();
        pTempVector.add(pBegin.clone());
        Double a0=0.0,b0=0.0,a1=0.0,b1=0.0;

        boolean flag=true;

        //save the first data
        comp.add(undeal.get(0).clone());
        //循环处理数据
        int size = undeal.size(), i;
        for (i = 1; i < size; ++i) {
           // pTempVector.add(undeal.get(i));
            pCurrent=undeal.get(i).clone();
            now_slope1 = (pCurrent.y - pEnd.y - m_Acc) / (pCurrent.time - pEnd.time);
            if (now_slope1 > slope1)    //上门的斜率只能变大
                slope1 = now_slope1;

            now_slope2 =(pCurrent.y - pEnd.y + m_Acc) / (pCurrent.time - pEnd.time);
            if (now_slope2 < slope2)    //下门的斜率只能变小
                slope2 = now_slope2;

            if (slope1 >= slope2) {    //当上门的斜率大于或者等于下门的斜率时，即两门的夹角大于或者等于180度。

                double[] yArray=new double[pTempVector.size()];
                double[] timeArray=new double[pTempVector.size()];
                int j=0;
                for(Point p:pTempVector){
                    yArray[j]=p.y;
                    timeArray[j]=p.time;
                    j++;
                }
                if(flag){//第一次压缩
                    T0=pTempVector.size();
                    //L0
                    a0=LinearFunc.getA(timeArray,yArray);
                    b0=LinearFunc.getB(timeArray,yArray);
                    comp.add(new Point((int)timeArray[0],a0*timeArray[0]+b0));
                    flag=false;
                }else{//第二次压缩,调整压缩参数
                    //L1
                    a1=LinearFunc.getA(timeArray,yArray);
                    b1=LinearFunc.getB(timeArray,yArray);
                    double timeTemp=(b0-b1)/(a1-a0);
                    double yTemp=a0*timeTemp+b0;
                    //pX
                    if(((pBegin.time)<timeTemp)&&(timeTemp<undeal.get(i-1).time)){//2.1 判断交点time是否在pBegin.x～(i-1)
                        comp.add(new Point((int)timeTemp,yTemp));
                    }else{
                        comp.add(pEnd);
                    }
                    T1=pTempVector.size();
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
                    pBegin=pEnd.clone();
                    a0=a1;
                    b0=b1;
                   // System.out.println("m_Acc:"+m_Acc+"=========T0:"+T0);
                }
                pTempVector.clear();
                //保存前一个节点
                //comp.add(undeal.get(i - 1));
                pEnd=undeal.get(i-1).clone();//修改最近保存数据时间点

                //初始化两扇门为当前点与上个点的斜率
                slope1 = (pCurrent.y - pEnd.y - m_Acc) / (pCurrent.time - pEnd.time);
                slope2 = (pCurrent.y - pEnd.y + m_Acc) / (pCurrent.time - pEnd.time);
            }
            pTempVector.add(pCurrent.clone());
            //  last_read_data = data;
        }
        //最后一个区间段计算交点
        double[] yArray=new double[pTempVector.size()];
        double[] timeArray=new double[pTempVector.size()];
        int j=0;
        for(Point p:pTempVector){
            yArray[j]=p.y;
            timeArray[j]=p.time;
            j++;
        }
        a1=LinearFunc.getA(timeArray,yArray);
        b1=LinearFunc.getB(timeArray,yArray);
        double timeTemp=(b0-b1)/(a1-a0);
        double yTemp=a0*timeTemp+b0;
        if(((pBegin.time)<timeTemp)&&(timeTemp<undeal.get(i-1).time)){//2.1 判断交点time是否在pBegin.x～(i-1)
            comp.add(new Point((int)timeTemp,yTemp));
        }else{
            comp.add(pEnd.clone());
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
