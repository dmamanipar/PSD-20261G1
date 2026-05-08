package pe.edu.upeu.sysalmacen;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
//Prueba de CI con Jenkins CD -Tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SysAlmacenApplication.class);
	}

}
