package top.itreatment.comparedir.entity;

import lombok.Builder;
import lombok.Data;
import top.itreatment.comparedir.core.CompareType;
import top.itreatment.comparedir.util.Util;

@Data
@Builder
public class FileNode implements Comparable<FileNode> {

    private String name;

    /**
     * 存储计算得到的值，不做重复的计算
     */
    private byte[] nameBytes;
    private int nameLength;
    //    private int relationPathLength;
    private CompareType tag;

    private boolean type;

    private long size;

    private long lastTime;

    private String relationPath;

    /**
     * 所在层级
     */
    private int level;

    public byte[] getNameBytes() {
        if (nameBytes == null) initNameBytes();
        return nameBytes;
    }

    /**
     * 用于比较
     *
     * @param o
     * @return
     */

    @Override
    public int compareTo(FileNode o) {
        if (o == null)
            return 1;

        if (o.level != level) {
            return o.level - level;
        }

        int i = Util.CompareString(relationPath, o.relationPath);
        if (i != 0) {
            return i;
        }

        i = Util.CompareString(name, o.name);
        return i;
    }

    private void initNameBytes() {
        String name = getName();
        if (name != null) {
            setNameBytes(name.getBytes());
        } else {
            setNameBytes(new byte[]{});
        }
    }
}
