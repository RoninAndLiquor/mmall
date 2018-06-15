import com.mmall.util.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyTest {

    public static void main(String[] args) {
        List<String> fileNames = getFileNames("F:\\Tomcat1", null);
        for(String str : fileNames){
            System.out.println(str);
        }
        System.out.println(plus(10));
    }

    public static Integer plus(int n){
        if(n<1){
            return 0;
        }else{
            return n+plus(n-1);
        }
    }



    public static List<String> getFileNames(String path,List<String> list){
        if(list == null){
            list = new ArrayList<String>();
        }
        File file =new File(path);
        File[] files = file.listFiles();
        for(File f:files){
            if(f.isDirectory()){
                getFileNames(f.getPath(),list);
            }
            list.add(f.getPath());
        }
        return list;
    }



    public static Integer calc(int n){
        Integer result = 0;
        for(int i=1;i<=n;i++){
            if(i%2!=0){
                result += i;
            }else{
                result -=i;
            }
        }
        return result;
    }


    public static void m(int i) {
        if (i == 1) {
            System.out.println("1*1=1 ");
        } else {
            m(i - 1);
            for (int j = 1; j <= i; j++) {
                System.out.print(j + "*" + i + "=" + j * i + " ");
            }
            System.out.println();
        }
    }
    public static Integer selfCalc(int n){
        if(n<2){
            return 1;
        }
        else{
            return n*selfCalc(n-1);
        }
    }

    static long fact(int n)
    {
        if(n <= 1)
        {
            return 1;
        }
        else
        {
            return n * fact(n - 1);
        }
    }

    public static Integer amount(int n){
        if(n<3){
            return 1;
        }else{
            return amount(n-2) +amount(n-1);
        }
    }

}
