package com.clougence.clouddm.console.web.model.fo.faker;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import com.clougence.clouddm.sdk.model.faker.FakerRunModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author olddream
 */
@Getter
@Setter
public class FakerConfigFO {

    @NotNull(message = "{faker.levels.notnull}")
    private List<String>       levels;

    @NotNull(message = "{faker.producer.thread.cnt.notnull}")
    @Range(max = 10, min = 1, message = "{faker.producer.thread.cnt.range}")
    private Integer            producer;

    @NotNull(message = "{faker.writer.thread.cnt.notnull}")
    @Range(max = 10, min = 1, message = "{faker.writer.thread.cnt.range}")
    private Integer            writer;

    private boolean            transaction;

    private boolean            ignoreErrors;

    @NotNull(message = "{faker.type.notnull}")
    private FakerRunModel      type;

    @NotNull(message = "{faker.ratio.insert.notnull}")
    private Integer            insertRatio;

    @NotNull(message = "{faker.ratio.update.notnull}")
    private Integer            updateRatio;

    @NotNull(message = "{faker.ratio.delete.notnull}")
    private Integer            deleteRatio;

    @NotNull(message = "{faker.running.time.notnull}")
    @Min(value = 1, message = "{faker.running.time.range}")
    private Integer            time;

    @NotNull(message = "{faker.config.table.notnull}")
    private List<FakerTableFO> tableConfigs;
}
