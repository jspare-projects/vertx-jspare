/*
 * Copyright 2016 JSpare.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jspare.vertx.jpa;

import io.vertx.core.AsyncResult;
import io.vertx.core.Verticle;
import lombok.SneakyThrows;
import org.jspare.core.MySupport;
import org.jspare.core.internal.ReflectionUtils;
import org.jspare.jpa.PersistenceOptions;
import org.jspare.jpa.PersistenceUnitProvider;
import org.jspare.vertx.autoconfiguration.Configurable;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;

/**
 * The Class PersistenceJpaModule.
 * <p>
 * Used for load {@link org.springframework.data.jpa.provider.PersistenceProvider } and endpoints to simple rest api.
 * </p>
 *
 * @author <a href="https://pflima92.github.io/">Paulo Lima</a>
 */
public class PersistenceJpaModule extends MySupport implements Configurable {

    @Inject
    private PersistenceUnitProvider provider;

    /**
     * The Constant NAME.
     */
    public static final String NAME = "persistenceJpa";

    private Map<String, PersistenceOptions> DATA_SOURCES = new HashMap<>();
    private List<String> ANNOTATED_CLASSES = new ArrayList<>();
    private Verticle verticle;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.jspare.vertx.experimental.Configurable#execute(io.vertx.core.Verticle,
     * java.lang.String[])
     */
    @Override
    public void execute(Verticle verticle, String[] args) {

        this.verticle = verticle;

        init();

        DATA_SOURCES.entrySet().forEach(es -> {
                    PersistenceOptions options = es.getValue();
                    options.setAnnotatedClasses(ANNOTATED_CLASSES);
                    provider.create(es.getKey(), options);
                }
        );
    }

    private void init() {

        if (verticle.getClass().isAnnotationPresent(AnnotatedClasses.class)) {
            AnnotatedClasses ann = verticle.getClass().getAnnotation(AnnotatedClasses.class);
            ANNOTATED_CLASSES.addAll(Arrays.asList(ann.value()));
        }

        ReflectionUtils.getMethodsWithAnnotation(verticle.getClass(), PersistenceUnitOptions.class).forEach(this::setPersistenceOption);

        if (DATA_SOURCES.isEmpty()) {
            DATA_SOURCES.put(PersistenceUnitProvider.DEFAULT_DS, new PersistenceOptions());
        }
    }

    @SneakyThrows
    private void setPersistenceOption(Method method) {

        method.setAccessible(true);
        String dataSource = ReflectionUtils.getAnnotation(method, PersistenceUnitOptions.class).value();
        PersistenceOptions options = (PersistenceOptions) method.invoke(verticle);
        DATA_SOURCES.put(dataSource, options);
    }

    private void unhandled(AsyncResult<Void> ar) {
    }
}