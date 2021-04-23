package junit.activiti;

import com.xjh.atcrowdfunding.util.DesUtil;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;


public class SpringJavaMailTest {
    @Test
    public void test1() throws Exception {
        //1.使用Java程序发送邮件
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("spring/spring-*.xml");
        //2.邮件发送器，由spring框架提供
        JavaMailSenderImpl javaMailSender=(JavaMailSenderImpl) applicationContext.getBean("sendMail");

        javaMailSender.setDefaultEncoding("UTF-8");
        MimeMessage mail=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mail);
        helper.setSubject("告白书");

        StringBuilder context=new StringBuilder();

        String param="ILY";
        param= DesUtil.encrypt(param,"abcdefghijklmnopquvwxyz");
        context.append("<a href='http://www.atcrowdfunding.com/test/act.do?p="+param+"'>激活链接</a>");
        helper.setText(context.toString(),true);
        helper.setFrom("admin@atjiahai.com");
        helper.setTo("test@atjiahai.com");
        javaMailSender.send(mail);
        System.out.println();
    }
}
