package top.itreatment.comparedir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.core.globalcompare.DefaultGlobalInfoCompare;
import top.itreatment.comparedir.core.traversingimpl.ParallelBreadthTraversing;
import top.itreatment.comparedir.entity.CompareResult;
import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.entity.GlobalInfo;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@SpringBootApplication
public class ComparedirApplication {

    public static String onePath;
    public static String twoPath;

    public static void main(String[] args) {
        if (checkArgs(args)) {
            startSpringBoot(args);
        }
    }

    private static boolean checkArgs(String[] args) {
        if (args.length >= 2) {
            onePath = args[0];
            twoPath = args[1];
        } else {
            System.out.println("请输入两个要比较文件夹的路径");
            return false;
        }
        return true;
    }

    private static void startSpringBoot(String[] args) {
        SpringApplication.run(ComparedirApplication.class, args);
    }

    @Bean
    DefaultGlobalInfoCompare defaultGlobalInfoCompare() {
        return new DefaultGlobalInfoCompare();
    }

    @Bean
    public static CompareResult<FileNode> compare(DefaultGlobalInfoCompare defaultGlobalInfoCompare) {
        ParallelBreadthTraversing oneParallelBreadthTraversing = new ParallelBreadthTraversing();
        ParallelBreadthTraversing twoParallelBreadthTraversing = new ParallelBreadthTraversing();
        Callable<GlobalInfo<FileNode>> callable = () -> oneParallelBreadthTraversing.getNodeList(onePath);
        FutureTask<GlobalInfo<FileNode>> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        GlobalInfo<FileNode> oneGlobalInfo = twoParallelBreadthTraversing.getNodeList(twoPath);
        GlobalInfo<FileNode> twoGlobalInfo;
        try {
            twoGlobalInfo = futureTask.get();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        CompareType match = defaultGlobalInfoCompare.match(oneGlobalInfo, twoGlobalInfo);
        return new CompareResult<>(oneGlobalInfo, twoGlobalInfo, match);
    }


}
