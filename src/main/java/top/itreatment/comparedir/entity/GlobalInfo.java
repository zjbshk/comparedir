package top.itreatment.comparedir.entity;


import lombok.Data;

import java.util.List;

@Data
public class GlobalInfo<T> {

    private String rootPath;

    private int fileNum;

    private long fileSize;

    private boolean exist;

    private float matchRate;

    private List<T> fileNodeList;

    @Override
    public String toString() {
        return "GlobalInfo{" +
                "rootPath='" + rootPath + '\'' +
                ", fileNum=" + fileNum +
                ", fileSize=" + fileSize +
                ", exist=" + exist +
                ", matchRate=" + matchRate +
                '}';
    }
}
