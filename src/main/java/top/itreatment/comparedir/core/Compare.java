package top.itreatment.comparedir.core;

public interface Compare<T> {
    CompareType match(T one, T two);
}
