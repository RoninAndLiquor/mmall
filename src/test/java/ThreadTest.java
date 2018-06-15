import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadTest extends Thread{

    public static void main(String[] args){
        ThreadTest t = new ThreadTest();
        t.start();
        int k = 0;
        String[] strings = new String[k];
        String str ="1,2,3,4,5,6,7,8,9";
        String[] split = str.split(";");
        for(String s:split){
            strings = s.split(",");
            k+= strings.length;
        }
        System.out.println("k="+k);
        System.out.println("k="+strings.length);
        for(int i=0;i<strings.length;i++){
            System.out.println(strings[i]);
        }
        String string = new String("hello");
        char[] c = {'h','e','l','l','o'};
        System.out.println(string.equals(new String("hello")));
        List list = new ArrayList();
        double array[] = {112,111,22,456,231};
        for(int i=0;i<array.length;i++){
            list.add(new Double(array[i]));
        }
        Collections.sort(list);
        for(int i=0;i<array.length;i++){
            System.out.println(list.get(i));
        }
    }
    public void start() {
    }

}
