/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.api.common.boot;

import java.util.*;
import java.util.function.Predicate;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnifiedPostConstructUtils {

    public static void doPostConstruct(ApplicationContext context) throws Exception {
        doPostConstruct(context, null);
    }

    public static void doPostConstruct(ApplicationContext context, Predicate<Class<?>> typeFilter) throws Exception {
        List<UnifiedPostConstructWrap> postConstructList = collectPostConstructs(context, typeFilter);
        for (UnifiedPostConstruct postConstruct : postConstructList) {
            postConstruct.init();
        }
    }

    public static void doDestroyConstruct(ApplicationContext context) {
        doDestroyConstruct(context, null);
    }

    public static void doDestroyConstruct(ApplicationContext context, Predicate<Class<?>> typeFilter) {
        List<UnifiedPostConstructWrap> postConstructList = collectPostConstructs(context, typeFilter);
        Collections.reverse(postConstructList);
        for (UnifiedPostConstruct postConstruct : postConstructList) {
            postConstruct.stop();
        }
    }

    private static List<UnifiedPostConstructWrap> collectPostConstructs(ApplicationContext context, Predicate<Class<?>> typeFilter) {
        Map<String, UnifiedPostConstruct> postConstructs = context.getBeansOfType(UnifiedPostConstruct.class);

        List<UnifiedPostConstructWrap> postConstructList = new ArrayList<>();
        postConstructs.forEach((beanName, bean) -> {
            Class<?> pcClass = context.getType(beanName);
            pcClass = ClassUtils.getUserClass(pcClass);
            if (pcClass == null) {
                return;
            }
            if (typeFilter != null && !typeFilter.test(pcClass)) {
                return;
            }

            UnifiedPostConstructOrder order = pcClass.getAnnotation(UnifiedPostConstructOrder.class);
            if (order == null) {
                postConstructList.add(new UnifiedPostConstructWrap(0, bean, pcClass));
            } else {
                postConstructList.add(new UnifiedPostConstructWrap(order.value(), bean, pcClass));
            }
        });
        postConstructList.sort(Comparator.comparingInt(UnifiedPostConstructWrap::getOrder));
        return postConstructList;
    }

}

@Slf4j
class UnifiedPostConstructWrap implements UnifiedPostConstruct {

    private final int                  order;
    private final Class<?>             targetType;
    private final UnifiedPostConstruct target;

    public UnifiedPostConstructWrap(int order, UnifiedPostConstruct target, Class<?> targetType){
        this.order = order;
        this.target = target;
        this.targetType = targetType;
    }

    public int getOrder() { return order; }

    @Override
    public void init() throws Exception {
        this.target.init();
        log.info(this.targetType.getSimpleName() + " inited.");
    }

    @Override
    public void stop() {
        this.target.stop();
        log.info(this.targetType.getSimpleName() + " stopped.");
    }

    @Override
    public String toString() {
        return targetType.getSimpleName() + ", order " + this.order;
    }
}
