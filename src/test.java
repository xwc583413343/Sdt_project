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
        Double accum = 0.0;
        for (Double d : resultSet) {
            accum += (d - mean) * (d - mean);
        }
        double stdev = Math.sqrt(accum / (resultSet.size() - 1)); //方差
        return stdev;
    }

    public static void sdfCal(double m_acc) {
        int i = 0;
        Vector<Point> undeal = new Vector<>(), compress = new Vector<>(), dealt = new Vector<>();

        double maxX = 0.0;
        double minX = 0.0;
        double maxY = 0.0;
        double minY = 0.0;
        double PI = 3.14159265358979323846;
        for (double x_val = -2 * PI; x_val < 128 * PI; ++i, x_val += 0.2) {
            double sin_y = Math.sin(x_val / 4);
            undeal.add(new Point(i, sin_y));
            if (maxX < i)
                maxX = i;
            else if (i < minX)
                minX = i;
            if (maxY < sin_y)
                maxY = sin_y;
            else if (sin_y < minY)
                minY = sin_y;
        }
        int num = undeal.size();
        System.out.println("undeal size:"+num);
        //Sdt nsdt=new Sdt(m_acc);
        LinearSdt nsdt=new LinearSdt(m_acc);
        nsdt.compress(undeal, compress);
        num = compress.size();
        System.out.println("deal size:"+num);

        System.out.println("原生数据量为:" + undeal.size() +"   压缩后数据量为:" +compress.size() );
        System.out.println("压缩比率:" +(float) undeal.size() / compress.size());
        nsdt.uncompress(compress, dealt);
        double stdev = caculVariance(undeal, dealt);
        System.out.println("压缩误差:" +stdev );

    }

    public static void main(String[] args) {
        double m_acc =0.1;
        sdfCal(m_acc);
    }

}
