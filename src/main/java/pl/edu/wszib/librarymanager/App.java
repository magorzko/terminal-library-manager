package pl.edu.wszib.librarymanager;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.edu.wszib.librarymanager.configuration.AppConfiguration;
import pl.edu.wszib.librarymanager.core.ICore;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfiguration.class);
        context.getBean(ICore.class).run();
    }
}
