import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class WSDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        //监听的文件
        Path dir = Paths.get(".");
        
        WatchService watchService = FileSystems.getDefault().newWatchService();
        //注册监听
        dir.register(watchService, 
                StandardWatchEventKinds.ENTRY_CREATE, 
                StandardWatchEventKinds.ENTRY_DELETE, 
                StandardWatchEventKinds.ENTRY_MODIFY);
        //循环监听
        while (true) {
            WatchKey key = watchService.take();
            List<WatchEvent<?>> events = key.pollEvents();
            for (WatchEvent<?> event : events) {
                WatchEvent.Kind<?> kind = event.kind();
                //如果是溢出事件，则忽略跳过
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }
                //获取事件发生的路径
                WatchEvent<Path> watchEvent = (WatchEvent<Path>) event;
                Path fileName = watchEvent.context();
                System.out.println(kind + " " + fileName);
            }
            //重置key，继续监听
            boolean valid = key.reset();
            if (!valid) {
                System.out.println("监听的目录不可用了");
                break;
            }
        }

}
}
