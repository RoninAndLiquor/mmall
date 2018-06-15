import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;

public class RuntimeTest {

    public static void main(String[] args) {
        String str = "123456|GGDKKKCKMDJSSD";
        String[] split = str.split("\\|");
        for(String s:split){
            System.out.println(s);
        }
        closeProcess(3306);
    }
    public static boolean closeProcess(int port){
        try {
            Process exec = Runtime.getRuntime().exec("cmd /k netstat -ano|findstr "+port);
            InputStream inputStream = exec.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[4096];
            String str = null;
            int i = 0;
            while((i=bis.read(bytes))!=-1){
                str = new String(bytes);
                break;
            }
            bis.close();
            inputStream.close();
            if(str.contains("LISTENING")){
                String[] listenings = str.split("LISTENING");
                String pid = null;
                if(listenings[1].indexOf("TCP")!=-1){
                    String[] tcps = listenings[1].split("TCP");
                    pid = tcps[0].trim();
                    System.out.println(pid);
                }
                if(pid!=null){
                    Process exec1 = Runtime.getRuntime().exec("cmd /k tasklist|findstr " + pid);
                    InputStream inputStream1 = exec1.getInputStream();
                    BufferedInputStream bis1 = new BufferedInputStream(inputStream1);
                    byte[] bytes1 = new byte[4096];
                    int b = 0;
                    String nameStr = null;
                    while((b=bis1.read(bytes1))!=-1){
                        nameStr = new String(bytes1);
                        break;
                    }
                    if(nameStr!=null){
                        String name = nameStr.split(pid)[0].trim();
                        System.out.println(name);
                        if(name !=null){
                            Process exec2 = Runtime.getRuntime().exec("cmd /k taskkill -f -t -im " + name);
                            System.out.println(exec2.isAlive());
                            if(!exec2.isAlive()){
                                System.out.println(name.split(".")[0]+"进程已关闭");
                                return true;
                            }
                        }
                    }
                }
            }else{
                System.out.println("没有找到“"+port+"”端口号的进程");
            }
            ///exec.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
