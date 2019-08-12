package top.itreatment.comparedir.core.traversingimpl;

import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.filter.DefaultFileFilter;
import top.itreatment.comparedir.util.Util;

import java.io.File;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentBreadthTraversing extends BaseTraversing {

    private static class KeyValue {
        File root;
        int level;

        public KeyValue(File root, int level) {
            this.root = root;
            this.level = level;
        }
    }

    private List<KeyValue> noAccessdirList = new Vector<>();
    private List<KeyValue> AccessingdirList = new Vector<>();

    @Override
    protected void traversing(File root, String rootAbsolutePath, int level) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        noAccessdirList.add(new KeyValue(root, level));
        while (true) {
            int size = noAccessdirList.size();
            if (size == 0) break;
            AccessingdirList.clear();
            AccessingdirList.addAll(noAccessdirList);
            noAccessdirList.clear();
            CountDownLatch countDownLatch = new CountDownLatch(size);
            AccessingdirList.forEach(keyValue -> {
                executorService.execute(() -> {
                    traversingOne(keyValue.root, rootAbsolutePath, keyValue.level);
                    countDownLatch.countDown();
                });
            });
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }

    private void traversingOne(File root, String rootAbsolutePath, int level) {
        FileNode fileNode = Util.getFileNode(root, rootAbsolutePath, level);
        if (fileNode.isType()) {
            File[] list = root.listFiles(new DefaultFileFilter());
            if (list != null)
                for (File file : list) {
                    if (file.isDirectory()) noAccessdirList.add(new KeyValue(file, level + 1));
                    else fileNodeList.add(Util.getFileNode(file, rootAbsolutePath, level + 1));
                }
        }
        fileNodeList.add(fileNode);
    }
}
