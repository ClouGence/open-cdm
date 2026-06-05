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
package com.clougence.schema.umi.struts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-05-21
 */
@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public abstract class ConstraintUmiData extends AttributeUmiData {

    private List<UmiConstraint> constraints;

    public void addConstraint(UmiConstraint constraint) {
        if (constraint == null) {
            return;
        }

        if (this.constraints == null) {
            this.constraints = new ArrayList<>();
        }

        this.constraints.add(constraint);
    }

    public List<UmiConstraint> getConstraints(UmiConstraintType constraintType) {
        if (this.getConstraints() == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(this.getConstraints().stream().filter(c -> {
            return c.getConstraintType().getFullTypeCode().equals(constraintType.getFullTypeCode());
        }).collect(Collectors.toList()));
    }

    public <T extends UmiConstraint> List<T> getConstraints(Class<T> classType) {
        if (this.getConstraints() == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableList(this.getConstraints().stream().filter(classType::isInstance).map(c -> (T) c).collect(Collectors.toList()));
    }

    public <T extends UmiConstraint> boolean hasConstraint(Class<T> classType) {
        return !getConstraints(classType).isEmpty();
    }

    public boolean hasConstraint(UmiConstraintType constraintType) {
        return !getConstraints(constraintType).isEmpty();
    }

    protected <T extends UmiConstraint> void overwriteConstraint(Class<T> classType, List<T> values) {
        List<UmiConstraint> newConstraints = new ArrayList<>();
        if (this.getConstraints() != null) {
            newConstraints.addAll(getConstraints());
        }

        List<UmiConstraint> needRemove = newConstraints.stream().filter(classType::isInstance).collect(Collectors.toList());
        for (UmiConstraint needOne : needRemove) {
            newConstraints.remove(needOne);
        }

        newConstraints.addAll(values);

        this.setConstraints(newConstraints);
    }

    protected <T extends UmiConstraint> void overwriteConstraint(UmiConstraintType constraintType, List<T> values) {
        List<UmiConstraint> newConstraints = new ArrayList<>();
        if (this.getConstraints() != null) {
            newConstraints.addAll(getConstraints());
        }

        List<UmiConstraint> needRemove = newConstraints.stream().filter(c -> {
            return StringUtils.equals(c.getConstraintType().getFullTypeCode(), constraintType.getFullTypeCode());
        }).collect(Collectors.toList());

        for (UmiConstraint needOne : needRemove) {
            newConstraints.remove(needOne);
        }

        newConstraints.addAll(values);

        this.setConstraints(newConstraints);
    }
}
