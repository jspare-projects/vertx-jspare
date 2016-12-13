package org.jspare.vertx.builder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.jspare.core.annotation.Resource;
import org.jspare.core.container.ContainerUtils;
import org.jspare.vertx.annotation.DeploymentOptionsBuilder;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import lombok.SneakyThrows;

@Resource
public class VerticleCollector implements Collector<Optional<VerticleData>> {

	@Override
	@SneakyThrows
	public Optional<VerticleData> collect(Class<?> clazz) {
		
		// Ignore all classes without EventBusController	
		if(!clazz.isAnnotationPresent(org.jspare.vertx.annotation.Verticle.class)){
			
			return Optional.empty();
		}

		Verticle verticle = (Verticle) clazz.newInstance();
		DeploymentOptions deploymentOptions = getDeploymentOptions(clazz, verticle);
		
		ContainerUtils.processInjection(clazz, verticle);
		
		VerticleData data = new VerticleData(verticle, deploymentOptions);
		
		return Optional.of(data);
	}
	
	@SneakyThrows
	private DeploymentOptions getDeploymentOptions(Class<?> clazz, Object verticle){
		
		// Validate if clazz contains one valid DeploymentOptions
		Optional<Method> oMethod = Arrays.asList(clazz.getDeclaredMethods()).stream().filter(m -> 
			m.isAnnotationPresent(DeploymentOptionsBuilder.class) && m.getReturnType().equals(DeploymentOptions.class)
		).findFirst();
		
		DeploymentOptions deploymentOptions = null;
		if(oMethod.isPresent()){
			
			// Call for deployment options 
			
			Method method = oMethod.get();
			method.setAccessible(true);
			deploymentOptions = (DeploymentOptions) method.invoke(verticle);
		}else{
			
			deploymentOptions = new DeploymentOptions();
		}
		
		return deploymentOptions;
	}
}