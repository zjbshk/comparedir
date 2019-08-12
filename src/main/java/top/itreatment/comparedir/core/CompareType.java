package top.itreatment.comparedir.core;

public enum CompareType {
    DIFFERENT(-1, "不相同"),
    SAME(0, "相同"),
    UNKNOWN(1, "未知"),
    UNCERTAIN(2, "不确定");

    private int key;
    private String description;

    CompareType(int key, String description) {
        this.key = key;
        this.description = description;
    }
}
