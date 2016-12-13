package org.jspare.vertx.injector;

import static org.jspare.core.container.Environment.my;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.jspare.core.annotation.Inject;
import org.jspare.core.container.Context;
import org.jspare.core.container.InjectorStrategy;
import org.jspare.core.container.MySupport;
import org.jspare.core.exception.EnvironmentException;
import org.jspare.core.exception.Errors;
import org.jspare.vertx.annotation.SharedWorkerExecutor;
import org.jspare.vertx.annotation.VertxInject;
import org.jspare.vertx.utils.JsonObjectLoader;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.file.FileSystem;
import io.vertx.core.shareddata.SharedData;

public class VertxInjectStrategy extends MySupport implements InjectorStrategy {

	private static final String DEFAULT_WORKER_EXECUTOR_NAME = "defaultWorkerExecutor";

	private String VERTX_PATTERN = "vertx:%s";

	@Inject
	private Context context;

	@Override
	public void inject(Object result, Field field) {

		Vertx vertx = null;

		try {

			VertxInject inject = field.getAnnotation(VertxInject.class);
			String instanceRef = inject.value();
			Optional<Vertx> oVertx = Optional.ofNullable(context.getAs(formatInstanceKey(instanceRef)));
			if (oVertx.isPresent()) {

				vertx = oVertx.get();
			} else {
				
				VertxOptions options = null;
				if(StringUtils.isNotEmpty(inject.vertxOptions())){
					
					options = my(JsonObjectLoader.class).loadOptions(inject.vertxOptions(), VertxOptions.class);
				}else{
					
					options = new VertxOptions();
				}
				
				vertx = Vertx.vertx(options);
			}
			
			setField(result, field, vertx);

		} catch (IllegalArgumentException | IllegalAccessException e) {

			throw new EnvironmentException(Errors.INVALID_INJECTION.throwable(e));
		}
	}

	protected void setField(Object result,  Field field, Vertx vertx) throws IllegalArgumentException, IllegalAccessException {

		field.setAccessible(true);

		if(result instanceof Vertx){
			
			field.set(result, vertx);
		}
		else if(result instanceof EventBus){
			
			field.set(result, vertx.eventBus());
		}
		else if(result instanceof WorkerExecutor){
			
			if(field.isAnnotationPresent(SharedWorkerExecutor.class)){
				
				SharedWorkerExecutor annWe = field.getAnnotation(SharedWorkerExecutor.class);
				field.set(result, vertx.createSharedWorkerExecutor(annWe.name(), annWe.poolSize(), annWe.maxExecuteTime()));
			}else{
				
				field.set(result, vertx.createSharedWorkerExecutor(DEFAULT_WORKER_EXECUTOR_NAME));
			}
		}
		else if(result instanceof FileSystem){

			field.set(result, vertx.fileSystem());
		}
		else if(result instanceof SharedData){
			
			field.set(result, vertx.sharedData());
		}
	}

	private String formatInstanceKey(String instanceRef) {

		return String.format(VERTX_PATTERN, instanceRef);
	}
}