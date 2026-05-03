package com.clougence.clouddm.sdk.ui.editor.data.reload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Reload {

    private boolean         enable;
    private String          reloadSql;
    private EditorResultSet result;

    public static Reload success(EditorResultSet result) {
        Reload reload = new Reload();
        reload.setEnable(false);
        reload.setResult(result);
        return reload;
    }

    public static Reload failed(String message) {
        EditorResultSet result = new EditorResultSet();
        result.setSuccess(false);
        result.setMessage(message);

        Reload reload = new Reload();
        reload.setEnable(false);
        reload.setResult(result);
        return reload;
    }

    public static Reload reload(String reloadSql) {
        Reload reload = new Reload();
        reload.setEnable(true);
        reload.setReloadSql(reloadSql);
        reload.setResult(new EditorResultSet());
        return reload;
    }
}
