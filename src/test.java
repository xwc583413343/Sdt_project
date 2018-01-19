import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Vector;

/**
 * @author xwc
 * @version 1.0
 * @created 17-12-20
 */
public class test {
    public static double caculVariance(Vector<Point> undeal, Vector<Point> dealt) {
        Vector<Double> resultSet = new Vector<>();//计算均值方差
        for (int i = 0; i < undeal.size(); i++) {
            double temp = undeal.get(i).y - dealt.get(i).y;
            resultSet.add(temp);
        }
        double sum = 0.0;
        for (Double p : resultSet) {
            sum += p*p;
        }
        double stdev = Math.sqrt(sum/resultSet.size()); //方差
        return stdev;
    }

    //undeal代表带有噪声数据,cundeal噪声处理后数据,p代表噪声强度，t代表噪声频率
    public  static void creatData(Vector<Point> undeal,Vector<Point> cundeal,int p,int t,boolean flag){
        //噪声函数
        int i = 0;
        double PI = 3.14159265358979323846;
    /* sin(x)
        Random r=new Random();
        for (double x_val = -2 * PI; x_val < 1024 * PI; ++i, x_val += 0.2) {
            double sin_y=0.0;
            if(flag&&i%t==0){
                sin_y= Math.sin(x_val / 4)+r.nextInt(p);
            }else {
                sin_y= Math.sin(x_val / 4);
            }
            undeal.add(new Point(i, sin_y));
            sin_y= Math.sin(x_val / 4);
            cundeal.add(new Point(i,sin_y));
        }

        //y=-100*sinc ( 0.05*pi*x ) +100
        for(double x_val=-2* PI;x_val<1024*PI;x_val+=0.2,++i) {
            double sin_y = -100 * Math.sin(x_val/4) / (x_val/4);
            undeal.add(new Point(i, sin_y));
        }
    */
        File file=new File("/home/xwc/workspace/Sdt_project/20170701-20171004.xls");
        GetExcelInfo.readExcel(file,undeal,cundeal);

    }

    public static void sdfCalFactory(FatherSdt nsdt,Vector<Point> undeal,Vector<Point> cundeal) {

        Vector<Point> compress = new Vector<>(), dealt = new Vector<>();
        //

        int num = undeal.size();
        int num2=cundeal.size();
        DecimalFormat df=new DecimalFormat("####0.000000");
        long startTime=System.currentTimeMillis();
        nsdt.compress(undeal, compress);
        long endTime=System.currentTimeMillis();
        //System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        nsdt.uncompress(compress, dealt);
        double stdev = caculVariance(cundeal, dealt);
        System.out.println("原生数据量为:" + undeal.size() +"   压缩后数据量为:" +compress.size() +"     压缩比率:" +(float) undeal.size() / compress.size()+"   压缩误差:" +df.format(stdev));
    }
    public static void test01(){
        double m_acc=0.01;
        double m_accArray[]={1,0.8,0.5,0.3,0.1,0.05,0.03};

        Vector<Point> undeal = new Vector<>(),cundeal=new Vector<>();
        creatData(undeal,cundeal,4,500,false);

        for(int i=0;i<m_accArray.length;i++){
            System.out.println("m_acc:"+m_accArray[i]);
            m_acc=m_accArray[i];
            Sdt sdt=new Sdt(m_acc);
            LinearSdt lsdt=new LinearSdt(m_acc);
            DynamicSdt dsdt=new DynamicSdt(m_acc);
            MixSdt msdt=new MixSdt(m_acc);
            NoiseMixSdt nmsdt=new NoiseMixSdt(m_acc,1000);

            sdfCalFactory(sdt,undeal,cundeal);
            sdfCalFactory(lsdt,undeal,cundeal);
            sdfCalFactory(dsdt,undeal,cundeal);
            sdfCalFactory(msdt,undeal,cundeal);
            sdfCalFactory(nmsdt,undeal,cundeal);
        }

    }

    public static void main(String[] args) {
        test01();

    }


}
