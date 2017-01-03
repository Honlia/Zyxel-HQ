package cn.superfw.framework.spring;

import org.springframework.mail.javamail.JavaMailSenderImpl;

public class JavaMailSenderImplEx extends JavaMailSenderImpl {

    public boolean checkMailServer() {
        try {
            connectTransport();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
