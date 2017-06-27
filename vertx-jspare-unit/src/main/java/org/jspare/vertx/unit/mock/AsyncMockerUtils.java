package org.jspare.vertx.unit.mock;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.jspare.core.Environment;
import org.jspare.core.internal.Bind;
import org.jspare.core.internal.ReflectionUtils;
import org.jspare.unit.mock.MockerUtils;

import java.lang.reflect.Field;

/**
 * Created by paulo.ferreira on 23/06/2017.
 */
@UtilityClass
public class AsyncMockerUtils {

  public void initialize(Class<?> clazz) {
    ReflectionUtils.getFieldsWithAnnotation(clazz, AsyncMock.class).forEach(AsyncMockerUtils::addProxy);
  }

  /**
   * Adds the proxy.
   *
   * @param field the field
   */
  @SneakyThrows
  private void addProxy(Field field) {
    Object mocker = AsyncMocker.createProxy(field.getType());
    Environment.registry(Bind.bind(field.getType()), mocker);
  }
}
