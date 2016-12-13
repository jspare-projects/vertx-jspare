package org.jspare.vertx.builder;

import java.lang.reflect.Method;

import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class MessageData {

	private Object controller;
	
	private Method method;

	private String name;
	
	public <T> Handler<Message<T>> wrap(){
		
		return new Handler<Message<T>>() {

			@Override
			@SneakyThrows
			public void handle(Message<T> event) {

				if(method.getParameterCount() == 1){
					
					method().invoke(controller, event);
				}else{
					
					method.invoke(controller);
				}
			}
		};
	}
	
}