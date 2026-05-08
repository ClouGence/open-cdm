package com.clougence.clouddm.init.model;

import lombok.Data;

/**
 * Definition of a single initialization configuration field.
 * The field schema comes from init-fields.json, and the field value is resolved from runtime classpath property files.
 */
@Data
public class InitFieldDef {

    /** Property key matching the corresponding entry in a .properties file. */
    private String  propertyKey;

    /** Default value resolved from the active runtime configuration. */
    private String  defaultValue;

    /** UI category such as database, security, or connectivity. */
    private String  category;

    /** Form input type such as text, password, or number. */
    private String  inputType;

    /** Whether the field is required. */
    private boolean required;

    /** Localized label shown in the initialization form. */
    private String  label;

    /** Localized description shown beside the field. */
    private String  description;
}
