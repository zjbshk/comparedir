package top.itreatment.comparedir.util;

import top.itreatment.comparedir.entity.FileNode;
import top.itreatment.comparedir.entity.dto.TreeNodeDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Util {


    public static FileNode getFileNode(File root, String rootAbsolutePath) {
        return getFileNode(root, rootAbsolutePath, 0);
    }

    public static FileNode getFileNode(File root, String rootAbsolutePath, int level) {
        if (root == null || rootAbsolutePath == null) {
            throw new IllegalArgumentException("必要参数不能为null");
        }
        String replace;
        replace = root.getAbsolutePath().replace(rootAbsolutePath, "");
        return FileNode.builder()
                .name(root.getName())
                .type(root.isDirectory())
                .lastTime(root.lastModified())
                .size(root.length())
                .level(level)
                .relationPath(replace)
                .build();
    }

    public static int CompareString(String one, String two) {
        int j = 0, k = 0;

        byte[] nameBytesJ = one.getBytes();
        byte[] nameBytesK = two.getBytes();

        int lenJ = nameBytesJ.length;
        int lenK = nameBytesK.length;
        while (j < lenJ && k < lenK) {
            int b = nameBytesK[k] - nameBytesJ[j];
            if (b != 0) {
                return b;
            }
            j++;
            k++;
        }
        return lenJ - lenK;
    }

    public static List<FileNode> getLevelList(List<FileNode> tempFileNodeList, int leval) {
        int size = tempFileNodeList.size();

//        二分法查找等级一样的区域
        int index = -1;
        int start = 0, end = size;
        while (start <= end) {
            int middle = (start + end) / 2;
            FileNode fileNode = tempFileNodeList.get(middle);
            int i = fileNode.getLevel() - leval;
            if (i < 0) {
                start = middle;
            } else if (i == 0) {
                index = middle;
                break;
            } else {
                end = middle;
            }
        }

        List<FileNode> fileNodeList = new ArrayList<>();
        if (index != -1) {
            for (int right = index; ; right--) {
                FileNode fileNode = tempFileNodeList.get(right);
                if (fileNode.getLevel() == leval) {
                    fileNodeList.add(fileNode);
                } else {
                    break;
                }
            }

            for (int left = index + 1; ; left++) {
                FileNode fileNode = tempFileNodeList.get(left);
                if (fileNode.getLevel() == leval) {
                    fileNodeList.add(fileNode);
                } else {
                    break;
                }
            }
        }
        return fileNodeList;
    }

    public static List<FileNode> getChildFileNodeList(List<FileNode> tempFileNodeList, String relationPath, int leval) {
        final String relationPathFinal = relationPath;
        tempFileNodeList = Util.getLevelList(tempFileNodeList, leval + 1);
        tempFileNodeList = tempFileNodeList.parallelStream().filter(fileNode -> fileNode.getRelationPath().startsWith(relationPathFinal)).collect(Collectors.toList());
        return tempFileNodeList;
    }


}
