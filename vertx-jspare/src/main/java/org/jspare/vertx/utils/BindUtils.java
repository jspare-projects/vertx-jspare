package org.jspare.vertx.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jspare.core.internal.Bind;
import org.jspare.core.internal.ReflectionUtils;

@Slf4j
@UtilityClass
public class BindUtils {

  public void bindFromResource(Class<?> bindClass){

    if(bindClass.isInterface()){
      if(log.isWarnEnabled()){
        log.warn("Interface {} annotated with @Resource will be ignored.", bindClass.getName());
      }
      return;
    }

    ReflectionUtils
      .collecInterfaces(bindClass)
      .forEach(i -> Bind.bind(i).to(bindClass));
  }
}
