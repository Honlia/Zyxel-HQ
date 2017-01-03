package cn.superfw.framework.web;

public interface BLogicService<P, R> {
    R execute(P param);
}
