import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

/**
 * @author xwc
 * @version 1.0
 * @created 17-12-20
 */
public class test {
    //计算方差
    public static double caculVariance(Vector<Point> undeal, Vector<Point> dealt) {
        Vector<Double> resultSet = new Vector<>();//计算均值方差
        for (int i = 0; i < undeal.size(); i++) {
            double temp = undeal.get(i).y - dealt.get(i).y;
            resultSet.add(temp);
        }
        double sum = 0.0;
        for (Double p : resultSet) {
            sum += p;
        }
        double mean = sum / resultSet.size(); //均值
        double accum=0;
        for (Double d : resultSet) {
            accum += (d - mean) * (d - mean);
        }
        double stdev = Math.sqrt(accum / (resultSet.size() - 1)); //方差
        return stdev;
    }

    public static void sdfCal(double m_acc) {
        int i = 0;
        Vector<Point> undeal = new Vector<>(), compress = new Vector<>(), dealt = new Vector<>();
        double PI = 3.14159265358979323846;
/* sin(x)
        for (double x_val = -2 * PI; x_val < 1024 * PI; ++i, x_val += 0.2) {
            double sin_y = Math.sin(x_val / 4);
            undeal.add(new Point(i, sin_y));
        }

        //y=-100*sinc ( 0.05*pi*x ) +100
        for(double x_val=-2* PI;x_val<1024*PI;x_val+=0.2,++i) {
            double sin_y = -100 * Math.sin(x_val/4) / (x_val/4);
            undeal.add(new Point(i, sin_y));
        }
*/
        File file=new File("/home/xwc/workspace/Sdt_project/20170701-20171004.xls");
        undeal=GetExcelInfo.readExcel(file);

        int num = undeal.size();
        //System.out.println("undeal size:"+num);
        //Sdt nsdt=new Sdt(m_acc);
        LinearSdt nsdt=new LinearSdt(m_acc);
       // DynamicSdt nsdt=new DynamicSdt(m_acc);
        //DecimalFormat    df   = new DecimalFormat("######0.00");
        DecimalFormat df=new DecimalFormat("####0.000000");
        //MixSdt nsdt=new MixSdt(m_acc);
        nsdt.compress(undeal, compress);

        System.out.println("原生数据量为:" + undeal.size() +"   压缩后数据量为:" +compress.size() );
        System.out.println("压缩比率:" +(float) undeal.size() / compress.size());
        nsdt.uncompress(compress, dealt);

        double stdev = caculVariance(undeal, dealt);
        System.out.println("压缩误差:" +df.format(stdev) );
        System.out.println();

    }
    public static void sdfCalFactory(FatherSdt nsdt) {
        int i = 0;
        Vector<Point> undeal = new Vector<>(), compress = new Vector<>(), dealt = new Vector<>();
        double PI = 3.14159265358979323846;
 /* sin(x)
        for (double x_val = -2 * PI; x_val < 1024 * PI; ++i, x_val += 0.2) {
            double sin_y = Math.sin(x_val / 4);
            undeal.add(new Point(i, sin_y));
        }

        //y=-100*sinc ( 0.05*pi*x ) +100
        for(double x_val=-2* PI;x_val<1024*PI;x_val+=0.2,++i) {
            double sin_y = -100 * Math.sin(x_val/4) / (x_val/4);
            undeal.add(new Point(i, sin_y));
        }
 */
        File file=new File("/Users/weichaoxie/IdeaProjects/Sdt_project/20170701-20171004.xls");
        undeal=GetExcelInfo.readExcel(file);

        int num = undeal.size();
        DecimalFormat df=new DecimalFormat("####0.000000");
        long startTime=System.currentTimeMillis();
        nsdt.compress(undeal, compress);
        long endTime=System.currentTimeMillis();
        //System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        nsdt.uncompress(compress, dealt);
        double stdev = caculVariance(undeal, dealt);
        System.out.println("原生数据量为:" + undeal.size() +"   压缩后数据量为:" +compress.size() +"     压缩比率:" +(float) undeal.size() / compress.size()+"   压缩误差:" +df.format(stdev));
    }

/*
    public static void main(String[] args) {

        double m_acc=0.01;
        double m_accArray[]={1,0.5,0.3,0.1,0.05,0.03,0.01};
        for(int i=0;i<m_accArray.length;i++){
            System.out.println("m_acc:"+m_accArray[i]);
            sdfCal(m_accArray[i]);

        }
    }
*/
    public static void main(String[] args) {

        double m_acc=0.01;
        double m_accArray[]={5,1,0.8,0.5,0.3,0.1,0.05,0.03,0.01};
        for(int i=0;i<m_accArray.length;i++){
            System.out.println("m_acc:"+m_accArray[i]);
            m_acc=m_accArray[i];
            Sdt sdt=new Sdt(m_acc);
            LinearSdt lsdt=new LinearSdt(m_acc);
            DynamicSdt dsdt=new DynamicSdt(m_acc);
            MixSdt msdt=new MixSdt(m_acc);
            sdfCalFactory(sdt);
            sdfCalFactory(lsdt);
            sdfCalFactory(dsdt);
            sdfCalFactory(msdt);

        }
    }


}
